package com.example.mini_android_project.controller;

import com.example.mini_android_project.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomController {
    private List<Room> roomList;

    public RoomController() {
        roomList = new ArrayList<>();
        loadSampleData();
    }

    private void loadSampleData() {
        roomList.add(new Room("P001", "Phòng 101", 2500000, true, "", ""));
        roomList.add(new Room("P002", "Phòng 102", 3000000, false, "Nguyễn Văn A", "0901234567"));
        roomList.add(new Room("P003", "Phòng 201", 2800000, true, "", ""));
        roomList.add(new Room("P004", "Phòng 202", 3200000, false, "Trần Thị B", "0912345678"));
        roomList.add(new Room("P005", "Phòng 301", 2600000, true, "", ""));
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void addRoom(Room room) {
        roomList.add(room);
    }

    public void updateRoom(int position, Room room) {
        roomList.set(position, room);
    }

    public void deleteRoom(int position) {
        roomList.remove(position);
    }

    public boolean isRoomIdExists(String roomId) {
        for (Room room : roomList) {
            if (room.getRoomId().equalsIgnoreCase(roomId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRoomIdExistsExcept(String roomId, int excludePosition) {
        for (int i = 0; i < roomList.size(); i++) {
            if (i != excludePosition && roomList.get(i).getRoomId().equalsIgnoreCase(roomId)) {
                return true;
            }
        }
        return false;
    }
}
