package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

import java.lang.reflect.Array;

/**
 * Created by Dan on 12/2/2015.
 */
public class TeleOpCommands extends OpMode {
    public static int WINCH=1;
    public static int WINCHPIVOT=2;
    public static int WINCHWHEEL=1;
    public static int RIGHT=2;
    public static int LEFT=1;
    public GyroSensor gyro;
    public DcMotorController winchMC;
    public DcMotorController driveMC;
    public DcMotorController wheelMC;
    public Servo climbersLeft;
    public Servo climbersRight;
    public ColorSensor color;


    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
    @Override
    public void init() {
    }
    @Override
    public void loop() {
    }

}
