package com.example.ticketbooking.model;

public class Location {
    private int ID;
    private String Name;

    public Location() {}

    public int getID() { return ID; }
    public String getName() { return Name; }

    @Override
    public String toString() { return Name; }
}
