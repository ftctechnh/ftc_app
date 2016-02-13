package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.concurrent.TimeUnit;

/**
 * Created by THE OVERLORDS on 1/7/16.
 */
public class ContinuousServo extends OpMode {

    Servo contServo;
    double servoPosition = 0.0;


    /**
     * Constructor
     */
    public ContinuousServo() {

    }

    public void init() {

        contServo = hardwareMap.servo.get("servo");
        contServo.setPosition(.5);

    }

    public void loop() {

     /*   if (gamepad1.a){
            contServo.setPosition(.4);
        }

        if (gamepad1.b){
            contServo.setPosition(.41);
        }

        if (gamepad1.x){
            contServo.setPosition(.42);
        }

        if (gamepad1.y){
            contServo.setPosition(.43);
        }

        if (gamepad1.left_bumper){
            contServo.setPosition(.44);
        }

        if (gamepad1.right_bumper){
            contServo.setPosition(.45);
        }
*/


        if (gamepad1.a){
            contServo.setPosition(1);
            //contServo.setDirection(Servo.Direction.REVERSE);
        }

        if (gamepad1.b){
            contServo.setPosition(0);
        }

        if (gamepad1.x){
            contServo.setPosition(.5);
            //contServo.setDirection(Servo.Direction.FORWARD);
        }

        //if (gamepad1.y){
            //contServo.setPosition(.41);
        //}

            //contServo.setDirection(Servo.Direction.REVERSE);
       // }

        telemetry.addData("Servo Pos", String.format("%.2f", contServo.getPosition()));
        telemetry.addData("Servo Dir", contServo.getDirection());

        /*if (gamepad1.a){

            for(double i = 0.0; i <= 1.0; i += 0.01) {

                contServo.setPosition(i);
                telemetry.addData("i", String.format("%.2f", i));
                telemetry.addData("Servo Pos", String.format("%.2f", contServo.getPosition()));
                try {
                    wait(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }



        if (gamepad1.a) {
            contServo.setDirection(Servo.Direction.REVERSE);
            contServo.setPosition(0.0);
        }
        if (gamepad1.b) {
            contServo.setPosition(1.0);
            contServo.setDirection(Servo.Direction.FORWARD);
        }


        telemetry.addData("Servo Pos", String.format("%.2f", contServo.getPosition()));
        telemetry.addData("Servo Dir", contServo.getDirection());


        telemetry.addData("Servo Pos", String.format("%.2f", contServo.getPosition()));

        if (gamepad1.a){

            contServo.setPosition(servoPosition);

            if (servoPosition <= .95){

                servoPosition += 0.05;

            }

        }
*/
    }

    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

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
}
