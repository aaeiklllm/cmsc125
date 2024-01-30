import java.util.ArrayList;
import java.util.List;

public class SJFScheduler implements Scheduler {
    private double AWT = 0;
    private double ATT = 0;
    @Override
    public void scheduleProcesses(List<Process> processes) {
        int currentTime = 0;
        List<Process> arrivedProcesses = new ArrayList<>(processes);

        while (!arrivedProcesses.isEmpty()) {
            Process shortestBurstProcess = findShortestBurstTime(arrivedProcesses, currentTime);

            //If there is no shortest BT for the current time
            while ((shortestBurstProcess = findShortestBurstTime(arrivedProcesses, currentTime)) == null) {
                currentTime++;
            }


            // Check if the process has arrived
            if (currentTime < shortestBurstProcess.getArrivalTime()) {
                currentTime = shortestBurstProcess.getArrivalTime();
            }

            // Update completion time
            shortestBurstProcess.setCompletionTime(currentTime + shortestBurstProcess.getBurstTime());

            // Update turnaround time
            shortestBurstProcess.setTurnaroundTime(shortestBurstProcess.getCompletionTime() - shortestBurstProcess.getArrivalTime());

            // Update waiting time
            shortestBurstProcess.setWaitingTime(shortestBurstProcess.getTurnaroundTime() - shortestBurstProcess.getBurstTime());

            // Move the current time to the completion time of the current process
            currentTime = shortestBurstProcess.getCompletionTime();

            // Remove the scheduled process from the arrived processes list
            arrivedProcesses.remove(shortestBurstProcess);
        }
    }

    private Process findShortestBurstTime(List<Process> processes, int currentTime) {
        Process shortestBurstProcess = null;
        int shortestBurstTime = Integer.MAX_VALUE;

        for (Process process : processes) {
            if (process.getArrivalTime() <= currentTime && process.getBurstTime() < shortestBurstTime) {
                shortestBurstProcess = process;
                shortestBurstTime = process.getBurstTime();
            }
        }

        return shortestBurstProcess;
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
