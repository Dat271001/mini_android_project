package com.example.mini_android_project.model;

public class Room {
    private String roomId;
    private String roomName;
    private double price;
    private boolean isAvailable; // true = Còn trống, false = Đã thuê
    private String tenantName;
    private String phoneNumber;

    public Room(String roomId, String roomName, double price, boolean isAvailable,
                String tenantName, String phoneNumber) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.price = price;
        this.isAvailable = isAvailable;
        this.tenantName = tenantName;
        this.phoneNumber = phoneNumber;
    }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getStatusText() {
        return isAvailable ? "Còn trống" : "Đã thuê";
    }
}
