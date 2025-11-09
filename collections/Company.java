public class Company extends TelephoneEntry {

    Company(String name, String countryCode, String localNumber, String street, String city) {
        super(name, street, city, countryCode, localNumber);
    }

    TelephoneNumber getTelephoneNumber() {
        return this.address.getTelephoneNumber();
    }

    String getAddress() {
        return this.address.getAddress();
    }

    String getName() {
        return this.name;
    }

    void description() {
        System.out.println(this.name);
        this.address.printAddress();
    }
}
