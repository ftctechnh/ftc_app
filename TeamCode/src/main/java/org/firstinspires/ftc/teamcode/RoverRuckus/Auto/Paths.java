package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.drive.Drive;
import com.acmerobotics.roadrunner.path.heading.ConstantInterpolator;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.DriveConstants;

@Config
public class Paths {

    public static int START_DIST = 14;

    static Trajectory UNHOOK = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(0, 15), new ConstantInterpolator(0))
            .build();

    static Trajectory UNDO_UNHOOK = new TrajectoryBuilder(new Pose2d(0, 15, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(0, 0), new ConstantInterpolator(0))
            .build();

    static Trajectory FORWARD_A_LITTLE = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(3, 0), new ConstantInterpolator(0))
            .build();

    static Trajectory TURN_UNHOOK = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .turn(Math.PI/3)
            .build();

    static Trajectory TURN_UNDO_UNHOOK = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .turn(-Math.PI/3)
            .build();

    static Trajectory BACKUP = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(-15, 0), new ConstantInterpolator(0))
            .build();

    static Trajectory FORWARD = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(10, 0), new ConstantInterpolator(0))
            .build();

    static Trajectory FORWARD_RIGHT = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(8, -8), new ConstantInterpolator(0))
            .build();

    public static Pose2d DEPOT_START = new Pose2d(-START_DIST, START_DIST, Math.PI*0.75);

    public static Pose2d CRATER_START = new Pose2d(START_DIST, START_DIST, Math.PI*0.25);
    static Trajectory DEPOT_TO_SAME_CRATER = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(-100, -20), new ConstantInterpolator(0))
            .build();

    static Trajectory DEPOT_TO_OTHER_CRATER = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(-100, 20), new ConstantInterpolator(0))
            .build();

    static Pose2d DEPOT = new Pose2d(-63, 63, 0);

    static Trajectory DEPOT_TO_CRATER_CENTER = new TrajectoryBuilder(DEPOT, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(-40, 63, 0))
            .splineTo(new Pose2d(28, 28, -45))
            .strafeRight(15)
            .closeComposite()
            .build();

    static Trajectory DEPOT_TO_CRATER_LEFT = new TrajectoryBuilder(DEPOT, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(-40, 63, 0))
            .splineTo(new Pose2d(16, 40, -45))
            .strafeRight(15)
            .closeComposite()
            .build();

    static Trajectory DEPOT_TO_CRATER_RIGHT = new TrajectoryBuilder(DEPOT, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(-40, 63, 0))
            .splineTo(new Pose2d(40, 16, -45))
            .strafeRight(15)
            .closeComposite()
            .build();

    public static final Trajectory[] DEPO_TO_CRATER_SELECTOR = new Trajectory[] {DEPOT_TO_CRATER_LEFT, DEPOT_TO_CRATER_CENTER, DEPOT_TO_CRATER_RIGHT};

    static Trajectory DEPOT_SAME_RIGHT = new TrajectoryBuilder(DEPOT_START, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(-48, 24, Math.PI*0.75))
            .splineTo(new Pose2d(-54, 54, Math.PI*.5))
            .closeComposite()
            .build();

    static Trajectory DEPOT_SAME_LEFT = new TrajectoryBuilder(DEPOT_START, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(-24, 48, Math.PI*0.75))
            .splineTo(new Pose2d(-48, 48, Math.PI))
            .closeComposite()
            .build();

    static Trajectory DEPOT_SAME_CENTER = new TrajectoryBuilder(DEPOT_START, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(-54, 54, Math.PI*0.75))
            .closeComposite()
            .build();



    public static final Trajectory[] DEPO_SAME_SELECTOR = new Trajectory[] {DEPOT_SAME_LEFT, DEPOT_SAME_CENTER, DEPOT_SAME_RIGHT};

    static Trajectory CRATER_SAME_RIGHT = new TrajectoryBuilder(CRATER_START, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(26, 52, Math.PI*0.25))
            .closeComposite()
            .build();

    static Trajectory CRATER_SAME_LEFT = new TrajectoryBuilder(CRATER_START, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(52, 26, Math.PI*0.25))
            .closeComposite()
            .build();

    static Trajectory CRATER_SAME_CENTER = new TrajectoryBuilder(CRATER_START, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(36, 36, Math.PI*0.25))
            .closeComposite()
            .build();

    public static final Trajectory[] CRATER_SAME_SELECTOR = new Trajectory[] {CRATER_SAME_LEFT, CRATER_SAME_CENTER, CRATER_SAME_RIGHT};
}
