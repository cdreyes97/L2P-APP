package com.example.l2p_app.models;

public class Request {

    public enum Status {
        PENDIENTE,
        ACEPTADO,
        RECHAZADO
    }

    private String userName;
    private String userUID;
    private String message;
    private String RoomUID;
    private String roomGame;
    private String roomOwner;
    private Status Status;

    public Request() {
    }

    public Request(String userName, String userUID, String message, String roomUID, Request.Status status) {
        this.userName = userName;
        this.userUID = userUID;
        this.message = message;
        RoomUID = roomUID;
        Status = status;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoomUID() {
        return RoomUID;
    }

    public void setRoomUID(String roomUID) {
        RoomUID = roomUID;
    }

    public Request.Status getStatus() {
        return Status;
    }

    public void setStatus(Request.Status status) {
        Status = status;
    }
}
