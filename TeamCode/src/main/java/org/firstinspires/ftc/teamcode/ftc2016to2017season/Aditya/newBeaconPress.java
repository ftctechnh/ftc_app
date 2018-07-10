package org.firstinspires.ftc.teamcode.ftc2016to2017season.Aditya;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.beta.AutonomousGeneral;

/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "newBeaconPressAuto_original")
@Disabled
public class newBeaconPress extends AutonomousGeneral {

    OpticalDistanceSensor ODSFront;
    OpticalDistanceSensor ODSBack;
    ModernRoboticsI2cRangeSensor rangeSensor;

    double baseline1;
    double baseline2;

    boolean rightDetected;
    boolean leftDetected;

    String currentTeam = "red";
    String currentColor = "blank";

    @Override
    public void runOpMode() {

        initiate();

        ODSFront = hardwareMap.opticalDistanceSensor.get("ODSFront");
        ODSBack = hardwareMap.opticalDistanceSensor.get("ODSBack");
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");

        baseline1 = ODSFront.getRawLightDetected();
        baseline2 = ODSBack.getRawLightDetected();

        readNewColor();

        waitForStart();
/*
        encoderDrive(0.3, 38.1, 38.1, 8.0);  // S1: Forward 47 Inches with 5 Sec timeout
        sleep(500);
        //sleep(1000);     // pause for servos to move

        encoderDrive(0.3,-27,27,8.0);
        sleep(1000);


        shootingDrive(0.8,850);



        sleep(500);     // pause for servos to move
        intakeDrive(0.8, 1800);
        sleep(800);
        shootingDrive(0.8, 850);;
        //shootingDrive(0.8, 850);


        sleep(500);     // pause for servos to move
        intakeDrive(0.8, 1800);
        sleep(800);
        //shootingDrive(0.8, 850);;
        //shootingDrive(0.8, 850);
        sleep(500);

        encoderDrive(0.3, 8.5,-8.5,5);
        sleep(500);
        //end of steven's code
*/
        if (bColorSensorLeft.red() > bColorSensorLeft.blue()) {
            currentColor = "red";
        } else if (bColorSensorLeft.red() < bColorSensorLeft.blue()) {
            currentColor = "blue";
        }

        readNewColor();
       // lineAlignRed();

        pressBeacon();

        sleep(200);

        moveToNextBeaconRed();

        sleep(200);

        readNewColor();
        sleep(50);
        pressBeacon();
        /*sleep(150);

            if (currentTeam.equals("red")){
                moveToNextBeaconRed();
            } else if (currentTeam.equals("blue")){
                moveToNextBeaconBlue();
            }

        sleep(150);

        pressBeacon();  */

        }

    public void readNewColor() {

        currentColor = "blank";

        if (bColorSensorLeft.red() > bColorSensorLeft.blue()) {
            currentColor = "red";

            telemetry.addData("current color is red", bColorSensorLeft.red());
            telemetry.update();
        } else if (bColorSensorLeft.red() < bColorSensorLeft.blue()) {
            currentColor = "blue";

            telemetry.addData("current color is blue", bColorSensorLeft.blue());
            telemetry.update();

        } else {

            currentColor = "blank";
        }
    }

    public void moveToNextBeaconRed() {

        sleep(250);

        encoderDrive(0.4, 27, -27, 6);

        sleep(150);

        lineAlignRed();

        sleep(150);

        allignRangeDist(19.1);
}

    public void moveToNextBeaconBlue() {

        sleep(250);

        encoderDrive(0.4, -27, 27, 6);

        sleep(150);

        lineAlignBlue();

        sleep(150);

        allignRangeDist(19.1);
    }

    public boolean whiteLineDetectedFront(){
        if ((ODSFront.getRawLightDetected() > (baseline1*5))){
            rightDetected = true;
            return true;
        }
        rightDetected = false;
        return false;
    }

    public boolean whiteLineDetectedBack(){
        if ((ODSBack.getRawLightDetected() > (baseline2*5))){
            leftDetected = true;
            return true;
        }
        leftDetected = false;
        return false;
    }

    public void allignRangeDist(double distInCM){

        while (rangeSensor.getDistance(DistanceUnit.CM) > distInCM){
            straightDrive(0.2);
        }

        while (rangeSensor.getDistance(DistanceUnit.CM) < distInCM){
            straightDrive(-0.2);
        }
        stopMotors();
    }

    public void pressBeacon() {

        if(currentTeam.equals("blue")) {
            lineAlignBlue();
        } else {
            lineAlignRed();
        }

        allignRangeDist(19.1);
        //sleep(5000);

        readNewColor();
        if (currentColor.equals(currentTeam)) {


          //  encoderDrive(0.2, 3.5, -3.5, 5);

          //  sleep(150);

            encoderDrive(0.2, 19, 19, 7);

            sleep(400);

            encoderDrive(0.5, -15, -15, 6);

            sleep(200);

         //   encoderDrive(0.3, -3.5, 3.5, 5);

         //   sleep(150);
        } else {


         //   encoderDrive(0.5, -7, -7, 5);

            sleep(500);

            encoderDrive(0.3, 10, -10, 4);
            //currently, this turn value is made for alpha. Adjust for beta

            sleep(150);

            encoderDrive(0.25, 21, 21, 8);

            sleep(400);

            encoderDrive(0.5, -21, -21, 6);

            sleep(150);

            encoderDrive(0.7, -10, -10, 7);
            //currently, this turn value is made for alpha. Adjust for beta

            sleep(450);

            readNewColor();
        }
    }

    public void lineAlignRed() {

        while (whiteLineDetectedFront() == false) {

            straightDrive(0.2);

            if (rangeSensor.getDistance(DistanceUnit.CM) < 20) {
                encoderDrive(0.4, -15, -15, 6);
                sleep(250);
                encoderDrive(0.6, 20, -20, 8);
            }
        }

        encoderDrive(0.2, 6.35, 6.35, 4);
        sleep(100);

        while (whiteLineDetectedBack() == false) {

            newTurnLeft(0.1);
        }
        stopMotors();
        sleep(100);
            encoderDrive(0.1, -1, 1, 6);
       // stopMotors();
        sleep(100);

    }

    public void lineAlignBlue() {

        while (whiteLineDetectedFront() == false) {

            straightDrive(0.4);

            if (rangeSensor.getDistance(DistanceUnit.CM) < 20) {
                encoderDrive(0.4, -10, -10, 6);
                sleep(250);
                encoderDrive(0.6, -20, 20, 8);
            }
        }

        encoderDrive(0.3, 8, 8, 4);

        while (whiteLineDetectedFront() == false) {

            newTurnLeft(0.3);
        }

        //encoderDrive(0.3, -5, 5, 6);
    }
}
//-------------------------------------------------------------------//


