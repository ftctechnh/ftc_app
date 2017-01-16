package org.firstinspires.ftc.teamcode.Aditya;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.AutonomousGeneral;
import org.firstinspires.ftc.teamcode.Eric.AutonomousRedNearColorEric;

/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "newBeaconPressAuto")
public class newBeaconPress extends AutonomousGeneral {

    OpticalDistanceSensor ODSRight;
    OpticalDistanceSensor ODSLeft;
    OpticalDistanceSensor ODSCenter;
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

        ODSRight = hardwareMap.opticalDistanceSensor.get("ODSRight");
        ODSLeft = hardwareMap.opticalDistanceSensor.get("ODSLeft");
        ODSCenter = hardwareMap.opticalDistanceSensor.get("ODSCenter");
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");

        baseline1 = ODSRight.getRawLightDetected();
        baseline2 = ODSLeft.getRawLightDetected();

        readNewColor();

        waitForStart();


        if (colorSensor.red() > colorSensor.blue()) {
            currentColor = "red";
        } else if (colorSensor.red() < colorSensor.blue()) {
            currentColor = "blue";
        }

        if (currentColor.equals(currentTeam)) {

            allignRangeDist(16.5);

            sleep(150);

            encoderDrive(0.25, 17, 17, 7);

            sleep(400);

            encoderDrive(0.5, -15, -15, 6);

        } else {

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

            if (currentTeam.equals("red")){
                moveToNextBeaconRed();
            } else if (currentTeam.equals("blue")){
                moveToNextBeaconBlue();
            }
        }
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

        while (whiteLineDetectedLeft() == false || whiteLineDetectedRight()){

            if (whiteLineDetectedLeft() == false){
                back_left_motor.setPower(0.6);
                front_left_motor.setPower(0.6);
            }

            if (whiteLineDetectedRight() == false){
                back_right_motor.setPower(0.6);
                front_right_motor.setPower(0.6);
            }
        }

        sleep(250);

        encoderDrive(0.4, -27, 27, 6);

        sleep(150);

        allignRangeDist(19.1);
}

    public void moveToNextBeaconBlue() {
        sleep(250);

        encoderDrive(0.4, -27, 27, 6);

        sleep(150);

        while (whiteLineDetectedLeft() == false || whiteLineDetectedRight()){

            if (whiteLineDetectedLeft() == false){
                back_left_motor.setPower(0.6);
                front_left_motor.setPower(0.6);
            }

            if (whiteLineDetectedRight() == false){
                back_right_motor.setPower(0.6);
                front_right_motor.setPower(0.6);
            }
        }

        sleep(250);

        encoderDrive(0.4, 27, -27, 6);

        sleep(150);

        allignRangeDist(16.5);
    }

    public boolean whiteLineDetectedRight(){
        if ((ODSRight.getRawLightDetected() > (baseline1*5))){
            rightDetected = true;
            return true;
        }
        rightDetected = false;
        return false;
    }

    public boolean whiteLineDetectedLeft(){
        if ((ODSLeft.getRawLightDetected() > (baseline2*5))){
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
}
//-------------------------------------------------------------------//


