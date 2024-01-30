import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FCFSScheduler implements Scheduler {
    private double AWT = 0;
    private double ATT = 0;
    @Override
    public void scheduleProcesses(List<Process> processes) {
        Collections.sort(processes, Comparator.comparing(Process::getArrivalTime)
                .thenComparing(Process::getBurstTime)
                .thenComparing(Process::getPriority));

        int currentTime = 0;

        if (!processes.isEmpty()) {
            Process firstProcess = processes.get(0);
            currentTime = firstProcess.getArrivalTime();
        }

        for (Process process : processes) {

            // Now 'firstProcess' contains the first process in the list
            // Update completion time
            process.setCompletionTime(currentTime + process.getBurstTime());

            // Update turnaround time
            process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());

            // Update waiting time
            process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());

            // Move the current time to the completion time of the current process
            currentTime = process.getCompletionTime();
        }
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
