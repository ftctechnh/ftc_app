package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Arm Test")
public class MotorDrive extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        DcMotor motor = hardwareMap.dcMotor.get("motor");
        motor.setPower(-1);
        Thread.sleep(2000);
        motor.setPower(0);
    }
}
