package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

/**
 * Created by student on 11/22/17.
 */

public class AutoRobot {

    public ColorSensor colorSensorL;

    public Servo loweringJewelServo;
    public Servo turningJewelServo;

    private DcMotor FrontLeftDrive = null;
    private DcMotor FrontRightDrive = null;
    private DcMotor BackLeftDrive = null;
    private DcMotor BackRightDrive = null;

   // int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
   // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

    public double downPos = .9;
    public final double MID_UP_POS = 0;
    public final double LEFT_POS = .30;
    public final double RIGHT_POS = .70;

    // Telemetry
    public static Telemetry telemetry;

    // The original opmode.
    public static LinearOpMode opMode;

    public void init(LinearOpMode om) {
        opMode = om;
        HardwareMap hardwareMap = opMode.hardwareMap;

        // Motors
        colorSensorL = hardwareMap.get(ColorSensor.class, "color sensor left");
        loweringJewelServo = hardwareMap.get(Servo.class, "lowering servo" );
        turningJewelServo = hardwareMap.get(Servo.class, "turning servo");

        FrontLeftDrive = hardwareMap.get(DcMotor.class, "front_left");
        FrontRightDrive = hardwareMap.get(DcMotor.class, "front_right");
        BackLeftDrive = hardwareMap.get(DcMotor.class, "back_left");
        BackRightDrive = hardwareMap.get(DcMotor.class, "back_right");

        FrontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        BackLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        BackRightDrive.setDirection(DcMotor.Direction.FORWARD);
        FrontRightDrive.setDirection(DcMotor.Direction.FORWARD);

        FrontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        loweringJewelServo.setPosition(0);
        turningJewelServo.setPosition(.5);

        FrontLeftDrive.setPower(0);
        BackLeftDrive.setPower(0);
        BackRightDrive.setPower(0);
        FrontRightDrive.setPower(0);

    }

    public boolean isLeft() {

        telemetry.addData("Red:", colorSensorL.red());
        telemetry.addData("Blue:", colorSensorL.blue());

        telemetry.update();

        if (colorSensorL.red() > colorSensorL.blue()) {
            telemetry.addLine("See Red");
            telemetry.update();
            return true;
        } else {
            telemetry.addLine("See Blue");
            telemetry.update();
            return false;
        }
    }

    public void lower() {

        loweringJewelServo.setPosition(downPos);
        telemetry.addData("lowerArm", loweringJewelServo.getPosition());
        telemetry.update();
    }

    public void jewel(boolean isRed) {
        telemetry.addData("Red:", colorSensorL.red());
        telemetry.addData("Blue:", colorSensorL.blue());

        telemetry.update();

        if (colorSensorL.red() > colorSensorL.blue()) {

            if (isRed) {
                turningJewelServo.setPosition(RIGHT_POS);
                telemetry.addLine("Moving Right");
            } else {
                turningJewelServo.setPosition(LEFT_POS);
                telemetry.addLine("Moving Left");
            }
            opMode.sleep(1500);

            loweringJewelServo.setPosition(MID_UP_POS);
            turningJewelServo.setPosition(.5);
            loweringJewelServo.setPosition(0);
        }
        else if (colorSensorL.red() < colorSensorL.blue()){

            if (isRed) {
                turningJewelServo.setPosition(LEFT_POS);
                telemetry.addLine("Moving Left");
            } else {
                turningJewelServo.setPosition(RIGHT_POS);
                telemetry.addLine("Moving Right");
            }

            opMode.sleep(1500);

            loweringJewelServo.setPosition(MID_UP_POS);
            turningJewelServo.setPosition(.5);
            loweringJewelServo.setPosition(0);
        }
        else {
            turningJewelServo.setPosition(.46);
            loweringJewelServo.setPosition(.95);

            opMode.sleep(1000);

            if (colorSensorL.red() < colorSensorL.blue()) {
                turningJewelServo.setPosition(RIGHT_POS);
                telemetry.addLine("Moving Right");

                opMode.sleep(1000);
                loweringJewelServo.setPosition(0);
                turningJewelServo.setPosition(.5);
            }
            else if (colorSensorL.red() > colorSensorL.blue()){
                turningJewelServo.setPosition(LEFT_POS);
                telemetry.addLine("Hitting Left");

                opMode.sleep(1000);
                loweringJewelServo.setPosition(0);
                turningJewelServo.setPosition(.5);
            }
            else {
                loweringJewelServo.setPosition(MID_UP_POS);
                turningJewelServo.setPosition(.5);
                loweringJewelServo.setPosition(0);
            }
        }
        telemetry.addData("Servo Pos", turningJewelServo.getPosition());
        telemetry.update();
    }


}
