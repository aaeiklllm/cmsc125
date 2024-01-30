import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlarmClock extends Clock {
    private Date endTime; // the date/time set by the user
    private String message;
    private volatile boolean timerDone;

    public AlarmClock() {
    }

    public void setAlarm(Date endTime, String message) {
        this.endTime = endTime;
        this.message = message;
    }

    @Override
    public void run() {
        // Calculate the delay until the end time
        long delayMillis = endTime.getTime() - System.currentTimeMillis();

        // Schedule a task to run after the delay
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            // Perform a one minute action after endTime is reached
            String minute = "0:59";
            String pattern = "mm:ss";

            SimpleDateFormat sdf2 = new SimpleDateFormat(pattern);
            Date setTime = null;

            try {
                setTime = sdf2.parse(minute); // set inputted time to sdf format
            } catch (Exception e) {
                System.out.println("Error in parsing.");
            }

            TimerClock timerClock = new TimerClock();
            timerClock.setTimer(setTime, message);
            timerClock.timer();
            timerDone = true;

            // Shutdown the scheduler after the task is executed
            scheduler.shutdown();
        }, delayMillis, TimeUnit.MILLISECONDS);
    }

    void alarm() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public boolean isTimerDone() {
        return timerDone;
    }
}
