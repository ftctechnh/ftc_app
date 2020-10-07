package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Tuner{


    private Gamepad gamepad;
    private Telemetry telemetry;

    private int index = 0;

    private String[] titles;
    private double[] values;

    private ButtonPress leftBumper = new ButtonPress();
    private ButtonPress rightBumper = new ButtonPress();


    Tuner(String[] titles, double[] values, Gamepad gamepad, Telemetry telemetry){
        this.titles = titles;
        this.values = values;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }

    public void tune(){ //should run continuously

        if(leftBumper.status(gamepad.left_bumper) == ButtonPress.Status.COMPLETE){
            if(index == 0){
                index = titles.length-1;
            }else{
                index--;
            }
        }

        if (rightBumper.status(gamepad.right_bumper) == ButtonPress.Status.COMPLETE){
            if(index > titles.length-2){
                index = 0;
            }else{
                index++;
            }
        }

        if (gamepad.left_trigger > 0.05){
            values[index] -= gamepad.left_trigger*0.01;
        }
        if (gamepad.right_trigger > 0.05){
            values[index] += gamepad.right_trigger*0.01;
        }

        telemetry.addData(titles[index], values[index]);
        telemetry.addData("index", index);

    }

    public double get(String title){
        int keyIndex = find(titles, title);
        if(keyIndex == -1){
            telemetry.addData("ERROR", "DIDN'T FIND "+title);
            return 0;
        }else{
            return values[keyIndex];

        }
    }


    // Function to find the index of an String in a String array
    public static int find(String[] array, String target){
        for (int i = 0; i < array.length; i++)
            if (array[i] == target)
                return i;
        return -1;
    }


}
