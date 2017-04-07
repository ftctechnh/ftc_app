package org.firstinspires.ftc.teamcode.Steven;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Main.AutonomousGeneral_charlie;


/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "blueBeaconCharlieWorlds")
public class blueAuto_charlie_worlds extends AutonomousGeneral_charlie {



    //
    boolean second_beacon_press = false;
    String currentTeam = "blue";
    private ElapsedTime runtime = new ElapsedTime();
    public int gyro_corr_cnt =0;
    public int beacon_angle;
    public double pathhighspeed = 0.8;
    public boolean ball_first = false;
    //String currentColor = "blank";
    int initialHeading =361;
    public double[] timeProfile = new double[30];
    int profileindex = 0;

    @Override
    public void runOpMode() {

        initiate();
        idle();
        setMotorsModeToEncDrive();
        stopMotors();
     //   sleep(1000);
        /*gyro.calibrate();
        runtime.reset();
        while((gyro.isCalibrating()&&runtime.seconds()<5)){
            idle();
        }*/

        initialHeading = gyro.getHeading();
        telemetry.addData("READY TO START", initialHeading);
        telemetry.addData("gyro cal finished in ms", runtime.milliseconds());

        telemetry.update();


        waitForStart();
        runtime.reset();
        second_beacon_press = false;




       // minimizeError();
        setMotorsModeToEncDrive();
        stopMotors();
        timeProfile[profileindex++] = runtime.milliseconds();
        //encoderMecanumCrossDrive(pathhighspeed,65,65,5,1);
        encoderMecanumCrossDrive(1,141,141,5,2);
        encoderMecanumDrive(1,20,20,5,1);
        timeProfile[profileindex++] = runtime.milliseconds();
        servoBeaconPress();

        }
    public void servoBeaconPress(){
        boolean left_detected = false;
        boolean beacon_press_success = false;

        lineAlignStrafe();
        timeProfile[profileindex++] = runtime.milliseconds();//first time: 9447.32 ms
        //second time: 19998.19
        //sleep(2000);
        //moveTowardWall();


        readNewColorLeft();// this is the right if you are standing in the same direction as the back of the robot

        if(currentColorBeaconLeft.equals(currentTeam)){
            left_detected = true;
            //pressBeaconButton();

        }
        else{
            left_detected = false;
            setMotorsModeToEncDrive();
           // encoderMecanumCrossDrive(0.5,18,18,2,1);
            //setMotorsModeToRangeSensing();
            //pressBeaconButton();
        }
        //add a line to move servo back to initial position
        timeProfile[profileindex++] = runtime.milliseconds();//first time: 12991.84



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
        setMotorsModeToEncDrive();;
        encoderMecanumDrive(1,100,100,5,1);
       // encoderMecanumCrossDrive(pathhighspeed,78.74,78.74,5,1);
        //sleep(2000);
       // encoderMecanumCrossDrive(pathhighspeed,25.4,25.4,5,2);
        //sleep(2000);
        //if use strafing, add error correction so that it stays within a certain distance of the wall
        lineAlignStrafe();

}

    public void lineAlignStrafe() {
        moveTowardWall();
        if(second_beacon_press){
            setMotorsToEnc(29, 29, 0.3);
        }
        else {
            setMotorsModeToColorSensing();
        }
        strafeRight(0.6);
        while(whiteLineDetectedBack() == false){


        }
        stopMotors();

        stopMotors();
        timeProfile[profileindex++] = runtime.milliseconds();


    }

    public void lineAlign() {

        if(second_beacon_press){
            setMotorsToEnc(29, 29, 0.3);
        }
        else {
            setMotorsModeToColorSensing();
        }
            telemetry.addData("DISTANCEDISTANCE:", rangeSensor.getDistance(DistanceUnit.CM));
            telemetry.update();
            if (rangeSensor.getDistance(DistanceUnit.CM) < 30) {
                crossDrive(1, 0.7);
            } else {
                crossDrive(2, 0.7);
            }
            double lineAligntime = runtime.milliseconds()+2500;
            boolean speedflag = false;
            while (whiteLineDetectedBack() == false) {
                if(runtime.milliseconds() > lineAligntime && !speedflag){
                    stopMotors();
                    setMotorsModeToColorSensing();
                    speedflag = true;
                    if (rangeSensor.getDistance(DistanceUnit.CM) < 30) {
                        crossDrive(1, 0.7);
                    } else {
                        crossDrive(2, 0.7);
                    }
                }
                if (rangeSensor.getDistance(DistanceUnit.CM) < 25) {
                    crossDrive(1, 0.7);
                } else if (rangeSensor.getDistance(DistanceUnit.CM) > 50) {
                    crossDrive(2, 0.7);
                }


            }

        stopMotors();
        timeProfile[profileindex++] = runtime.milliseconds();
        //moveTowardWall();
        //gyroCorrection(0);

    }



    public void moveTowardWall(){
        setMotorsModeToRangeSensing();
        straightDrive(-0.8);
        while (rangeSensor.getDistance(DistanceUnit.CM) > 20) {

        }
        stopMotors();
        idle();
        setMotorsModeToRangeSensing();

        straightDrive(0.8);
        while (rangeSensor.getDistance(DistanceUnit.CM) < 15) {

        }
        stopMotors();
        idle();
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
        timeProfile[profileindex++] = runtime.milliseconds();//21406.18
        if (second_beacon_press)
        {
            if(ball_first) {

                gyroCorrection((beacon_angle + 45));
                encoderMecanumDrive(pathhighspeed, 130, 130, 5, 0);
                sleep(500);
                encoderMecanumDrive(pathhighspeed, -10, -10, .2, 0);
                encoderMecanumDrive(pathhighspeed, 30, 30, .2, 0);
            }
            else{

                setMotorsModeToEncDrive();
                encoderMecanumCrossDrive(pathhighspeed, 90, 90, 5, 4);

                encoderMecanumDrive(pathhighspeed,56,-56,5,0);
             //   gyroCorrection((beacon_angle + 135));
                encoderShoot(0.8);
                intake_motor.setPower(1);
                sleep(1400);
                intake_motor.setPower(0);
                encoderShoot(0.8);

                encoderMecanumDrive(pathhighspeed,-50,-50,5,0);
                encoderMecanumDrive(pathhighspeed, 10, 10, .2, 0);
                encoderMecanumDrive(pathhighspeed, -30, -30, .2, 0);
                timeProfile[profileindex++] = runtime.milliseconds();//32730.99
            }
        }
        else
        {

        }
        for(int i = 0; i < profileindex; i++){
            //telemetry.addData(""+i,timeProfile[i]);
        }
        telemetry.update();
        sleep(30000);
    }


    public void pressBeaconButton()
    {
        setMotorsModeToEncDrive();
        stopMotors();
        double distFromWall = rangeSensor.getDistance(DistanceUnit.CM)+4;
        telemetry.addData("distance",distFromWall);
        telemetry.update();

        encoderMecanumDrive(0.6, -distFromWall, -distFromWall, 5,0);
//
        sleep(500);
//
        encoderMecanumDrive(0.6, distFromWall, distFromWall, 5,0);
        //setMotorsModeToRangeSensing();
        moveTowardWall();

//        encoderDrive(.3, -distFromWall, -distFromWall, 1);
//        sleep(500);
//        encoderDrive(.3, distFromWall, distFromWall, 1);
    }
    public void gyroCorrection(int angle){

        ;
        angle = angle%360;
        setMotorsModeToRangeSensing();
        telemetry.addData("gyroCorrection starting", gyro.getHeading());
        telemetry.addData("gyroCorrection cnt",gyro_corr_cnt);
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

        /*gyro_corr_cnt++;
        telemetry.addData("gyroCorrection done", gyro.getHeading());
        telemetry.addData("gyroCorrection cnt",gyro_corr_cnt);
        telemetry.update();*/
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



}
//-------------------------------------------------------------------//


