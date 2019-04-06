package com.along.androidchat.model;

/**
 * Created by Long
 * Date: 4/5/2019
 * Time: 11:20 PM
 */
public class Room {
    private String RoomName;

    public String getRoomName() {
        return RoomName;
    }

    public Room(String roomName, String userID, String name, String avatar) {
        RoomName = roomName;
        UserID = userID;
        Name = name;
        Avatar = avatar;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    private String UserID;
    private String Name;
    private String Avatar;
}
