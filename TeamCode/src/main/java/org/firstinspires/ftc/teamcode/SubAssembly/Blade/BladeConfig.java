package org.firstinspires.ftc.teamcode.SubAssembly.Blade;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.SubAssembly.Blade.BladeConfig;
import org.firstinspires.ftc.teamcode.Utilities.EnumWrapper;
import org.firstinspires.ftc.teamcode.Utilities.ServoControl;

import java.util.EnumMap;

/* Sub Assembly Class*/
public class BladeConfig {

    /* Declare private class object */
    private Telemetry telemetry = null;         /* local copy of telemetry object from opmode class */
    private HardwareMap hardwareMap = null;     /* local copy of HardwareMap object from opmode class */
    private String name = "Blade";

    /* servo setpoints */
    public enum Setpoints implements EnumWrapper<Setpoints> {
        POS_1, POS_2, POS_3;
    }

    /* Servos     */
    private Servo bladeServo;

    /* Setpoint enumeration maps */
    private EnumMap<Setpoints, Double> MapServo_0;

    /* Servo control for setpoint maps */
    public ServoControl<Setpoints, EnumMap<Setpoints, Double>> Servo_1;


    /* Subassembly constructor */
    public BladeConfig(LinearOpMode opMode) {
        /* Set local copies from opmode class */
        telemetry = opMode.telemetry;
        hardwareMap = opMode.hardwareMap;

        telemetry.addLine(name + " initialize");

        /* Servo and Setpoint configurations
         * */
        /* Create setpoint maps */
        MapServo_0 = new EnumMap<Setpoints, Double>(Setpoints.class);

        /* Assign setpoint values */
        MapServo_0.put(Setpoints.POS_1, 0.0);
        MapServo_0.put(Setpoints.POS_2, 0.4);
        MapServo_0.put(Setpoints.POS_3, 0.8);
        /* Map hardware devices */
        bladeServo = hardwareMap.servo.get("Blade");

        /* Create servo control objects and initialize positions */
        Servo_1 = new ServoControl(bladeServo, MapServo_0, Setpoints.POS_1, true);
    }

    /* Subassembly methods */
    public void test() {
        telemetry.addLine(name + " test");
    }

}


