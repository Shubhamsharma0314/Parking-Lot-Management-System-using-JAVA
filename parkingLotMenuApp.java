import java.util.*;

// Vehicle ......
class Vehicle {
    String type, registration, color;
    long TimeIn;

    Vehicle(String type, String registration, String color) {
        this.type = type;
        this.registration = registration;
        this.color = color;
        this.TimeIn = System.currentTimeMillis();
    }
}

// Slot ......
class Slot {
    String type;
    Vehicle vehicle;
    String ticket_Id;

    Slot(String type) {
        this.type = type;
    }

    boolean isAvailable() {
        return vehicle == null;
    }
}

public class ParkingLotMenuApp {
    static String parkingLotId;
    static List<List<Slot>> slots = new ArrayList<>();

    static void createParkingLot(String id, int floors, int slotsPerFloor) {
        parkingLotId = id;
        for (int i = 0; i < floors; i++) {
            List<Slot> floor = new ArrayList<>();
            for (int j = 0; j < slotsPerFloor; j++) {
                if (j == 0)
                    floor.add(new Slot("TRUCK"));
                else if (j == 1 || j == 2)
                    floor.add(new Slot("BIKE"));
                else
                    floor.add(new Slot("CAR"));
            }
            slots.add(floor);
        }
        System.out.println("Parking Lot Created Successfully!");
    }

    static int getRate(String type) {
        switch (type.toUpperCase()) {
            case "CAR":
                return 20;
            case "BIKE":
                return 10;
            case "TRUCK":
                return 40;
            default:
                return 0;
        }
    }

    static void parkVehicle(String type, String registration, String color) {
        for (int i = 0; i < slots.size(); i++) {
            for (int j = 0; j < slots.get(i).size(); j++) {
                Slot s = slots.get(i).get(j);
                if (s.type.equalsIgnoreCase(type) && s.isAvailable()) {
                    s.vehicle = new Vehicle(type, registration, color);
                    s.ticket_Id = parkingLotId + "_" + (i + 1) + "_" + (j + 1);
                    System.out.println("Parked! Ticket ID: " + s.ticket_Id);
                    return;
                }
            }
        }
        System.out.println("No Slot Available for " + type);
    }

    static void unPark(String ticket_Id) {
        try {
            String[] parts = ticket_Id.split("_");
            int floor = Integer.parseInt(parts[1]) - 1;
            int slotNo = Integer.parseInt(parts[2]) - 1;
            Slot s = slots.get(floor).get(slotNo);
            long TimeOut = System.currentTimeMillis();
            long durationMillis = TimeOut - s.vehicle.TimeIn;

            double hours = Math.ceil(durationMillis / (1000.0 * 60 * 60));

            int rate = getRate(s.vehicle.type);
            double amount = hours * rate;

            System.out.println("Vehicle Type: " + s.vehicle.type);
            System.out.println("Parked Hours: " + (int) hours);
            System.out.println("Rate per Hour: Rs." + rate);
            System.out.println("Total Amount: Rs." + amount);

            if (!s.isAvailable()) {
                s.vehicle = null;
                s.ticket_Id = null;
                System.out.println("Unparked Successfully!");
            } else {
                System.out.println("Slot Already Empty!");
            }
        } catch (Exception e) {
            System.out.println("Invalid Ticket ID!");
        }
    }

    static void displaySlots(String type, boolean available) {
        for (int i = 0; i < slots.size(); i++) {
            System.out.print((available ? "Free" : "Occupied") +
                    " slots for " + type + " on Floor " + (i + 1) + ": ");
            for (int j = 0; j < slots.get(i).size(); j++) {
                Slot s = slots.get(i).get(j);
                if (s.type.equalsIgnoreCase(type) &&
                        (available ? s.isAvailable() : !s.isAvailable())) {
                    System.out.print((j + 1) + " ");
                }
            }
            System.out.println();
        }
    }

    static void countFreeSlots(String type) {
        for (int i = 0; i < slots.size(); i++) {
            int count = 0;
            for (Slot s : slots.get(i))
                if (s.type.equalsIgnoreCase(type) && s.isAvailable())
                    count++;
            System.out.println("Free " + type + " slots on Floor " + (i + 1) + ": " + count);
        }
    }

    public static void main(String[] args) {
        Scanner obj = new Scanner(System.in);

        System.out.print("Enter Parking Lot ID: ");
        String id = obj.nextLine();
        System.out.print("Enter Floors: ");
        int floors = obj.nextInt();
        System.out.print("Enter Slots per Floor: ");
        int spf = obj.nextInt();
        createParkingLot(id, floors, spf);

        while (true) {
            System.out
                    .println("\n1.Park  \n2.Unpark  \n3.Count Free  \n4.Display Free  \n5.Display Occupied  \n6.Exit");
            int ch = obj.nextInt();
            obj.nextLine();

            switch (ch) {
                case 1:
                    System.out.print("Type(CAR/BIKE/TRUCK): ");
                    String type = obj.nextLine();
                    System.out.print("Reg No: ");
                    String reg = obj.nextLine();
                    System.out.print("Color: ");
                    String color = obj.nextLine();
                    parkVehicle(type, reg, color);
                    break;

                case 2:
                    System.out.print("Enter Ticket ID: ");
                    unPark(obj.nextLine());
                    break;

                case 3:
                    System.out.print("Type: ");
                    countFreeSlots(obj.nextLine());
                    break;

                case 4:
                    System.out.print("Type: ");
                    displaySlots(obj.nextLine(), true);
                    break;

                case 5:
                    System.out.print("Type: ");
                    displaySlots(obj.nextLine(), false);
                    break;

                case 6:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}