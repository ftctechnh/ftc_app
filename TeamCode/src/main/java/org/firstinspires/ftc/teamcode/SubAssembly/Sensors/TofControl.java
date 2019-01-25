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
    }

    public void GetDistance() {
        opmode.telemetry.addData("range", String.format("%.01f cm", Tof1.getDistance(DistanceUnit.CM)));
        opmode.telemetry.addData("range", String.format("%.01f cm", Tof2.getDistance(DistanceUnit.CM)));
        opmode.telemetry.update();
    }

    public void CompareDistance() {
        double distance1 = Tof1.getDistance(DistanceUnit.CM);
        double distance2 = Tof2.getDistance(DistanceUnit.CM);
        opmode.telemetry.addLine("distance: " +distance1);
        opmode.telemetry.addLine("distance: " +distance2);
    }
}

