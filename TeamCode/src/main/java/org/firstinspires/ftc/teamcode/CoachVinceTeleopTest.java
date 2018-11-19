package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.HardwareBruinBot;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Coach Vince Teleop Test", group = "Vince")
public class CoachVinceTeleopTest extends LinearOpMode {

    HardwareBruinBot robot = new HardwareBruinBot();
    public void runOpMode() {
        float drive;
        float strafe;
        float rotate;
        float maxDrive;
        float frontMax;
        float rearMax;
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
        telemetry.addData("Say", "Coach Vince Rocks!");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // DRIVING SECTION!!!! ----------------------------------------------------------------
            drive = -gamepad1.left_stick_y;// Negative because the gamepad is weird
            strafe = gamepad1.left_stick_x;
            rotate = gamepad1.right_stick_x;
 /*
            frontMax = Math.max(Math.abs(drive + strafe + rotate), Math.abs(drive - strafe - rotate));
            rearMax = Math.max(Math.abs(drive - strafe + rotate), Math.abs(drive + strafe - rotate));
            maxDrive = Math.max(frontMax, rearMax);
            maxDrive = Math.max(maxDrive,1);
            drive = drive/maxDrive;
            strafe = strafe/maxDrive;
            rotate = rotate/maxDrive;
            double moveScaling = 0.3; // 1 for full speed, 0.3 is controllable
            //maxDrive = Math.max(Math.max((drive+strafe+rotate),(drive - strafe + rotate)));
            robot.leftFrontDrive.setPower(moveScaling*(drive + strafe + rotate)); //= drive + strafe + rotate;
            robot.leftRearDrive.setPower(moveScaling * (drive - strafe + rotate)); //= drive - strafe + rotate;
            robot.rightFrontDrive.setPower(moveScaling * (drive - strafe - rotate)); //= drive - strafe - rotate;
            robot.rightRearDrive.setPower(moveScaling * (drive + strafe - rotate)); //= drive + strafe - rotate;
            */
            moveBot(drive, rotate, strafe,0.3);

            // LIFTING ARM SECTION!!!! --------------------------------------------------------

            // Read the driving gamepad triggers for the lifting arm
            liftingArmUp = gamepad1.right_trigger;
            liftingArmDown = gamepad1.left_trigger;

            if (liftingArmUp > liftingArmDown) {
                robot.landerLatchLift.setPower(liftingArmUp);
            }
            else
            {
                robot.landerLatchLift.setPower(-liftingArmDown);
            }

            // EXTENDING ARM SECTION! --------------------------------------------------------

            // Read the second gamepad and move the arm
            armExtension = gamepad2.right_stick_y;

            //Limits the motors output. Somewhere between 0.1 and 0.2 works.  Any higher and the limit switches dont stop the rms from crashing into each other
            double armExtensionScaling = 0.15;

            // Check for Extension Arm Stops, command arm if you're not in the stops
            // ArmExtension is negative when the arm is extending, positive when it is retracting

            if (armExtension > 0) {  // An arm retraction is commanded
                telemetry.addData("Arm is:","Retracting");
                if (robot.extendArmBackStop.getState() == false){// As long as the back limit switch isn't pressed, move the arm back

                    robot.armExtend.setPower(armExtensionScaling * armExtension);
                }
                else
                    robot.armExtend.setPower(0); // Otherwise set the arm power to zero
            }
                else { // An arm extension is commanded
                telemetry.addData("Arm Is:","Extending");
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
        float maxDrive;
        float frontMax;
        float rearMax;


        // Find the maximum value of the inputs and normalize
        frontMax = Math.max(Math.abs((float)drive + (float)strafe + (float)rotate), Math.abs((float)drive - (float)strafe - (float)rotate));
        rearMax = Math.max(Math.abs((float)drive - (float)strafe + (float)rotate), Math.abs((float)drive + (float)strafe - (float)rotate));
        maxDrive = Math.max(frontMax, rearMax);
        maxDrive = (float) Math.max(maxDrive,(float)scaleFactor);
        drive = drive/maxDrive;
        strafe = strafe/maxDrive;
        rotate = rotate/maxDrive;

/*
        //calculate motor powers
        //double scaleFactor = .7; // Max autonomous speed of the robot
        double tmpScale = 1;
        // Ensuring we don't have a divide by zero error
        if (((drive + strafe) == 0) || ((drive-strafe) == 0))
            drive=drive+0.00001;

        // Solve this equation backwards:
        // MotorX = TranslationX * scaleFactor + RotationX
        // to find scaleFactor that ensures -1 <= MotorX <= 1 and 0 < scaleFactor <= 1
        // drive+strafe+rotate
        if (Math.abs(strafe + drive + rotate)>1) {
            tmpScale = (1 - rotate) / (drive + strafe);
        } else if ((strafe + rotate + drive)<-1) {
            tmpScale = (rotate - 1) / (drive + strafe);
        }
        if (tmpScale < scaleFactor) {
            scaleFactor = tmpScale;
        }
        // drive-strafe+rotate
        if (Math.abs(-strafe + drive + rotate)>1) {
            tmpScale = (1 - rotate) / (drive + -strafe);
        } else if ((-strafe + rotate + drive)<-1) {
            tmpScale = (rotate - 1) / (drive + -strafe);
        }
        if (tmpScale < scaleFactor) {
            scaleFactor = tmpScale;
        }
        //drive-strafe-rotate
        if (Math.abs(-strafe + drive + -rotate)>1) {
            tmpScale = (1 - -rotate) / (drive + -strafe);
        } else if ((strafe + -rotate + drive)<-1) {
            tmpScale = (-rotate - 1) / (drive + -strafe);
        }
        if (tmpScale < scaleFactor) {
            scaleFactor = tmpScale;
        }
        // drive + strafe - rotate
        if (Math.abs(strafe + drive + -rotate)>1) {
            tmpScale = (1 - -rotate) / (drive + strafe);
        } else if ((strafe+rotate+drive)<-1) {
            tmpScale = (-rotate - 1) / (drive + strafe);
        }
        if (tmpScale < scaleFactor) {
            scaleFactor = tmpScale;
        }
*/

        robot.leftFrontDrive.setPower(scaleFactor*(drive + strafe) + rotate);
        robot.leftRearDrive.setPower(scaleFactor*(drive - strafe) + rotate);
        robot.rightFrontDrive.setPower(scaleFactor*(drive - strafe) - rotate);
        robot.rightRearDrive.setPower(scaleFactor*(drive + strafe) - rotate);
        //telemetry.addData("drive",drive);
        //telemetry.addData("strafe",strafe);
        //telemetry.addData("rotate",rotate);
        //telemetry.addData("scaleFactor",scaleFactor);
        //telemetry.update();
    }
}


