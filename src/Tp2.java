import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Scanner;

public class Tp2 {
    public static void main(String[] args) {
        solution sol = new solution();
        sol.launch(args);
    }
}

class solution {

    private String[] args;

    public void launch(String[] args) {
        this.args = args;
        parseFile();
    }

    public void parseFile() {
        String path = "tests/exemple1.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();

            Scanner scanner = new Scanner(line);

            LineNumberReader reader = new LineNumberReader(new FileReader(path));
            reader.skip(Integer.MAX_VALUE);
            int size = (reader.getLineNumber() - 1) * 2; // Bound le nombre maximal de inputs
            reader.close();

            while ((line = br.readLine()) != null) {
                scanner = new Scanner(line);
                scanner.useDelimiter("[^0-9.-]+");

                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

    }

}