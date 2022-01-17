import java.util.ArrayList;

public class Room {
    int id;
    int hotelId;
    int roomNumber;
    int numberOfBeds;
    String name;
    ArrayList<Room> bookedRooms = new ArrayList<Room>();

    public Room(int id, int hotelId, int roomNumber, int numberOfBeds, String name) {
        this.id = id;
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.numberOfBeds = numberOfBeds;
        this.name = name;
    }

    public void roomDetails(){
        System.out.println("Name: +   " + name + " Room id/Hotelid " + id + "/" + hotelId + "   RoomNumber: " + roomNumber + " beds: " + numberOfBeds );
    }

    public void bookedRooms(){

    }


}
