import java.text.SimpleDateFormat;
import java.util.Date;
public class Clock implements Runnable{
    private Date date = new Date();
    private boolean running = true;
    public Date getCurrentTime() {
        date.setTime(System.currentTimeMillis());
        return date;
    }
    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        while(true) {
            date.setTime(System.currentTimeMillis());
            System.out.print("\r" + sdf.format(date));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Error: Thread interrupted");
                // Restore interrupted status
                Thread.currentThread().interrupt();
            }
        }
    }
}
