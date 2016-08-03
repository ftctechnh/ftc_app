package com.qualcomm.ftcrobotcontroller.opmodes.ftc6347;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by FTCGearedUP on 8/6/2015.
 */
public class Ramp_Blue extends LinearOpMode {

    DcMotor motor1; //right
    DcMotor motor2; //left
    DcMotor motor3;
    DcMotor motor4;
    DcMotor motor5;
    DcMotor motor6;
    DcMotor motor7;
    DcMotorController winch;
    DcMotorController wheelie;
    Servo red;
    Servo blue;
    Servo arm1;
    Servo climberExtend;
    Servo climberDump;
    UltrasonicSensor ultrasonic4;
    UltrasonicSensor ultrasonic5;
    LightSensor reflectedLight;
    TouchSensor touch1;
    GyroSensor xAxis;
    GyroSensor zAxis;

    public void setDriveMode(DcMotorController.RunMode mode) {
        if (motor2.getMode() != mode) {
            motor2.setMode(mode);
        }

        if (motor1.getMode() != mode) {
            motor1.setMode(mode);
        }
    }

    private void getMotorInfo() {
        telemetry.addData("Motor1", motor1.getCurrentPosition());
        telemetry.addData("Motor2", motor2.getCurrentPosition());
    }

    private void driveToPosition(int inches, double power, int direction, boolean wheelieBar) throws InterruptedException {

        //first distance the robot travels
        inches = inches * 167;

        if(direction == 1){ //go forward when 1

            motor1.setDirection(DcMotor.Direction.REVERSE);//set driving direction
            motor2.setDirection(DcMotor.Direction.FORWARD);
        }
        if(direction == -1){ //go back when -1

            motor1.setDirection(DcMotor.Direction.FORWARD);//set driving direction
            motor2.setDirection(DcMotor.Direction.REVERSE);
        }

        //reset tread distance encoders
        setDriveMode(DcMotorController.RunMode.RESET_ENCODERS);

        // Wait for Encoders to be reset
        while (motor2.getCurrentPosition() != 0 && motor1.getCurrentPosition() != 0) {
            waitOneFullHardwareCycle();
        }

        //setting the desired number of counts the motor will travel, setting a target
        motor2.setTargetPosition(inches);
        motor1.setTargetPosition(inches);

        //the motors will go to the target position (above)
        setDriveMode(DcMotorController.RunMode.RUN_TO_POSITION);

        // Don't know if this is necessary
        waitOneFullHardwareCycle();

        //the motors will move at this speed
        motor2.setPower(-power);
        motor1.setPower(-power);

        while (motor2.isBusy() && motor1.isBusy()) {
            waitOneFullHardwareCycle();
            getMotorInfo();
            wheelieBar(wheelieBar);
        }
    }

    private void reset() throws InterruptedException{

        motor1.setMode(DcMotorController.RunMode.RESET_ENCODERS);//reset encoders
        motor2.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        while (motor2.getCurrentPosition() != 0 && motor1.getCurrentPosition() != 0) {
            waitOneFullHardwareCycle(); //wait for encoders to reset
        }

    }

    private void turnEncoder(int angle, double power, int direction, boolean wheelieBar) throws InterruptedException {

        //first distance the robot travels

        angle = angle * 23;

        if(direction == 1){ //turn right when 1

            motor1.setDirection(DcMotor.Direction.FORWARD);//set turning direction
            motor2.setDirection(DcMotor.Direction.FORWARD);
        }
        if(direction == -1){ //turn left when -1

            motor1.setDirection(DcMotor.Direction.REVERSE);//set turning direction
            motor2.setDirection(DcMotor.Direction.REVERSE);
        }

        //reset tread distance encoders
        setDriveMode(DcMotorController.RunMode.RESET_ENCODERS);

        // Wait for Encoders to be reset
        while (motor2.getCurrentPosition() != 0 && motor1.getCurrentPosition() != 0) {
            waitOneFullHardwareCycle();
        }

//setting the desired number of counts the motor will travel, setting a target
        motor2.setTargetPosition(angle);
        motor1.setTargetPosition(angle);

        //the motors will go to the target position (above)
        setDriveMode(DcMotorController.RunMode.RUN_TO_POSITION);

        // Don't know if this is necessary
        waitOneFullHardwareCycle();

        //the motors will move at this speed
        motor2.setPower(power);
        motor1.setPower(power);

        while (motor2.isBusy() && motor1.isBusy()) {
            waitOneFullHardwareCycle();
            getMotorInfo();
            wheelieBar(wheelieBar);
        }
    }

    void turn(int angle, boolean wheelieBar) throws InterruptedException { //phone crashes when this is run

        setDriveMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motor1.setDirection(DcMotor.Direction.REVERSE);
        motor2.setDirection(DcMotor.Direction.FORWARD);

        angle = xAxis.getHeading() + angle;//set target angle to turn to

        if(angle < 0){

            angle = 360 + angle; //handles negative targets
        }

        while(xAxis.getHeading() != angle) {

            telemetry.addData("xAxis Heading", Double.toString(xAxis.getHeading()));
            telemetry.addData("Angle", Double.toString(angle));

            if (xAxis.getHeading() < angle) {

                motor1.setPower(-0.5); //turn right
                motor2.setPower(0.5);
            }
            if (xAxis.getHeading() > angle) {

                motor1.setPower(0.5); //turn left
                motor2.setPower(-0.5);
            }
            waitOneFullHardwareCycle();
            wheelieBar(wheelieBar);
        }
    }

    void wheelieBar(boolean position){

        if (position && touch1.getValue() == 0){//not pushed and toggle = 1
            motor7.setPower(-0.4); //move down when moved up but should be down
        }
        else{
            motor7.setPower(0);
        }
    }
///////////////////////////////////////////////////RUN///////////////////////////////////////////////
    @Override
    public void runOpMode() throws InterruptedException{

        /////////////////////////Motors////////////////////////////
        motor1 = hardwareMap.dcMotor.get("1");
        motor2 = hardwareMap.dcMotor.get("2");
        motor3 = hardwareMap.dcMotor.get("3");
        motor4 = hardwareMap.dcMotor.get("4");
        motor5 = hardwareMap.dcMotor.get("5");
        motor6 = hardwareMap.dcMotor.get("6");
        motor7 = hardwareMap.dcMotor.get("7");

        winch = hardwareMap.dcMotorController.get("c1"); //0
        wheelie = hardwareMap.dcMotorController.get("c2"); //1

        motor1.setDirection(DcMotor.Direction.REVERSE);
        motor2.setDirection(DcMotor.Direction.FORWARD);
        motor7.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Motor 1 Dir", motor1.getDirection().toString());
        telemetry.addData("Motor 2 Dir", motor2.getDirection().toString());

        motor1.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motor2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        /////////////////////////////Servos////////////////////////////
        red = hardwareMap.servo.get("sr3");// 3
        blue = hardwareMap.servo.get("sr1");// 1
        arm1 = hardwareMap.servo.get("sr6");// 6
        climberExtend = hardwareMap.servo.get("sr4");// 4
        climberDump = hardwareMap.servo.get("sr5"); // 5

        red.setPosition(0.91); //initialize red arm
        blue.setPosition(0.02); //initialize blue arm
        arm1.setPosition(0.85); //initialize hang arm1, higher servo
        climberExtend.setPosition(0.6); //initialize climber extender
        climberDump.setPosition(0.82); //initialize climbers to be facing up

        //////////////////////////////Sensors///////////////////////////
        //////////////Legacy/////////////////
        reflectedLight = hardwareMap.lightSensor.get("s3");
        reflectedLight.enableLed(true);
        ultrasonic4 = hardwareMap.ultrasonicSensor.get("s4"); //s4
        ultrasonic5 = hardwareMap.ultrasonicSensor.get("s5"); //s5
        ////////////CDI////////////
        touch1 = hardwareMap.touchSensor.get("t1"); //D 0
        xAxis = hardwareMap.gyroSensor.get("g2");//I2c 1
        zAxis = hardwareMap.gyroSensor.get("g1");//I2c 0
        xAxis.calibrate(); //calibrate both sensors
        //zAxis.calibrate();

        ////////////////////////////Variables//////////////////////////


        /////////////////////////////Start//////////////////////////
        waitForStart();

        driveToPosition(10, 0.3, 1, false);//move 10 inches

        turn(40, false);//turn right 45 degrees

        motor3.setPower(-0.5); //turn on intake

        driveToPosition(35, 0.3, 1, true);//move 15 inches

        motor3.setPower(0); //stop intake

        driveToPosition(10, 0.3, 1, true);//move 30 inches

        motor7.setPower(0.2);//move drive2 bar up
        sleep(400);
        motor7.setPower(0);

        turn(-88, false);//turn left 90

        driveToPosition(40, 0.4, -1, false);//move back 40

        reset(); //reset encoders

    }
}

