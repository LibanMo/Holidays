import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DataService {

    Connection conn = null;

    String BookingQuery;

    String Pool;
    String Enter;
    String Restaurant;
    String Clubs;
    ArrayList<Room> BookRoom = new ArrayList<Room>();
    int GuestId;
    ArrayList<Person> persons = new ArrayList<Person>();

    public DataService() {
        connectToDb();
    }

    public void connectToDb() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:HolidaysMaker.db");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getPersonData() {

        String query = "SELECT * FROM guests";

        try {
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Id");
                int bookingid = rs.getInt("Booking_id");
                String firstName = rs.getString("First_Name");
                String lastName = rs.getString("Last_Name");
                persons.add(new Person(id, firstName, lastName));

            }
            for (Person p : persons) {
                p.getInfo();
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createGuest() {
        Scanner scanner = new Scanner(System.in);


        System.out.println("Enter Your FirstName");
        String FirstName = scanner.next();
        System.out.println("Enter Your LastName");
        String LastName = scanner.next();
        System.out.println("Enter your phone number");
        String phoneNum = scanner.next();
        System.out.println("Enter your Email Adress");
        String emailAdress = scanner.next();
        System.out.println("Enter your Birth  YYYY-MM-DD");

        String date = scanner.next();

        String query = "Insert into guests(first_name,Last_name,phone_number, email_address, date_of_birth)" +
                " Values(?, ?, ?, ?, ?)";


        try {
            PreparedStatement state = conn.prepareStatement(query);
            state.setString(1, FirstName);
            state.setString(2, LastName);
            state.setString(3, phoneNum);
            state.setString(4, emailAdress);
            state.setString(5, date);

            state.executeUpdate();

            ResultSet keys = state.getGeneratedKeys();

            while (keys.next()) {
                GuestId = keys.getInt(1);
                persons.add(new Person(GuestId, FirstName, LastName));
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    public void CreateBooking() {
        System.out.println("Would You Like To Make a Booking?  1.Yes    0.No");
        Scanner scanner = new Scanner(System.in);
        int val = scanner.nextInt();

        switch (val) {
            case 1:
                System.out.println("Name the date you would like to visit");
                System.out.println("YYYY-MM-DD");
                String checkIn = scanner.next();
                System.out.println();
                System.out.println("YYYY-MM-DD");
                String checkOut = scanner.next();

                System.out.println("How Many Beds Would You Like");
                int amountofBeds = scanner.nextInt();

                System.out.println("What Facility are you searching for?");
                System.out.println(" 1. Pool  ");
                int choicePool = scanner.nextInt();
                System.out.println(" 2. Night Entertainment ");
                int choiceEnter = scanner.nextInt();
                System.out.println(" 3. Restaurant  ");
                int choiceRestaurant = scanner.nextInt();
                System.out.println(" 4. Children's Club ");
                int choiceClubs = scanner.nextInt();

                if (choicePool == 1) {
                    Pool = "Yes";
                } else {
                    Pool = "No";
                }

                if (choiceEnter == 1) {
                    Enter = "Yes";
                } else {
                    Enter = "No";
                }

                if (choiceRestaurant == 1) {
                    Restaurant = "Yes";
                } else {
                    Restaurant = "No";
                }

                if (choiceClubs == 1) {
                    Clubs = "Yes";
                } else {
                    String Clubs = "No";
                }

                Getroom(checkIn, checkOut, amountofBeds, Pool, Restaurant, Enter, Clubs);
        }
    }


    public void Getroom(String checkIn, String checkOut, int amountofBeds, String Pool, String Restaurant, String Enter, String Clubs) {
        ArrayList<Room> rooms = new ArrayList<Room>();

        BookingQuery = "SELECT * FROM Room INNER JOIN Hotel ON Room.Hotel_Id = Hotel.Id " +
                "WHERE Hotel.Pool = ? AND Hotel.Restaurant = ? AND Hotel.Enterteinment = ? AND Hotel.Childrens_club = ? AND Room.Number_of_beds = ? AND Room.Id " +
                "NOT IN ( SELECT Booking_details.Room_Id FROM Booking_details WHERE CheckIn < ? AND CheckOut > ?);";
        ;
        try {
            PreparedStatement state = conn.prepareStatement(BookingQuery);
            state.setString(1, Pool);
            state.setString(2, Restaurant);
            state.setString(3, Enter);
            state.setString(4, Clubs);
            state.setInt(5, amountofBeds);
            state.setString(6, checkIn);
            state.setString(7, checkOut);
            ResultSet rs = state.executeQuery();


            while (rs.next()) {

                int roomId = rs.getInt("Id");
                int hotelId = rs.getInt("Hotel_id");
                int roomNumber = rs.getInt("Number");
                int numberOfBeds = rs.getInt("Number_of_beds");
                String roomName = rs.getString("Name");
                rooms.add(new Room(roomId, hotelId, roomNumber, numberOfBeds, roomName));

            }


            for (int i = 0; i < rooms.size(); i++) {
                System.out.println("Book this press this number: " + i);
                rooms.get(i).roomDetails();
                System.out.println("--------------------------------");
            }
            Scanner scanner = new Scanner(System.in);
            int RoomChoice = scanner.nextInt();

            for (int i = 0; i < rooms.size(); i++) {
                BookRoom.add(rooms.get(i));
            }

            setBooking(GuestId, BookRoom.get(RoomChoice).id, BookRoom.get(RoomChoice).hotelId, checkIn, checkOut, RoomChoice);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void setBooking(int customerId, int roomId, int hotelId, String checkIn, String checkOut, int counter) {
        int choice = counter;
        String query = "INSERT INTO Booking (Customer_Id) VALUES (?)";
        int theBookingId = 0;
        try {
            PreparedStatement state = conn.prepareStatement(query);
            state.setInt(1, customerId);

            ResultSet rs = state.getGeneratedKeys();

            while (rs.next()) {
                theBookingId = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        saveBookingDetails(GuestId, BookRoom.get(choice).id, BookRoom.get(choice).hotelId, checkIn, checkOut);

    }

    public void saveBookingDetails(int theBookingId, int hotelId, int roomId, String checkIn, String checkOut) {
        String query = "INSERT INTO Booking_details (Booking_Id, Hotel_Id, Room_Id, CheckIn, CheckOut) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement state = conn.prepareStatement(query);
            state.setInt(1, theBookingId);
            state.setInt(2, hotelId);
            state.setInt(3, roomId);
            state.setString(4, checkIn);
            state.setString(5, checkOut);

            state.executeUpdate();

            ResultSet keys = state.getGeneratedKeys();
            while (keys.next()) {
                theBookingId = keys.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void showBooking() {
        String query = "SELECT * FROM Booking_Details";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                int bookid = rs.getInt("Booking_id");
                int hotelid = rs.getInt("Hotel_id");
                int roomid = rs.getInt("Room_id");
                String date1 = rs.getString("CheckIn");
                String date2 = rs.getString("CheckOut");

                System.out.println("id: " + id + " BookID: " + bookid + " hotelId: " + hotelid + " roomid: " + roomid + " startDate: " + date1 + " endDate: " + date2);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}