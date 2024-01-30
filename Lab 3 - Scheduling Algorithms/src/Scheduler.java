import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public interface Scheduler {

    void scheduleProcesses(List<Process> processes);

    void getAverages(List<Process> processes);

    default void displayResults(List<Process> processes) {
        // Sort processes by process name
        Collections.sort(processes, Comparator.comparing(Process::getProcessName));

        System.out.println("Process\t\tPrio\t\tAT\t\t\tBT\t\t\tCT\t\t\tTT\t\t\tWT");
        for (Process process : processes) {
            System.out.printf(
                    "%s\t\t\t%d\t\t\t%d\t\t\t%d\t\t\t%d\t\t\t%d\t\t\t%d%n",
                    process.getProcessName(),
                    process.getPriority(),
                    process.getArrivalTime(),
                    process.getBurstTime(),
                    process.getCompletionTime(),
                    process.getTurnaroundTime(),
                    process.getWaitingTime()
            );
        }
    }
}
