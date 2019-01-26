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
    public double wallAngle;

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

    public double getDistance1() {
        return Tof1.getDistance(DistanceUnit.CM);
    }

    public double getDistance2() {
        return Tof2.getDistance(DistanceUnit.CM);
    }

    public double getDistance3() {
        return Tof3.getDistance(DistanceUnit.CM);
    }

    public double wallFollow() {
        wallAngle = java.lang.Math.atan((getDistance1() - getDistance2()) / 25);
        return wallAngle;
    }

}