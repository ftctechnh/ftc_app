package org.firstinspires.ftc.teamcode.OldCode.Willow;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * Created by windorabug on 11/15/16.
 */

public class Blue2beacons { @Autonomous(name="getting to the blue beacon #2", group="Pushbot")
@Disabled
public class redbeaconleft extends LinearOpMode {

    /* Declare OpMode members. */
    HardwarePushbot robot   = new HardwarePushbot();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();


    static final double     FORWARD_SPEED = 1.0;
    static final double     TURN_SPEED    = 0.5;

    //the software team does all the work here
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here*/
    @Override
    public void runOpMode() throws InterruptedException {

    robot.init(hardwareMap);

    // Send telemetry message to signify robot waiting
    telemetry.addData("Status", "Ready to run");    //
    telemetry.update();

    /* Wait for the game to start (driver presses PLAY)
    */


    //();

    // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

    // Step 1:  Drive forward for .5 seconds
    robot.leftDrive.setPower(FORWARD_SPEED);
    robot.rightDrive.setPower(FORWARD_SPEED);
    runtime.reset();
    //while (

    //redbeaconleft() && (runtime.seconds() < 0.5)) {
        telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
        telemetry.update();
        idle();

    }

// Step 3:  Drive Backwards for 1 Second
        /*robot.leftDrive.setPower(-FORWARD_SPEED);
        robot.rightDrive.setPower(-FORWARD_SPEED);
        runtime.reset();
        while (blue1beacons() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Path", "Leg 3: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
            blue1beacons();*/


    // Step 2:  Spin right for 1.0 seconds
    //robot.leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
    //robot.leftDrive.setPower(TURN_SPEED);
    //robot.leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
    //robot.rightDrive.setPower(TURN_SPEED);
    //runtime.reset();
    //while (

    //Blue1beacons() && (runtime.seconds() < 1.0)) {
        //telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
        //telemetry.update();
        //idle();
    }


    // Step 3:  Drive Backwards for 1 Second
       /* robot.leftDrive.setPower(-FORWARD_SPEED);
        robot.rightDrive.setPower(-FORWARD_SPEED);
        runtime.reset();
        while (blue1beacons() && (runtime.seconds() < 1.2)) {
            telemetry.addData("Path", "Leg 3: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
            blue1beacons();
        }*/


    // Step 3:  Drive forward for .7 second
    //robot.leftDrive.setPower(FORWARD_SPEED);
    //robot.rightDrive.setPower(FORWARD_SPEED);
    //runtime.reset();
    //while (
/*
            () && (runtime.seconds() < 0.7)) {
        telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
        telemetry.update();
        idle();
*/
    }

    // Step 4:  Spin left for 1.3 seconds
   // robot.leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
    //robot.leftDrive.setPower(TURN_SPEED);
    //robot.leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
    //robot.rightDrive.setPower(TURN_SPEED);
    //runtime.reset();
    //while (

   //() && (runtime.seconds() < 1.3)) {
   //    telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
   //     telemetry.update();
   //     idle();
   // }

    // Step 5:  Drive forward for .6 seconds
    //robot.leftDrive.setPower(FORWARD_SPEED);
    //robot.rightDrive.setPower(FORWARD_SPEED);
    //runtime.reset();
    //while (

    //() && (runtime.seconds() < 0.6)) {
    //    telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
    //    telemetry.update();
    //   idle();

    //}

    // Step 6:  Spin right for 1.3 seconds
    //robot.leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
    //robot.leftDrive.setPower(TURN_SPEED);
    //robot.leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
    //robot.rightDrive.setPower(TURN_SPEED);
    //runtime.reset();
    //while (

    //() && (runtime.seconds() < 1.0)) {
    //    telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
    //    telemetry.update();
    //    idle();
    //}

    // Step 7:  Drive forward for 0.5 seconds
    //robot.leftDrive.setPower(FORWARD_SPEED);
    //robot.rightDrive.setPower(FORWARD_SPEED);
    //runtime.reset();
    //while (

    //() && (runtime.seconds() < 0.2)) {
    //    telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
    //    telemetry.update();
    //    idle();

    //}

    // Step 8:  Stop and close the claw.
    //robot.leftDrive.setPower(3);
    //robot.rightDrive.setPower(3);
    //robot.leftClaw.setPosition(1.0);
    //robot.rightClaw.setPosition(1.0);

    //telemetry.addData("Path", "Complete");
    //telemetry.update();
    //    Blue1beacons(1000);
    //    Blue1beacons();
//}
//}

//}
