public class Reservation {
    private String reservationID;  // **Reservation ID** (stored as String for flexibility)
    private String customerName;   // **Customer Name**
    private String roomType;       // **Room Type**

    public Reservation(String reservationID, String customerName, String roomType) {
        this.reservationID = reservationID;
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public String getReservationID() {
        return reservationID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return String.format("ReservationID: %s, Customer: %s, RoomType: %s",
                reservationID, customerName, roomType);
    }
}
