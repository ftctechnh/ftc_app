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

        _base.imu.startAccelMeasurement();

        while(opModeIsActive())
        {
            _base.imu.pull();

            telemetry.addData("Calibration" , _base.imu.calibrationStatus());

            telemetry.addData("imu x" , _base.imu.xAngle());
            telemetry.addData("imu y" , _base.imu.yAngle());
            telemetry.addData("imu z" , _base.imu.zAngle());

            telemetry.addLine();

            telemetry.addData("accel x" , _base.imu.xAccel());
            telemetry.addData("accel y" , _base.imu.yAccel());
            telemetry.addData("accel z" , _base.imu.zAccel());

            telemetry.addLine();

            telemetry.addData("pos x" , _base.imu.xPos());
            telemetry.addData("pos y" , _base.imu.yPos());
            telemetry.addData("pos z" , _base.imu.zPos());

            telemetry.update();
        }
    }
}
