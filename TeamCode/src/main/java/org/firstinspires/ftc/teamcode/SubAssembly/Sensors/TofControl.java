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
        Tof3 = hwMap.get(Rev2mDistanceSensor.class, "Tof3");
    }

    public void Telemetry() {
        opmode.telemetry.addData("range3", String.format("%.01f cm", Tof3.getDistance(DistanceUnit.CM)));
        opmode.telemetry.update();
    }


    public double getDistance3() {
        return Tof3.getDistance(DistanceUnit.CM) - 36;
    }

   /* public void getWallAngle() {
        double distanceDifference = getDistance1() - getDistance2();
        double divided = distanceDifference / 25.0;
        wallAngle = java.lang.Math.atan(divided);
        wallAngle = java.lang.Math.toDegrees(wallAngle);
    }*/
}