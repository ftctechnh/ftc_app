
package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.Range;
import java.lang.Math;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

@Autonomous(name="Preciousss: Autonomous6217Red", group="Preciousss")

/*
 * Created by Josie and Ben on 11/4/16.
 *
 */
public class Auto6217Red extends LinearOpMode {

    //FR = Front Right, FL = Front Left, BR = Back Right, BL = Back Left.
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor motorBR;
    DcMotor motorBL;
    Servo servoTapper;

    NormalizedColorSensor colorSensor;
    static ModernRoboticsI2cGyro gyro;
    boolean iAmBlue = false ;
    boolean iAmRed = true;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotor.Direction.FORWARD);
        motorFR = hardwareMap.dcMotor.get("motorFR");
        motorFR.setDirection(DcMotor.Direction.REVERSE);
        motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBR.setDirection(DcMotor.Direction.REVERSE);
        servoTapper = hardwareMap.servo.get("tapper");

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight)colorSensor).enableLight(true);
        }
        waitForStart();

        boolean autoClear = false;
        telemetry.setAutoClear(autoClear);

        servoTapper.setPosition(0.0d);
        Wait(5);
        servoTapper.setPosition(0.5d);
        Wait(5);
        boolean iSeeBlue = false;
        boolean iSeeRed = false;

        // Read the sensor
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        telemetry.addLine()
                .addData("r", "%.3f", colors.red)
                .addData("b", "%.3f", colors.blue);

        telemetry.update();

        if (colors.red > colors.blue) {
            iSeeRed = true;
            iSeeBlue = false;
            }
        else {
            iSeeBlue = true;
            iSeeRed = false;
        }

        Wait(5);

        if  ((iSeeRed && iAmRed) || (iSeeBlue && iAmBlue)) {
            telemetry.addData("1", "move right");
            // move(0f, .25f, 2);
        }
        else {
            telemetry.addData("1", "move left");
          //  move(0f,-.25f,2);
        }
        telemetry.update();
        servoTapper.setPosition(0.1d);
        Wait(5);
    }

    void move(float posx, float posy, float waitTime) {
        float FRBLPower = posy + posx;
        float FLBRPower = posy - posx;
        motorFR.setPower(FRBLPower);
        motorFL.setPower(FLBRPower);
        motorBR.setPower(FLBRPower);
        motorBL.setPower(FRBLPower);
        Wait(waitTime);
        sR();
    }

    void pivotRight(float waitTime) {

        motorFL.setPower(-1.f);
        motorBL.setPower(-1.f);
        motorFR.setPower(1.f);
        motorBR.setPower(1.f);
        Wait(waitTime);
    }

    void pivotLeft(float waitTime) {

        motorFL.setPower(1.f);
        motorBL.setPower(1.f);
        motorFR.setPower(-1.f);
        motorBR.setPower(-1.f);
        Wait(waitTime);
    }

    void pivotBy(int angle) {

        // Positive angle turns clockwise with power given

        // Any faster than this and the gyro is far less accurate
        float power = .25f;

        // The gyro tends to overestimate the angle
        float fudgeFactor = 0.97f;
        int initialHeading = 0;
        angle = Math.round(angle * fudgeFactor);
        if (angle < 0) {
            // Counterclockwise for negative angle
            power = -power;
            // Always need a positive angle for later comparison involving absolute value
            angle = -angle;
        } else {
            initialHeading = 360;
        }

        gyro.resetZAxisIntegrator();
        motorFL.setPower(-power);
        motorBL.setPower(-power);
        motorFR.setPower(power);
        motorBR.setPower(power);

        int curHeading = 0;
        int iCount = 0;
        while (curHeading < angle) {
            iCount = iCount+1;
            curHeading = Math.abs(gyro.getHeading() - initialHeading);
            telemetry.addData("1", "%03d", curHeading);
            telemetry.addData("2", "%03d", gyro.getIntegratedZValue());
            telemetry.addData("3", "%03d", iCount);
            telemetry.update();
        }
        sR();
    }

    void pivotByZ(int angle) {

        // Positive angle turns clockwise with power given

        // Any faster than this and the gyro is far less accurate
        float power = .2f;

        // The gyro tends to overestimate the angle
        float fudgeFactor = 0.95f;
        angle = Math.round(angle * fudgeFactor);
        if (angle < 0) {
            // Counterclockwise for negative angle
            power = -power;
            // Always need a positive angle for later comparison involving absolute value
            angle = -angle;
        }

        gyro.resetZAxisIntegrator();
        motorFL.setPower(-power);
        motorBL.setPower(-power);
        motorFR.setPower(power);
        motorBR.setPower(power);

        int curHeading = 0;
        int iCount = 0;
        while (curHeading < angle) {
            iCount = iCount+1;
            curHeading = Math.abs(gyro.getIntegratedZValue());
            telemetry.addData("1", "%03d", curHeading);
            telemetry.addData("2", "%03d", gyro.getIntegratedZValue());
            telemetry.addData("3", "%03d", iCount);
            telemetry.update();
        }
        sR();
    }

    void pivotTo(int heading) {

        // Positive angle turns clockwise with power given

        // Any faster than this and the gyro is far less accurate
        float power = .5f;
        float tolerance = 0.1f * heading;

        int initialHeading = gyro.getHeading();
        motorFL.setPower(-power);
        motorBL.setPower(-power);
        motorFR.setPower(power);
        motorBR.setPower(power);

        int curHeading = 0;
        int iCount = 0;
        while (!((curHeading < heading + tolerance) && (curHeading > heading - tolerance))) {
            iCount = iCount++;
            curHeading = gyro.getHeading();
            telemetry.addData("1", "%03d", curHeading);
            telemetry.addData("2", "%03d", iCount);
            telemetry.update();
        }
        sR();
    }

    void letsGo(float posx, float posy) {
        float FRBLPower = posy - posx;
        float FLBRPower = posy + posx;
        motorFR.setPower(FRBLPower);
        motorFL.setPower(FLBRPower);
        motorBR.setPower(FLBRPower);
        motorBL.setPower(FRBLPower);
        Wait(0.1f);
    }

    void sR() {
        float power = 0.f;
        motorFL.setPower(power);
        motorBL.setPower(power);
        motorFR.setPower(power);
        motorBR.setPower(power);
    }

    void Wait(double WaitTime) {
        runtime.reset();
        while (runtime.seconds() < WaitTime) {
            //Comment this out to avoid it overwriting other telemetry
            //telemetry.addData("5", " %2.5f S Elapsed", runtime.seconds());
            //telemetry.update();
        }
    }

    double lightLevel(double odsVal) {
        /*
         * This method adjusts the light sensor input, read as an ODS,
         * to obtain values that are large enough to use to drive
         * the robot.  It does this by taking the third decimal values
         * and beyond as the working values.
         */
        // LB = left bumper, RB = right bumper.
        int prefix = (int) (odsVal * 10);
        odsVal = Math.pow(10, odsVal) * prefix;

        return (odsVal);
    }
}














