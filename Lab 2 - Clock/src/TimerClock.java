import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerClock extends Clock {
    private String setTime; // the date/time set by the user

    private int setMinutes = 0;
    private int setSeconds = 0;
    private int time = 0;
    private int minutes = 0;
    private int seconds = 0;

    private String message = "";

    TimerClock() {
    }

    public void setTimer(Date time){
        String pattern = "mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        setTime = sdf.format(time); //format to make it string & show only time
    }

    public void setTimer(Date time, String message){
        String pattern = "mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        setTime = sdf.format(time); //format to make it string & show only time
        this.message = message;
    }

    @Override
    public void run() {
        for (int i = time; i >= 0; i--) {
            minutes = i / 60;
            seconds = i % 60;

            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            Date currentTime = new Date(); // Consider using LocalTime or other modern time classes
            currentTime.setMinutes(minutes);
            currentTime.setSeconds(seconds);

            if (message == "") {
                System.out.println(sdf.format(currentTime));
            }
            else {
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                Date currentTime2 = getCurrentTime(); // Update the current time
                String time = sdf2.format(currentTime2);
                System.out.println(time + " " + message);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
            }
        }
    }

    void timer() {
        String[] timeSplit = setTime.split(":");
        setMinutes = Integer.parseInt(timeSplit[0]);
        setSeconds = Integer.parseInt(timeSplit[1]);
        time = setMinutes * 60 + setSeconds;

        Thread thread = new Thread(this);
        thread.start();

        try {
            thread.join(); // wait for the thread to finish before displaying elapsed time
        } catch (InterruptedException e) {
           System.out.println("Thread interrupted.");
        }

        System.out.println();
    }
}
