package com.example.l2p_app.models;

public class Room {

    private String UID;
    private String name;
    private String game;
    private String description;
    private int roomCapacity;
    private int capacityUsed;

    public Room(String name, String game, String description, int roomCapacity, int capacityUsed) {
        this.name = name;
        this.game = game;
        this.description = description;
        this.roomCapacity = roomCapacity;
        this.capacityUsed = capacityUsed;
    }

    public Room(){}

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public int getCapacityUsed() {
        return capacityUsed;
    }

    public void setCapacityUsed(int capacityUsed) {
        this.capacityUsed = capacityUsed;
    }
}
