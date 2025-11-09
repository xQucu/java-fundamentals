import java.util.*;

public class TelephoneApp {
    public static void main(String[] args) {
        TreeMap<TelephoneNumber, TelephoneEntry> telephoneEntries = new TreeMap<TelephoneNumber, TelephoneEntry>();

        TelephoneEntry[] entries = {
                new Person("Alice", "Brown", "44", "441 789 334", "22 Baker Street", "London"),
                new Person("Michael", "Davis", "1", "305 887 9932", "14 Ocean Drive", "Miami"),
                new Person("Sophie", "Kowalska", "48", "692 115 487", "Nowowiejska 45", "Warszawa"),
                new Person("Hiroshi", "Tanaka", "81", "03 4567 8999", "Shibuya 2-15-1", "Tokyo"),
                new Person("Emma", "Dupont", "33", "01 42 58 99 12", "Rue de Rivoli 77", "Paris"),
                new Person("Carlos", "Gonzalez", "34", "91 223 4455", "Gran Via 101", "Madrid"),
                new Company("Amazon", "1", "206 266 1000", "410 Terry Ave North", "Seattle"),
                new Company("Samsung", "82", "02 2255 0114", "129 Samsung-ro", "Seoul"),
                new Company("BMW", "49", "089 382 0", "Petuelring 130", "Munich"),
                new Company("Spotify", "46", "08 517 617 00", "Regeringsgatan 19", "Stockholm")
        };

        for (TelephoneEntry entry : entries) {
            telephoneEntries.put(entry.getTelephoneNumber(), entry);
        }

        Iterator<Map.Entry<TelephoneNumber, TelephoneEntry>> iterator = telephoneEntries.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<TelephoneNumber, TelephoneEntry> pair = iterator.next();
            pair.getValue().description();
        }
    }
}
