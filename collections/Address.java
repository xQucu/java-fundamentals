public class Address {
    private TelephoneNumber telephoneNumber;
    private String street;
    private String city;

    Address(String countryCode, String localNumber, String street, String city) {
        this.telephoneNumber = new TelephoneNumber(countryCode, localNumber);
        this.street = street;
        this.city = city;
    }

    public TelephoneNumber getTelephoneNumber() {
        return this.telephoneNumber;
    }

    public String getAddress() {
        return this.street + ", " + this.city;
    }

    public void printAddress() {
        System.out.println(this.street + ", " + this.city);
        this.telephoneNumber.printNumber();
    }
}
