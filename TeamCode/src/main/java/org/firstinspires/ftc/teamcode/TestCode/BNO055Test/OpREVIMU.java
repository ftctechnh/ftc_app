package org.firstinspires.ftc.teamcode.TestCode.BNO055Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name = "IMU Test" , group = "Prototypes")
public class OpREVIMU extends LinearOpMode
{
    private Base _base = new Base();

    @Override
    public void runOpMode() throws InterruptedException
    {
        _base.init(hardwareMap);

        waitForStart();

        while(opModeIsActive())
        {
            _base.imu.pull();

            telemetry.addData("imu" , "Pulling data okay");
            telemetry.update();

            telemetry.clear();

            telemetry.addData("imu x" , _base.imu.xAngle());
            telemetry.addData("imu y" , _base.imu.yAngle());
            telemetry.addData("imu z" , _base.imu.zAngle());

            telemetry.update();
        }
    }
}
