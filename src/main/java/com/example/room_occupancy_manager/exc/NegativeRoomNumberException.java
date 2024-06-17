package com.example.room_occupancy_manager.exc;

public class NegativeRoomNumberException extends RuntimeException {

    public NegativeRoomNumberException() {
        super("Number of rooms cannot be negative.");
    }
}

