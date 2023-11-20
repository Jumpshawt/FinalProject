package com.example.finalproject.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/*
EventModal.java
Geordie Stenner T00702740
---------------
An class that holds information about the event.
 */

public class EventModal implements Parcelable {
    public static final int EVENT = 0;
    public static final int DATE = 1;
    int img, id, type;
    String name, location, org, description, fireId;
    LocalDate date;
    LocalTime time;

    //default constructor
    public EventModal()
    {
        this.type = EVENT;
        this.img = 0;
        this.time = LocalTime.parse("10:15:45");
        this.date = LocalDate.of(2003,4,5);
        this.name = "name";
        this.org = "";
        this.location = "";
        this.description = "";
        this.fireId = "not working";
    }

    //Constructor for event
    public EventModal(int img, String name, String org, String location, String description, String time, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/d");
        this.type = EVENT;
        this.img = img;
        this.time = LocalTime.parse(time);
        this.name = name;
        this.date = LocalDate.parse(date, formatter);
        this.org = org;
        this.location = location;
        this.description = description;
    }

    //Constructor for creating dates
    public EventModal(String name, LocalDate date) {
        this.type = DATE;
        this.name = name;
        this.date = date;
        this.img = 0;
        this.time = LocalTime.parse("00:00:00");
        this.org = "";
        this.location = "";
        this.description = "";
        this.fireId = "not working";

    }
    protected EventModal(Parcel in) {
        img = in.readInt();
        id = in.readInt();
        type = in.readInt();
        name = in.readString();
        location = in.readString();
        org = in.readString();
        description = in.readString();
        fireId = in.readString();
        time = (LocalTime) in.readValue(getClass().getClassLoader());
        date = (LocalDate) in.readValue(getClass().getClassLoader());

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
        dest.writeString(fireId);
        dest.writeValue(getTime());
        dest.writeValue(getDate());

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

    public int getImg() {
        return img;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getOrg() {
        return org;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getFireId() {
        return fireId;
    }

    public void setFireId(String fireId) {
        this.fireId = fireId;
    }
}
