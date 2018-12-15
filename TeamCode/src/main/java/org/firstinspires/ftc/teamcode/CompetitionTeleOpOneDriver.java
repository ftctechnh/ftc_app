package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Competition TeleOp One Driver", group = "Rohan")
public class CompetitionTeleOpOneDriver extends LinearOpMode {

    HardwareBruinBot robot = new HardwareBruinBot();
    public void runOpMode() {
        float drive = 0;
        float strafe =0;
        float rotate =0;
        float armExtension = 0;
        float armRotation;
        float mineralServosIn;
        float mineralServosOut;
        float liftingArmUp = 0;
        float liftingArmDown = 0;
        ElapsedTime runtime = new ElapsedTime();
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "BruinBots Rock!");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)

        waitForStart();
        //robot.landerLatchLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Telemetry Section ------------------------------------------------------------------
            telemetry.addData("Lift Encoder:", robot.landerLatchLift.getCurrentPosition());
            telemetry.addData("Arm Encoder:", robot.armRotate.getCurrentPosition());
            telemetry.update();

            // DRIVING SECTION!!!! ----------------------------------------------------------------
            drive = gamepad2.left_stick_y;// Negative because the gamepad is weird
            strafe = - gamepad2.left_stick_x;
            rotate =  gamepad2.right_stick_x;

            moveBot(drive, rotate, strafe,0.3);

 //           robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // LIFTING ARM SECTION!!!! --------------------------------------------------------

            // Read the driving gamepad buttons for the lifting arm

            if (gamepad2.b)
                {liftingArmUp = (float)0.2;}
                else {
                if (gamepad2.a) {
                    liftingArmDown = (float) 0.2;
                } else {
                    liftingArmDown = 0;
                    liftingArmUp = 0;
                }

            }

            if (liftingArmUp > liftingArmDown) {
                robot.landerLatchLift.setPower(liftingArmUp);
            }
            else if (liftingArmUp < liftingArmDown)
            {
                robot.landerLatchLift.setPower(-liftingArmDown);
            } else robot.landerLatchLift.setPower(0);


            if (gamepad2.x){
                //Reset the Encoder
                robot.landerLatchLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }

            int landingLevel = -2900;  // Target level to land
            double latchPower;
            if (gamepad2.y){
                // Send arm to latch height
                runtime.reset();
                while (opModeIsActive() && (runtime.seconds() < 4) && robot.landerLatchLift.getCurrentPosition() > landingLevel) {
                    robot.landerLatchLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    latchPower = -0.2;
                    robot.landerLatchLift.setPower(latchPower);
                }
                robot.landerLatchLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                robot.landerLatchLift.setPower(0);
            }

            //Shows current position of the latch arm using encoder


            // EXTENDING ARM SECTION! --------------------------------------------------------

            // Read the second gamepad and move the arm
            if (gamepad2.right_bumper) {armExtension = (float) -1;}
                else if (gamepad2.left_bumper) {armExtension = (float) 1;}
                    else armExtension = 0;


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
                else if (armExtension < 0) { // An arm extension is commanded
                //telemetry.addData("Arm Is:","Extending");
                if (robot.extendArmFrontStop.getState() == false) { // As long as the front limit switch isn't pressed, move the arm forward

                    robot.armExtend.setPower(armExtensionScaling * armExtension);
                }
                else
                    robot.armExtend.setPower(0);  // Otherwise set the power to zero
            } else robot.armExtend.setPower(0);

            //telemetry.addData("BackStop", hwMap.extendArmBackStop.getState());
            //telemetry.addData("FrontStop", hwMap.extendArmFrontStop.getState());

            // ARM ROTATION SECTION!!! -------------------------------------------------------------
            armRotation = gamepad2.right_stick_y;
            double armRotationScaling = 0.4; // 1.0 for full power, used to scale the arm power
            robot.armRotate.setPower(armRotationScaling * armRotation);

            if (gamepad2.dpad_down) {
                // Lower arm to ground
                int groundPosition = 3200;
                if (robot.armRotate.getCurrentPosition() < groundPosition) {
                    // Rotate arm up to level - Negative drive power
                    while (opModeIsActive() && robot.armRotate.getCurrentPosition() < groundPosition) {
                        robot.armRotate.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        robot.armRotate.setPower(0.25);

                    }
                    robot.armRotate.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                } else {
                    robot.armRotate.setPower(0);

                }

            } else if  (gamepad2.dpad_left){
                // Make the arm flat
                int levelPosition = 3000;
                if (robot.armRotate.getCurrentPosition() > levelPosition){
                    // Rotate arm up to level - Negative drive power
                    while (opModeIsActive() && robot.armRotate.getCurrentPosition() > levelPosition) {
                        robot.armRotate.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        robot.armRotate.setPower(-0.25);
                    }
                    robot.armRotate.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                } else if (robot.armRotate.getCurrentPosition() < levelPosition){
                    // Rotate arm down to level - Positive drive power
                    while (opModeIsActive() && robot.armRotate.getCurrentPosition() < levelPosition) {
                        robot.armRotate.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        robot.armRotate.setPower(0.25);
                    }
                    robot.armRotate.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                } else robot.armRotate.setPower(0);

            } else if (gamepad2.dpad_up){
                // Stow the arm
                // Retract the arm
                runtime.reset();
                while ((runtime.seconds() < 3) && robot.extendArmBackStop.getState() == false){// As long as the back limit switch isn't pressed, move the arm back

                    robot.armExtend.setPower(0.15);
                }

                robot.armExtend.setPower(0); // Otherwise set the arm power to zero
                // Rotate arm to home
                runtime.reset();
                while (opModeIsActive() && (runtime.seconds() < 3) && robot.armRotate.getCurrentPosition() > 50) {
                    robot.armRotate.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.armRotate.setPower(-0.25);
                }
                robot.armRotate.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            } else if (gamepad2.dpad_right){
                // Reset Rotation Arm Encoder
                robot.armRotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }



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

            telemetry.addData("rightFront Drive Motor:", robot.rightFrontDrive.getCurrentPosition());
            telemetry.update();

        }
    }

    public void moveBot(double drive, double rotate, double strafe, double scaleFactor)
    {
        // This module takes inputs, normalizes them to DRIVE_SPEED, and drives the motors
//        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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


