import java.util.ArrayList;

public class DifferentVectorsLengthsException extends Exception {
    private ArrayList<Integer> lengths;

    public DifferentVectorsLengthsException(ArrayList<Integer> lengths) {
        super("DifferentVectorsLengthsException");
        this.lengths = lengths;
    }

    public ArrayList<Integer> getLengths() {
        return lengths;
    }

}
