package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware;

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

}
