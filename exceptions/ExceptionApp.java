import java.util.ArrayList;

public class ExceptionApp {
    public static void main(String[] args) {
        IOManager iom = new IOManager();

        boolean areVectorsValid = false;
        while (!areVectorsValid) {
            Vectors vectors = new Vectors();
            iom.print("Please input vector elements separated by comma.");
            getVectors(iom, vectors);

            try {
                Vector resultingVector = vectors.tryAdding();
                iom.print("Adding vectors result in: " + resultingVector.toString());
                areVectorsValid = true;
            } catch (DifferentVectorsLengthsException e) {
                iom.print(e.getMessage());
                iom.print("First vector is " + e.getLengths().get(0));
                iom.print("Please try inputting vectors once again.");
            }
        }
    }

    private static void getVectors(IOManager iom, Vectors vectors) {
        boolean running = true;
        while (running) {
            ArrayList<Integer> numbers = iom.handleInput();
            if (numbers.isEmpty() && !vectors.getVectorLengths().isEmpty()) {
                running = false;
            } else {
                Vector vector = new Vector(numbers);
                vectors.push(vector);
            }
        }
    }
}
