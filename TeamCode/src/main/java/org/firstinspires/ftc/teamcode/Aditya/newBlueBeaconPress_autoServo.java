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

        //encoderDriveShootBlue(0.2, -135, -108, 8.0, -20, 2);

        sleep(250); //sleeps for 0.25 seconds

        servoBeaconPress(); //presses beacon



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
        sleep(250); // sleeps 0.25 seconds
        encoderDrive(0.4, -25, -25, 6); //drives backward 25cm
        sleep(450); // sleeps 0.45 seconds
        encoderDrive(0.2, 29, -29, 6); //turns with making 29 cm on wheels

        sleep(150); // sleeps 0.15 seconds
        encoderDrive(0.5, 65, 65, 6); // drives foreward 65 cm
}


    public void pressBeacon() {
      {
            telemetry.addData("","lineAlignRed()"); // This is what gets semt to rc
            telemetry.update(); // This updates the information above
            lineAlign();//aligns to line
        }

        telemetry.addData("","allignRangeDist(12)"); // This is what gets semt to rc
        telemetry.update(); // This updates the information above
        allignRangeDist(8); // unknown
        sleep(400); // sleeps 0.4 seconds
        encoderDrive(0.2, 6, 6, 5); // drives foreward 6 cm
        sleep(400); // sleeps 0.4 seconds

        encoderDrive(0.2, -6, -6, 5); // drives backward 6 cm
        telemetry.addData("range:",rangeSensor.getDistance(DistanceUnit.CM)); // tells range sensor distance
        telemetry.update(); //sends info above
        readNewColorLeft(); // reads left color
        readNewColorRight(); // reads right color
        telemetry.addData("current color is", currentColor); // tells current color (of which?)
        telemetry.update(); // sends info above



        if (currentColor.equals(currentTeam)) {
            //second_beacon_press = true;
        }else {
            sleep(5000); // sleeps for 5 seconds

         //   encoderDrive(0.2, (8 + distance - 12), (8 + distance - 12), 5);
            encoderDrive(0.2, 6, 6, 5); //drives 6 cm foreword
            sleep(400); // waits 0.4 seconds
            encoderDrive(0.2,-6,-6,10); // drives backward

            //second_beacon_press = false;
        }
        sleep(400); // sleeps for 0.4 seconds

        encoderDrive(0.2, 10, -10, 5); // turns both wheels 6 cm
        encoderDrive(0.5, -170, -120, 5); // makes arc turn backward
        sleep(500); // waits 0.5 seconds
        encoderDrive(0.2, 10, 10, 5); // goes foreword 10 cm
        encoderDrive(0.2, -20, -20, 5); /// goes backward 20 cm
    }

    public void lineAlign() {

        while (whiteLineDetectedBack() == false) {

            straightDrive(-0.1);

         //   rangeCorrection();

        }
        sleep(150); // waits 0.15
        if (second_beacon_press == true)
        {
            encoderDrive(0.1, -15, -15, 4); // drive backward 15 cm
        }
        else
        {
            encoderDrive(0.1, -15, -15, 4); //  drives backward 15 cm
        }

        sleep(150); // waits 0.15 seconds

        while (whiteLineDetectedBack() == false) {

            newTurnRight(0.1);
        }
        stopMotors();
      //  encoderDrive(0.1, 5, 5, 4); // drives foreward
        sleep(150); // waits 0.15 seconds

    }

    public void servoBeaconPress(){
        lineAlign();
        sleep(250);
        allignRangeDistReverse(11);
        sleep(250);

        readNewColorLeft();
        readNewColorRight();
        printColorsSeen();

        sleep(2000);

        if(currentColorBeaconLeft.equals("blank")){
            while(currentColorBeaconLeft.equals("blank")){
                turnRight(0.05);
                readNewColorLeft();
                printColorsSeen();
            }
        }
        else if(currentColorBeaconRight.equals("blank")){
            while(currentColorBeaconRight.equals("blank")){
                turnLeft(0.05);
                readNewColorRight();
                printColorsSeen();
            }
        }
        //alligns with beacon if out of range

        if(currentColorBeaconLeft.equals(currentTeam)){
            beaconPresser.setPosition(0.0);
        }
        else if(currentColorBeaconRight.equals(currentTeam)){
            beaconPresser.setPosition(1.0);
        }
        //allign servo!

        sleep(300);

//        double distFromWall = rangeSensor.getDistance(DistanceUnit.CM)-2;
//
//        encoderDrive(0.3, -distFromWall, -distFromWall, 6);
//
//        sleep(750);
//
//        encoderDrive(0.3, distFromWall, distFromWall, 6);
        //presses beacon!

        readNewColorRight();
        readNewColorLeft();
        printColorsSeen();
        //presses beacon!

//        if(currentColorBeaconLeft.equals(currentTeam) && currentColorBeaconRight.equals(currentTeam)){
//
//            printColorsSeen();
//            sleep(400);
//
//            encoderDrive(0.2, 8, -8, 5);
//            encoderDrive(0.5, 110, 130, 5);
//            sleep(500);
//            encoderDrive(0.2, -10, -10, 5);
//            encoderDrive(0.2, 30, 30, 5);
//        }
//        else{
//
//            if (currentColorBeaconLeft == currentColorBeaconRight){
//                sleep(4000);
//
//                distFromWall = rangeSensor.getDistance(DistanceUnit.CM)-2;
//
//                encoderDrive(-0.3, distFromWall, distFromWall, 6);
//
//                sleep(2000);
//
//                encoderDrive(0.3, distFromWall, distFromWall, 6);
//
//            }
//            else{
//                telemetry.addData("failed", "we are currently working on a solution :)");
//                telemetry.update();
//
//            }
//            sleep(400);
//
//            encoderDrive(0.2, 8, -8, 5);
//            encoderDrive(0.5, 110, 130, 5);
//            sleep(500);
//            encoderDrive(0.2, -10, -10, 5);
//            encoderDrive(0.2, 30, 30, 5);
//        }
    }

    public void printColorsSeen(){
        telemetry.addData("left color", currentColorBeaconLeft);
        telemetry.addData("right color", currentColorBeaconRight);
        telemetry.update();
    }


}
//-------------------------------------------------------------------//


