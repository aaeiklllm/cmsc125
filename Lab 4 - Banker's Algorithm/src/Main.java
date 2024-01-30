import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static List<List<Integer>> allocation = new ArrayList<>();
    public static List<List<Integer>> max = new ArrayList<>();
    public static List<List<Integer>> need = new ArrayList<>();
    public static List<Integer> available = new ArrayList<>();
    public static List<Integer> work = new ArrayList<>();
    public static List<Boolean> finish = new ArrayList<>();
    public static List<Integer> safeSequence = new ArrayList<>();
    public static int allocationRows = 0;
    public static int allocationCols = 0;
    public static int maxRows = 0;
    public static int maxCols = 0;

    public static void main(String[] args) {
        System.out.print("\nBANKER'S ALGORITHM");
        String filename = "src/input_file3.csv";

        readCSV(filename);
        System.out.println("\nALLOCATION");
        printMatrix(allocation);
        System.out.println("\nMAX");
        printMatrix(max);
        System.out.println("\nAVAILABLE");
        printAvailable(available);

        if (areMatricesCompatible()) {
            calculateNeed();
            System.out.println("\nNEED");
            printMatrix(need);

            if (isSafe()) {
                System.out.print("\nSTATE: Safe - ");
                for (int i = 0; i < safeSequence.size(); i++) {
                    System.out.print("P" + safeSequence.get(i));
                    if (i < safeSequence.size() - 1) {
                        System.out.print(", ");
                    }
                }

            } else {
                System.out.println("\nSTATE: Unsafe");
            }
        } else {
            System.out.println("\nInputs are not compatible with each other.");
        }
    }

    public static void readCSV(String file) {
        BufferedReader reader = null;
        boolean readingNeed = false;
        boolean readingAvailable = false;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (!readingNeed) {
                        readingNeed = true;
                    } else if (!readingAvailable) {
                        readingAvailable = true;
                    }
                    continue;
                }

                String[] row = line.split(",");
                List<Integer> matrixRow = new ArrayList<>();

                try {
                    for (int i = 0; i < row.length; i++) {
                        matrixRow.add(Integer.parseInt(row[i]));
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\nNon-integer value found in the input file.");
                    System.exit(1);
                }

                if (readingNeed && !readingAvailable) {
                    max.add(matrixRow);
                } else if (readingAvailable) {
                    available = matrixRow;
                } else {
                    allocation.add(matrixRow);
                }
            }

            allocationRows = allocation.size();
            allocationCols = allocation.get(0).size();
            maxRows = max.size();
            maxCols = max.get(0).size();
        } catch (FileNotFoundException e) {
            System.out.println("\nError: File doesn't exist.");
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

    public static boolean areMatricesCompatible() {
        if (allocationRows == maxRows && allocationCols == maxCols && available.size() == maxCols) {
            return true;
        } else {
            return false;
        }
    }

    public static void printMatrix(List<List<Integer>> matrix) {
        for (List<Integer> row : matrix) {
            for (Integer element : row) {
                System.out.print(element + "\t");
            }
            System.out.println();
        }
    }

    public static void printAvailable(List<Integer> available) {
        for (Integer element : available) {
            System.out.print(element + "\t");
        }
        System.out.println();
    }

    public static void calculateNeed() {
        need = new ArrayList<>();
        for (int i = 0; i < allocationRows; i++) {
            List<Integer> needRow = new ArrayList<>();
            for (int j = 0; j < allocationCols; j++) {
                needRow.add(max.get(i).get(j) - allocation.get(i).get(j));
            }
            need.add(needRow);
        }
    }
    public static boolean isSafe() {
        work = new ArrayList<>(available);
        finish = new ArrayList<>();

        for (int i = 0; i < allocationRows; i++) {
            finish.add(false);
        }

        boolean systemSafe = true;
        int i = 0;

        while (true) {
            boolean processExecuted = false;

            for (int j = 0; j < allocationRows; j++) {
                if (!finish.get(j) && canFinish(j)) {
                    executeProcess(j);
                    processExecuted = true;
                }
            }

            if (checkAllProcessesFinished()) {
                break;
            }

            if (!processExecuted) {
                systemSafe = false;
                break;
            }

            i = (i + 1) % allocationRows;
        }

        return systemSafe;
    }

    public static boolean checkAllProcessesFinished() {
        for (boolean isFinished : finish) {
            if (!isFinished) {
                return false;
            }
        }
        return true;
    }


    public static boolean canFinish(int process) {
//        System.out.println(process);
        List<Integer> needRow = need.get(process);
//        System.out.println(needRow);
        List<Integer> workCopy = new ArrayList<>(work);
//        System.out.println(workCopy);

        for (int j = 0; j < maxCols; j++) {
            if (needRow.get(j) > workCopy.get(j)) {
//                System.out.println("Element " + needRow.get(j));
                return false;
            }
        }

        return true;
    }

    public static void executeProcess(int process) {
        for (int j = 0; j < allocationCols; j++) {
            work.set(j, work.get(j) + allocation.get(process).get(j));
        }

        finish.set(process, true);
        safeSequence.add(process);

        for (int j = 0; j < allocationCols; j++) {
            available.set(j, available.get(j) + allocation.get(process).get(j));
        }
    }
}
