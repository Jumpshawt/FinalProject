package com.example.finalproject.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MemoryCacheSettings;
import com.google.firebase.firestore.PersistentCacheSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
//Networking stuff
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private static final int DATE_INDEX = 0;
    private static final int TIME_INDEX = 1;
    private static final int REPEAT_INDEX = 2;

    private static final int REPEAT_END_INDEX = 3;
    private static final int NAME_INDEX = 4;

    private static final int ORG_INDEX = 5;
    private static final int LOCATION_INDEX = 6;
    private static final int DESCRIPTION_INDEX = 7;


    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private static String downloadURL = "https://raw.githubusercontent.com/Jumpshawt/clubSchedules/main/Untitled%20spreadsheet%20-%20Sheet1.csv";

    Context context;
    EventAdapter adapter;
    ArrayList<EventModal> arrayList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        arrayList.add(new EventModal("Today",LocalDate.now()));
        arrayList.add(new EventModal("Tomorrow",LocalDate.now().plus(1, ChronoUnit.DAYS)));
        arrayList.add(new EventModal("Later This week",LocalDate.now().plus(2, ChronoUnit.DAYS)));
        LocalDate nextWeekStart = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        arrayList.add(new EventModal("Next Week", nextWeekStart));
        loadEventModalsFromFirestore();

        super.onCreate(savedInstanceState);

    }



    public void loadEventModalsFromFirestore() {
        // Get a reference to the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Load from the cache first
        db.collection("EventModals")
                .get(Source.CACHE)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Handle the data
                            handleQueryResult(task);
                        } else {
                            Log.w("tag", "Error getting documents from cache.", task.getException());
                        }

                        // Then load from the server
                        db.collection("EventModals")
                                .get(Source.SERVER)
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            // Handle the data
                                            handleQueryResult(task);
                                        } else {
                                            Log.w("TAG", "Error getting documents from server.", task.getException());
                                        }
                                    }
                                });
                    }
                });
    }
    private void handleQueryResult(Task<QuerySnapshot> task) {
        // Create a list to hold the EventModal objects
        List<EventModal> eventModals = new ArrayList<>();

        // Loop through the documents in the snapshot
        for (QueryDocumentSnapshot document : task.getResult()) {
            // Create a new EventModal object and load each value
            // ...
            EventModal eventModal = new EventModal();

            // Load each value individually
            eventModal.img = document.getLong("img").intValue();
            eventModal.id = document.getLong("id").intValue();
            eventModal.type = document.getLong("type").intValue();
            eventModal.name = document.getString("name");
            eventModal.location = document.getString("location");
            eventModal.org = document.getString("org");
            eventModal.description = document.getString("description");

            // Parse the date as a LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            eventModal.date = LocalDate.parse(document.getString("date"), formatter);

            eventModal.time = LocalTime.parse(document.getString("time"));

            // Add the EventModal object to the list
            arrayList.add(eventModal);
        }
        //once the new items have been added, update the content.and sort them
        adapter.notifyDataSetChanged();
        sortAndCull();

    }
    public void saveEventModalToFirestore(EventModal eventModal) {
        // Get a reference to the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Convert the EventModal object to a Map
        Map<String, Object> eventModalMap = new HashMap<>();
        eventModalMap.put("img", eventModal.img);
        eventModalMap.put("id", eventModal.id);
        eventModalMap.put("type", eventModal.type);
        eventModalMap.put("name", eventModal.name);
        eventModalMap.put("location", eventModal.location);
        eventModalMap.put("org", eventModal.org);
        eventModalMap.put("description", eventModal.description);
        eventModalMap.put("date", eventModal.date.toString());
        eventModalMap.put("time", eventModal.time.toString());



        // Add the data to the database
        db.collection("EventModals").add(eventModalMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }



        public void loadEventModalsFromFirebase() {
        // Get a reference to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("EventModals");

        // Attach a listener to the reference
    //TODO: Make it so that this also stores the string as a file,
    // so the user doesn't always need an internet connection.
    // At the start of that file also include a last updated timestamp so that the user doesn't need to download as much.

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Create a list to hold the EventModal objects
                    

                    // Loop through the children of the snapshot
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        // Create a new EventModal object
                        EventModal eventModal = new EventModal();

                        // Load each value individually
                        eventModal.img =(childSnapshot.child("img").getValue(Integer.class));
                        eventModal.id = (childSnapshot.child("id").getValue(Integer.class));
                        eventModal.type =(childSnapshot.child("type").getValue(Integer.class));
                        eventModal.name = childSnapshot.child("name").getValue(String.class);
                        eventModal.location = childSnapshot.child("location").getValue(String.class);
                        eventModal.org = childSnapshot.child("org").getValue(String.class);
                        eventModal.description = childSnapshot.child("description").getValue(String.class);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        eventModal.date = LocalDate.parse(childSnapshot.child("date").getValue(String.class), formatter);
                        eventModal.time = LocalTime.parse(childSnapshot.child("time").getValue(String.class));
                        arrayList.add(eventModal);
                        adapter.notifyDataSetChanged();
                        // Add the EventModal object to the list

                    }
                    sortAndCull();


                    // Now you have a list of EventModal objects loaded from Firebase
                    // Do something with the list here, like updating your RecyclerView
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("TAG", "Failed to read value.", error.toException());
                }
            });
    }
    public void uploadEventModalToFirebase(EventModal eventModal) {
        // Get a reference to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("EventModals");

        // Convert the EventModal object to a Map
        Map<String, Object> eventModalMap = new HashMap<>();
        eventModalMap.put("img", eventModal.img);
        eventModalMap.put("id", eventModal.id);
        eventModalMap.put("type", eventModal.type);
        eventModalMap.put("name", eventModal.name);
        eventModalMap.put("location", eventModal.location);
        eventModalMap.put("org", eventModal.org);
        eventModalMap.put("description", eventModal.description);
        eventModalMap.put("date", eventModal.date.toString());
        eventModalMap.put("time", eventModal.time.toString());
        // Push the data to the database
        myRef.push().setValue(eventModalMap);

    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.context =getContext();
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.eventItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new CustomLayoutManager(recyclerView.getContext()));
        adapter = new EventAdapter(getContext(),arrayList,recyclerView);
        recyclerView.setAdapter(adapter);

        return view;

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //TODO: Add a way of refreshing the page by scrolling to the top.
    //TODO: Reduce the amount of code
    public void downloadCsvAsEventModal(String urlString) {
        // Create a new thread to perform network operations
        new Thread(() -> {
            try {
                // Create a URL object from the string
                URL url = new URL(urlString);
                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Get an InputStream from the connection
                InputStream inputStream = connection.getInputStream();

                // Read the InputStream into a String
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    List<String[]> data = new ArrayList<>();
                    if (!line.replace(",", " ").isEmpty()||!line.replace("", "").isEmpty()) {
                        String[] values = line.split(",");
                        data.add(values);
                    }

                    stringBuilder.append(line + "\n");
                }

                // Close the resources
                reader.close();
                inputStream.close();
                connection.disconnect();

                // Get the text
                String text = stringBuilder.toString();

                String[][] text2 = parseCSV(text);

                //TODO: instead of checking if on every loop, subtract the text2 length and the arraylist size to determine instead.
                getActivity().runOnUiThread(() -> {
                    for (int i = 0; i < text2.length; i++) {
                        {
                            arrayList.add(stringToEventModal(text2[i]));
                        }
                    }
                    sortAndCull();
                    adapter.notifyDataSetChanged();
                    for (EventModal i :
                    arrayList) {
                    if (i.type == 0)
                      saveEventModalToFirestore(i);
                    }

                });
            } catch (IOException e) {

                e.printStackTrace();
            }
        }).start();
    }


    public String[][] parseCSV(String csvString) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new StringReader(csvString))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.replace(",", "").isEmpty()||!line.replace("-", "").isEmpty()) {
                    String[] values = line.split(",");
                    data.add(values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert list of string arrays to 2D array
        String[][] array = new String[data.size()][];
        for (int i = 0; i < array.length; i++) {
            array[i] = data.get(i);
        }

        return array;
    }
    public void sortAndCull()
    {        Collections.sort(arrayList, new Comparator<EventModal>() {
        @Override
        public int compare(EventModal em1, EventModal em2) {
            int dateComparison = em1.getDate().compareTo(em2.getDate());
            if (dateComparison == 0) {
                // Dates are equal, compare times
                return em1.getTime().compareTo(em2.getTime());
            } else {
                return dateComparison;
            }
        }

    });
    arrayList.removeIf(eventModal -> eventModal.getDate().isBefore(LocalDate.now()));
        for (int i = 1; i < arrayList.size() - 1; i++) {
            if (arrayList.get(i).getType() == 1 && arrayList.get(i - 1).getType() == 1) {
                arrayList.remove(i-1);
                // Decrement i to compensate for the removed element
                i--;
            }
        }
    }

    public EventModal stringToEventModal(String[] input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/d");


        EventModal return_modal = new EventModal();

        //TODO: Figure out why the time isn't parsing.

        return_modal.time = LocalTime.parse((input[TIME_INDEX]));
        return_modal.date = LocalDate.parse(input[DATE_INDEX],formatter);
        return_modal.name = input[NAME_INDEX];
        return_modal.org = input[ORG_INDEX];
        return_modal.location = input[LOCATION_INDEX];
        return_modal.description = input[DESCRIPTION_INDEX];
        return return_modal;
        }
    }
