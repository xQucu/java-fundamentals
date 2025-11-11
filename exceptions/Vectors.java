import java.util.ArrayList;

public class Vectors {
    private ArrayList<Vector> vectors;

    public Vectors() {
        this.vectors = new ArrayList<Vector>();
    }

    public void push(Vector v) {
        this.vectors.add(v);
    }

    public Vector tryAdding() throws DifferentVectorsLengthsException {
        assertEqualVectorLengths();
        Vector resultingVector = new Vector();

        for (int i = 0; i < vectors.get(0).getLength(); i++) {
            int sum = 0;
            for (Vector vector : vectors) {
                sum += vector.getElement(i);
            }
            resultingVector.add(sum);
        }
        return resultingVector;
    }

    public void assertEqualVectorLengths() throws DifferentVectorsLengthsException {
        int controlLength = vectors.get(0).getLength();
        for (Vector vector : vectors) {
            if (vector.getLength() != controlLength) {
                throw new DifferentVectorsLengthsException(this.getVectorLengths());
            }
        }
    }

    public ArrayList<Integer> getVectorLengths() {
        ArrayList<Integer> lengths = new ArrayList<Integer>();
        for (Vector vector : vectors) {
            lengths.add(vector.getLength());
        }

        return lengths;
    }
}
