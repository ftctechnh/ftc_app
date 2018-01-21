
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="Preciousss: JuryAutonomous6217Blue2", group="Preciousss")

/*
 * Created by Josie and Ben on 11/4/17.
 *
 */
public class JuryAutonomous6217Blue2 extends LinearOpMode {

    //FR = Front Right, FL = Front Left, BR = Back Right, BL = Back Left.
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor motorBR;
    DcMotor motorBL;
    Servo servoTapper;

    NormalizedColorSensor colorSensor;
    static ModernRoboticsI2cGyro gyro;
    boolean iAmBlue = false;
    boolean iAmRed = true;
    boolean isBoxSide = true;



    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        // V u f o r i a  s e t u p



        // H a r d w a r e   M a p p i n g

        motorFR = hardwareMap.dcMotor.get("motorFR");
        motorFR.setDirection(DcMotor.Direction.REVERSE);
        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotor.Direction.FORWARD);
        motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBR.setDirection(DcMotor.Direction.REVERSE);

        servoTapper = hardwareMap.servo.get("tapper");
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight) colorSensor).enableLight(true);
        }


        // S t a r t

        waitForStart();

        // J e w e l s

        boolean autoClear = false;
        telemetry.setAutoClear(autoClear);
        telemetry.addLine("starting");
        telemetry.update();

        servoTapper.setPosition(0.0d);
        Wait(1);
        servoTapper.setPosition(0.7d);
        Wait(1);
        boolean iSeeBlue = false;
        boolean iSeeRed = false;

        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        telemetry.addLine()
                .addData("r", "%.3f", colors.red)
                .addData("b", "%.3f", colors.blue);

        telemetry.update();

        if (colors.red > colors.blue) {
            iSeeRed = true;
            iSeeBlue = false;
        } else {
            iSeeBlue = true;
            iSeeRed = false;
        }

        Wait(.2f);

        if ((iSeeRed && iAmRed) || (iSeeBlue && iAmBlue)) {
            telemetry.addData("1", "move right");
            move(0f, -.2f, .3f);
            Wait(.2);
            servoTapper.setPosition(0.2d);
            Wait(.2);
            move(0f, .2f, .3f);
        } else {
            telemetry.addData("1", "move left");
            move(0f, .2f, .3f);
            Wait(.2);
            servoTapper.setPosition(0.2d);
            Wait(.2);
            move(0f, -.2f, .3f);
        }


        move(0f, 0.5f, .23f);

        Wait(1);

        move(-.7f,0f,1f);

        Wait(1);

        pivotRight(1.735f);

        Wait(1);

        move(0f,-.25f,.2f);

        Wait(1);



        move(0f,.25f,.35f);

        Wait(1);

        move(0f,-.25f,.5f);
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

        motorFL.setPower(-.5f);
        motorBL.setPower(-.5f);
        motorFR.setPower(.5f);
        motorBR.setPower(.5f);
        Wait(waitTime);
        sR();
    }

    void pivotLeft(float waitTime) {

        motorFL.setPower(.25f);
        motorBL.setPower(.25f);
        motorFR.setPower(-.25f);
        motorBR.setPower(-.25f);
        Wait(waitTime);
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














