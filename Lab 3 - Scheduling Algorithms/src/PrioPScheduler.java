import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class PrioPScheduler implements Scheduler {
    private Queue<Process> processQueue = new LinkedList<>();
    private int currentTime = 0;
    private double AWT = 0;
    private double ATT = 0;

    private boolean adjustmentMade = false;

    @Override
    public void scheduleProcesses(List<Process> processes) {
//        for (Process process : processes){
//            System.out.println(process);
//        }

        for (Process process : processes){
            process.setCurrRemainingBurstTime(process.getBurstTime());
        }

        int totalTime = calculateTotalBurstTime(processes);


        while (currentTime < totalTime) {
            Process highestPriorityProcess = findHighestPriority(processes, currentTime);

            if (highestPriorityProcess != null) {
                executeProcess(highestPriorityProcess, processes);
            } else {
                // No process is eligible to execute at the current time
                currentTime++;
            }
        }
    }


    private void executeProcess(Process process, List<Process> processes) {
        // Setting Prev Burst Time
        process.setPrevRemainingBurstTime(process.getCurrRemainingBurstTime());
//        System.out.println(process.getProcessName() + " BT: " + process.getPrevRemainingBurstTime());

        // Setting Current Remaining Burst Time
        int remainingBurstTime = Math.max(0, process.getCurrRemainingBurstTime() - 1);
        process.setCurrRemainingBurstTime(remainingBurstTime);
//        System.out.println(process.getProcessName() + " RBT: " + process.getCurrRemainingBurstTime());

        // Setting Current Time
        currentTime++;

        // Check for a higher-priority process that has arrived
        Process higherPriorityProcess = findHighestPriority(processes, currentTime);
        if (higherPriorityProcess != null &&
                higherPriorityProcess.getPriority() < process.getPriority() &&
                higherPriorityProcess.getArrivalTime() <= currentTime) {

            // Switch to the higher-priority process
            process.setCurrRemainingBurstTime(remainingBurstTime); // Reset the remaining burst time for the current process
//            System.out.println("Switching to " + higherPriorityProcess.getProcessName() + " at time " + currentTime);
            executeProcess(higherPriorityProcess, processes);
            return; // Exit the current execution
        }

        // Check if the process is completed
        if (process.getCurrRemainingBurstTime() == 0) {
            // Continue with the completion logic
//            System.out.println("Completion Time: " + currentTime);
            process.setCompletionTime(currentTime);

            int turnaroundTime = currentTime - process.getArrivalTime();
            process.setTurnaroundTime(turnaroundTime);

            int waitingTime = turnaroundTime - process.getBurstTime();
            process.setWaitingTime(waitingTime);
        } else {
            // Process is not completed, continue execution
//            System.out.println(process.getProcessName() + " continues at time " + currentTime);
            executeProcess(process, processes);
        }
    }


    private int calculateTotalBurstTime(List<Process> processes) {
        int totalTime = 0;

        for (Process process : processes) {
            totalTime += process.getBurstTime();
        }

        return totalTime;
    }

    private Process findHighestPriority(List<Process> processes, int currentTime) {
        // Initialize variables to track the process with the highest priority
        Process highestPriorityProcess = null;
        int highestPriority = Integer.MAX_VALUE;
        int earliestArrivalTime = Integer.MAX_VALUE;

        // Check processes in the list
        for (Process process : processes) {
//            System.out.println(process);
//            System.out.println();
            // Check if the process is eligible and has a higher priority
            if (process.getArrivalTime() <= currentTime && process.getCurrRemainingBurstTime() > 0) {
                if (process.getPriority() < highestPriority ||
                        (process.getPriority() == highestPriority &&
                                process.getArrivalTime() < earliestArrivalTime)) {
                    highestPriorityProcess = process;
                    highestPriority = process.getPriority();
                    earliestArrivalTime = process.getArrivalTime();
                }
            }
        }

//         Print debugging information
//        System.out.println("Selected process " + (highestPriorityProcess != null ?
//                highestPriorityProcess.getProcessName() : "None") +
//                " with priority " + (highestPriorityProcess != null ?
//                highestPriorityProcess.getPriority() : "None") +
//                " at time " + currentTime);

        // Return the process with the highest priority
        return highestPriorityProcess;
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
