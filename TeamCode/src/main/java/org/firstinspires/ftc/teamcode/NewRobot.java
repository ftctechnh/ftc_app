package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Jeremy and Raghav on 10/18/2017.
 */

//! NOTE -1 is FORWARD ON JOYSTICK
public class NewRobot
{
    private int liftLevels[] = {0,72,144,288}; //Currently not levels or stops
    private int liftDeadzone = 10;
    private short currentLvl;

    private ColorSensor topColorSens = null;
    private ColorSensor forwardColorSens = null;
    private ColorSensor floorColorSens = null;

    private DcMotorImplEx liftMotor = null;
    private DcMotorImplEx wingMotor = null;
    private DcMotorImplEx driveLeftOne = null;
    private DcMotorImplEx driveRightOne = null;

    public static final String VUFORIA_KEY = "AepnoMf/////AAAAGWsPSj5vh0WQpMc0OEApBsgbZVwduMSeEZFjXMlBPW7WiZRgwGXsOTLiGMxL4qjU0MYpZitHxs4E/nOUHseMX+SW0oopu6BnWL3cAqFIptSrdMpy4y6yB3N6l+FPcGFZxzadvRoiOfAuYIu5QMHSeulfQ1XApDhBQ79lNUXv9LZ7bngBI3BEYVB+slmTGHKhRW2NI5fUtF+rLRiou4ZcNir2eZh0OxEW4zAnTnciVB2R28yyHkYz8xJtACm+4heWLdpw/zf66LRpvTGLwkASci7ZkGJp4NrG5Of4C0b3+iq/EeEmX2PiY5lq2fkUE0dejdztmkFWYBW7c/Y+bIYGER/3gt6I8UhAB78cR7p2mOaY"; //Key used for Vuforia.
    private VuforiaLocalizer vuforia = null;
    private RelicRecoveryVuMark vuMark = null;
    private VuforiaTrackables relicTrackables = null;
    private VuforiaTrackable relicTemplate = null;

    public NewRobot(HardwareMap hardwareMap)
    {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        //Comment out if you don't want camera view on robo phone
        //VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        //floorColorSens = hardwareMap.colorSensor.get("floorColorSens");
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        relicTrackables.activate();
        vuMark = RelicRecoveryVuMark.from(relicTemplate);

        liftMotor = hardwareMap.get(DcMotorImplEx.class, "liftMotor");
        wingMotor = hardwareMap.get(DcMotorImplEx.class, "wingMotor");
        zeroStuff();
    }

    public char getGlyphCipher()
    {
        vuMark = RelicRecoveryVuMark.from(relicTemplate);

        if (vuMark.equals(RelicRecoveryVuMark.CENTER))
            return 'c';
        else if (vuMark.equals(RelicRecoveryVuMark.LEFT))
            return 'l';
        else if (vuMark.equals(RelicRecoveryVuMark.RIGHT))
            return 'r';
        else
            return '?';
    }

    public float getHueValue(ColorSensor in_ColorSens)
    {
        float hsvValues[] = {0F,0F,0F};
        Color.RGBToHSV(in_ColorSens.red(), in_ColorSens.green(), in_ColorSens.blue(), hsvValues);
        return hsvValues[0];
    }

    public char getColor(ColorSensor in_ColorSens)
    {
        float hue = getHueValue( in_ColorSens);

        if (hue < 5 || hue > 330)
            return 'r';
        else if (hue > 219 && hue < 241)
            return 'b';
        else
            return '?';
    }

    public void zeroStuff() //Methods sets motors at low power to put the motors to their resting positions
    {                       //basically sets up lift's step counts starting at its bottom position
        currentLvl = 0;
        resetEncoders();
    }

    private void resetEncoders()//sets encoders to 0 for motors
    {
        liftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        liftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        liftMotor.setVelocity(0, AngleUnit.DEGREES);

        wingMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        wingMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        wingMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wingMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        wingMotor.setVelocity(0, AngleUnit.DEGREES);
    }

    public void moveLift(int adjLevels)
    {
        moveLift(adjLevels, .24f);
    }

    public void moveLift(int adjLevels, float pow) //For the lift, I'll use levels or encoders points that stop
    {
        if (adjLevels + currentLvl < 0)
            return;
        else if (adjLevels + currentLvl > 3)
            return;

        currentLvl += adjLevels;

        if (adjLevels > 0)
        {
            liftMotor.setPower(-Math.abs(pow));
            while (-liftMotor.getCurrentPosition() < liftLevels[currentLvl] - liftDeadzone){}
        }
        else
        {
            liftMotor.setPower(Math.abs(pow));
            while (-liftMotor.getCurrentPosition() > liftLevels[currentLvl] + liftDeadzone){}
        }

        liftMotor.setPower(0);
    }

    public void fineMoveLift(float y)
    {
        fineMoveLift(y, .15f);
    }

    public void fineMoveLift(float y, float factor)
    {
        if (y > .3)
        {
            liftMotor.setPower(Math.abs(y * factor));
        }
        else if (y < -.3)
        {
            liftMotor.setPower(-Math.abs(factor * y));
        }
        else
        {
            liftMotor.setPower(0);
        }
    }

    public void moveWing(boolean moveDown)
    {
        if(moveDown)
        {
            wingMotor.setPower(-.3);
            while(wingMotor.getCurrentPosition() > -300){}
        }
        else
        {
            wingMotor.setPower(.3);
            while(wingMotor.getCurrentPosition() < 0){}
        }

        wingMotor.setPower(0);
    }

    public void moveWing(float pow)
    {
        wingMotor.setPower(pow);
    }

    public ColorSensor getTopColorSens()
    {
        return topColorSens;
    }

    public ColorSensor getForwardColorSens()
    {
        return forwardColorSens;
    }

    public ColorSensor getFloorColorSens()
    {
        return floorColorSens;
    }

    public DcMotorImplEx getLiftMotor()
    {
        return liftMotor;
    }

    public DcMotorImplEx getWingMotor()
    {
        return wingMotor;
    }
}
