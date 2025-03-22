public class Room {
    private String roomType;     // **Room Type**
    private int roomNumber;      // **Room Number**
    private boolean isAvailable; // **Availability**

    public Room(String roomType, int roomNumber) {
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.isAvailable = true; // all rooms start as available
    }

    public String getRoomType() {
        return roomType;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    @Override
    public String toString() {
        // e.g. "Room#1 (Single) - Available"
        return String.format("Room#%d (%s) - %s",
                roomNumber,
                roomType,
                (isAvailable ? "Available" : "Unavailable")
        );
    }
}
