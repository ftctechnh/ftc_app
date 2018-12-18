package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware;

import org.firstinspires.ftc.teamcode.framework.userHardware.paths.DriveSegment;
import org.firstinspires.ftc.teamcode.framework.userHardware.paths.Path;
import org.firstinspires.ftc.teamcode.framework.userHardware.paths.TurnSegment;

public final class Constants {
    ////////DRIVE////////
    public static final double DRIVE_MINERAL_LIFT_RAISED_SCALAR = 0.5;
    public static final double DRIVE_COUNTS_PER_INCH = 38.0;

    public static final boolean DRIVE_AUTO_INVERT = false;


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
    public final static int ROBOT_LIFT_LOWERED_POSITION = 0;
    public final static int ROBOT_LIFT_RAISED_POSITION = 100;


    ////////AUTON PATHS////////
    public final static double AUTON_PATH_SPEED = 1;
    public final static double AUTON_PATH_ERROR = 3;
    public final static int AUTON_TURN_PERIOD = 100;

    public final static Path collectRightMineral = new Path("crater side to depot");
    static {
        collectRightMineral.addSegment(new DriveSegment("back up from lander", -10, AUTON_PATH_SPEED));
        collectRightMineral.addSegment(new TurnSegment("turn to mineral", 155, AUTON_PATH_SPEED, AUTON_PATH_ERROR, AUTON_TURN_PERIOD));
        collectRightMineral.addSegment(new DriveSegment("drive to minerals", 24, AUTON_PATH_SPEED));
        collectRightMineral.addSegment(new DriveSegment("back up from minerals", -8, AUTON_PATH_SPEED));
    }
}
