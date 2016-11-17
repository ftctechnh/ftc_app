package org.firstinspires.ftc.teamcode.Willow;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * Created by windorabug on 11/15/16.
 */

public class Blue2beacons {@Autonomous(name="getting to the blue beacon #2", group="Pushbot")
@Disabled
public class redbeaconleft extends LinearOpMode {

    /* Declare OpMode members. */
    HardwarePushbot robot   = new HardwarePushbot();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();


    static final double     FORWARD_SPEED = 1.0;
    static final double     TURN_SPEED    = 0.5;

    //the software team does all the work here
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
    @Override
    public void runOpMode() throws InterruptedException {
         */
    robot.init(hardwareMap);

    // Send telemetry message to signify robot waiting
    telemetry.addData("Status", "Ready to run");    //
    telemetry.update();

    // Wait for the game to start (driver presses PLAY)
    redbeaconleft();

    // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

    // Step 1:  Drive forward for .5 seconds
    robot.leftMotor.setPower(FORWARD_SPEED);
    robot.rightMotor.setPower(FORWARD_SPEED);
    runtime.reset();


    redbeaconleft() && (runtime.seconds() < 0.5)) {
        telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
        telemetry.update();
        idle();

    }

// Step 3:  Drive Backwards for 1 Second
        /*robot.leftMotor.setPower(-FORWARD_SPEED);
        robot.rightMotor.setPower(-FORWARD_SPEED);
        runtime.reset();
        while (blue1beacons() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Path", "Leg 3: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
            blue1beacons();*/


    // Step 2:  Spin right for 1.0 seconds
    robot.leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    robot.leftMotor.setPower(TURN_SPEED);
    robot.leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    robot.rightMotor.setPower(TURN_SPEED);
    runtime.reset();
    while (

    redbeaconleft() && (runtime.seconds() < 1.0)) {
        telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
        telemetry.update();
        idle();
    }


    // Step 3:  Drive Backwards for 1 Second
       /* robot.leftMotor.setPower(-FORWARD_SPEED);
        robot.rightMotor.setPower(-FORWARD_SPEED);
        runtime.reset();
        while (blue1beacons() && (runtime.seconds() < 1.2)) {
            telemetry.addData("Path", "Leg 3: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
            blue1beacons();
        }*/


    // Step 1:  Drive forward for .7 second
    robot.leftMotor.setPower(FORWARD_SPEED);
    robot.rightMotor.setPower(FORWARD_SPEED);
    runtime.reset();
    while (

    redbeaconleft() && (runtime.seconds() < 0.7)) {
        telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
        telemetry.update();
        idle();

    }

    // Step 2:  Spin left for 1.3 seconds
    robot.leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    robot.leftMotor.setPower(TURN_SPEED);
    robot.leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    robot.rightMotor.setPower(TURN_SPEED);
    runtime.reset();
    while (

    redbeaconleft() && (runtime.seconds() < 1.3)) {
        telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
        telemetry.update();
        idle();
    }

    // Step 1:  Drive forward for .6 seconds
    robot.leftMotor.setPower(FORWARD_SPEED);
    robot.rightMotor.setPower(FORWARD_SPEED);
    runtime.reset();
    while (

    redbeaconleft() && (runtime.seconds() < 0.6)) {
        telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
        telemetry.update();
        idle();

    }

    // Step 2:  Spin right for 1.3 seconds
    robot.leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    robot.leftMotor.setPower(TURN_SPEED);
    robot.leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    robot.rightMotor.setPower(TURN_SPEED);
    runtime.reset();
    while (

    redbeaconleft() && (runtime.seconds() < 1.0)) {
        telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
        telemetry.update();
        idle();
    }

    // Step 1:  Drive forward for 0.5 seconds
    robot.leftMotor.setPower(FORWARD_SPEED);
    robot.rightMotor.setPower(FORWARD_SPEED);
    runtime.reset();
    while (

    redbeaconleft() && (runtime.seconds() < 0.2)) {
        telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
        telemetry.update();
        idle();

    }

    // Step 4:  Stop and close the claw.
    robot.leftMotor.setPower(3);
    robot.rightMotor.setPower(3);
    //robot.leftClaw.setPosition(1.0);
    //robot.rightClaw.setPosition(1.0);

    telemetry.addData("Path", "Complete");
    telemetry.update();
    redbeaconright(1000);
    redbeaconright();
}
}

}
