package org.firstinspires.ftc.teamcode.Aditya;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.AutonomousGeneral;
import org.firstinspires.ftc.teamcode.Eric.AutonomousRedNearColorEric;

/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "newBeaconPressAuto")
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
        shootingDrive(0.8, 850);;
        //shootingDrive(0.8, 850);
        sleep(500);

        encoderDrive(0.3, 11.5,-11.5,5);
        sleep(500);
        //end of steven's code

        if (colorSensor.red() > colorSensor.blue()) {
            currentColor = "red";
        } else if (colorSensor.red() < colorSensor.blue()) {
            currentColor = "blue";
        }

        pressBeacon();

        sleep(150);

            if (currentTeam.equals("red")){
                moveToNextBeaconRed();
            } else if (currentTeam.equals("blue")){
                moveToNextBeaconBlue();
            }

        sleep(150);

        pressBeacon();

        }

    public void readNewColor() {

        currentColor = "blank";

        if (colorSensor.red() > colorSensor.blue()) {
            currentColor = "red";

            telemetry.addData("current color is red", colorSensor.red());
            telemetry.update();
        } else if (colorSensor.red() < colorSensor.blue()) {
            currentColor = "blue";

            telemetry.addData("current color is blue", colorSensor.blue());
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

        while (rangeSensor.cmOptical() > distInCM){
            straightDrive(0.3);
        }
        while (rangeSensor.cmOptical() < distInCM){
            straightDrive(-0.3);
        }
    }

    public void pressBeacon() {

        if(currentTeam.equals("blue")) {
            lineAlignBlue();
        } else {
            lineAlignRed();
        }

        readNewColor();
        if (currentColor.equals(currentTeam)) {

            allignRangeDist(19.1);

            encoderDrive(0.3, 3.5, -3.5, 5);

            sleep(150);

            encoderDrive(0.25, 17, 17, 7);

            sleep(400);

            encoderDrive(0.5, -15, -15, 6);

            sleep(200);

            encoderDrive(0.3, -3.5, 3.5, 5);

            sleep(150);
        } else {

            allignRangeDist(19.1);

            encoderDrive(0.5, -7, -7, 5);

            sleep(500);

            encoderDrive(0.4, -7.5, 7.5, 4);
            //currently, this turn value is made for alpha. Adjust for beta

            sleep(150);

            encoderDrive(0.25, 28, 28, 8);

            sleep(400);

            encoderDrive(0.5, -15, -15, 6);

            sleep(150);

            encoderDrive(0.7, 7.5, -7.5, 7);
            //currently, this turn value is made for alpha. Adjust for beta

            sleep(450);

            readNewColor();
        }
    }

    public void lineAlignRed() {

        while (whiteLineDetectedFront() == false) {

            straightDrive(0.4);

            if (rangeSensor.getDistance(DistanceUnit.CM) < 20) {
                encoderDrive(0.4, 10, 10, 6);
                sleep(250);
                encoderDrive(0.6, 20, -20, 8);
            }
        }

        encoderDrive(0.3, 8, 8, 4);

        while (whiteLineDetectedFront() == false) {

            newTurnRight(0.3);
        }

        encoderDrive(0.3, 5, -5, 6);
    }

    public void lineAlignBlue() {

        while (whiteLineDetectedFront() == false) {

            straightDrive(0.4);

            if (rangeSensor.getDistance(DistanceUnit.CM) < 20) {
                encoderDrive(0.4, 10, 10, 6);
                sleep(250);
                encoderDrive(0.6, -20, 20, 8);
            }
        }

        encoderDrive(0.3, 8, 8, 4);

        while (whiteLineDetectedFront() == false) {

            newTurnLeft(0.3);
        }

        encoderDrive(0.3, -5, 5, 6);
    }
}
//-------------------------------------------------------------------//


