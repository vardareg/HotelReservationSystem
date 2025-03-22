import java.util.*;
import java.io.*;

public class HotelSystem {

    // Four stacks for the different room types.
    // The top of the stack holds the room with the smallest ID.
    private Stack<Room> singleStack = new Stack<>();
    private Stack<Room> doubleStack = new Stack<>();
    private Stack<Room> suiteStack = new Stack<>();
    private Stack<Room> deluxeStack = new Stack<>();

    // Waiting queues (for reservations that cannot be immediately fulfilled)
    private Queue<Reservation> singleQueue = new LinkedList<>();
    private Queue<Reservation> doubleQueue = new LinkedList<>();
    private Queue<Reservation> suiteQueue = new LinkedList<>();
    private Queue<Reservation> deluxeQueue = new LinkedList<>();

    // Booked reservations (for demonstration, we only store reservations, not which specific room)
    private List<Reservation> bookedList = new ArrayList<>();

    // We maintain a map of roomNumber -> Room to track actual Room objects
    private Map<Integer, Room> roomMap = new HashMap<>();

    // Final lists: which rooms are available/unavailable at the end
    private List<Room> unavailableRooms = new ArrayList<>();
    private List<Room> availableRooms = new ArrayList<>();

    /**
     * Initializes all 20 rooms, places them in a map,
     * and pushes them onto the appropriate stacks in reverse order.
     * This ensures the smallest room number is on top of each stack.
     */
    public void initializeAndStoreRooms() {
        // Create 20 rooms: 1–5 Single, 6–10 Double, 11–15 Suite, 16–20 Deluxe
        for (int i = 1; i <= 5; i++) {
            roomMap.put(i, new Room("Single", i));
        }
        for (int i = 6; i <= 10; i++) {
            roomMap.put(i, new Room("Double", i));
        }
        for (int i = 11; i <= 15; i++) {
            roomMap.put(i, new Room("Suite", i));
        }
        for (int i = 16; i <= 20; i++) {
            roomMap.put(i, new Room("Deluxe", i));
        }

        // Push them onto stacks in reverse, so the smallest ID ends up on top
        for (int i = 5; i >= 1; i--) {
            singleStack.push(roomMap.get(i));
        }
        for (int i = 10; i >= 6; i--) {
            doubleStack.push(roomMap.get(i));
        }
        for (int i = 15; i >= 11; i--) {
            suiteStack.push(roomMap.get(i));
        }
        for (int i = 20; i >= 16; i--) {
            deluxeStack.push(roomMap.get(i));
        }
    }

    /**
     * Reads reservations from a CSV file.
     * The first line is assumed to be the header and will be skipped.
     * The rest of the lines should look like: "1,John Smith,Single"
     */
    public void readReservationsFromFile(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            boolean firstLine = true;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue; // skip empty lines
                }

                // Skip the header row (the first line)
                if (firstLine) {
                    firstLine = false;
                    // If you want to check the header format, you can do so here
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length != 3) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }
                
                String reservationID = parts[0].trim(); 
                String customerName = parts[1].trim();
                String roomType = parts[2].trim(); 

                Reservation reservation = new Reservation(
                        reservationID, customerName, roomType);

                // Attempt to allocate the room or enqueue the reservation
                processReservation(reservation);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        }
    }

    /**
     * Attempts to process a single reservation:
     * - Check if a room of the requested type is immediately available (stack not empty).
     *   - If yes, pop from stack (allocate room), mark it unavailable, and add to bookedList.
     *   - If no, enqueue the reservation in the waiting line.
     */
    private void processReservation(Reservation reservation) {
        String type = reservation.getRoomType().toLowerCase();
        switch (type) {
            case "single":
                allocateRoomIfAvailable(singleStack, reservation, singleQueue);
                break;
            case "double":
                allocateRoomIfAvailable(doubleStack, reservation, doubleQueue);
                break;
            case "suite":
                allocateRoomIfAvailable(suiteStack, reservation, suiteQueue);
                break;
            case "deluxe":
                allocateRoomIfAvailable(deluxeStack, reservation, deluxeQueue);
                break;
            default:
                System.out.println("Unknown room type: " + reservation.getRoomType());
        }
    }

    /**
     * Allocates a room from the given stack if available,
     * otherwise enqueues the reservation in the waiting queue.
     */
    private void allocateRoomIfAvailable(Stack<Room> roomStack,
                                         Reservation reservation,
                                         Queue<Reservation> waitingQueue) {
        if (!roomStack.isEmpty()) {
            Room room = roomStack.pop(); // smallest ID from top
            room.setAvailable(false);    // mark room as booked/unavailable
            bookedList.add(reservation);
        } else {
            waitingQueue.offer(reservation);
        }
    }

    /**
     * Processes the waiting queues, attempting to allocate rooms
     * to any queued reservations if rooms are available.
     */
    public void processWaitingQueues() {
        allocateFromQueue(singleStack, singleQueue);
        allocateFromQueue(doubleStack, doubleQueue);
        allocateFromQueue(suiteStack, suiteQueue);
        allocateFromQueue(deluxeStack, deluxeQueue);
    }

    /**
     * Helper method to pop from a room stack and poll from a queue
     * until one of them is empty.
     */
    private void allocateFromQueue(Stack<Room> roomStack, Queue<Reservation> waitingQueue) {
        while (!waitingQueue.isEmpty() && !roomStack.isEmpty()) {
            Reservation reservation = waitingQueue.poll();
            Room room = roomStack.pop();
            room.setAvailable(false);
            bookedList.add(reservation);
        }
    }

    /**
     * Makes all odd-numbered booked rooms available again
     * and pushes them back on top of their respective stacks.
     */
    public void releaseOddNumberedBookedRooms() {
        // 1. Collect available room numbers from the stacks
        Set<Integer> availableRoomNumbers = new HashSet<>();
        for (Room r : singleStack) {
            availableRoomNumbers.add(r.getRoomNumber());
        }
        for (Room r : doubleStack) {
            availableRoomNumbers.add(r.getRoomNumber());
        }
        for (Room r : suiteStack) {
            availableRoomNumbers.add(r.getRoomNumber());
        }
        for (Room r : deluxeStack) {
            availableRoomNumbers.add(r.getRoomNumber());
        }

        // 2. For each room in roomMap, if it's NOT in availableRoomNumbers -> it's booked
        //    If that booked room has an odd room number, release it.
        List<Room> roomsToRelease = new ArrayList<>();
        for (Room room : roomMap.values()) {
            if (!availableRoomNumbers.contains(room.getRoomNumber())) {
                // This room is booked
                if (room.getRoomNumber() % 2 == 1) {
                    // odd-numbered room -> release it
                    room.setAvailable(true);
                    roomsToRelease.add(room);
                }
            }
        }

        // 3. Sort these released rooms in descending order so pushing them
        //    ensures smallest room number ends up on top
        roomsToRelease.sort((r1, r2) -> r2.getRoomNumber() - r1.getRoomNumber());

        // 4. Push them back onto the appropriate stack
        for (Room r : roomsToRelease) {
            String type = r.getRoomType().toLowerCase();
            switch (type) {
                case "single":
                    singleStack.push(r);
                    break;
                case "double":
                    doubleStack.push(r);
                    break;
                case "suite":
                    suiteStack.push(r);
                    break;
                case "deluxe":
                    deluxeStack.push(r);
                    break;
            }
        }
    }

    /**
     * At the end, we build two lists:
     * - unavailableRooms (rooms that remain booked)
     * - availableRooms   (rooms left on the stacks)
     */
    public void generateFinalLists() {
        unavailableRooms.clear();
        availableRooms.clear();

        // figure out which room numbers are still in the stacks
        Set<Integer> availableSet = new HashSet<>();
        for (Room r : singleStack)   availableSet.add(r.getRoomNumber());
        for (Room r : doubleStack)   availableSet.add(r.getRoomNumber());
        for (Room r : suiteStack)    availableSet.add(r.getRoomNumber());
        for (Room r : deluxeStack)   availableSet.add(r.getRoomNumber());

        // if a roomNumber is not in availableSet -> it's unavailable
        for (Room r : roomMap.values()) {
            if (availableSet.contains(r.getRoomNumber())) {
                availableRooms.add(r);
            } else {
                unavailableRooms.add(r);
            }
        }
    }

    // ---------------------------------------
    // Printing / Debugging Methods
    // ---------------------------------------
    public void printInitialPiles() {
        System.out.println("=== Initial Room Piles (Top -> Bottom) ===");
        printStack("Single", singleStack);
        printStack("Double", doubleStack);
        printStack("Suite", suiteStack);
        printStack("Deluxe", deluxeStack);
        System.out.println();
    }

    public void printFinalPiles() {
        System.out.println("=== Final Room Piles (Top -> Bottom) ===");
        printStack("Single", singleStack);
        printStack("Double", doubleStack);
        printStack("Suite", suiteStack);
        printStack("Deluxe", deluxeStack);
        System.out.println();
    }

    private void printStack(String label, Stack<Room> stack) {
        System.out.println(label + " Stack:");
        // By default, the top is the last element in the list
        List<Room> temp = new ArrayList<>(stack);
        Collections.reverse(temp); // now first is top -> last is bottom
        for (Room r : temp) {
            System.out.println("  " + r);
        }
    }

    public void printWaitingLines(String description) {
        System.out.println("=== Waiting Lines " + description + " ===");
        System.out.println("Single Queue: " + singleQueue);
        System.out.println("Double Queue: " + doubleQueue);
        System.out.println("Suite Queue: " + suiteQueue);
        System.out.println("Deluxe Queue: " + deluxeQueue);
        System.out.println();
    }

    public void printFinalRoomStatus() {
        System.out.println("=== Final Room Status ===");
        System.out.println("-- Unavailable Rooms --");
        for (Room r : unavailableRooms) {
            System.out.println("  " + r);
        }
        System.out.println("-- Available Rooms --");
        for (Room r : availableRooms) {
            System.out.println("  " + r);
        }
        System.out.println();
    }
}
