package com.qualcomm.ftcrobotcontroller.opmodes.ftc6347;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.ftcrobotcontroller.opmodes.ftc6347.DriveFunctions.DIRECTION;

/**
 * Created by FTCGearedUP on 8/6/2015.
 */
public class Red3 extends LinearOpMode {

    DriveFunctions df = new DriveFunctions(0.075,hardwareMap,telemetry);
    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    DcMotor motor4;
    DcMotor motor5;
    DcMotorController winch;
    Servo climbers;
    Servo red;
    Servo blue;
    Servo arm1;
    Servo arm2;
    UltrasonicSensor ultrasonicSensor;
    LightSensor reflectedLight;

    @Override
    public void runOpMode() throws InterruptedException {

        /////////////////////////Motors////////////////////////////
        motor1 = hardwareMap.dcMotor.get("1");
        motor2 = hardwareMap.dcMotor.get("2");
        motor3 = hardwareMap.dcMotor.get("3");
        motor4 = hardwareMap.dcMotor.get("4");
        motor5 = hardwareMap.dcMotor.get("5");

        winch = hardwareMap.dcMotorController.get("c1");

        motor2.setDirection(DcMotor.Direction.REVERSE);
        motor3.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Motor 1 Dir", motor1.getDirection().toString());
        telemetry.addData("Motor 2 Dir", motor2.getDirection().toString());

        motor1.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motor2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        /////////////////////////////Servos/////////////////////////////
        climbers = hardwareMap.servo.get("sr2");// 2
        red = hardwareMap.servo.get("sr3");// 3
        blue = hardwareMap.servo.get("sr1");// 1
        arm1 = hardwareMap.servo.get("sr5");// 5
        arm2 = hardwareMap.servo.get("sr4");// 4

        climbers.setPosition(1.0); //initialize arm
        red.setPosition(1); //initialize red arm
        blue.setPosition(0); //initialize blue arm
        //arm1_Position = 0.6;
        arm1.setPosition(0.3); //initialize hang arm1
        arm2.setPosition(0.2); //initialize hang arm2

        //////////////////////////////Sensors///////////////////////////
        reflectedLight = hardwareMap.lightSensor.get("s3");
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("s4");

        ////////////////////////////Variables//////////////////////////

        waitForStart();

        motor3.setPower(-1);

        df.drive(40, DIRECTION.FORWARD); //Drive 40" forward

        df.turn(1, 812); //Turn 45 degrees left

        df.drive(41, DIRECTION.FORWARD); //Drive 39" forward

        df.turn(1, 812); //Turn 45 degrees left

        motor3.setPower(0);

        while (ultrasonicSensor.getUltrasonicLevel() > 13 || ultrasonicSensor.getUltrasonicLevel() == 0) {

            telemetry.addData("Ultrasonic", ultrasonicSensor.getUltrasonicLevel());

            if (ultrasonicSensor.getUltrasonicLevel() == 0 || ultrasonicSensor.getUltrasonicLevel() == 255) {

                motor1.setPower(0);// stop while the robot reads a zero value
                motor2.setPower(0);
            }

            else {

                motor1.setPower(0.2 + df.getOffset());// run while the robot reads a non-zero value
                motor2.setPower(0.2);
            }
        }
        motor1.setPower(0); //stop
        motor2.setPower(0);

        // int time = 20;

       /* climbers.setPosition(1);
        sleep(time);
        climbers.setPosition(0.9);
        sleep(time);
        climbers.setPosition(0.8);
        sleep(time);
        climbers.setPosition(0.7);
        sleep(time);
        climbers.setPosition(0.6);
        sleep(time);
        climbers.setPosition(0.5);
        sleep(time);
        climbers.setPosition(0.4);
        sleep(time);
        climbers.setPosition(0.3);
        sleep(time);
        climbers.setPosition(0.2);
        sleep(time);
        climbers.setPosition(0.1);
        sleep(time);
*/
        climbers.setPosition(1);//Throw climbers
        sleep(100);
        climbers.setPosition(0);// Retract arm
        sleep(100);

        df.drive(20, DIRECTION.FORWARD); // Drive 20" forward

        motor1.setPower(0); //stop
        motor2.setPower(0);

    }
}
