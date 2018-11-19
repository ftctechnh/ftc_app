package org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.framework.AbstractTeleop;

public class DistanceSensor2m {

    private HardwareMap hwMap;
    private com.qualcomm.hardware.rev.Rev2mDistanceSensor sensorTimeOfFlight1;
    private DistanceSensor sensorRange1;
    private String InputString;


    public DistanceSensor2m( String CfgNameOnPhone ) {
        InputString = CfgNameOnPhone;
        // This is an odd way to fix the hardware map issue
        //    where the normal hardware map getter does not work.
        hwMap = AbstractTeleop.getOpModeInstance().hardwareMap;

        sensorRange1 = hwMap.get(DistanceSensor.class, CfgNameOnPhone);
        sensorTimeOfFlight1 = (com.qualcomm.hardware.rev.Rev2mDistanceSensor)sensorRange1;

    }

    public double getDistanceIN() {
        // one of the distance sensors is not giving us the right distance so this is the fix.
        if (InputString == "distance2") {
            return (sensorRange1.getDistance(DistanceUnit.INCH) + 0.5);
        } else {
            return (sensorRange1.getDistance(DistanceUnit.INCH));
        }
    }


}
