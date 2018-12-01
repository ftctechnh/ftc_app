package org.firstinspires.ftc.teamcode.components.motors;

import com.qualcomm.robotcore.hardware.DcMotor;

import junit.framework.Assert;

import org.firstinspires.ftc.teamcode.fakes.FakeDcMotor;
import org.junit.Test;

public class DriveMotorTest
{
    @Test
    public void Run_RunWithLegalPower() {
        DcMotor motor = new FakeDcMotor();
        DriveMotor driveMotor = new DriveMotor(motor);

        driveMotor.run(0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void Run_RunWithIllegalPower_ThrowsException() {
        DcMotor motor = new FakeDcMotor();
        DriveMotor driveMotor = new DriveMotor(motor);

        driveMotor.run(10.0);
    }
}
