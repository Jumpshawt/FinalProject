package com.example.finalproject.ui;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.finalproject.ui.home.EventModal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class GeordieFirebaseMethods {

    private static final String TAG = "GeordieFirbaseMethod";

    public static void removeEventModalFromFirestore(@NonNull String id)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("EventModals").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
    public static void saveEventModalToFirestore(@NonNull EventModal eventModal) {
        // Get a reference to the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Convert the EventModal object to a Map
        Map<String, Object> eventModalMap = new HashMap<>();
        eventModalMap.put("img", eventModal.getImg());
        eventModalMap.put("id", eventModal.getId());
        eventModalMap.put("type", eventModal.getType());
        eventModalMap.put("name", eventModal.getName());
        eventModalMap.put("location", eventModal.getLocation());
        eventModalMap.put("org", eventModal.getOrg());
        eventModalMap.put("description", eventModal.getDescription());
        eventModalMap.put("date", eventModal.getDate().toString());
        eventModalMap.put("time", eventModal.getTime().toString());

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

    public static void editEventModalOnFirestore(@NonNull EventModal eventModal) {
        // Get a reference to the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Convert the EventModal object to a Map
        Map<String, Object> eventModalMap = new HashMap<>();
        eventModalMap.put("img", eventModal.getImg());
        eventModalMap.put("id", eventModal.getId());
        eventModalMap.put("type", eventModal.getType());
        eventModalMap.put("name", eventModal.getName());
        eventModalMap.put("location", eventModal.getLocation());
        eventModalMap.put("org", eventModal.getOrg());
        eventModalMap.put("description", eventModal.getDescription());
        eventModalMap.put("date", eventModal.getDate().toString());
        eventModalMap.put("time", eventModal.getTime().toString());

        // Add the data to the database
        db.collection("EventModals").document(eventModal.getFireId()).set(eventModalMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
    }

    public static EventModal queryDocToEventModal(QueryDocumentSnapshot document)
    {
        EventModal eventModal = new EventModal();

        // Load each value individually
        eventModal.setImg(document.getLong("img").intValue());
        eventModal.setId(document.getLong("id").intValue());
        eventModal.setFireId(document.getId());

        eventModal.setType(document.getLong("type").intValue());
        eventModal.setName(document.getString("name"));
        eventModal.setLocation(document.getString("location"));
        eventModal.setOrg(document.getString("org"));
        eventModal.setDescription(document.getString("description"));

        // Parse the date as a LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        eventModal.setDate(LocalDate.parse(document.getString("date"), formatter));
        eventModal.setTime(LocalTime.parse(document.getString("time")));
        return eventModal;
    }
}
