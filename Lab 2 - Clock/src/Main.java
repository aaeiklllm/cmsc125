
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        boolean repeat = true;
        try (Scanner input = new Scanner(System.in)) {
            do {

                System.out.println("1) Alarm");
                System.out.println("2) Timer");
                System.out.println("3) Stopwatch");
                System.out.println("4) Exit Program");
                System.out.print("Choose an option: ");

                int choice = Integer.parseInt((input.nextLine()));

                switch (choice) {
                    case 1:
                        try {
                            System.out.print("\nAlarm");
                            Calendar currentCalendar = Calendar.getInstance(); //current day

                            Calendar nextDayCalendar = Calendar.getInstance();
                            nextDayCalendar.add(Calendar.DAY_OF_YEAR, 1); //tomorrow

                            Clock clock = new Clock();
                            Date currentDate = clock.getCurrentTime();
                             System.out.println("\nCurrent Date: " + currentDate);

                            System.out.print("\nEnter time (Format HH:mm): ");
                            String inputTime = input.nextLine(); //getting user input time

                            String pattern = "HH:mm";
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            Date endTime = sdf.parse(inputTime); //set inputted time to sdf format

                            Calendar endCalendar = Calendar.getInstance();
                            endCalendar.setTime(endTime);

                            currentCalendar.set(Calendar.HOUR_OF_DAY, endCalendar.get(Calendar.HOUR_OF_DAY));
                            currentCalendar.set(Calendar.MINUTE, endCalendar.get(Calendar.MINUTE));
                            currentCalendar.set(Calendar.SECOND, 0);
                            endTime = currentCalendar.getTime(); // set time to current date

                            if (endTime.before(currentDate)) {
                                currentCalendar.set(Calendar.YEAR, nextDayCalendar.get(Calendar.YEAR));
                                currentCalendar.set(Calendar.MONTH, nextDayCalendar.get(Calendar.MONTH));
                                currentCalendar.set(Calendar.DAY_OF_MONTH, nextDayCalendar.get(Calendar.DAY_OF_MONTH));

                                endTime = currentCalendar.getTime(); //set end date tomorrow
                            }

                            System.out.println("End Date: " + endTime);

                            System.out.print("Message: ");

                            String message = input.nextLine();
                            AlarmClock alarmClock = new AlarmClock();
                            alarmClock.setAlarm(endTime, message);
                            alarmClock.alarm();

                            while (!alarmClock.isTimerDone()) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    System.out.println("Main thread interrupted.");
                                    Thread.currentThread().interrupt();
                                }
                            }
                            break;

                        } catch (Exception e) {
                            System.out.println("\nInvalid input.");
                        }
                        break;

                    case 2:
                        try {
                            System.out.print("\nTimer");
                            System.out.print("\nEnter time (Format mm:ss): ");
                            String inputTime = input.nextLine(); //getting user input time

                            String pattern = "mm:ss";
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            Date setTime = sdf.parse(inputTime); //set inputted time to sdf format

                            TimerClock timerClock = new TimerClock();
                            timerClock.setTimer(setTime);
                            timerClock.timer();


                        } catch (Exception e) {
                            System.out.println("\nInvalid input.");
                        }
                        break;

                    case 3:
                        System.out.println("\nStopwatch");
                        StopwatchClock stopwatchClock = new StopwatchClock();
                        stopwatchClock.stopwatch();
                        break;

                    case 4:
                        repeat = false;
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Choose an option!");
                }
            } while (repeat);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
}