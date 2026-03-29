package com.example.ticketbooking.Model;

public class Location {

    private int ID;
    private String Name;

    public int getID() {
        return ID;
    }


    public String getName() {
        return Name;
    }

    public Location() {

    }
    @Override
    public String toString(){
        return Name;
    }
}
