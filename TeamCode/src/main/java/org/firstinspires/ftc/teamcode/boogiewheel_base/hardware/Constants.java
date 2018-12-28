package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware;

import com.sun.tools.javac.nio.PathFileManager;

import org.firstinspires.ftc.teamcode.framework.userHardware.paths.DriveSegment;
import org.firstinspires.ftc.teamcode.framework.userHardware.paths.Path;
import org.firstinspires.ftc.teamcode.framework.userHardware.paths.TurnSegment;

public final class Constants {
    ////////DRIVE////////
    public static final double DRIVE_MINERAL_LIFT_RAISED_SCALAR = 0.5;
    public static final double DRIVE_COUNTS_PER_INCH = 38.0;

    public static final double DRIVE_RELEASE_WHEELS_POWER = -0.5;

    public static final boolean DRIVE_AUTO_INVERT = false;

    public static final double DRIVE_TEAM_MARKER_EXTENDED = 0.5;
    public static final double DRIVE_TEAM_MARKER_RETRACTED = 0;


    ////////INTAKE////////
    //Brushes
    public static final double INTAKE_STOP_POWER = 0;
    public static final double INTAKE_FORWARD_POWER = 1;
    public static final double INTAKE_REVERSE_POWER = -1;
    public static final double INTAKE_LOWER_POWER = 0.2;

    //Lift
    public final static double INTAKE_LIFT_LOWERED_POSITION = 1;
    public final static double INTAKE_LIFT_RAISED_POSITION = 0.2;


    ////////MINERAL LIFT////////
    //Lift
    public final static int MINERAL_LIFT_COLLECT_POSITION = 0;
    public final static int MINERAL_LIFT_DUMP_POSITION = 3600;

    //Gate
    public final static double MINERAL_GATE_OPEN_POSITION = 0.7;
    public final static double MINERAL_GATE_CLOSED_POSITION = 0.0;


    ////////ROBOT LIFT////////
    //Lift
    public final static int ROBOT_LIFT_LOWERED_POSITION = -2500;
    public final static int ROBOT_LIFT_RAISED_POSITION = 100;
    public final static int ROBOT_LIFT_RELEASE_PAWL_POSITION = 200;
    public final static double ROBOT_LIFT_LOWER_POWER = -0.7;

    //Pawl
    public final static double ROBOT_LIFT_PAWL_RELEASED = 0.1;
    public final static double ROBOT_LIFT_PAWL_ENGAGED = 0.0;


    ////////AUTON PATHS////////
    public final static double AUTON_PATH_SPEED = 1;
    public final static double AUTON_TURN_ERROR = 8;
    public final static int AUTON_TURN_PERIOD = 100;
    public final static int AUTON_DISTANCE_ERROR = 25;

    public final static Path unlatchRobot = new Path("unlatch robot");

    static {
        unlatchRobot.addSegment(new TurnSegment("turn away from hook", 20, AUTON_PATH_SPEED, AUTON_TURN_ERROR, AUTON_TURN_PERIOD));
        unlatchRobot.addSegment(new DriveSegment("drive away from hook", -5, AUTON_PATH_SPEED, AUTON_DISTANCE_ERROR));
    }

    public final static Path collectRightMineral = new Path("collect right mineral");

    static {
        collectRightMineral.addSegment(new TurnSegment("turn to gold mineral", 150, AUTON_PATH_SPEED, AUTON_TURN_ERROR, AUTON_TURN_PERIOD));
        collectRightMineral.addSegment(new DriveSegment("drive to minerals", 24, AUTON_PATH_SPEED, AUTON_DISTANCE_ERROR));
        collectRightMineral.addSegment(new DriveSegment("back up from minerals", -13, AUTON_PATH_SPEED, AUTON_DISTANCE_ERROR));
    }

    public final static Path collectLeftMineral = new Path("collect left mineral");

    static {
        collectLeftMineral.addSegment(new TurnSegment("turn to gold mineral", -155, AUTON_PATH_SPEED, AUTON_TURN_ERROR, AUTON_TURN_PERIOD));
        collectLeftMineral.addSegment(new DriveSegment("drive to minerals", 24, AUTON_PATH_SPEED, AUTON_DISTANCE_ERROR));
        collectLeftMineral.addSegment(new DriveSegment("back up from minerals", -11, AUTON_PATH_SPEED, AUTON_DISTANCE_ERROR));
    }

    public final static Path collectCenterMineral = new Path("collect center mineral");

    static {
        collectCenterMineral.addSegment(new TurnSegment("turn to gold mineral", 180, AUTON_PATH_SPEED, AUTON_TURN_ERROR, AUTON_TURN_PERIOD));
        collectCenterMineral.addSegment(new DriveSegment("drive to minerals", 22, AUTON_PATH_SPEED, AUTON_DISTANCE_ERROR));
        collectCenterMineral.addSegment(new DriveSegment("back up from minerals", -13, AUTON_PATH_SPEED, AUTON_DISTANCE_ERROR));
    }

    public final static Path craterSideToCrater = new Path("crater side to crater");

    static {
        craterSideToCrater.addSegment(new TurnSegment("turn to wall", -90, AUTON_PATH_SPEED, AUTON_TURN_ERROR, AUTON_TURN_PERIOD));
        craterSideToCrater.addSegment(new DriveSegment("drive to wall", 50, AUTON_PATH_SPEED, AUTON_DISTANCE_ERROR));
        craterSideToCrater.addSegment(new TurnSegment("turn to depot", -48, AUTON_PATH_SPEED, AUTON_TURN_ERROR, AUTON_TURN_PERIOD));
        craterSideToCrater.addSegment(new DriveSegment("drive to depot", 33, AUTON_PATH_SPEED, AUTON_DISTANCE_ERROR));
        craterSideToCrater.addSegment(new DriveSegment("drive to crater", -70, AUTON_PATH_SPEED, 30, -43));
    }

}