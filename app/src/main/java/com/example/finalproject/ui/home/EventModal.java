package com.example.finalproject.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;



public class EventModal implements Parcelable {
        int img, id, type;
    String name, location, org, description;
    LocalDate date;
    LocalTime time;


    public EventModal()
    {
        this.type = 0;
        this.img = 0;

        this.time = LocalTime.parse("10:15:45");
        this.date = LocalDate.of(2003,4,5);
        this.name = "name";
        this.org = "";
        this.location = "";
        this.description = "";
    }


    public EventModal(int img, String name, String org, String location, String description, String time, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/d");
        this.type = 0;
        this.img = img;
        this.time = LocalTime.parse(time);
        this.name = name;
        this.date = LocalDate.parse(date, formatter);
        this.org = org;
        this.location = location;
        this.description = description;
    }

    public EventModal(String name, LocalDate date) {
        this.type = 1;
        this.name = name;
        this.date = date;
        this.img = 0;
        this.time = LocalTime.parse("00:00:00");
        this.org = "";
        this.location = "";
        this.description = "";
    }


    protected EventModal(Parcel in) {
        img = in.readInt();
        id = in.readInt();
        type = in.readInt();
        name = in.readString();
        location = in.readString();
        org = in.readString();
        description = in.readString();
    }

    public static final Creator<EventModal> CREATOR = new Creator<EventModal>() {
        @Override
        public EventModal createFromParcel(Parcel in) {
            return new EventModal(in);
        }

        @Override
        public EventModal[] newArray(int size) {
            return new EventModal[size];
        }
    };

    public <T> LocalDate getDate() {
        return date;
    }
    public <T> LocalTime getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(img);
        dest.writeInt(id);
        dest.writeInt(type);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(org);
        dest.writeString(description);
    }

    private Object getMonth(int monthValue) {
        return new DateFormatSymbols().getMonths()[monthValue-1];
    }

    public static String timeTostring(LocalTime time) {
        boolean am = true;
        int hour = time.getHour();
        if (hour >12) {
            hour -= 12;
            am = false;
        }
        return (hour+ ":" + time.getMinute()+ ((am)?" AM":" PM"));
    }
}
