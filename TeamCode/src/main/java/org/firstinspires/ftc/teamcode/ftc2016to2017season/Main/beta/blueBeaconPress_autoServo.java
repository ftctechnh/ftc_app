package org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.beta;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "blueBeacon1")
@Disabled
public class blueBeaconPress_autoServo extends AutonomousGeneral {



    //
    boolean second_beacon_press = false;
    String currentTeam = "blue";
    private ElapsedTime runtime = new ElapsedTime();
    //String currentColor = "blank";

    @Override
    public void runOpMode() {

        initiate();

//        readNewColorLeft();
//        readNewColorRight();


        waitForStart();

        second_beacon_press = false;

        newEncoderDriveShoot(1,-140,-120,2, 1, .1); // 150 = 5 feettime 2.5

        servoBeaconPress();

        }


    public void moveToNextBeacon() {
        second_beacon_press = true;
      //  sleep(250);
        newEncoderDrive(1, 15, 15, .8);
       // sleep(450);
        intake_motor.setPower(.8);
        newEncoderDriveShoot(1, -44, 44, 1.5, 1, .8);
//        gyro_leftTurn(270, 1);
//        shooting_motor.setPower(.8);
        sleep(100);
        shooting_motor.setPower(0);
        intake_motor.setPower(0);
        //sleep(450);
        newEncoderDrive(1, -95, -110, 1);
        servoBeaconPress();
}


    public void lineAlign() {

        setMotorsModeToColorSensing();

        while (whiteLineDetectedFront() == false) {

            straightDrive(-1);

         //   rangeCorrection();

        }
        stopMotors();

        setMotorsModeToColorSensing();

        while (whiteLineDetectedBack() == false) {

            newTurnRight(1);
        }
        stopMotors();
      //  encoderDrive(0.1, 5, 5, 4);
     //   sleep(150);

    }

    public void servoBeaconPress(){
        boolean left_detected = false;
        boolean beacon_press_success = false;

        lineAlign();
      //  sleep(250);
       // allignRangeDistReverse(11);
        setMotorsModeToRangeSensing();
        while (rangeSensor.getDistance(DistanceUnit.CM) > 11) {
            straightDrive(-1);
        }
        stopMotors();
        //sleep(250);

        readNewColorLeft();
        readNewColorRight();


       // sleep(2000);

        if(currentColorBeaconLeft.equals("blank")){
            printColorsSeen();
            setMotorsModeToColorSensing();
            while(currentColorBeaconLeft.equals("blank")){
                turnRight(1);
                readNewColorLeft();
                printColorsSeen();
            }
            stopMotors();
        }
        else if(currentColorBeaconRight.equals("blank")){
            printColorsSeen();
            setMotorsModeToColorSensing();
            while(currentColorBeaconRight.equals("blank")){
                turnLeft(1);
                readNewColorRight();
                printColorsSeen();
            }
            stopMotors();
        }
        //alligns with beacon if out of range

        if(currentColorBeaconLeft.equals(currentTeam)){
            beaconPresser.setPosition(0.0);
            left_detected = true;
        }
        else if(currentColorBeaconRight.equals(currentTeam)){
            beaconPresser.setPosition(1.0);
            left_detected = false;
        }
        //allign servo!

     //   sleep(300);

        pressBeaconButton();
        //presses beacon!

        readNewColorRight();
        readNewColorLeft();

        //presses beacon!


        // below evaluate beacon press result and move to next step if it is success and handle failures if failures are seen
        if (left_detected == true)
        {
            if(currentColorBeaconRight.equals(currentTeam))
            {
                beacon_press_success = true;
            }
        }
        else
        {
            if(currentColorBeaconLeft.equals(currentTeam))
            {
                beacon_press_success = true;
            }

            if((currentColorBeaconLeft.equals("blank"))){
                beacon_press_success = true;
                beaconPresser.setPosition(1.0);
                sleep(100);
                beaconPresser.setPosition(0.0);
            }
        }

        if (beacon_press_success == true)
        {
            if (second_beacon_press == false)
            {
              moveToNextBeacon();
            }
            else
            {
                parkCenterVortex();
            }
        }
        else
        {
            printColorsSeen();

            if ((currentColorBeaconLeft == currentColorBeaconRight) &&
                    (currentColorBeaconLeft != "blank") &&
                    (second_beacon_press == false))
            {
                sleep(4000);

                pressBeaconButton();
            }
            else{
                telemetry.addData("failed", "we are currently working on a solution :)");
                telemetry.update();
                if(left_detected){
                    beaconPresser.setPosition(0.0);
                }
                else
                {
                    beaconPresser.setPosition(1.0);
                }
                pressBeaconButton();

            }
            sleep(500);

            parkCenterVortex();
        }
    }

    public void printColorsSeen(){
        telemetry.addData("left color", currentColorBeaconLeft);
        telemetry.addData("right color", currentColorBeaconRight);
        telemetry.update();
    }

    public void parkCenterVortex()
    {
        if (second_beacon_press)
        {
            newEncoderDrive(1, -30, 30, .5);
            newEncoderDrive(1, 140, 140, 1);
            sleep(500);
            newEncoderDrive(1, -10, -10, .2);
            newEncoderDrive(1, 30, 30, .5);
        }
        else
        {
            newEncoderDrive(1, 12, -12, .5);
            newEncoderDrive(1, 100, 140, 1);
            sleep(500);
            newEncoderDrive(1, -10, -10, .2);
            newEncoderDrive(1, 30, 30, .5);
        }
    }


    public void pressBeaconButton()
    {
        double distFromWall = rangeSensor.getDistance(DistanceUnit.CM)+13;

        newEncoderDrive(1, -distFromWall, -distFromWall, .5);
//
        sleep(500);
//
        newEncoderDrive(1, distFromWall, distFromWall, 1);

//        encoderDrive(.3, -distFromWall, -distFromWall, 1);
//        sleep(500);
//        encoderDrive(.3, distFromWall, distFromWall, 1);
        beaconPresser.setPosition(0.0);
    }

}
//-------------------------------------------------------------------//


