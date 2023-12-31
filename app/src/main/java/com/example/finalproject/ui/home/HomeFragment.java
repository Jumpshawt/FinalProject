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
import com.example.finalproject.ui.GeordieFirebaseMethods;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
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
    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private static String downloadURL = "https://raw.githubusercontent.com/Jumpshawt/clubSchedules/main/Untitled%20spreadsheet%20-%20Sheet1.csv";

    Context context;
    EventAdapter adapter;
    ArrayList<EventModal> arrayList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        arrayList.add(new EventModal("Today", LocalDate.now()));
        arrayList.add(new EventModal("Tomorrow", LocalDate.now().plus(1, ChronoUnit.DAYS)));
        arrayList.add(new EventModal("Later This week", LocalDate.now().plus(2, ChronoUnit.DAYS)));
        LocalDate nextWeekStart = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        arrayList.add(new EventModal("Next Week", nextWeekStart));
        loadEventModalsFromFirestore();

        super.onCreate(savedInstanceState);

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

    public void loadEventModalsFromFirestore() {

        // Get a reference to the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Enable offline persistence
        db.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build());

        db.collection("EventModals")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    EventModal modalAdded = GeordieFirebaseMethods.queryDocToEventModal(dc.getDocument());
                                    modalAdded.setFireId(dc.getDocument().getId());
                                    Log.d(TAG, "New Event: " + dc.getDocument().getData() + "\n id is" + GeordieFirebaseMethods.queryDocToEventModal(dc.getDocument()).getFireId());
                                    
                                    arrayList.add(modalAdded);
                                    adapter.notifyItemInserted(arrayList.size());
                                    break;
                                case MODIFIED:
                                    //figure out which
                                    for (int i = 0, arrayListSize = arrayList.size(); i < arrayListSize; i++) {
                                        EventModal modalModified = arrayList.get(i);
                                        if (modalModified.getFireId() == dc.getDocument().getId()) {
                                            modalModified = GeordieFirebaseMethods.queryDocToEventModal(dc.getDocument());
                                            adapter.notifyItemChanged(i);
                                            break;
                                        }
                                    }
                                    Log.d(TAG, "Modified Event: " + dc.getDocument().getData());
                                    break;
                                     case REMOVED:
                                    for (EventModal modalDeleted : arrayList) {
                                        if (modalDeleted.fireId == dc.getDocument().getId())
                                            arrayList.remove(modalDeleted);
                                    }
                                    adapter.notifyDataSetChanged();
                                    Log.d(TAG, "Removed Event: " + dc.getDocument().getData());
                                    break;
                            }
                        }
                        sortAndCull();
                        adapter.notifyDataSetChanged();
                    }
                });
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
                            GeordieFirebaseMethods.saveEventModalToFirestore(i);
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
    {Collections.sort(arrayList, (em1, em2) -> {
        int dateComparison = em1.getDate().compareTo(em2.getDate());
        if (dateComparison == 0) {
            // Dates are equal, compare times
            return em1.getTime().compareTo(em2.getTime());
        } else {
            return dateComparison;
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


