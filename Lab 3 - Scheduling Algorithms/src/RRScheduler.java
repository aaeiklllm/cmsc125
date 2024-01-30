import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;

public class RRScheduler implements Scheduler {
    Queue<Process> processQueue = new LinkedList<>();

    private int currentTime = 0;
    private int timeQuantum = 0;
    private double AWT = 0;
    private double ATT = 0;

    @Override
    public void scheduleProcesses(List<Process> processes) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the time quantum: ");
            timeQuantum = scanner.nextInt();

            processQueue = new LinkedList<>(processes);
            int totalTime = calculateTotalBurstTime(processes);
            currentTime = 0;

            for (Process process : processes) {
                process.setCurrRemainingBurstTime(process.getBurstTime());
            }

            while (!processQueue.isEmpty() || currentTime != totalTime) {
                Process process = processQueue.poll();  // Remove and retrieve the head of the queue
                if (process != null) {
                    executeProcess(process);
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid integer.");
        }
    }

    private void executeProcess(Process process) {
        int remainingBurstTime = Math.min(timeQuantum, process.getCurrRemainingBurstTime());
//        System.out.println(process.getProcessName() + " BT: " + remainingBurstTime);

        process.setPrevRemainingBurstTime(process.getCurrRemainingBurstTime());
        process.setCurrRemainingBurstTime(process.getCurrRemainingBurstTime() - remainingBurstTime);

//        System.out.println(process.getProcessName() + " RBT: " + process.getCurrRemainingBurstTime());

        currentTime += remainingBurstTime;
//        System.out.println("Current Time: " + currentTime);

        if (process.getCurrRemainingBurstTime() == 0) {
            int completionTime = currentTime;
//            System.out.println("Completion Time: " + completionTime);
            process.setCompletionTime(completionTime);

            int turnaroundTime = completionTime - process.getArrivalTime();
            process.setTurnaroundTime(turnaroundTime);

            int waitingTime = turnaroundTime - process.getBurstTime();
            process.setWaitingTime(waitingTime);
        } else {
//             Process still has remaining burst time, add it back to the queue
            processQueue.add(process);
        }
    }

    private int calculateTotalBurstTime(List<Process> processes) {
        int totalTime = 0;

        for (Process process : processes) {
            totalTime += process.getBurstTime();
        }

        return totalTime;
    }

    private Process findShortestRemainingTime(List<Process> processes, int currentTime) {
        // Initialize variables to track the process with the shortest remaining time
        Process shortestRemainingTimeProcess = null;
        int shortestRemainingTime = Integer.MAX_VALUE;
        int earliestArrivalTime = Integer.MAX_VALUE;

        // Check processes in the list
        for (Process process : processes) {
            // Check if the process is eligible and has a shorter remaining burst time
            if (process.getArrivalTime() <= currentTime && process.getCurrRemainingBurstTime() > 0) {
                if (process.getCurrRemainingBurstTime() < shortestRemainingTime ||
                        (process.getCurrRemainingBurstTime() == shortestRemainingTime &&
                                process.getArrivalTime() < earliestArrivalTime)) {
                    shortestRemainingTimeProcess = process;
                    shortestRemainingTime = process.getCurrRemainingBurstTime();
                    earliestArrivalTime = process.getArrivalTime();
                }
            }
        }

        // Print debugging information
//        if (shortestRemainingTimeProcess != null) {
//            System.out.println();
//            System.out.println("Selected process " + shortestRemainingTimeProcess.getProcessName() +
//                    " with burst time " + shortestRemainingTimeProcess.getCurrRemainingBurstTime());
//        }

        // Return the process with the shortest remaining time
        return shortestRemainingTimeProcess;
    }

    @Override
    public void getAverages(List<Process> processes) {
        for (Process process : processes) {
            AWT += process.getWaitingTime();
            ATT += process.getTurnaroundTime();
        }

        AWT = AWT / processes.size();
        ATT = ATT / processes.size();

        System.out.println("AWT: " + AWT);
        System.out.println("ATT: " + ATT);
        System.out.println();
    }

    @Override
    public void displayResults(List<Process> processes) {
        Scheduler.super.displayResults(processes);
    }
}
