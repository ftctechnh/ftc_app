package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.robotplus.hardware.IMUWrapper;
import org.firstinspires.ftc.teamcode.robotplus.hardware.MecanumDrive;
import org.firstinspires.ftc.teamcode.robotplus.hardware.Robot;
import org.firstinspires.ftc.teamcode.robotplus.hardware.TankDrive;

/**
 * Created by BAbel on 11/3/2017.
 */

@TeleOp (name = "IMU Rotation Testing", group = "Test")
public class TestIMUGyroscope extends OpMode {

    private Robot robot;
    private IMUWrapper imuWrapper;

    @Override
    public void init() {
        robot = new Robot(hardwareMap);
        imuWrapper = new IMUWrapper(hardwareMap);
    }

    @Override
    public void loop() {
        robot.getDrivetrain().defaultDrive(gamepad1, telemetry);

        telemetry.addData("Calibration:", imuWrapper.getIMU().getCalibrationStatus().toString());

        telemetry.addData("Orientation:", imuWrapper.getOrientation().toAngleUnit(AngleUnit.RADIANS).toString());

        telemetry.addData("\tFirst Angle:", imuWrapper.getOrientation().toAngleUnit(AngleUnit.RADIANS).firstAngle);
        telemetry.addData("\tSecond Angle:", imuWrapper.getOrientation().toAngleUnit(AngleUnit.RADIANS).secondAngle);
        telemetry.addData("\tThird Angle:", imuWrapper.getOrientation().toAngleUnit(AngleUnit.RADIANS).thirdAngle);
    }

    @Override
    public void stop() {
        robot.stopMoving();
    }
}
