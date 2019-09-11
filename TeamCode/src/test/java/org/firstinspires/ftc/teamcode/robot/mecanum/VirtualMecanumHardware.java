package org.firstinspires.ftc.teamcode.robot.mecanum;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.simulator.VirtualRobot;
import org.firstinspires.ftc.teamcode.common.math.MathUtil;
import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.firstinspires.ftc.teamcode.robot.mecanum.MecanumHardware;
import org.firstinspires.ftc.teamcode.robot.mecanum.MecanumPowers;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelHardware;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelPowers;
import org.mockito.Mockito;

public class VirtualMecanumHardware extends MecanumHardware implements VirtualRobot {
    double TRACK_WIDTH = 17;

    double MAX_FORWARD_SPEED = 60; // Per second
    double MAX_STRAFE_SPEED = 55;

    Pose position;
    double time;
    MecanumPowers wheelPowers;

    public VirtualMecanumHardware(Object o) {
        this(new Pose(0, 0, 0));
    }

    public VirtualMecanumHardware(Pose position) {
        this.position = position;
        this.time = 0;
        this.wheelPowers = new MecanumPowers(0, 0, 0, 0);
    }

    public void initBNO055IMU(HardwareMap hardwareMap) {
        this.imu = Mockito.mock(BNO055IMU.class);
    }

    @Override
    public void setPowers(MecanumPowers powers) {
        this.wheelPowers = powers;
    }

    public Pose pose() {
        return position;
    }

    @Override
    public void elapse(double secs) {
        if (
                Math.abs(wheelPowers.frontLeft) > 1 ||
                Math.abs(wheelPowers.frontRight) > 1 ||
                Math.abs(wheelPowers.backLeft) > 1 ||
                Math.abs(wheelPowers.backRight) > 1
        ) {
            throw new AssertionError();
        }

        // +y is to the left
        // Calculations are simple because our wheels are oriented at 45 degrees
        Pose relativeOdometry = new Pose(
                MAX_FORWARD_SPEED * (
                        wheelPowers.frontLeft +
                        wheelPowers.frontRight +
                        wheelPowers.backLeft +
                        wheelPowers.backRight) / 4,

                MAX_STRAFE_SPEED * (
                        wheelPowers.frontRight +
                        wheelPowers.backLeft +
                        -wheelPowers.frontLeft +
                        -wheelPowers.backRight) / 4,

                (MAX_FORWARD_SPEED / TRACK_WIDTH) * (
                        wheelPowers.frontRight +
                        wheelPowers.backRight +
                        -wheelPowers.frontLeft +
                        -wheelPowers.backLeft) / 4
        ).scale(secs);

        System.out.println(relativeOdometry.toString());

        position = MathUtil.relativeOdometryUpdate(position, relativeOdometry);
        time += secs;
    }

    @Override
    public Pose getStampedPosition() {
        return new Pose(position.x, position.y, position.heading);
    }
}
