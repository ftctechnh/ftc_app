package org.firstinspires.ftc.robotcontroller.internal.Devices;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Eamonn on 10/29/2016. Funtastic.
 */


public class TrapDoorMechanic
{
    private DcMotor[] axelRotation = null;

    public TrapDoorMechanic(HardwareMap hardwareMap)
    {
        axelRotation = new DcMotor[1];
        axelRotation[0] = hardwareMap.dcMotor.get("trapDoorMotor");

    }

    public synchronized void setPower(double power) {
        axelRotation[0].setPower(power);
    }
}
