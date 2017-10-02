package org.firstinspires.ftc.teamcode.ftc2016to2017season.Main;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "redBeaconCharlieWorlds")
@Disabled

public class redAuto_charlie_worlds extends AutonomousGeneral_charlie {


    boolean second_beacon_press = false;
    String currentTeam = "red";

    public double pathhighspeed = 0.8;

   // public double[] timeProfile = new double[30];
   // int profileindex = 0;

    double servoLeftPos = 0;
    double servoRightPos = 1;
    double initialPos = 0.5;

    @Override
    public void runOpMode() {

        initiate();
        idle();
        setMotorsModeToEncDrive();
        stopMotors();

        //currentColorBeaconLeft = "blank";

        telemetry.addData("READY TO START", "");
        telemetry.update();

        waitForStart();
        //runtime.reset();
        autoBeaconPresser.setPosition(initialPos);
        second_beacon_press = false;


        setMotorsModeToEncDrive();
        stopMotors();
       // timeProfile[profileindex++] = runtime.milliseconds();



        encoderMecanumCrossDrive(1,175,175,5,3);
      //  timeProfile[profileindex++] = runtime.milliseconds();
        servoBeaconPress();
           // ColorSensorRead();

        }
    public void servoBeaconPress(){


        doubleLineAlign();
        //strafes until ODS hits white line
        moveTowardWall();
        ColorSensorRead();//sets servo to correct position
        pressBeaconButton();
        autoBeaconPresser.setPosition(initialPos);
        if (second_beacon_press == false)
        {
            moveToNextBeacon();
        }
        else
        {
            setMotorsModeToRangeSensing();
            parkCenterVortex();
        }
        // below evaluate beacon press result and move to next step if it is success and handle failures if failures are seen
    }

    public void  moveToNextBeacon(){
        second_beacon_press = true;
        setMotorsModeToEncDrive();
        encoderMecanumDrive(1,95,95,5,-1);
        if (rangeSensor.getDistance(DistanceUnit.CM) < 15) {
            encoderMecanumDrive(1,15,15,2,0);
        } else if (rangeSensor.getDistance(DistanceUnit.CM) > 40) {
            encoderMecanumDrive(1,-15,-15,2,0);
        }
        //if use strafing, add error correction so that it stays within a certain distance of the wall
        servoBeaconPress();

}
    public void ColorSensorRead(){
        readNewColorLeft();



        if (currentColorBeaconLeft.equals(currentTeam)){
            autoBeaconPresser.setPosition(servoLeftPos);

        }
        else if(currentColorBeaconLeft.equals("blue")){
            autoBeaconPresser.setPosition(servoRightPos);

        }

    }

    public void lineAlignStrafe() {
        setMotorsModeToColorSensing();

        strafeLeft(0.3);
        while(whiteLineDetectedBack() == false){

        }
        stopMotors();

       // timeProfile[profileindex++] = runtime.milliseconds();


    }

    public void lineAlign() {

            setMotorsModeToColorSensing();


            if (rangeSensor.getDistance(DistanceUnit.CM) < 20) {
                crossDrive(4, 0.7);
            } else {
                crossDrive(3, 0.7);
            }
            double lineAligntime = runtime.milliseconds()+2500;
            boolean speedflag = false;
            while (whiteLineDetectedBack() == false) {
                if(runtime.milliseconds() > lineAligntime && !speedflag){
                    stopMotors();
                    setMotorsModeToColorSensing();
                    speedflag = true;
                    if (rangeSensor.getDistance(DistanceUnit.CM) < 20) {
                        crossDrive(4, 0.7);
                    } else {
                        crossDrive(3, 0.7);
                    }
                }
                if (rangeSensor.getDistance(DistanceUnit.CM) < 20) {
                    crossDrive(4, 0.7);
                } else if (rangeSensor.getDistance(DistanceUnit.CM) > 40) {
                    crossDrive(3, 0.7);
                }


            }

        stopMotors();
        //timeProfile[profileindex++] = runtime.milliseconds();
       

    }

    public void doubleLineAlign() {

        setMotorsModeToColorSensing();


        if (rangeSensor.getDistance(DistanceUnit.CM) < 20) {
            crossDrive(4, 0.7);
        } else {
            crossDrive(3, 0.7);
        }
        double lineAligntime = runtime.milliseconds()+2500;
        boolean speedflag = false;
        while (whiteLineDetectedBack() == false && whiteLineDetectedFront() == false) {
            if(runtime.milliseconds() > lineAligntime && !speedflag){
                stopMotors();
                setMotorsModeToColorSensing();
                speedflag = true;
                if (rangeSensor.getDistance(DistanceUnit.CM) < 20) {
                    crossDrive(4, 0.7);
                } else {
                    crossDrive(3, 0.7);
                }
            }
            if (rangeSensor.getDistance(DistanceUnit.CM) < 20) {
                crossDrive(4, 0.7);
            } else if (rangeSensor.getDistance(DistanceUnit.CM) > 40) {
                crossDrive(3, 0.7);
            }


        }
        stopMotors();

        {
            boolean back_detect_first = false;
            boolean front_detect_first = false;
            if ((whiteLineDetectedBack() == true)) {
                if((rangeSensor.getDistance(DistanceUnit.CM) > 27)) {
                    encoderMecanumDrive(0.5, -8, -8, 2, 0);
                    back_detect_first = true;
                }
                else if(rangeSensor.getDistance(DistanceUnit.CM) > 15){
                    encoderMecanumDrive(0.5, -4, -4, 2, 0);
                    back_detect_first = true;
                }
               // sleep(250);





            }

            if ((whiteLineDetectedFront() == true) ){
                if((rangeSensor.getDistance(DistanceUnit.CM) > 27)) {
                    encoderMecanumDrive(0.5, -8, -8, 2, 0);
                    front_detect_first = true;
                }
                else if (rangeSensor.getDistance(DistanceUnit.CM) > 15){
                    encoderMecanumDrive(0.5, -4, -4, 2, 0);
                    front_detect_first = true;
                }


              //  sleep(250);


            }

            if ((front_detect_first || back_detect_first )&&(rangeSensor.getDistance(DistanceUnit.CM)>10)) {
                stopMotors();
                back_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                front_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                back_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                front_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                //timeProfile[profileindex++] = runtime.milliseconds();

                while (whiteLineDetectedBack() == false) {
                    if (back_detect_first) {
                        turnLeft(0.3);
                    }
                    if (front_detect_first) {
                        turnRight(0.3);
                    }
                }

                stopMotors();
            }
        }
    }


    public void moveTowardWall(){

        if (rangeSensor.getDistance(DistanceUnit.CM) > 15) {
            setMotorsModeToRangeSensing();
            straightDrive(-0.8);


            while (rangeSensor.getDistance(DistanceUnit.CM) > 15) {

            }
            stopMotors();
        }

        if(rangeSensor.getDistance(DistanceUnit.CM)<6) {
            setMotorsModeToRangeSensing();

            straightDrive(0.8);
            while (rangeSensor.getDistance(DistanceUnit.CM) < 8) {

            }
            stopMotors();
            idle();
        }

    }

    public void printColorsSeen(){
        telemetry.addData("left color", currentColorBeaconLeft);
        telemetry.addData("right color", currentColorBeaconRight);
        telemetry.update();
    }

    public void parkCenterVortex()
    {
        setMotorsModeToEncDrive();
        stopMotors();
        //timeProfile[profileindex++] = runtime.milliseconds();//21406.18
        if (second_beacon_press)
        {


                setMotorsModeToEncDrive();
                encoderMecanumCrossDrive(pathhighspeed, 100, 100, 5, 1);

                encoderMecanumDrive(pathhighspeed,-55, 55,5,0);

                encoderShoot(.8);
                intake_motor.setPower(.8);
                sleep(800);
                intake_motor.setPower(0);
                encoderShoot(.8);

                encoderMecanumDrive(pathhighspeed,-50,-50,5,0);
                encoderMecanumDrive(pathhighspeed, 10, 10, .2, 0);
                encoderMecanumDrive(pathhighspeed, -30, -30, .2, 0);
                //timeProfile[profileindex++] = runtime.milliseconds();//32730.99

        }
        else
        {

        }
        /*for(int i = 0; i < profileindex; i++){
            //telemetry.addData(""+i,timeProfile[i]);
        }
        telemetry.update();
        sleep(30000);*/
    }


    public void pressBeaconButton()
    {
        setMotorsModeToEncDrive();
        stopMotors();
        double distFromWall = rangeSensor.getDistance(DistanceUnit.CM)+4;


        encoderMecanumDrive(0.6, -distFromWall, -distFromWall, 5,0);

        sleep(100);

        encoderMecanumDrive(0.6, distFromWall, distFromWall, 5,0);

    }
    public void gyroCorrection(int angle){

        ;
        angle = angle%360;
        setMotorsModeToRangeSensing();
        telemetry.addData("gyroCorrection starting", gyro.getHeading());
       // telemetry.addData("gyroCorrection cnt",gyro_corr_cnt);
        telemetry.update();
        int gyro_read = gyro.getHeading();
        if((gyro_read >(((angle-5)+360)%360)) && (gyro_read < ((angle+5)%360)))
        {
            return;
        }
        if((angle+180)%360<180){
            if (gyro_read>angle || gyro_read<((angle+180)%360)){//make sure the angle is correct
                turnLeft(0.7);
                while(gyro_read>angle || gyro_read<((angle+180)%360)){
                    gyro_read = gyro.getHeading();
                    idle();
                }
            }
            else{
                turnRight(0.7);
                while(gyro_read<angle && gyro_read>((angle+180)%360)){
                    gyro_read = gyro.getHeading();
                    idle();
                }
            }
        }
        else{
            if (gyro_read>angle && gyro_read<((angle+180)%360)){//make sure the angle is correct
                turnLeft(0.7);
                while(gyro_read>angle && gyro_read<((angle+180)%360)){
                    gyro_read = gyro.getHeading();
                }
            }
            else{
                turnRight(0.7);
                while(gyro_read<angle || gyro_read>((angle+180)%360)){
                    gyro_read = gyro.getHeading();
                    //idle();
                }
            }
        }


        stopMotors();

    }

    public void minimizeError(){
        front_left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        front_right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        back_right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        back_left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        encoderMecanumDrive(0.3,5,5,4,0);
        front_left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        front_right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //stopMotors();
    }


    public double getInitialPos() {
        return initialPos;
    }
}
//-------------------------------------------------------------------//


