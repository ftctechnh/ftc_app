package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.path.heading.ConstantInterpolator;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.DriveConstants;

public class Paths {

    static Trajectory UNHOOK = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .splineTo(new Pose2d(0, 5, 0), new ConstantInterpolator(0))
            .build();

    static Trajectory UNDO_UNHOOK = new TrajectoryBuilder(new Pose2d(0, 5, 0), DriveConstants.BASE_CONSTRAINTS)
            .splineTo(new Pose2d(0, 0, 0), new ConstantInterpolator(0))
            .build();

    static Pose2d DEPOT_START = new Pose2d(-13, 13, Math.PI*0.75);

    static Pose2d CRATER_START = new Pose2d(13, 13, Math.PI*0.25);


    static Trajectory DEPOT_RIGHT = new TrajectoryBuilder(DEPOT_START, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(-48, 24, Math.PI*0.75))
            .splineTo(new Pose2d(-60, 60, Math.PI*.5))
            .closeComposite()
            .build();

    static Trajectory DEPOT_LEFT = new TrajectoryBuilder(DEPOT_START, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(-24, 48, Math.PI*0.75))
            .splineTo(new Pose2d(-60, 60, Math.PI))
            .closeComposite()
            .build();

    static Trajectory DEPOT_CENTER = new TrajectoryBuilder(DEPOT_START, DriveConstants.BASE_CONSTRAINTS)
            .splineTo(new Pose2d(-60, 60, Math.PI*0.75))
            .build();

    static Trajectory CRATER_CENTER = new TrajectoryBuilder(CRATER_START, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(36, 36, Math.PI*0.25))
            .splineTo(new Pose2d(-30, 60, 0))
            .splineTo(new Pose2d(-60, 60, 0))
            .closeComposite()
            .build();

    static Trajectory CRATER_LEFT = new TrajectoryBuilder(CRATER_START, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(24, 48, Math.PI*0.25))
            .splineTo(new Pose2d(-30, 60, 0))
            .splineTo(new Pose2d(-60, 60, 0))
            .closeComposite()
            .build();

    static Trajectory CRATER_RIGHT = new TrajectoryBuilder(CRATER_START, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(48, 24, Math.PI*0.25))
            .splineTo(new Pose2d(-30, 60, 0))
            .splineTo(new Pose2d(-60, 60, 0))
            .closeComposite()
            .build();

    public static final Trajectory[] DEPOT_MINERAL_SELECTOR = new Trajectory[] {DEPOT_LEFT, DEPOT_CENTER, DEPOT_RIGHT};
    public static final Trajectory[] CRATER_MINERAL_SELECTOR = new Trajectory[] {CRATER_LEFT, CRATER_CENTER, CRATER_RIGHT};

    static Trajectory DEPOT_TURN_SAME_CRATER(Pose2d prevEnd) {
        return new TrajectoryBuilder(prevEnd, DriveConstants.BASE_CONSTRAINTS)
                .beginComposite()
                .turnTo(0.5 * Math.PI)
                .closeComposite()
                .build();
    }

    static Trajectory DEPOT_TURN_OTHER_CRATER(Pose2d prevEnd) {
        return new TrajectoryBuilder(prevEnd, DriveConstants.BASE_CONSTRAINTS)
                .beginComposite()
                .turnTo(0 * Math.PI)
                .closeComposite()
                .build();
    }

    static Trajectory DEPOT_TO_SAME_CRATER = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(-80, -30), new ConstantInterpolator(0))
            .build();

    static Trajectory DEPOT_TO_OTHER_CRATER = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(-80, 30), new ConstantInterpolator(0))
            .build();

}
