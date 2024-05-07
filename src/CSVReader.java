import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

    private String filename;
    private String separator;


    public CSVReader() {
        this("input.csv", ";");
    }

    public CSVReader(String filename) {
        this(filename, ";");
    }

    public CSVReader(String filename, String separator) {
        setFilename(filename);
        setSeparator(separator);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }


    public int[][] lesen() {
        FileReader fileInput;
        BufferedReader input;

        String zeile;

        try {
            fileInput = new FileReader(filename);
            input = new BufferedReader(fileInput);

            zeile = input.readLine();
            int rows = 0;
            int columns = zeile.split(separator).length;

            while (zeile != null) {
                rows++;
                zeile = input.readLine();
            }

            int[][] matrix = new int[rows][columns];

            fileInput.close();
            input = new BufferedReader(new FileReader(filename));

            int i = 0;
            zeile = input.readLine();

            while (zeile != null) {
                String[] feldArray = zeile.split(separator);

                for (int j = 0; j < feldArray.length; j++) {
                    matrix[i][j] = Integer.parseInt(feldArray[j]);
                }

                zeile = input.readLine();
                i++;
            }

            input.close();
            return matrix;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
