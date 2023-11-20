package com.example.finalproject.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.HashMap;
public class EventAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    ArrayList<EventModal> arrayList;
    RecyclerView recyclerView;
    Context context;
    Calendar cal;

    public EventAdapter(Context context, ArrayList<EventModal> arrayList, RecyclerView recyclerView) {
        this.context = context;
        this.arrayList = arrayList;
        this.recyclerView = recyclerView;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Your code here
            }
        });

        return new RecyclerViewHolder(view);

    }

    @Override
    public int getItemViewType(final int position) {
        switch (arrayList.get(position).type )
        {
            case 0:
                return R.layout.event_row;
            case 1:
                return R.layout.event_divider;
            default:
                return R.layout.event_row;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Random rand = new Random();

        //assign a random image
        switch (rand.nextInt(5))
        {
            case 0:
                arrayList.get(position).img=(R.mipmap.weird);
                holder.getImage().setImageResource(R.mipmap.weird);
                break;
            case 1:
                arrayList.get(position).img=(R.mipmap.yelling);
                holder.getImage().setImageResource(R.mipmap.yelling);
                break;
            case 2:
                arrayList.get(position).img=(R.mipmap.thinking);
                holder.getImage().setImageResource(R.mipmap.thinking);
                break;

            case 3:
                arrayList.get(position).img=(R.mipmap.hulk);
                holder.getImage().setImageResource(R.mipmap.weird);
                break;

            case 4:
                holder.getImage().setImageResource(R.mipmap.hulk);

                break;

            default:
                holder.getImage().setImageResource(R.mipmap.hulk);
                break;
        }
        final EventModal i = arrayList.get(position);
        holder.getTime().setText(GeordieMethods.modalTime(i));
        holder.getName().setText(i.name);
        holder.getOrg().setText(i.org);
        holder.getLocation().setText(i.location);

        holder.getHolder().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedPosition=holder.getAdapterPosition();
                Toast.makeText(context, "loading a new event into info: id:"+ arrayList.get(selectedPosition).getFireId(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(view.getContext(), EventInfoScreen.class);

                intent.putExtra("object", (Parcelable)arrayList.get(selectedPosition));
                intent.putExtra("time", getMonth(arrayList.get(selectedPosition).date.getMonthValue()) + " "
                        + arrayList.get(selectedPosition).date.getDayOfMonth() + " @"
                        + GeordieMethods.timeTostring(arrayList.get(selectedPosition).time));
                context.startActivity(intent);

            }
    });

        holder.getJoin().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Joined", Toast.LENGTH_SHORT).show();
            }
        });
        holder.getShare().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "sharing", Toast.LENGTH_SHORT).show();
            }
        });
        holder.getLike().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "liked", Toast.LENGTH_SHORT).show();

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

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
