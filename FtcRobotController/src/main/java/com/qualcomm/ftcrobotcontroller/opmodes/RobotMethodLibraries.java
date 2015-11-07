package com.qualcomm.ftcrobotcontroller.opmodes;
/**
 *
 * @author sam
 * @since 1.0
 */

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;

/**
 * @deprecated
 * @since 1.2
 * don't use this in a program but feel free to copy code
 */
public class RobotMethodLibraries extends OpMode {
    public DcMotorController Dcm2;
    public DcMotorController Dcm;
    public DcMotorController Dcm3;
    public DcMotor FL;
    public DcMotor FR;
    public DcMotor OtherMotor;
    public DcMotor BL;
    public DcMotor BR;
    public ServoController sr;
    public Servo Lbump;
    public Servo Rbump;
    public ColorSensor CL;
    /**
     * I'm a constructor so you can instantiate me!
     */
    public RobotMethodLibraries() {

    }
    /**
     * enum presets
     */
    public enum Preset {
        ProgramBot,
        LoserBot,
        Nothing,
        Something_I_Dont_Know_About,
        Geoff_config,
        TrevorBot,
        PresetGoesHere,
        Tom,
        Sparkles,
        TooManyCats,
        // Add more Presets here
    }

    /**
     * no parameters means default values
     */
    public void Init_Motors() {
        FL = hardwareMap.dcMotor.get("FL");
        FR = hardwareMap.dcMotor.get("FR");
        BL = hardwareMap.dcMotor.get("BL");
        BR = hardwareMap.dcMotor.get("BR");
        FR.setDirection(DcMotor.Direction.REVERSE);
        BR.setDirection(DcMotor.Direction.REVERSE);
        Lbump = hardwareMap.servo.get("L");
        Rbump = hardwareMap.servo.get("R");
        // sr = hardwareMap.servoController.get("Servo Controller 1");
        // sr = Lbump.getController();
        OtherMotor = hardwareMap.dcMotor.get("Attach");
        // Dcm = hardwareMap.dcMotorController.get("Motor Controller 1");
        // Dcm2 = hardwareMap.dcMotorController.get("Motor Controller 3");
        // Dcm3 = hardwareMap.dcMotorController.get("Motor Controller 2");
        // Dcm = FL.getController();
        // Dcm3 = FR.getController();
        // Dcm2 = OtherMotor.getController();
        /*if (Dcm != FL.getController())
            Dcm = FL.getController();
        if (Dcm2 != OtherMotor.getController())
            Dcm2 = OtherMotor.getController();
        if (Dcm3 != FR.getController())
            Dcm3 = FR.getController();
        if (sr != Lbump.getController())
            sr = Lbump.getController();*/
    }
    /**
     * if you don't want to use the defaults, define and use a preset
     */
    public void Init_Motors(Preset p) {
        if (p == Preset.ProgramBot) {
            FL = hardwareMap.dcMotor.get("FL");
            FR = hardwareMap.dcMotor.get("FR");
            BL = hardwareMap.dcMotor.get("BL");
            BR = hardwareMap.dcMotor.get("BR");
            FR.setDirection(DcMotor.Direction.REVERSE);
            BR.setDirection(DcMotor.Direction.REVERSE);
            Lbump = hardwareMap.servo.get("L");
            Rbump = hardwareMap.servo.get("R");
            // sr = hardwareMap.servoController.get("Servo Controller 1");
            // sr = Lbump.getController();
            OtherMotor = hardwareMap.dcMotor.get("Attach");
            // Dcm = hardwareMap.dcMotorController.get("Motor Controller 1");
            // Dcm2 = hardwareMap.dcMotorController.get("Motor Controller 3");
            // Dcm3 = hardwareMap.dcMotorController.get("Motor Controller 2");
            // Dcm = FL.getController();
            // Dcm3 = FR.getController();
            // Dcm2 = OtherMotor.getController();
        /* if (Dcm != FL.getController())
            Dcm = FL.getController();
        if (Dcm2 != OtherMotor.getController())
            Dcm2 = OtherMotor.getController();
        if (Dcm3 != FR.getController())
            Dcm3 = FR.getController();
        if (sr != Lbump.getController())
            sr = Lbump.getController();*/
        }
        if (p == Preset.Nothing) {
            // nothing here!
        }
        if (p == Preset.Tom) {
            FL = hardwareMap.dcMotor.get("motor 2");
            BL = hardwareMap.dcMotor.get("motor 1");
            FR = hardwareMap.dcMotor.get("motor 4");
            BR = hardwareMap.dcMotor.get("motor 3");
            OtherMotor = hardwareMap.dcMotor.get("motor 5");
            Lbump = hardwareMap.servo.get("servo 1");
            Rbump = hardwareMap.servo.get("servo 2");
            sr = hardwareMap.servoController.get("Servo Controller 1");
            Dcm = hardwareMap.dcMotorController.get("Motor Controller 2");
            Dcm2 = hardwareMap.dcMotorController.get("Motor Controller 3");
            Dcm3 = hardwareMap.dcMotorController.get("Motor Controller 4");
        }
        if (p == Preset.Sparkles) {
            BL = hardwareMap.dcMotor.get("m1");
            FL = hardwareMap.dcMotor.get("m2");
            BR = hardwareMap.dcMotor.get("m3");
            FR = hardwareMap.dcMotor.get("m4");
            OtherMotor = hardwareMap.dcMotor.get("m5");
            Rbump = hardwareMap.servo.get("s1");
            Lbump = hardwareMap.servo.get("s2");
            CL = hardwareMap.colorSensor.get("Color");
        }
    }
    /**
     * @deprecated Init_motors(String, String, String) is deprecated
     * @since 1.0
     * use Init_motors() or Init_motors(Preset p)
     */
    public final void Init_Motors(String M1, String M2, String M3/*, String Mc1*/) {
        FL = hardwareMap.dcMotor.get(M1);
        FR = hardwareMap.dcMotor.get(M2);
        OtherMotor = hardwareMap.dcMotor.get(M3);
        // Dcm = hardwareMap.dcMotorController.get(Mc1);
    }

    public final double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24, 0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }
        double dScale;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }
        return dScale;
    }
    @Override
    public void init() {
        /** ignore it */
    }
    public void loop() {
        /** does nothing */
    }
}