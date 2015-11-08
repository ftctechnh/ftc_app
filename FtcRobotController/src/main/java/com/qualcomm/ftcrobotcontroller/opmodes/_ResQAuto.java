package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Range;

//TODO: Detect red/blue line, change ODS boundary
//Get sweeper working
//Get button pushing working
//do dumping
//Tune get out of the way?
public abstract class _ResQAuto extends LinearOpMode {
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
        final double EOPDThreshold = 0.5 * (BLACKVALUE + WHITEVALUE);
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


        rightWheel = hardwareMap.dcMotor.get("rightwheel");
        leftWheel = hardwareMap.dcMotor.get("leftwheel");
        leftWheel.setDirection(DcMotor.Direction.REVERSE);
        rightWheel.setDirection(DcMotor.Direction.FORWARD);

        buttonServo = hardwareMap.servo.get("buttonservo");
        buttonservoPosition = 0.0;
        button2Servo = hardwareMap.servo.get("button2servo");
        button2servoPosition = 0.0;
        sweeperServo = hardwareMap.servo.get("sweeperservo");
        sweeperservoPosition = 0.0;
        sweeper2Servo = hardwareMap.servo.get("sweeper2servo");
        sweeper2servoPosition = 0.0;
        peopleServo = hardwareMap.servo.get("peopleservo");
        peopleservoPosition = 0.0;

        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("sonic");
        //colorsensor = hardwareMap.colorSensor.get("colorsensor");
        //colorsensor.enableLed(false);

        waitForStart();
      /* buttonServoPosition = 0.7;
       buttonServoPosition = Range.clip(buttonServoPosition, BUTTONSERVO_MIN_RANGE, BUTTONSERVO_MAX_RANGE);
        buttonServo.setPosition(buttonServoPosition);*/
        /*rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftWheel.setTargetPosition(5000);
        rightWheel.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);*/

        //do we need delay
        telemetry.addData("InDelay", "yes");
        sleep(getDelay());
        telemetry.addData("InDelay", "no");

        //This finds the white line
        reflectance = opticalDistanceSensor.getLightDetected();
        telemetry.addData("Reflectance Value", reflectance);
        while(reflectance < TARGET_REFLECTANCE) {
            telemetry.addData("Reflectance Value", reflectance);
            reflectance = opticalDistanceSensor.getLightDetected();
            leftWheel.setPower(0.2);
            rightWheel.setPower(0.2);
            waitForNextHardwareCycle();
        }

        if (getRedAlliance() == 0) {
            //Sweeps the balls and boxes
            sweeperservoPosition += sweeperServoDelta;
            sweeper2servoPosition += sweeper2ServoDelta;

            sweeperservoPosition = Range.clip(sweeperservoPosition, SWEEPERSERVO_MIN_RANGE, SWEEPERSERVO_MAX_RANGE);
            sweeperServo.setPosition(sweeperservoPosition);
            sweeper2servoPosition = Range.clip(sweeper2servoPosition, SWEEPER2SERVO_MIN_RANGE, SWEEPER2SERVO_MAX_RANGE);
            sweeperServo.setPosition(sweeper2servoPosition);

            sweeperservoPosition -= sweeperServoDelta;
            sweeper2servoPosition -= sweeper2ServoDelta;

            sweeperservoPosition = Range.clip(sweeperservoPosition, SWEEPERSERVO_MIN_RANGE, SWEEPERSERVO_MAX_RANGE);
            sweeperServo.setPosition(sweeperservoPosition);
            sweeper2servoPosition = Range.clip(sweeper2servoPosition, SWEEPER2SERVO_MIN_RANGE, SWEEPER2SERVO_MAX_RANGE);
            sweeperServo.setPosition(sweeper2servoPosition);

            //Overshoot to left side of line only as BLUE alliance
            leftWheel.setPower(0.1);
            rightWheel.setPower(0.1);

        } else {
            //Sweeps the balls and boxes
            sweeperservoPosition += sweeperServoDelta;
            sweeper2servoPosition += sweeper2ServoDelta;

            sweeperservoPosition = Range.clip(sweeperservoPosition, SWEEPERSERVO_MIN_RANGE, SWEEPERSERVO_MAX_RANGE);
            sweeperServo.setPosition(sweeperservoPosition);
            sweeper2servoPosition = Range.clip(sweeper2servoPosition, SWEEPER2SERVO_MIN_RANGE, SWEEPER2SERVO_MAX_RANGE);
            sweeperServo.setPosition(sweeper2servoPosition);

            sweeperservoPosition -= sweeperServoDelta;
            sweeper2servoPosition -= sweeper2ServoDelta;

            sweeperservoPosition = Range.clip(sweeperservoPosition, SWEEPERSERVO_MIN_RANGE, SWEEPERSERVO_MAX_RANGE);
            sweeperServo.setPosition(sweeperservoPosition);
            sweeper2servoPosition = Range.clip(sweeper2servoPosition, SWEEPER2SERVO_MIN_RANGE, SWEEPER2SERVO_MAX_RANGE);
            sweeperServo.setPosition(sweeper2servoPosition);


            //Goes back to the left side of the line only as RED alliance
            leftWheel.setPower(-0.1);
            rightWheel.setPower(-0.1);
            sleep(800);
            leftWheel.setPower(0.3);
            rightWheel.setPower(-0.3);
            sleep(1500);
        }

        //follow the left edge of the line
        while(true) {
            waitOneFullHardwareCycle();
            double distance = ultrasonicSensor.getUltrasonicLevel();
            reflectance = opticalDistanceSensor.getLightDetected();
            //double redvalue = colorsensor.red();
            //double bluevalue = colorsensor.blue();
            double valueB;
            double valueS;

          if (reflectance > EOPDThreshold) {
                value = reflectance - EOPDThreshold ;
                valueB = .1+4*value;
                valueS = .1-4*value;
                if (Math.abs(valueS) < 0.17)
                  valueS = (Math.signum(valueS) * 0.17);

                leftWheel.setPower(valueB);
                rightWheel.setPower(valueS);

            } else {
                value = EOPDThreshold - reflectance;
                valueB = .1+4*value;
                valueS = .1-4*value;
                if (Math.abs(valueS) < .17)
                  valueS = (Math.signum(valueS) * 0.17);

                rightWheel.setPower(valueB);
                leftWheel.setPower(valueS);
            }

            telemetry.addData("valueB", valueB);
            telemetry.addData("valueC", valueS);
            telemetry.addData("Reflectance Value", reflectance);
            telemetry.addData("Ultrasonic Value", distance);
            //telemetry.addData("Red Value", redvalue);
            //telemetry.addData("Blue Value", bluevalue);
            if (ultrasonicThreshold > distance && distance > 1.0)
                    break;

        }

        //Stop
        leftWheel.setPower(0);
        rightWheel.setPower(0);

        double redvalue = 0;
        // colorsensor.red();
        if( redvalue > 0){
            if( getRedAlliance() == 1){
                //If Alliance is red and the button is red
                //Servo Down

            }
        }else {
            if( getRedAlliance() == 0){
                //If Alliance is blue and the button is blue
                //Servo Down

            }
        }

        //Press Button
        leftWheel.setPower(0.1);
        rightWheel.setPower(0.1);
        sleep(1000);

        //End of Autonomous
        if (getDelay() == 0) {
            leftWheel.setPower(-0.3);
            rightWheel.setPower(-0.3);
            sleep(1000);
            if (getRedAlliance() == 1) {
                leftWheel.setPower(0.4);
                rightWheel.setPower(-0.4);
                sleep(2000);
                leftWheel.setPower(0.3);
                leftWheel.setPower(0.3);
                sleep(1500);
                leftWheel.setPower(0.4);
                rightWheel.setPower(-0.4);
                sleep(500);
                leftWheel.setPower(0.3);
                rightWheel.setPower(0.3);
                sleep(2000);
                leftWheel.setPower(0);
                rightWheel.setPower(0);
            } else {
                leftWheel.setPower(-0.4);
                rightWheel.setPower(0.4);
                sleep(3000);
                leftWheel.setPower(0.3);
                leftWheel.setPower(0.3);
                sleep(1500);
                leftWheel.setPower(-0.4);
                rightWheel.setPower(0.4);
                sleep(1500);
                leftWheel.setPower(0.3);
                rightWheel.setPower(0.3);
                sleep(1500);
                leftWheel.setPower(0);
                rightWheel.setPower(0);
            }
        }
    }


    abstract protected int getDelay();


    abstract protected int getRedAlliance();

}