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
            .lineTo(new Vector2d(0, -15), new ConstantInterpolator(0))
            .build();

    static Trajectory UNDO_UNHOOK = new TrajectoryBuilder(new Pose2d(0, -15, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(0, 0), new ConstantInterpolator(0))
            .build();

    static Trajectory FORWARD_A_LITTLE = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(3, 0), new ConstantInterpolator(0))
            .build();

    static Trajectory FORWARD_RIGHT = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(15, -15), new ConstantInterpolator(0))
            .build();

    static Trajectory FORWARD_LEFT = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(15, 15), new ConstantInterpolator(0))
            .build();

    public static Pose2d DEPOT_START = new Pose2d(-START_DIST, START_DIST, Math.PI*0.75);

    public static Pose2d CRATER_START = new Pose2d(START_DIST, START_DIST, Math.PI*0.25);
    static Trajectory DEPOT_TO_SAME_CRATER = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(-75, -15), new ConstantInterpolator(0))
            .build();

    static Trajectory DEPOT_TO_OTHER_CRATER = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(-75, 15), new ConstantInterpolator(0))
            .build();

    static Trajectory DEPOT_TO_SAME_CRATER_LONG = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(-90, -20), new ConstantInterpolator(0))
            .build();

    static Trajectory DEPOT_TO_OTHER_CRATER_LONG = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(-90, 20), new ConstantInterpolator(0))
            .build();

    static Pose2d DEPOT = new Pose2d(-63, 63, Math.PI);

    static Trajectory DEPOT_TO_CRATER_CENTER = new TrajectoryBuilder(DEPOT, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(-40, 63, Math.PI))
            .splineTo(new Pose2d(28, 28, 3*Math.PI/4))
            .strafeRight(15)
            .closeComposite()
            .build();

    static Trajectory DEPOT_TO_CRATER_LEFT = new TrajectoryBuilder(DEPOT, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(-40, 63, Math.PI))
            .splineTo(new Pose2d(16, 40, 3*Math.PI/4))
            .strafeRight(15)
            .closeComposite()
            .build();

    static Trajectory DEPOT_TO_CRATER_RIGHT = new TrajectoryBuilder(DEPOT, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(-40, 63, Math.PI))
            .splineTo(new Pose2d(40, 16, 3*Math.PI/4))
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

    static Trajectory CRATER_SAME_RIGHT_SEL = new TrajectoryBuilder(CRATER_START, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(48, 24, Math.PI*0.25))
            .closeComposite()
            .build();

    static Trajectory CRATER_SAME_LEFT_SEL = new TrajectoryBuilder(CRATER_START, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(24, 48, Math.PI*0.25))
            .closeComposite()
            .build();

    static Trajectory CRATER_SAME_CENTER_SEL = new TrajectoryBuilder(CRATER_START, DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(36, 36, Math.PI*0.25))
            .closeComposite()
            .build();

    static Trajectory CRATER_SAME_RIGHT_DIR = new TrajectoryBuilder(new Pose2d(48, 24, -Math.PI*0.25), DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(120, 56, 0))
            .splineTo(new Pose2d(160, 56, 0))
            .closeComposite()
            .build();

    public static int CRATER_SAME_LEFT_DIR_SPL_X = 80;
    public static int CRATER_SAME_LEFT_DIR_SPL_Y = 61;

    static Trajectory CRATER_SAME_LEFT_DIR = new TrajectoryBuilder(new Pose2d(24, 48, -Math.PI*0.25), DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(CRATER_SAME_LEFT_DIR_SPL_X, CRATER_SAME_LEFT_DIR_SPL_Y, 0))
            .splineTo(new Pose2d(120, CRATER_SAME_LEFT_DIR_SPL_Y, 0))
            .closeComposite()
            .build();

    static Trajectory CRATER_SAME_CENTER_DIR = new TrajectoryBuilder(new Pose2d(36, 36, -Math.PI*0.25), DriveConstants.BASE_CONSTRAINTS)
            .beginComposite()
            .splineTo(new Pose2d(100, 56, 0))
            .splineTo(new Pose2d(140, 56, 0))
            .closeComposite()
            .build();

    public static final Trajectory[] CRATER_SEL_SEL = new Trajectory[] {CRATER_SAME_LEFT_SEL, CRATER_SAME_CENTER_SEL, CRATER_SAME_RIGHT_SEL};
    public static final Trajectory[] CRATER_DIR_SEL = new Trajectory[] {CRATER_SAME_LEFT_DIR, CRATER_SAME_CENTER_DIR, CRATER_SAME_RIGHT_DIR};
}
