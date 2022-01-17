import java.util.ArrayList;

public class Hotel {

    String name;
    String Adress;
    int id;



    public Hotel(String name, String adress, int id) {
        this.name = name;
        Adress = adress;
       this.id = id;
    }

    public void GetHotels(){
        System.out.println(name);
    }

}
