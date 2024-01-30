import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        enterFileName();
    }

    public static void enterFileName() {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("Enter file name");
            System.out.println("(Press enter if file name is processes.csv)");
            String filename = input.nextLine();

            if (filename.isEmpty()) {
                filename = "processes.csv";
                readCSV(filename);
            } else if (!filename.endsWith(".csv")) {
                System.out.println("Incorrect file type!");
            } else {
                readCSV(filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readCSV(String csvFile) {
        String line;
        String csvSplitBy = "\\s+"; // Use "\\s+" for space-separated values

        //Stores columns in csv in an arraylist
        ArrayList<Integer> priority = new ArrayList<>();
        ArrayList<Integer> arrivalTime = new ArrayList<>();
        ArrayList<Integer> burstTime = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(csvSplitBy);

                if (values.length >= 3) {
                    //Listing processes per column
                    priority.add(Integer.parseInt(values[0]));
                    arrivalTime.add(Integer.parseInt(values[1]));
                    burstTime.add(Integer.parseInt(values[2]));
                } else {
                    System.out.println("Skipping line with insufficient values: " + line);
                }
            }

//            System.out.println("Priority: " + priority);
//            System.out.println("AT: " + arrivalTime);
//            System.out.println("BT: " + burstTime);

            // Creating a list of processes
            List<Process> processes = createProcesses(priority, arrivalTime, burstTime);

            //Calling the method enterChoice to choose which scheduling algorithm
            enterChoice(processes);

        } catch (IOException e) {
            System.out.println("Error reading the file. Please make sure the file exists and is in the correct format.");
        } catch (NumberFormatException e) {
            System.out.println("Error parsing integer value. Please check the format of the CSV file.");
            e.printStackTrace();
        }
    }

    public static void enterChoice(List<Process> origProcesses) {
        boolean repeat = true;
        try (Scanner input = new Scanner(System.in)) {
            do {
                System.out.println("Please select your scheduling algorithm:");
                System.out.println("1 - FCFS");
                System.out.println("2 - SJF");
                System.out.println("3 - SRTF");
                System.out.println("4 - Round Robin");
                System.out.println("5 - Priority (preemptive)");
                System.out.println("6 - Priority (nonpreemptive)");
                System.out.println("7 - Quit");
                System.out.print("Select algorithm: ");

                int choice = Integer.parseInt(input.nextLine());
                List<Process> processes = new ArrayList<>(origProcesses);

                switch (choice) {
                    case 1: //FCFS //DONE
                        System.out.println("First Come First Serve Algorithm");
                        Collections.sort(processes, (p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
                        Scheduler fcfsScheduler = new FCFSScheduler();
                        fcfsScheduler.scheduleProcesses(processes);
                        fcfsScheduler.displayResults(processes);
                        fcfsScheduler.getAverages(processes);
                        break;

                    case 2: //SJF //DONE
                        System.out.println("Shortest Job First Algorithm");
                        Collections.sort(processes, (p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
                        Scheduler sjfScheduler = new SJFScheduler();
                        sjfScheduler.scheduleProcesses(processes);
                        sjfScheduler.displayResults(processes);
                        sjfScheduler.getAverages(processes);
                        break;

                    case 3: //SRTF //DONE
                        System.out.println("Shortest Remaining Time First Algorithm");
                        Collections.sort(processes, Comparator
                                .<Process, Integer>comparing(Process::getArrivalTime)
                                .thenComparing(Process::getBurstTime)
                        );
                        Scheduler srtfScheduler = new SRTFScheduler();
                        srtfScheduler.scheduleProcesses(processes);
                        srtfScheduler.displayResults(processes);
                        srtfScheduler.getAverages(processes);
                        break;

                    case 4: //RR //DONE
                        System.out.println("Round Robin Algorithm");
                        Collections.sort(processes, (p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
                        Scheduler rrScheduler = new RRScheduler();
                        rrScheduler.scheduleProcesses(processes);
                        rrScheduler.displayResults(processes);
                        rrScheduler.getAverages(processes);
                        break;

                    case 5: //Prio P
                        System.out.println("Priority Preemptive Algorithm");
                        Collections.sort(processes, (p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
                        Scheduler prioPScheduler = new PrioPScheduler();
                        prioPScheduler.scheduleProcesses(processes);
                        prioPScheduler.displayResults(processes);
                        prioPScheduler.getAverages(processes);
                        break;

                    case 6: //Prio NP
                        System.out.println("Priority Non-preemptive Algorithm");
                        Collections.sort(processes, Comparator
                                .comparingInt(Process::getPriority)
                                .thenComparingInt(Process::getArrivalTime));
                        Scheduler prioNPScheduler = new PrioNPScheduler();
                        prioNPScheduler.scheduleProcesses(processes);
                        prioNPScheduler.displayResults(processes);
                        prioNPScheduler.getAverages(processes);
                        break;

                    case 7:
                        repeat = false;
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Choose an option!");
                        System.out.println();
                }
            } while (repeat);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    // Method to create a list of processes from column data
    private static List<Process> createProcesses(List<Integer> priority, List<Integer> arrivalTime, List<Integer> burstTime) {
        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < priority.size(); i++) {
            processes.add(new Process("P" + (i + 1), arrivalTime.get(i), burstTime.get(i), priority.get(i)));
        }
        return processes;
    }
}
