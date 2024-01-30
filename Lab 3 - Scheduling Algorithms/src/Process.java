public class Process {
    private final String processName;
    private final int arrivalTime;
    private int burstTime;
    private final int priority;
    private int completionTime;
    private int waitingTime;
    private int turnaroundTime;

    public int prevRemainingBurstTime;
    public int currRemainingBurstTime;

    private Process(String processName, int arrivalTime, int burstTime, int priority, int prevRemainingBurstTime, int currRemainingBurstTime, int completionTime, int waitingTime, int turnaroundTime) {
        this.processName = processName;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.prevRemainingBurstTime = burstTime;
        this.currRemainingBurstTime = burstTime;
        this.completionTime = completionTime;
        this.waitingTime = waitingTime;
        this.turnaroundTime = turnaroundTime;
    }

    public Process(String processName, int arrivalTime, int burstTime, int priority) {
        this(processName, arrivalTime, burstTime, priority, burstTime, burstTime, 0, 0, 0);
    }



    public void setBurstTime(int burstTime)
    {
        this.burstTime = burstTime;
    }

    public void setPrevRemainingBurstTime(int prevRemainingBurstTime)
    {
        this.prevRemainingBurstTime = prevRemainingBurstTime;
    }

    public void setCurrRemainingBurstTime(int currRemainingBurstTime)
    {
        this.currRemainingBurstTime = currRemainingBurstTime;
    }
    public void setCompletionTime(int completionTime)
    {
        this.completionTime = completionTime;
    }

    public void setWaitingTime(int waitingTime)
    {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime)
    {
        this.turnaroundTime = turnaroundTime;
    }

    public String getProcessName()
    {
        return this.processName;
    }

    public int getArrivalTime()
    {
        return this.arrivalTime;
    }

    public int getBurstTime()
    {
        return this.burstTime;
    }

    int getPrevRemainingBurstTime(){
        return this.prevRemainingBurstTime;
    }

    int getCurrRemainingBurstTime(){
        return this.currRemainingBurstTime;
    }

    public int getPriority()
    {
        return this.priority;
    }

    public int getCompletionTime()
    {
        return this.completionTime;
    }
    public int getWaitingTime()
    {
        return this.waitingTime;
    }

    public int getTurnaroundTime()
    {
        return this.turnaroundTime;
    }

    @Override
    public String toString() {
        return "Process{" +
                "processName='" + processName + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", priority=" + priority +
                '}';
    }

}
