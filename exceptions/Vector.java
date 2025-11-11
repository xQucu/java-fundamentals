import java.util.ArrayList;

public class Vector {
    private int length;
    private ArrayList<Integer> elements;

    public int getLength() {
        return length;
    }

    public Vector(ArrayList<Integer> elements) {
        this.elements = new ArrayList<Integer>();
        this.elements.addAll(elements);
        this.length = elements.size();
    }

    public Vector() {
        this.elements = new ArrayList<Integer>();
        this.length = 0;
    }

    public String toString() {
        return this.elements.toString();

    }

    public int getElement(int idx) {
        return elements.get(idx);
    }

    public void add(int element) {
        this.elements.add(element);
    }
}
