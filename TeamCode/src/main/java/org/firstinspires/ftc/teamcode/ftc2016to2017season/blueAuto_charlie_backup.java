package org.firstinspires.ftc.teamcode.ftc2016to2017season;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.AutonomousGeneral_charlie;


/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "blueBeaconCharlie")
@Disabled
public class blueAuto_charlie_backup extends AutonomousGeneral_charlie {



    //
    boolean second_beacon_press = false;
    String currentTeam = "blue";
    private ElapsedTime runtime = new ElapsedTime();
    public int gyro_corr_cnt =0;
    public int beacon_angle;
    public double pathhighspeed = 0.9;
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
        gyro.calibrate();
        runtime.reset();
        while((gyro.isCalibrating()&&runtime.seconds()<5)){
            idle();
        }

        initialHeading = gyro.getHeading();
        telemetry.addData("READY TO START", initialHeading);
        telemetry.addData("gyro cal finished in ms", runtime.milliseconds());

        telemetry.update();


        waitForStart();
        runtime.reset();
        second_beacon_press = false;

        minimizeError();

        encoderMecanumCrossDrive(0.8,110,110,5,2);
        /*if(ball_first) {
            beacon_angle = 90;
            minimizeError();
            encoderMecanumDrive(pathhighspeed, -35, -35, 5, 0);
            encoderShoot(0.8);
            intake_motor.setPower(1);
            sleep(1000);
            intake_motor.setPower(0);
            encoderShoot(0.8);


            gyroCorrection(45);
            setMotorsModeToEncDrive();
            encoderMecanumDrive(pathhighspeed, -40, -40, 6, 0);

            //encoderMecanumDrive(0.6,36,-36,5,0);
            gyroCorrection(beacon_angle);


            minimizeError();
            setMotorsModeToEncDrive();
            encoderMecanumDrive(pathhighspeed, -30, -30, 5, 0);


        }
        else {
            beacon_angle=initialHeading;
            minimizeError();
            timeProfile[profileindex++] = runtime.milliseconds();//1046.66 ms
            encoderMecanumDrive(pathhighspeed, 90, 90, 5, 1);
            timeProfile[profileindex++] = runtime.milliseconds();//2937.66 ms


            minimizeError();
            setMotorsModeToEncDrive();
            encoderMecanumDrive(pathhighspeed, -60, -60, 5, 0);
            timeProfile[profileindex++] = runtime.milliseconds();//5367.55 ms
        }*/
        servoBeaconPress();
        }


    public void moveToNextBeacon() {
        second_beacon_press = true;
      //  sleep(250);
        setMotorsModeToEncDrive();
        encoderMecanumDrive(pathhighspeed, 12, 12, 5,0);
        sleep(100);
        setMotorsModeToEncDrive();
        idle();
        encoderMecanumDrive(pathhighspeed, 95, 95, 5,1);
        idle();
        timeProfile[profileindex++] = runtime.milliseconds();//16241.67
        servoBeaconPress();

}


    public void lineAlign() {

            setMotorsModeToColorSensing();

            strafeRight(0.7);
            while (whiteLineDetectedBack() == false) {

                idle();

            }
            stopMotors();

    }

    public void servoBeaconPress(){
        boolean left_detected = false;
        boolean beacon_press_success = false;

        if(initialHeading!=361) {
            gyroCorrection(beacon_angle);
        }
        timeProfile[profileindex++] = runtime.milliseconds();//first time:6441.64 ms
                                                             //second time: 17211.97
        if(second_beacon_press) {
            moveTowardWall();
            timeProfile[profileindex++] = runtime.milliseconds();//first time: 8087.69 ms
            //second time: 19274.79
        }
        lineAlign();
        timeProfile[profileindex++] = runtime.milliseconds();//first time: 9447.32 ms
                                                             //second time: 19998.19
        if(initialHeading!=361) {
            gyroCorrection(beacon_angle);
        }



        readNewColorLeft();// this is the right if you are standing in the same direction as the back of the robot

        if(currentColorBeaconLeft.equals(currentTeam)){
            left_detected = true;
            pressBeaconButton();
        }
        else if(currentColorBeaconLeft.equals("red")){
            left_detected = false;
            setMotorsModeToRangeSensing();
            encoderMecanumDrive(pathhighspeed,10,10,2,1);
            pressBeaconButton();
        }
        timeProfile[profileindex++] = runtime.milliseconds();//first time: 12991.84
                                                             //second time: 21028.24(didn't push)
        //presses beacon!


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

    public void moveTowardWall(){
        setMotorsModeToRangeSensing();
        straightDrive(-pathhighspeed);
        while (rangeSensor.getDistance(DistanceUnit.CM) > 25) {

        }
        stopMotors();
        idle();
        setMotorsModeToRangeSensing();

        straightDrive(pathhighspeed);
        while (rangeSensor.getDistance(DistanceUnit.CM) < 25) {

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
                encoderMecanumDrive(0.5, 10, 10, 5, 0);
                //encoderMecanumDrive(0.7,-15,-15,5,0);
                //encoderMecanumDrive(0.7,56,-56,5,0);

                gyroCorrection((beacon_angle + 135));
                setMotorsModeToEncDrive();
                encoderMecanumDrive(pathhighspeed, -50, -50, 5, 0);

                encoderShoot(0.8);
                intake_motor.setPower(1);
                sleep(1000);
                intake_motor.setPower(0);
                encoderShoot(0.8);

                encoderMecanumDrive(pathhighspeed,-40,-40,5,0);
                encoderMecanumDrive(pathhighspeed, 10, 10, .2, 0);
                encoderMecanumDrive(pathhighspeed, -30, -30, .2, 0);
                timeProfile[profileindex++] = runtime.milliseconds();//32730.99
            }
        }
        else
        {

        }
        for(int i = 0; i < profileindex; i++){
            telemetry.addData(""+i,timeProfile[i]);
        }
        telemetry.update();
        sleep(30000);
    }


    public void pressBeaconButton()
    {
        setMotorsModeToEncDrive();
        stopMotors();
        double distFromWall = rangeSensor.getDistance(DistanceUnit.CM)+6;
        telemetry.addData("distance",distFromWall);
        telemetry.update();

        encoderMecanumDrive(0.7, -distFromWall, -distFromWall, 5,0);
//
        sleep(500);
//
        encoderMecanumDrive(0.7, distFromWall, distFromWall, 5,0);

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


