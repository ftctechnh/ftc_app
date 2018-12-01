package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.sun.tools.javac.util.ArrayUtils;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Map;

public class Tuner{


    private Gamepad gamepad;
    private Telemetry telemetry;

    private int index = 0;

    private String[] titles;
    private double[] values;


    Tuner(String[] titles, double[] values, Gamepad gamepad, Telemetry telemetry){
        this.titles = titles;
        this.values = values;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }

    public void tune(){ //should run continuously

        if (gamepad.left_bumper){ //maybe add release condition if going too fast
            if(index < 2){
                index = 8;
            }else{
                index--;
            }
        }
        if (gamepad.right_bumper){
            if(index > 7){
                index = 1;
            }else{
                index++;
            }
        }

        if (gamepad.left_trigger > 0.05){
            values[index] -= gamepad.left_trigger;
        }
        if (gamepad.right_trigger > 0.05){
            values[index] += gamepad.left_trigger;
        }

        telemetry.addData(titles[index], values[index]);

    }

    public double get(String title){
        int keyIndex = find(titles, title);
        return values[keyIndex];
    }


    // Function to find the index of an String in a String array
    public static int find(String[] array, String target){
        for (int i = 0; i < array.length; i++)
            if (array[i] == target)
                return i;
        return -1;
    }


}
