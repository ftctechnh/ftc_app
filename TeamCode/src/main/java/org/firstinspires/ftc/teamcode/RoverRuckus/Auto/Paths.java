package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;

public class Paths {
    static Pose2d DEPOT_START = new Pose2d(-13, 13, Math.PI*0.75);

    static Trajectory DEPOT_RIGHT = new TrajectoryBuilder(DEPOT_START, SingleSampling.baseConstraints)
            .beginComposite()
            .splineTo(new Pose2d(-48, 24, Math.PI*0.75))
            .splineTo(new Pose2d(-60, 60, Math.PI*.5))
            .closeComposite()
            .build();

    static Trajectory DEPOT_LEFT = new TrajectoryBuilder(DEPOT_START, SingleSampling.baseConstraints)
            .beginComposite()
            .splineTo(new Pose2d(-24, 48, Math.PI*0.75))
            .splineTo(new Pose2d(-60, 60, Math.PI))
            .turnTo(Math.PI*.5)
            .closeComposite()
            .build();

    static Trajectory DEPOT_CENTER = new TrajectoryBuilder(DEPOT_START, SingleSampling.baseConstraints)
            .beginComposite()
            .splineTo(new Pose2d(-60, 60, Math.PI*0.75))
            .turnTo(Math.PI*.5)
            .closeComposite()
            .build();

    public static final Trajectory[] DEPOT = new Trajectory[] {DEPOT_LEFT, DEPOT_CENTER, DEPOT_RIGHT};
}
