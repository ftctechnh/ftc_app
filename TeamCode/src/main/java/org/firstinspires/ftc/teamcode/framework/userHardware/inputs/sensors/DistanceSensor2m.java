package org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.framework.opModes.AbstractOpMode;

public class DistanceSensor2m {

    //private com.qualcomm.hardware.rev.Rev2mDistanceSensor timeOfFlightSensor;
    private DistanceSensor distanceSensor;

    public DistanceSensor2m(String name) {
        distanceSensor = AbstractOpMode.getHardwareMap().get(DistanceSensor.class, name);
        //timeOfFlightSensor = (com.qualcomm.hardware.rev.Rev2mDistanceSensor) distanceSensor;
    }

    public double getDistanceIN() {
        return (distanceSensor.getDistance(DistanceUnit.INCH));
    }
}
