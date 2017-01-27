package org.firstinspires.ftc.teamcode.Aditya;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Main.AutonomousGeneral;



/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "newBlueBeaconPressAutoServo")
public class newBlueBeaconPress_autoServo extends AutonomousGeneral {



    boolean second_beacon_press = false;
    String currentTeam = "blue";

    //String currentColor = "blank";

    @Override
    public void runOpMode() {

        initiate();

        readNewColorLeft();
        readNewColorRight();

        waitForStart();
        second_beacon_press = false;

        encoderDriveShootBlue(0.2, -135, -108, 8.0, -20, 2);

        sleep(250);

        servoBeaconPress();



       // lineAlignRed();

//        pressBeacon();
//        sleep(250);
//
//        sleep(500);
//
//        if (second_beacon_press == true)
//        {
//
//
//        moveToNextBeaconRed();
//
//
//        sleep(150);
//
//        pressBeacon();
//        }

        }


    public void moveToNextBeaconRed() {
        second_beacon_press = true;
        sleep(250);
        encoderDrive(0.4, -25, -25, 6);
        sleep(450);
        encoderDrive(0.2, 29, -29, 6);

        sleep(150);
        encoderDrive(0.5, 65, 65, 6);
}


    public void pressBeacon() {
      {
            telemetry.addData("","lineAlignRed()");
            telemetry.update();
            lineAlign();
        }

        telemetry.addData("","allignRangeDist(12)");
        telemetry.update();
        allignRangeDist(8);
        sleep(400);
        encoderDrive(0.2, 6, 6, 5);
        sleep(400);

        encoderDrive(0.2, -6, -6, 5);
        telemetry.addData("range:",rangeSensor.getDistance(DistanceUnit.CM));
        telemetry.update();
        readNewColorLeft();
        readNewColorRight();
        telemetry.addData("current color is", currentColor);
        telemetry.update();



        if (currentColor.equals(currentTeam)) {
            //second_beacon_press = true;
        }else {
            sleep(5000);

         //   encoderDrive(0.2, (8 + distance - 12), (8 + distance - 12), 5);
            encoderDrive(0.2, 6, 6, 5);
            sleep(400);
            encoderDrive(0.2,-6,-6,10);

            //second_beacon_press = false;
        }
        sleep(400);

        encoderDrive(0.2, 10, -10, 5);
        encoderDrive(0.5, -170, -120, 5);
        sleep(500);
        encoderDrive(0.2, 10, 10, 5);
        encoderDrive(0.2, -20, -20, 5);
    }

    public void lineAlign() {

        while (whiteLineDetectedBack() == false) {

            straightDrive(-0.1);

         //   rangeCorrection();

        }
        sleep(150);
        if (second_beacon_press == true)
        {
            encoderDrive(0.1, -15, -15, 4);
        }
        else
        {
            encoderDrive(0.1, -15, -15, 4);
        }

        sleep(150);

        while (whiteLineDetectedBack() == false) {

            newTurnRight(0.1);
        }
        stopMotors();
      //  encoderDrive(0.1, 5, 5, 4);
        sleep(150);

    }

    public void servoBeaconPress(){
        lineAlign();
        sleep(250);
        allignRangeDistReverse(11);
        sleep(250);

        readNewColorLeft();
        readNewColorRight();
        printColorsSeen();



        if(currentColorBeaconLeft.equals("blank")){
            while(currentColorBeaconLeft.equals("blank")){
                turnRight(0.15);
                readNewColorLeft();
                printColorsSeen();
            }
        }
        else if(currentColorBeaconRight.equals("blank")){
            while(currentColorBeaconRight.equals("blank")){
                turnLeft(0.15);
                readNewColorRight();
                printColorsSeen();
            }
        }

        if(currentColorBeaconLeft.equals(currentTeam)){
            beaconPresser.setPosition(0.0);
        }
        else if(currentColorBeaconRight.equals(currentTeam)){
            beaconPresser.setPosition(1.0);
        }

        sleep(300);

        double distFromWall = rangeSensor.getDistance(DistanceUnit.CM)-2;

        encoderDrive(0.3, -distFromWall, -distFromWall, 6);

        sleep(750);

        encoderDrive(0.3, distFromWall, distFromWall, 6);

        readNewColorRight();
        readNewColorLeft();
        printColorsSeen();

        if(currentColorBeaconLeft.equals(currentTeam) && currentColorBeaconRight.equals(currentTeam)){

            printColorsSeen();
            sleep(400);

            encoderDrive(0.2, 8, -8, 5);
            encoderDrive(0.5, 110, 130, 5);
            sleep(500);
            encoderDrive(0.2, -10, -10, 5);
            encoderDrive(0.2, 30, 30, 5);
        }
        else{

            if (currentColorBeaconLeft == currentColorBeaconRight){
                sleep(4000);

                distFromWall = rangeSensor.getDistance(DistanceUnit.CM)-2;

                encoderDrive(-0.3, distFromWall, distFromWall, 6);

                sleep(2000);

                encoderDrive(0.3, distFromWall, distFromWall, 6);

            }
            else{
                telemetry.addData("failed", "we are currently working on a solution :)");
                telemetry.update();

            }
            sleep(400);

            encoderDrive(0.2, 8, -8, 5);
            encoderDrive(0.5, 110, 130, 5);
            sleep(500);
            encoderDrive(0.2, -10, -10, 5);
            encoderDrive(0.2, 30, 30, 5);
        }
    }

    public void printColorsSeen(){
        telemetry.addData("left color", currentColorBeaconLeft);
        telemetry.addData("right color", currentColorBeaconRight);
        telemetry.update();
    }


}
//-------------------------------------------------------------------//


