package org.firstinspires.ftc.teamcode.ftc2016to2017season.Aditya;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.beta.AutonomousGeneral;


/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "newBlueBeaconExp")
@Disabled
public class newBlueBeaconPress_autoServoExp extends AutonomousGeneral {



    boolean second_beacon_press = false;
    String currentTeam = "blue";
    private ElapsedTime runtime = new ElapsedTime();
    //String currentColor = "blank";

    @Override
    public void runOpMode() {

        initiate();

        readNewColorLeft();
        readNewColorRight();

        waitForStart();
        second_beacon_press = false;

        //encoderDriveShootBlue(0.2, -135, -108, 8.0, -20, 2);

     //   newEncoderDrive(1,-135,-110,2);

      //  servoBeaconPress();

        }


    public void moveToNextBeacon() {
        second_beacon_press = true;
        sleep(250);
        newEncoderDrive(1, 25, 25, 1);
        sleep(450);
        newEncoderDrive(1, -39, 39, 2);

        sleep(450);
        newEncoderDrive(1, -85, -85, 1);
        servoBeaconPress();
}


    public void lineAlign() {

        setMotorsModeToColorSensing();

        while (whiteLineDetectedBack() == false) {

            straightDrive(-1);

         //   rangeCorrection();

        }
        stopMotors();
     //   sleep(150);
        if (second_beacon_press == true)
        {
            newEncoderDrive(1, -19, -19, .75);
        }
        else
        {
            newEncoderDrive(1, -19, -19, .75);
        }

     //   sleep(150);

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
        allignRangeDistReverse(11);
        //sleep(250);

        readNewColorLeft();
        readNewColorRight();
        printColorsSeen();

       // sleep(2000);

        if(currentColorBeaconLeft.equals("blank")){
            setMotorsModeToColorSensing();
            while(currentColorBeaconLeft.equals("blank")){
                turnRight(1);
                readNewColorLeft();
                printColorsSeen();
            }
            stopMotors();
        }
        else if(currentColorBeaconRight.equals("blank")){
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
        printColorsSeen();
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
        }

        if (beacon_press_success == true)
        {
            if (second_beacon_press == false)
            {
              moveToNextBeacon();
            }
        }
        else
        {

            if ((currentColorBeaconLeft == currentColorBeaconRight) && (currentColorBeaconLeft != "blank"))
            {
                sleep(4000);

                pressBeaconButton();
            }
            else{
                telemetry.addData("failed", "we are currently working on a solution :)");
                telemetry.update();

            }
            sleep(400);

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
        newEncoderDrive(1, 8, -8, .5);
        newEncoderDrive(1, 110, 130, 1);
        sleep(500);
        newEncoderDrive(1, -10, -10, .2);
        newEncoderDrive(1, 30, 30, .5);
    }

    public void pressBeaconButton()
    {
        double distFromWall = rangeSensor.getDistance(DistanceUnit.CM)+9;

        newEncoderDrive(1, -distFromWall, -distFromWall, 1);

    //    sleep(1000);

        newEncoderDrive(1, distFromWall, distFromWall, 1);
        beaconPresser.setPosition(0.0);
    }

}
//-------------------------------------------------------------------//


