package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Range;

//TODO: Detect red/blue line, change ODS boundary DONE, but need calibration
//TODO: Get sweeper working
//TODO: Get button pushing working DONE
//TODO: do dumping
//TODO: Tune get out of the way?
public  class _ResQAutoTesting extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor rightWheel;
        DcMotor leftWheel;

        Servo buttonServo;
        Servo button2Servo;
        Servo sweeperServo;
        Servo sweeper2Servo;
        Servo peopleServo;


        OpticalDistanceSensor opticalDistanceSensor;
        UltrasonicSensor ultrasonicSensor;
        ColorSensor colorsensor;

        double reflectance = 0;
        final double TARGET_REFLECTANCE = 0.1;
        final double BLACKVALUE = 0.023;
        final double WHITEVALUE = 0.29;
        final double REDVALUE = 0.19; //NEED TO TEST
        final double BLUEVALUE = 0.12; //NEED TO TEST
        final double EOPDThreshold = 0.5 * (REDVALUE + WHITEVALUE);
        final double ultrasonicThreshold = 9;

        final double POWER = 0.3;
        final double BASEPOWER = 0.2;

        final double BUTTONSERVO_MIN_RANGE  = 0.01;
        final double BUTTONSERVO_MAX_RANGE  = 1;
        final double BUTTON2SERVO_MIN_RANGE  = 0.01;
        final double BUTTON2SERVO_MAX_RANGE  = 1;
        final double SWEEPERSERVO_MIN_RANGE  = 0.01;
        final double SWEEPERSERVO_MAX_RANGE  = 1;
        final double SWEEPER2SERVO_MIN_RANGE  = 0.01;
        final double SWEEPER2SERVO_MAX_RANGE  = 1;
        final double PEOPLESERVO_MIN_RANGE  = 0.01;
        final double PEOPLESERVO_MAX_RANGE  = 1;

        double buttonservoPosition;
        double button2servoPosition;
        double sweeperservoPosition;
        double sweeper2servoPosition;
        double peopleservoPosition;

        double buttonServoDelta = 0.49;
        double button2ServoDelta = 0.49;
        double sweeperServoDelta = 0.49;
        double sweeper2ServoDelta = 0.49;
        double peopleServoDelta = 0.49;

        double value;
        int getRedAlliance = 1;


        rightWheel = hardwareMap.dcMotor.get("rightwheel");
        leftWheel = hardwareMap.dcMotor.get("leftwheel");
        leftWheel.setDirection(DcMotor.Direction.REVERSE);
        rightWheel.setDirection(DcMotor.Direction.FORWARD);

        buttonServo = hardwareMap.servo.get("buttonservo");
        buttonservoPosition = 0.3;
        buttonServo.setPosition(buttonservoPosition);
        button2Servo = hardwareMap.servo.get("button2servo");
        button2servoPosition = 0.3;
        button2Servo.setPosition(button2servoPosition);
       // sweeperServo = hardwareMap.servo.get("sweeperservo");
       // sweeperservoPosition = 0.0;
      //  sweeper2Servo = hardwareMap.servo.get("sweeper2servo");
        sweeper2servoPosition = 0.0;
      //  peopleServo = hardwareMap.servo.get("peopleservo");
      //  peopleservoPosition = 0.0;

        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
       // ultrasonicSensor = hardwareMap.ultrasonicSensor.get("sonic");
        colorsensor = hardwareMap.colorSensor.get("colorsensor");
        colorsensor.enableLed(false);

        waitForStart();
      /* buttonServoPosition = 0.7;
       buttonServoPosition = Range.clip(buttonServoPosition, BUTTONSERVO_MIN_RANGE, BUTTONSERVO_MAX_RANGE);
        buttonServo.setPosition(buttonServoPosition);*/
        /*rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftWheel.setTargetPosition(5000);
        rightWheel.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);*/

        colorsensor.enableLed(false);
        // colorsensor.red();

            telemetry.addData("Red", colorsensor.red());
            telemetry.addData("Blue", colorsensor.blue());
            sleep(3000);

        //sleep(5000);
        if(colorsensor.red()<0.1&&colorsensor.blue()>0.1){
            if( getRedAlliance == 1){
                //If Alliance is red and the button is red
                //Servo Down
                buttonServo.setPosition(1);
                sleep(1000);

            } else if (getRedAlliance == 0){
                button2Servo.setPosition(1);
                sleep(1000);
            }

        }else if (colorsensor.red()>0.1&&colorsensor.blue()<0.1){
            if( getRedAlliance == 1){
                button2Servo.setPosition(1);
                sleep(1000);

            } else if (getRedAlliance == 0){
                buttonServo.setPosition(1);
                sleep(1000);
            }
        }

        //Press Button
       // leftWheel.setPower(0.1);
        //rightWheel.setPower(0.1);
       // sleep(10000);



    }





}