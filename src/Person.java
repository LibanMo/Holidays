public class Person
{
    String firstName;
    String lastName;
    int id;
    int BookingId;

    public Person(int id, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    void getInfo(){
        System.out.println("id " + id + " FirstName " + firstName + " LastName " + lastName +
                "BookingId: " + BookingId ) ;
    }
}
