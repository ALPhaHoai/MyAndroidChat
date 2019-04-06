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

    public Room(String roomName, String userID, String name, String avatar, boolean isActive) {
        RoomName = roomName;
        UserID = userID;
        Name = name;
        Avatar = avatar;
        this.isActive = isActive;
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
    boolean isActive;
    int unread = 0;
    public boolean isActive() {
        return isActive;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
