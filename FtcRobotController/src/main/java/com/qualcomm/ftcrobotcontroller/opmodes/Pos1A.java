
package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;


public class Pos1A extends OpMode {
    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motor1PP;
    DcMotor motor2PP;


    final static double MOTOR_POWER = 0.15; // Higher values will cause the robot to move faster



    public Pos1A() {

    }


    @Override
    public void init() {


        motorLeft = hardwareMap.dcMotor.get("motor_2");
        motorRight = hardwareMap.dcMotor.get("motor_1");
        motor1PP = hardwareMap.dcMotor.get("motor_1P");
        motor2PP = hardwareMap.dcMotor.get("motor_2P");
        motorRight.setDirection(DcMotor.Direction.REVERSE);


    }

    @Override
    public void loop() {
        double reflection = 0.0;
        double left = 0, right = 0.0;



        /*
         * Use the 'time' variable of this op mode to determine
         * how to adjust the motor power.
         */
        if (this.time <= 1) {
            // from 0 to 1 seconds, run the motors for five seconds.
            left = 0.15;
            right = 0.15;
        } else if (this.time > 5 && this.time <= 8.5) {
            // between 5 and 8.5 seconds, point turn right.
            left = -0.15;
            right = 0.15;
        } else if (this.time > 8.5 && this.time <= 15) {
            // between 8 and 15 seconds, idle.
            left = 0.2;
            right = 0.2;
        } else if (this.time > 15d && this.time <= 18.5d) {
            // between 15 and 18.5 seconds, point turn left.
            left = 0.15;
            right = -0.15;
        } else if (this.time > 18.5d && this.time <= 21.5d){
            left = 0.2;
            right = 0.2;
        }


        motorRight.setPower(left);
        motorLeft.setPower(right);



        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("time", "elapsed time: " + Double.toString(this.time));
        telemetry.addData("reflection", "reflection:  " + Double.toString(reflection));
        telemetry.addData("left tgt pwr",  "left  pwr: " + Double.toString(left));
        telemetry.addData("right tgt pwr", "right pwr: " + Double.toString(right));
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }

}
