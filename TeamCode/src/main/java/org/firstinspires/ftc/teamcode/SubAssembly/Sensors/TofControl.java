package org.firstinspires.ftc.teamcode.SubAssembly.Sensors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;


import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/* Sub Assembly Class
 */
public class TofControl {
    /* Declare private class object */
    private LinearOpMode opmode = null; /* local copy of HardwareMap object from opmode class */

    //initializing motors
    private Rev2mDistanceSensor Tof1 = null;
    private Rev2mDistanceSensor Tof2 = null;
    private Rev2mDistanceSensor Tof3 = null;

    public double distance1;
    public double distance2;
    public double distance3;

    /* Subassembly constructor */
    public TofControl() {
    }

    public void init(LinearOpMode opMode) {
        HardwareMap hwMap;
        opMode.telemetry.addLine("Tof Control" + " initialize");
        opMode.telemetry.update();

        /* Set local copies from opmode class */
        opmode = opMode;
        hwMap = opMode.hardwareMap;

        /* Map hardware devices */
        Tof1 = hwMap.get(Rev2mDistanceSensor.class, "Tof1");
        Tof2 = hwMap.get(Rev2mDistanceSensor.class, "Tof2");
        Tof3 = hwMap.get(Rev2mDistanceSensor.class, "Tof3");
    }

    public void GetDistance() {
        opmode.telemetry.addData("range1", String.format("%.01f cm", Tof1.getDistance(DistanceUnit.CM)));
        opmode.telemetry.addData("range2", String.format("%.01f cm", Tof2.getDistance(DistanceUnit.CM)));
        opmode.telemetry.addData("range2", String.format("%.01f cm", Tof3.getDistance(DistanceUnit.CM)));
        opmode.telemetry.update();
    }

    public void CompareDistance() {
        distance1 = Tof1.getDistance(DistanceUnit.CM);
        distance2 = Tof2.getDistance(DistanceUnit.CM);
        distance3 = Tof3.getDistance(DistanceUnit.CM);
    }
}