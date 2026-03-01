import java.io.*;
import java.util.*;

class Room {
    int roomNumber;
    String category;
    double price;
    boolean isBooked;

    Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.isBooked = false;
    }
}

class Booking {
    String customerName;
    int roomNumber;
    String category;
    double amount;

    Booking(String customerName, int roomNumber, String category, double amount) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return customerName + "," + roomNumber + "," + category + "," + amount;
    }
}

public class HotelReservationSystem {

    static ArrayList<Room> rooms = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static final String FILE_NAME = "bookings.txt";

    public static void main(String[] args) {
        initializeRooms();
        loadBookings();

        int choice;
        do {
            System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View All Bookings");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewAvailableRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    viewBookings();
                    break;
                case 5:
                    System.out.println("Thank you for using the system!");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 5);
    }

    static void initializeRooms() {
        rooms.add(new Room(101, "Standard", 2000));
        rooms.add(new Room(102, "Standard", 2000));
        rooms.add(new Room(201, "Deluxe", 4000));
        rooms.add(new Room(202, "Deluxe", 4000));
        rooms.add(new Room(301, "Suite", 7000));
    }

    static void viewAvailableRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room room : rooms) {
            if (!room.isBooked) {
                System.out.println("Room No: " + room.roomNumber +
                        " | Category: " + room.category +
                        " | Price: ₹" + room.price);
            }
        }
    }

    static void bookRoom() {
        scanner.nextLine();
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        viewAvailableRooms();

        System.out.print("Enter room number to book: ");
        int roomNumber = scanner.nextInt();

        for (Room room : rooms) {
            if (room.roomNumber == roomNumber && !room.isBooked) {

                System.out.println("Room Price: ₹" + room.price);
                System.out.print("Proceed to payment? (yes/no): ");
                scanner.nextLine();
                String payment = scanner.nextLine();

                if (payment.equalsIgnoreCase("yes")) {
                    room.isBooked = true;
                    saveBooking(new Booking(name, room.roomNumber, room.category, room.price));
                    System.out.println("Booking Successful!");
                } else {
                    System.out.println("Payment Cancelled!");
                }
                return;
            }
        }

        System.out.println("Room not available!");
    }

    static void cancelBooking() {
        System.out.print("Enter room number to cancel: ");
        int roomNumber = scanner.nextInt();

        for (Room room : rooms) {
            if (room.roomNumber == roomNumber && room.isBooked) {
                room.isBooked = false;
                removeBooking(roomNumber);
                System.out.println("Booking Cancelled Successfully!");
                return;
            }
        }
        System.out.println("Booking not found!");
    }

    static void saveBooking(Booking booking) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(booking.toString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving booking.");
        }
    }

    static void loadBookings() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int roomNumber = Integer.parseInt(data[1]);
                for (Room room : rooms) {
                    if (room.roomNumber == roomNumber) {
                        room.isBooked = true;
                    }
                }
            }
        } catch (IOException e) {
            // File may not exist first time
        }
    }

    static void viewBookings() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            System.out.println("\nAll Bookings:");
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                System.out.println("Name: " + data[0] +
                        " | Room: " + data[1] +
                        " | Category: " + data[2] +
                        " | Paid: ₹" + data[3]);
            }
        } catch (IOException e) {
            System.out.println("No bookings found.");
        }
    }

    static void removeBooking(int roomNumber) {
        File inputFile = new File(FILE_NAME);
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("," + roomNumber + ",")) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error cancelling booking.");
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }
}