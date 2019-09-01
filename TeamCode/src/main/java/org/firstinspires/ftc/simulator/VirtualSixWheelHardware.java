package org.firstinspires.ftc.simulator;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.common.math.MathUtil;
import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelHardware;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelPowers;

public class VirtualSixWheelHardware extends SixWheelHardware implements VirtualRobot {
    double TRACK_WIDTH = 17;
    double MAX_SPEED = 40; // Per second

    Pose position;
    double time;
    SixWheelPowers wheelPowers;

    public VirtualSixWheelHardware(Object o) {
        this(new Pose(0, 0, 0));
    }

    public VirtualSixWheelHardware(Pose position) {
        this.position = position;
        this.time = 0;
        this.wheelPowers = new SixWheelPowers(0, 0);
    }

    public void setWheelPowers(SixWheelPowers powers) {
        this.wheelPowers = powers;
    }

    public Pose pose() {
        return position;
    }

    @Override
    public void elapse(double ms) {
        Pose relativeOdometry = new Pose(
                MAX_SPEED * (wheelPowers.left + wheelPowers.right) / 2,
                0,
                MAX_SPEED * (wheelPowers.right - wheelPowers.left) / TRACK_WIDTH
        ).scale(MAX_SPEED * ms / 1000);

        position = MathUtil.relativeOdometryUpdate(position, relativeOdometry);
        time += ms;
    }

    @Override
    public Pose getStampedPosition() {
        return new Pose(position.x, position.y, position.heading);
    }
}
