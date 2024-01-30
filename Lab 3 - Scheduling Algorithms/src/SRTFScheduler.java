import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class SRTFScheduler implements Scheduler {
    private int currentTime = 0;
    private double AWT = 0;
    private double ATT = 0;
    private boolean adjustmentMade = false;

    @Override
    public void scheduleProcesses(List<Process> processes) {
        int totalTime = calculateTotalBurstTime(processes);
        Process currentProcess = null;
        currentTime = 0;

        if (!processes.isEmpty()) {
            Process firstProcess = processes.get(0);
            currentTime = firstProcess.getArrivalTime();
        }

        for (Process process : processes){
            process.setCurrRemainingBurstTime(process.getBurstTime());
        }


        while (currentTime != totalTime) {
            Process shortestRemainingBTProcess = findShortestRemainingTime(processes, currentTime);

            if (shortestRemainingBTProcess != null) {
                currentProcess = shortestRemainingBTProcess;
//                System.out.println(currentProcess.getProcessName() + " Starting time: " + currentTime);
                executeProcess(currentProcess, processes);
            }
            else {
                break;
            }
        }
    }

    private void executeProcess(Process process, List<Process> processes) {
        // Setting Prev Burst Time
        process.setPrevRemainingBurstTime(process.getCurrRemainingBurstTime());
//        System.out.println(process.getProcessName() + " BT: " + process.getPrevRemainingBurstTime());

        // Setting Current Remaining Burst Time
//        System.out.println("Current Time: " + currentTime);

        // Check if the current process is the first one and its arrival time is not zero
        if (!adjustmentMade && processes.indexOf(process) == 0 && process.getArrivalTime() != 0) {
            currentTime = process.getArrivalTime();
//            System.out.println("Adjustment Current Time: " + currentTime);
            adjustmentMade = true; // Set the flag to true after making the adjustment
        }

        int remainingBurstTime;
        if (process.getArrivalTime() == 0) {
            remainingBurstTime = process.getBurstTime() - (currentTime + 1);
        } else {
            remainingBurstTime = process.getBurstTime() - currentTime;
//            System.out.println("Current Time: " + currentTime);
        }
        remainingBurstTime = Math.max(0, remainingBurstTime); // Set to zero if negative
        process.setCurrRemainingBurstTime(remainingBurstTime);
//        System.out.println(process.getProcessName() + " RBT: " + process.getCurrRemainingBurstTime());

        // Setting Current Time
        currentTime += (process.getPrevRemainingBurstTime() - process.getCurrRemainingBurstTime());
//        System.out.println("Current Time: " + currentTime);

        int completionTime = currentTime + process.getCurrRemainingBurstTime();
//        System.out.println("Completion Time: " + completionTime);
        process.setCompletionTime(completionTime);

        int turnaroundTime = completionTime - process.getArrivalTime();
        process.setTurnaroundTime(turnaroundTime);

        int waitingTime = turnaroundTime - process.getBurstTime();
        process.setWaitingTime(waitingTime);
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

//         Return the process with the shortest remaining time
        return shortestRemainingTimeProcess;
    }

    @Override
    public void getAverages(List<Process> processes) {
        for (Process process : processes) {
            AWT += process.getWaitingTime();
            ATT += process.getTurnaroundTime();
        }

        AWT = AWT/ processes.size();
        ATT = ATT/ processes.size();

        System.out.println("AWT: " + AWT);
        System.out.println("ATT: " + ATT);
        System.out.println();
    }

    @Override
    public void displayResults(List<Process> processes) {
        Scheduler.super.displayResults(processes);
    }
}