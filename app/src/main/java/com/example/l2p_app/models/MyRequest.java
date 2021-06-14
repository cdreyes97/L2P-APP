package com.example.l2p_app.models;

public class MyRequest {

    public enum Status {
        PENDIENTE,
        ACEPTADO,
        RECHAZADO
    }

    private String message;
    private String RoomUID;
    private String game;
    private MyRequest.Status Status;

    public MyRequest(String message, String roomUID, String game, MyRequest.Status status) {

        this.message = message;
        RoomUID = roomUID;
        this.game = game;
        Status = status;
    }

    public MyRequest() {}


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

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public MyRequest.Status getStatus() {
        return Status;
    }

    public void setStatus(MyRequest.Status status) {
        Status = status;
    }
}
