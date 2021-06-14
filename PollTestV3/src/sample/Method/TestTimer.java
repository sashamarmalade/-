package sample.Method;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import org.joda.time.DateTime;
import sample.Controller.Controller;


import java.sql.Time;
import java.util.ArrayList;

public class TestTimer implements Runnable {

    private final Thread thread;
    private DateTime DTime;
    private final Label LTime;
    private final Controller c;
    DataTheme DT;
    ArrayList<Object> Result;
    ActionEvent event;

    public TestTimer(Time time, Label LabelTime, Controller c, DataTheme DT, ArrayList<Object> Result, ActionEvent event){
        this.LTime = LabelTime;
        this.DTime = new DateTime(time);
        this.c = c;
        this.DT = DT;
        this.Result = Result;
        this.event = event;
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
                System.out.println(e.getMessage());
            }
        }

        Platform.runLater(() -> c.EndTime(event,DT, c.ConvertResult(Result)));
    }
}
