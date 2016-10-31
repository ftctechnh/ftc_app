package org.firstinspires.ftc.robotcontroller.internal.Devices;

/**
 * Created by Eamonn on 10/27/2016.
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class SweeperMechanic
{
    private DcMotor[] axelRotation = null;

    public SweeperMechanic(HardwareMap hardwareMap)
    {
        axelRotation = new DcMotor[1];
    }
    public synchronized void setPower (double power)
    {
        axelRotation[0].setPower(power);
    }
}
