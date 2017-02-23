package org.firstinspires.ftc.teamcode.Aditya;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Main.AutonomousGeneral;
import org.firstinspires.ftc.teamcode.Main.AutonomousGeneral_charlie;


/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "blueBeaconCharlie")
public class blueAuto_charlie extends AutonomousGeneral_charlie {



    //
    boolean second_beacon_press = false;
    String currentTeam = "blue";
    private ElapsedTime runtime = new ElapsedTime();
    public int gyro_corr_cnt =0;
    //String currentColor = "blank";

    @Override
    public void runOpMode() {

        initiate();
        idle();
        setMotorsModeToEncDrive();
        stopMotors();
        gyro.calibrate();
        while(gyro.isCalibrating()){
            idle();
        }
        telemetry.addData("","READY TO START");
        telemetry.update();


        waitForStart();

        second_beacon_press = false;


        encoderMecanumDrive(0.8,100,100,5,-1);

        encoderMecanumDrive(0.8,60,60,5,0);

        telemetry.addData("distance", rangeSensor.getDistance(DistanceUnit.CM));
        telemetry.update();
        servoBeaconPress();

        }


    public void moveToNextBeacon() {
        second_beacon_press = true;
      //  sleep(250);
        encoderMecanumDrive(0.7, -8, -8, 5,0);
        sleep(100);

        idle();
        encoderMecanumDrive(0.7, 95, 95, 5,-1);
        idle();
        servoBeaconPress();

}


    public void lineAlign() {

            setMotorsModeToColorSensing();

            strafeLeft(0.7);
            while (whiteLineDetectedBack() == false) {

                idle();

            }
            stopMotors();

    }

    public void servoBeaconPress(){
        boolean left_detected = false;
        boolean beacon_press_success = false;

      //  sleep(250);
       // allignRangeDistReverse(11);

        setMotorsModeToRangeSensing();


        gyroCorrection();

        setMotorsModeToRangeSensing();
        straightDrive(0.7);
        while (rangeSensor.getDistance(DistanceUnit.CM) > 17) {

        }
        stopMotors();
        idle();

        straightDrive(-0.7);
        while (rangeSensor.getDistance(DistanceUnit.CM) < 14) {

        }
        stopMotors();
idle();

        lineAlign();

        setMotorsModeToRangeSensing();
        encoderMecanumDrive(0.7,10,10,2,1);
        idle();

        readNewColorLeft();

        if(currentColorBeaconLeft.equals(currentTeam)){
            left_detected = true;
        }
        else {
            left_detected = false;
            setMotorsModeToRangeSensing();
            encoderMecanumDrive(0.7,15,15,2,-1);
        }
        //allign servo!

     //   sleep(300);

        pressBeaconButton();
        //presses beacon!


        if (second_beacon_press == false)
        {
            moveToNextBeacon();
        }
        else
        {
            setMotorsModeToRangeSensing();
            gyroCorrection();
            parkCenterVortex();
        }
        // below evaluate beacon press result and move to next step if it is success and handle failures if failures are seen
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
        if (second_beacon_press)
        {
            //encoderMecanumDrive(0.7,-15,-15,5,0);
            encoderMecanumDrive(0.7,108,-108,5,0);
            encoderMecanumDrive(0.7,-35,-35,5,0);
            encoderShoot(0.8);
            intakeDrive(0.8,1);
            encoderShoot(0.8);


            encoderMecanumDrive(0.7,140,140,5,0);
            sleep(500);
            encoderMecanumDrive(0.7, -10, -10, .2,0);
            encoderMecanumDrive(0.7, 30, 30, .2,0);
        }
        else
        {

        }
    }


    public void pressBeaconButton()
    {
        setMotorsModeToEncDrive();
        stopMotors();
        double distFromWall = rangeSensor.getDistance(DistanceUnit.CM)+8;
        telemetry.addData("distance",distFromWall);
        telemetry.update();

        encoderMecanumDrive(0.7, distFromWall, distFromWall, 5,0);
//
        sleep(500);
//
        encoderMecanumDrive(0.7, -distFromWall, -distFromWall, 5,0);

//        encoderDrive(.3, -distFromWall, -distFromWall, 1);
//        sleep(500);
//        encoderDrive(.3, distFromWall, distFromWall, 1);
    }
    public void gyroCorrection(){
        int gyro_read = gyro.getHeading();
        if((gyro_read < 5) || (gyro_read > 355))
        {
            return;
        }
        if (gyro.getHeading()<180){//make sure the angle is correct
            turnRight(0.7);
            while(gyro.getHeading()<180){
                idle();
            }
        }
        else{
            turnLeft(0.7);
            while(gyro.getHeading()>180){
                idle();
            }
        }
        gyro_corr_cnt++;
        telemetry.addData("gyroCorrection done", gyro.getHeading());
        telemetry.addData("gyroCorrection cnt",gyro_corr_cnt);
        telemetry.update();
        stopMotors();
    }


}
//-------------------------------------------------------------------//


