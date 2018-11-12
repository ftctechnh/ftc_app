package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.HardwareBruinBot;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Coach Vince Teleop Test", group = "Vince")
public class CoachVinceTeleopTest extends LinearOpMode {

    HardwareBruinBot hwMap = new HardwareBruinBot();
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
        hwMap.init(hardwareMap);



        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Coach Vince Rocks!");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // DRIVING SECTION!!!! ----------------------------------------------------------------
            drive = -gamepad1.left_stick_y;  // Negative because the gamepad is weird
            strafe = gamepad1.left_stick_x;
            rotate = gamepad1.right_stick_x;
            frontMax = Math.max(Math.abs(drive + strafe + rotate), Math.abs(drive - strafe - rotate));
            rearMax = Math.max(Math.abs(drive - strafe + rotate), Math.abs(drive + strafe - rotate));
            maxDrive = Math.max(frontMax, rearMax);
            maxDrive = Math.max(maxDrive,1);
            drive = drive/maxDrive;
            strafe = strafe/maxDrive;
            rotate = rotate/maxDrive;
            double moveScaling = 0.3; // 1 for full speed, 0.3 is controllable
            //maxDrive = Math.max(Math.max((drive+strafe+rotate),(drive - strafe + rotate)));
            hwMap.leftFrontDrive.setPower(moveScaling*(drive + strafe + rotate)); //= drive + strafe + rotate;
            hwMap.leftRearDrive.setPower(moveScaling * (drive - strafe + rotate)); //= drive - strafe + rotate;
            hwMap.rightFrontDrive.setPower(moveScaling * (drive - strafe - rotate)); //= drive - strafe - rotate;
            hwMap.rightRearDrive.setPower(moveScaling * (drive + strafe - rotate)); //= drive + strafe - rotate;

            // LIFTING ARM SECTION!!!! --------------------------------------------------------

            // Read the driving gamepad triggers for the lifting arm
            liftingArmUp = gamepad1.right_trigger;
            liftingArmDown = gamepad1.left_trigger;

            if (liftingArmUp > liftingArmDown) {
                hwMap.landerLatchLift.setPower(liftingArmUp);
            }
            else
            {
                hwMap.landerLatchLift.setPower(-liftingArmDown);
            }

            // EXTENDING ARM SECTION! --------------------------------------------------------

            // Read the second gamepad and move the arm
            armExtension = gamepad2.right_stick_y;

            //Limits the motors output. Somewhere between 0.1 and 0.2 works.  Any higher and the limit switches dont stop the rms from crashing into each other
            double armExtensionScaling = 0.2;

            // Check for Extension Arm Stops, command arm if you're not in the stops
            // ArmExtension is negative when the arm is extending, positive when it is retracting

            if (armExtension > 0) {  // An arm retraction is commanded
                telemetry.addData("Arm is:","Retracting");
                if (hwMap.extendArmBackStop.getState() == false){// As long as the back limit switch isn't pressed, move the arm back

                    hwMap.armExtend.setPower(armExtensionScaling * armExtension);
                }
                else
                    hwMap.armExtend.setPower(0); // Otherwise set the arm power to zero
            }
                else { // An arm extension is commanded
                telemetry.addData("Arm Is:","Extending");
                if (hwMap.extendArmFrontStop.getState() == false) { // As long as the front limit switch isn't pressed, move the arm forward

                    hwMap.armExtend.setPower(armExtensionScaling * armExtension);
                }
                else
                hwMap.armExtend.setPower(0);  // Otherwise set the power to zero
            }

            //telemetry.addData("BackStop", hwMap.extendArmBackStop.getState());
            //telemetry.addData("FrontStop", hwMap.extendArmFrontStop.getState());

            // ARM ROTATION SECTION!!! -------------------------------------------------------------
            armRotation = gamepad2.left_stick_y;
            double armRotationScaling = 0.4; // 1.0 for full power, used to scale the arm power
            hwMap.armRotate.setPower(armRotationScaling * armRotation);


            // MINERAL SERVO SECTION!!!! -----------------------------------------------------------
            // Read the triggers and roll the Mineral Servos
            mineralServosIn = gamepad2.right_trigger;
            mineralServosOut = gamepad2.left_trigger;
            if (mineralServosIn > mineralServosOut) {
                //hwMap.leftMineral.setPower(-mineralServosIn); // These don't look right, how do I get them to rotate continuously
                hwMap.rightMineral.setPower(mineralServosIn);
            }
            else
            {
                //hwMap.leftMineral.setPower(mineralServosOut);
                hwMap.rightMineral.setPower(-mineralServosOut);
            }

            telemetry.update();

        }
    }

}


