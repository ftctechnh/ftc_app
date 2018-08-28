package org.firstinspires.ftc.teamcode.SubAssembly.Sample;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utilities.EnumWrapper;
import org.firstinspires.ftc.teamcode.Utilities.ServoControl;

import java.util.EnumMap;


/* Sub Assembly Class
 */
public class SampleTemplate {
    /* Declare private class object */
    private Telemetry telemetry = null;         /* local copy of telemetry object from opmode class */
    private HardwareMap hardwareMap = null;     /* local copy of HardwareMap object from opmode class */
    private String name = "SubAssemblyName";

    /* Servos     */
    private Servo SERVO_1;

    /* Setpoint enumeration maps */
    private EnumMap<Setpoints, Double> MapServo_1;


    /* Declare public class object */
    public VoltageSensor Battery = null;

    /* Servo and Setpoint configurations
     * */
    /* Servo control for setpoint maps */
    public ServoControl<Setpoints, EnumMap<Setpoints, Double>> Servo_1;

    /* servo setpoints */
    public enum Setpoints implements EnumWrapper<Setpoints> {
        POS_1, POS_2, POS_3;
    }

    /* Subassembly constructor */
    public SampleTemplate(LinearOpMode opMode) {
        /* Set local copies from opmode class */
        telemetry = opMode.telemetry;
        hardwareMap = opMode.hardwareMap;

        telemetry.addLine(name + " initialize");

        /* Map hardware devices */
        Battery = hardwareMap.voltageSensor.get("Lower hub 3");

        /* Servo and Setpoint configurations
         * */
        /* Create setpoint maps */
        MapServo_1 = new EnumMap<Setpoints, Double>(Setpoints.class);

        /* Assign setpoint values */
        MapServo_1.put(Setpoints.POS_1, 0.0);
        MapServo_1.put(Setpoints.POS_2, 0.4);
        MapServo_1.put(Setpoints.POS_3, 0.8);

        /* Map hardware devices */
        SERVO_1 = hardwareMap.servo.get("ServoName");

        /* Create servo control objects and initialize positions */
        Servo_1 = new ServoControl(SERVO_1, MapServo_1, Setpoints.POS_1, true);
    }

    /* Subassembly methods */
    public void test() {
        telemetry.addLine(name + " test");
        telemetry.addData("Battery level = ", "%.2f V", Battery.getVoltage());
    }

}
