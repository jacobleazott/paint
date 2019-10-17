package sample;

import javafx.beans.Observable;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class ToolTimer implements Runnable {
    private String drawMode, newLog;
    private long startTime,endTime;
    private double totTime;
    // List that stores all the entries
    private List<String> logEntries = new ArrayList<>();
    @Override
    public void run() {/*This thread does not utilize run() */ }
    // What we use to initialize, since I do not have an initial thing selected
    // it will show up as 'Standby'
    public void beginTimer(String startTool){
        drawMode = startTool; //bring in the initial mode from TopMenus
        startTime = System.currentTimeMillis();
    }
    // When you switch between tools it creates the log data and resets timers.
    public void switchTool(String newTool){
        endTime = System.currentTimeMillis();
        totTime = ((double)endTime-(double)startTime)/1000; //calculate time on that tool in seconds
        newLog = drawMode+" was selected for "+totTime+" seconds";
        logEntries.add(newLog);
        drawMode = newTool;
        startTime = System.currentTimeMillis();
        System.out.println("New ArrayList:");
        for(int i = 0;i<logEntries.size();i++){
            System.out.println(logEntries.get(i));
        }
    }
    // Thread is ending close up the log file and save it.
    public void end(){
        switchTool("");
        Path file = Paths.get("tooltimes.log"); //place log file in PaintV0 directory
        try {
            Files.write(file, logEntries, StandardCharsets.UTF_8);
        }catch(IOException e){
            System.out.println("Failed to write log file");
        }
    }
}