import java.util.ArrayList;

public class DifferentVectorsLengthsException extends Exception {
    private ArrayList<Integer> lengths;

    public DifferentVectorsLengthsException(String message, ArrayList<Integer> lengths) {
        super(message);
        this.lengths = lengths;
    }

    public DifferentVectorsLengthsException(String message) {
        super(message);
    }

    public ArrayList<Integer> getLengths() {
        return lengths;
    }

}
