import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StopwatchClock extends Clock {
    private int time = 0;
    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;
    private boolean stop;

    StopwatchClock() {
        stop = false;
    }

    @Override
    public void run() {
        while (!stop) {
            hours = time / 3600;
            minutes = (time % 3600) / 60;
            seconds = (time % 3600) % 60;
            time++;

            System.out.printf("%02d:%02d:%02d\n", hours, minutes, seconds);

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Error");
            }
        }
    }

    void stopwatch() {
        Thread thread = new Thread(this);
        thread.start();

        // For user input handling
        Thread userInputThread = new Thread(() -> {
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                userInputReader.readLine(); // Wait for any input (Enter key)
            } catch (IOException e) {
                e.printStackTrace();
            }
            stop(); // Stop the stopwatch when any character is pressed
        });
        userInputThread.start();

        try {
            // Wait for the stopwatch thread to finish
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Wait for the user input thread to finish
        try {
            userInputThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.print("Elapsed time: ");
        System.out.printf("%02d:%02d:%02d\n", hours, minutes, seconds);
        System.out.println();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Error");
        }
    }

    void stop() {
        stop = true;
    }

}
