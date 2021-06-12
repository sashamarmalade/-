package sample.Method;

import javafx.application.Platform;
import javafx.scene.control.Label;
import org.joda.time.DateTime;

import java.awt.*;
import java.sql.Time;

public class TestTimer implements Runnable {
    private final Thread thread;
    private DateTime DTime;
    private Label LTime;
    public TestTimer(Time time, Label LabelTime){
        this.LTime = LabelTime;
        this.DTime = new DateTime(time);
        thread = new Thread(this, "Timer");
        thread.start();
    }

    public Time getRemainingTime(){
       return new Time(DTime.toDate().getTime());
    }

    @Override
    public void run() {
        while (DTime.getSecondOfDay() != 0) {
            try {
                DTime = DTime.minusSeconds(1);
                Platform.runLater(() -> LTime.setText(String.valueOf(getRemainingTime())));
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(e.getMessage());;
            }
        }
    }
}
