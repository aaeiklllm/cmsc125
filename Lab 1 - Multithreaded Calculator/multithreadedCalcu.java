import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class multithreadedCalcu{
    public static List<List<String>> matrix1 = new ArrayList<>();
    public static List<List<String>> matrix2 = new ArrayList<>();
    public static int maxRows = 3;
    public static int maxCols = 3;
    public static int matrix1Rows = 0;
    public static int matrix1Cols = 0;
    public static int matrix2Rows = 0;
    public static int matrix2Cols = 0;

    public static void main(String[] args) {
        System.out.print("\nMATRIX MULTIPLICATION");

        try (Scanner input = new Scanner(System.in)) {
            System.out.println();
            System.out.print("\nAbsolute file path: ");
            String filename = input.nextLine();

            if (!filename.endsWith(".csv")) {
                System.out.println("\nIncorrect file type.");
                filename = input.nextLine();
            } else {
                readCSV(filename);
                System.out.println("\nMATRIX 1");
                printMatrix(matrix1);
                System.out.println("\nMATRIX 2");
                printMatrix(matrix2);

                if (areMatricesCompatible()){
                    dotProduct(matrix1,matrix2);
                }
                else{
                    System.out.println("\nMatrices are not compatible.");
                }
            }
        } 
    }
     
    public static void readCSV(String file) {
        BufferedReader reader = null;
        boolean readingMatrix2 = false; 
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            int rowNumber1 = 0;
            int rowNumber2 = 0;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    readingMatrix2 = true;
                    continue; 
                }

                String[] row = line.split(",");
                List<String> matrixRow = new ArrayList<>();
                int maxColsToRead = readingMatrix2 ? maxCols : maxCols * 2;


                for (int i = 0; i < row.length && (readingMatrix2 || i < maxColsToRead); i++) {
                    matrixRow.add(row[i]);
                }

                if (readingMatrix2) {
                    matrix2.add(matrixRow);
                    rowNumber2++;
                    if (rowNumber2 == 1) {
                        matrix2Cols = matrixRow.size();
                    }
                } else {
                    matrix1.add(matrixRow);
                    rowNumber1++;
                    if (rowNumber1 == 1) {
                        matrix1Cols = matrixRow.size();
                    }
                }
            }
            matrix1Rows = rowNumber1;
            matrix2Rows = rowNumber2;

            if (matrix1Rows > maxRows || matrix1Cols > maxCols || matrix2Rows > maxRows || matrix2Cols > maxCols) {
                System.out.println("\nMatrix dimensions exceed the maximum allowed (3x3).");
                System.exit(1);
            }
        } catch (FileNotFoundException e) {
            System.out.println("\nFile doesn't exist.");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close(); 
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean areMatricesCompatible(){
        if (matrix1Cols == matrix2Rows){
            return true;
        }
        else{
            return false;
        }
    }

    public static List<List<String>> dotProduct(List<List<String>> matrix1, List<List<String>> matrix2){
    
    if (matrix1.isEmpty() && matrix2.isEmpty()){
        System.out.println("\nBoth matrices are empty");
        System.exit(1);
    }

    if (matrix1.isEmpty()){
        System.out.println("\nMatrix 1 is empty");
        System.exit(1);
    } 
   
    if (matrix2.isEmpty()){
        System.out.println("\nMatrix 2 is empty");
        System.exit(1);
    }

    int rows1 = matrix1.size();
    int cols1 = matrix1.get(0).size();
    int cols2 = matrix2.get(0).size();

    List<List<String>> resultMatrix = new ArrayList<>();

        for (int i = 0; i < rows1; i++) {
            List<String> resultRow = new ArrayList<>();
            for (int j = 0; j < cols2; j++) {
                String sum = "0";
                for (int k = 0; k < cols1; k++) {
                    try {
                        String element1 = matrix1.get(i).get(k);
                        String element2 = matrix2.get(k).get(j);
                        sum = addStrings(sum, multiplyStrings(element1, element2));
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Invalid data.");
                        System.exit(1);
                    }
                }
                resultRow.add(sum);
            }
            resultMatrix.add(resultRow);
        }

        System.out.println("\nRESULTING MATRIX");
    printMatrix(resultMatrix);

    return resultMatrix;
}

    public static String multiplyStrings(String num1, String num2) {
        try {
            return String.valueOf(Integer.parseInt(num1) * Integer.parseInt(num2));
        } catch (NumberFormatException e) {
            System.out.println("Invalid data.");
            System.exit(0);
            return null; // This line won't be reached, but you need a return statement
        }
    }


public static String addStrings(String num1, String num2) {
    return String.valueOf(Integer.parseInt(num1) + Integer.parseInt(num2));
}

    public static void printMatrix(List<List<String>> matrix) {
        for (List<String> row : matrix) {
            for (String element : row) {
                System.out.print(element + "\t"); 
            }
            System.out.println();
        }
    }
}