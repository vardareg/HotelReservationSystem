# Hotel Room Reservation System 🏨

This project is a simple **Hotel Room Reservation System** implemented in Java. It simulates room allocation using **Stacks** and **Queues**, mimicking real-world hotel booking behavior and waiting lines.

## 📁 Project Structure

```
.
├── Main.java              # Entry point for the program
├── Room.java              # Room class definition
├── Reservation.java       # Reservation class definition
├── HotelSystem.java       # Core logic for room allocation, queue handling, etc.
└── reservations.txt       # Sample reservation input (CSV format)
```

## 🚀 Features

- Room types: `Single`, `Double`, `Suite`, and `Deluxe`
- Stack-based room allocation (LIFO)
- Queue-based waiting list when rooms are full (FIFO)
- Odd-numbered room release simulation
- Dynamic reallocation from waiting queues
- Final status report for room availability

## 📌 How It Works

1. Rooms are initialized and pushed to stacks by type.
2. Reservations are read from a `reservations.txt` file.
3. Reservations are either immediately allocated or placed in queues.
4. Booked odd-numbered rooms are released mid-process.
5. Re-check waiting queues after room releases.
6. The system outputs detailed logs at each step.

## 🏦 Input File Format

The input file must be named `reservations.txt` and follow this structure:

```csv
Reservation ID,Customer Name,Room Type
1,John Smith,Single
2,Ethan Thomas,Double
...
```

## 🧪 Example Run

You can run the system via the `Main` class:

```bash
javac *.java
java Main
```

## 📋 Sample Output

```
=== Initial Room Piles (Top -> Bottom) ===
Single Stack:
  Room#5 (Single) - Available
  ...
Suite Stack:
  Room#15 (Suite) - Available
...

=== Waiting Lines after processing waiting queues ===
Double Queue: [ReservationID: 30, Customer: Grace Baker, RoomType: Double]
...

=== Final Room Status ===
-- Unavailable Rooms --
  Room#2 (Single) - Unavailable
  ...
-- Available Rooms --
  Room#1 (Single) - Available
  ...
```

## 🛠️ Technologies Used

- Java (Core Java, Collections API)
- Stack, Queue, List, Map
- File I/O (Scanner)

## 📚 Concepts Covered

- Data Structures (Stack, Queue, HashMap)
- Object-Oriented Programming
- File Handling in Java
- Simulation & Process Modeling

## 📄 License

This project is for educational use. Feel free to use or modify it for learning or academic purposes.

---


