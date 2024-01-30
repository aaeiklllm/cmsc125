import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrioNPScheduler implements Scheduler {
    private double AWT = 0;
    private double ATT = 0;

    @Override
    public void scheduleProcesses(List<Process> processes) {
        int currentTime = 0;
        List<Process> arrivedProcesses = new ArrayList<>(processes);

        while (!arrivedProcesses.isEmpty()) {
            Process highestPriorityProcess = findHighestPriority(arrivedProcesses, currentTime);

            // If there is no process with the highest priority for the current time
            while ((highestPriorityProcess = findHighestPriority(arrivedProcesses, currentTime)) == null) {
                currentTime++;
            }

            // Check if the process has arrived
            if (currentTime < highestPriorityProcess.getArrivalTime()) {
                currentTime = highestPriorityProcess.getArrivalTime();
            }

            // Update completion time
            highestPriorityProcess.setCompletionTime(currentTime + highestPriorityProcess.getBurstTime());

            // Update turnaround time
            highestPriorityProcess.setTurnaroundTime(highestPriorityProcess.getCompletionTime() - highestPriorityProcess.getArrivalTime());

            // Update waiting time
            highestPriorityProcess.setWaitingTime(highestPriorityProcess.getTurnaroundTime() - highestPriorityProcess.getBurstTime());

            // Move the current time to the completion time of the current process
            currentTime = highestPriorityProcess.getCompletionTime();

            // Remove the scheduled process from the arrived processes list
            arrivedProcesses.remove(highestPriorityProcess);
        }
    }

    private Process findHighestPriority(List<Process> processes, int currentTime) {
        Process highestPriorityProcess = null;
        int highestPriority = Integer.MAX_VALUE;

        for (Process process : processes) {
            if (process.getArrivalTime() <= currentTime && process.getPriority() < highestPriority) {
                highestPriorityProcess = process;
                highestPriority = process.getPriority();
            }
        }

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
