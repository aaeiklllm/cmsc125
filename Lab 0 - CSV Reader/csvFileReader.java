import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class csvFileReader {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Absolute file path: ");
            String filename = input.nextLine();

            if (!filename.endsWith(".csv")) {
                System.out.println("Incorrect file type!");
                filename = input.nextLine();
            } else {
                readCSV(filename);
            }
        }
    }   

    public static void readCSV(String file) {
        BufferedReader reader = null;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null){
                String[] row = line.split(",");
                for (int i = 0; i < row.length; i++) {
                    System.out.printf("%-2s", row[i]);
                }
                System.out.println("");
            }
        }
        catch(FileNotFoundException e){
            System.out.println("File doesn't exist!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}

