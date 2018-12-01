package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.HardwareBruinBot;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Competition TeleOp", group = "Rohan")
public class CompetitionTeleOp extends LinearOpMode {

    HardwareBruinBot robot = new HardwareBruinBot();
    public void runOpMode() {
        float drive;
        float strafe;
        float rotate;
        float armExtension;
        float armRotation;
        float mineralServosIn;
        float mineralServosOut;
        float liftingArmUp;
        float liftingArmDown;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "BruinBots Rock!");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)

        waitForStart();
        robot.landerLatchLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // DRIVING SECTION!!!! ----------------------------------------------------------------
            drive = -gamepad1.left_stick_y;// Negative because the gamepad is weird
            strafe = gamepad1.left_stick_x;
            rotate = gamepad1.right_stick_x;

            moveBot(drive, rotate, strafe,0.3);

            // LIFTING ARM SECTION!!!! --------------------------------------------------------

            // Read the driving gamepad triggers for the lifting arm
            liftingArmUp = gamepad1.right_trigger;
            liftingArmDown = gamepad1.left_trigger;
            robot.landerLatchLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            if (liftingArmUp > liftingArmDown) {
                robot.landerLatchLift.setPower(liftingArmUp);
            }
            else
            {
                robot.landerLatchLift.setPower(-liftingArmDown);
            }
            //Shows current position of the latch arm using encoder
            telemetry.addData("Arm Encoder:", robot.landerLatchLift.getCurrentPosition());
            telemetry.update();

            // EXTENDING ARM SECTION! --------------------------------------------------------

            // Read the second gamepad and move the arm
            armExtension = gamepad2.right_stick_y;

            //Limits the motors output. Somewhere between 0.1 and 0.2 works.  Any higher and the limit switches dont stop the rms from crashing into each other
            double armExtensionScaling = 0.15;

            // Check for Extension Arm Stops, command arm if you're not in the stops
            // ArmExtension is negative when the arm is extending, positive when it is retracting

            if (armExtension > 0) {  // An arm retraction is commanded
                //telemetry.addData("Arm is:","Retracting");
                if (robot.extendArmBackStop.getState() == false){// As long as the back limit switch isn't pressed, move the arm back

                    robot.armExtend.setPower(armExtensionScaling * armExtension);
                }
                else
                    robot.armExtend.setPower(0); // Otherwise set the arm power to zero
            }
                else { // An arm extension is commanded
                //telemetry.addData("Arm Is:","Extending");
                if (robot.extendArmFrontStop.getState() == false) { // As long as the front limit switch isn't pressed, move the arm forward

                    robot.armExtend.setPower(armExtensionScaling * armExtension);
                }
                else
                    robot.armExtend.setPower(0);  // Otherwise set the power to zero
            }

            //telemetry.addData("BackStop", hwMap.extendArmBackStop.getState());
            //telemetry.addData("FrontStop", hwMap.extendArmFrontStop.getState());

            // ARM ROTATION SECTION!!! -------------------------------------------------------------
            armRotation = gamepad2.left_stick_y;
            double armRotationScaling = 0.4; // 1.0 for full power, used to scale the arm power
            robot.armRotate.setPower(armRotationScaling * armRotation);


            // MINERAL SERVO SECTION!!!! -----------------------------------------------------------
            // Read the triggers and roll the Mineral Servos
            mineralServosIn = gamepad2.right_trigger;
            mineralServosOut = gamepad2.left_trigger;
            if (mineralServosIn > mineralServosOut) {
                robot.rightMineral.setPower(mineralServosIn);
            }
            else
            {
                robot.rightMineral.setPower(-mineralServosOut);
            }

            telemetry.update();

        }
    }
    public void moveBot(double drive, double rotate, double strafe, double scaleFactor)
    {
        // This module takes inputs, normalizes them to DRIVE_SPEED, and drives the motors

        // How to normalize...Version 3
        //Put the raw wheel speeds into an array
        double wheelSpeeds[] = new double[4];
        wheelSpeeds[0] = drive + strafe - rotate;
        wheelSpeeds[1] = drive - strafe - rotate;
        wheelSpeeds[2] = drive - strafe + rotate;
        wheelSpeeds[3] = drive + strafe + rotate;
        // Find the magnitude of the first element in the array
        double maxMagnitude = Math.abs(wheelSpeeds[0]);
        // If any of the other wheel speeds are bigger, save that value in maxMagnitude
        for (int i = 1; i < wheelSpeeds.length; i++)
        {
            double magnitude = Math.abs(wheelSpeeds[i]);
            if (magnitude > maxMagnitude)
            {
                maxMagnitude = magnitude;
            }
        }
        // Normalize all of the magnitudes to below 1
        if (maxMagnitude > 1.0)
        {
            for (int i = 0; i < wheelSpeeds.length; i++)
            {
                wheelSpeeds[i] /= maxMagnitude;
            }
        }
        // Send the normalized values to the wheels, further scaled by the user
        robot.leftFrontDrive.setPower(scaleFactor * wheelSpeeds[0]);
        robot.leftRearDrive.setPower(scaleFactor * wheelSpeeds[1]);
        robot.rightFrontDrive.setPower(scaleFactor * wheelSpeeds[2]);
        robot.rightRearDrive.setPower(scaleFactor * wheelSpeeds[3]);

    }
}


