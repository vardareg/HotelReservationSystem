public class Main {
    public static void main(String[] args) {
        // Instantiate the hotel system
        HotelSystem hotelSystem = new HotelSystem();

        // 1. Initialize rooms and place them in stacks
        hotelSystem.initializeAndStoreRooms();
        
        // 2. Print the initial piles (for debugging/illustration)
        hotelSystem.printInitialPiles();

        // 3. Read reservations from the CSV file (change filename if needed)
        hotelSystem.readReservationsFromFile("reservations.txt");

        // 4. Print waiting lines after initial processing
        hotelSystem.printWaitingLines("after initial processing");

        // 5. Attempt to process waiting queues (assign rooms to any pending reservations)
        hotelSystem.processWaitingQueues();
        hotelSystem.printWaitingLines("after processing waiting queues");

        // 6. Release all odd-numbered booked rooms
        hotelSystem.releaseOddNumberedBookedRooms();

        // 7. Process waiting queues again (some rooms have been freed)
        hotelSystem.processWaitingQueues();
        hotelSystem.printWaitingLines("after releasing odd-numbered rooms");

        // 8. Generate final lists of available/unavailable rooms
        hotelSystem.generateFinalLists();

        // 9. Print final piles and final status
        hotelSystem.printFinalPiles();
        hotelSystem.printFinalRoomStatus();
    }
}
