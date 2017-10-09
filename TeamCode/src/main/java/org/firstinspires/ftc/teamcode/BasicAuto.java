//Just FYI, the grey comments are just things to notice, but the green ones are top priority
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;



@TeleOp(name="Basic Auto", group="Linear Opmode")

public class BasicAuto extends LinearOpMode {

    // Declare OpMode members. AKA, variables and shit.
    private ElapsedTime runtimeAuto = new ElapsedTime();


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();





        // Wait for the game to start (driver presses PLAY). Telemetry added for debugging and later understanding
        telemetry.addData("Status", "Waiting for start");
        telemetry.update();
        waitForStart();
        runtimeAuto.reset();

        // This is the main loop. It will run until the driver presses stop
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;


            /**
            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;
             */


            //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


            //and here is where I'd put my autonomous code, IF I HAD SOME!!!!


            /*
            //demo for how to set power
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);
            */






            //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  * * * * * * * * * * * * * * *


            // Show the elapsed game time and wheel power, for debugging and just helping
            telemetry.addData("Status", "Run Time: " + runtimeAuto.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", (float)0/*leftPower*/,(float)0 /*rightPower*/);
            telemetry.update();
        }
    }
}
