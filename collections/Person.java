public class Person extends TelephoneEntry {
    private String lastName;

    Person(String firstName, String lastName, String countryCode, String localNumber, String street, String city) {
        super(firstName, street, city, countryCode, localNumber);
        this.lastName = lastName;
    }

    TelephoneNumber getTelephoneNumber() {
        return this.address.getTelephoneNumber();
    }

    String getAddress() {
        return this.address.getAddress();
    }

    String getName() {
        return this.name + " " + this.lastName;
    }

    void description() {
        System.out.println(this.name + " " + this.lastName);
        this.address.printAddress();
    }
}
