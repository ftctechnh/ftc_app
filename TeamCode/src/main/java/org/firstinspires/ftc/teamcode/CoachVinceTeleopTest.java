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
            double moveScaling = 0.3; // 1 for full speed
            //maxDrive = Math.max(Math.max((drive+strafe+rotate),(drive - strafe + rotate)));
            hwMap.leftFrontDrive.setPower(moveScaling*(drive + strafe + rotate)); //= drive + strafe + rotate;
            hwMap.leftRearDrive.setPower(moveScaling * (drive - strafe + rotate)); //= drive - strafe + rotate;
            hwMap.rightFrontDrive.setPower(moveScaling * (drive - strafe - rotate)); //= drive - strafe - rotate;
            hwMap.rightRearDrive.setPower(moveScaling * (drive + strafe - rotate)); //= drive + strafe - rotate;

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

            // Read the second gamepad and move the arm
            armExtension = gamepad2.right_stick_y;
            //telemetry.addData("ArmExtension",String.valueOf(armExtension));

            armRotation = gamepad2.left_stick_y;
            double armExtensionScaling = 0.2;
            double armRotationScaling = 0.4; // 1.0 for full power, used to scale the arm power
            hwMap.armRotate.setPower(armRotationScaling * armRotation);
            // Check for Extension Arm Stops, command arm if you're not in the stops
            // ArmExtension is negative when the arm is extending, positive when it is retracting
            //telemetry.addData("extendArm",armExtension);
            if (armExtension > 0) {
                telemetry.addData("Arm is:","Retracting");
                if (hwMap.extendArmBackStop.getState() == false){

                    hwMap.armExtend.setPower(armExtensionScaling * armExtension);
                }
                else
                    hwMap.armExtend.setPower(0);
            }
                else { // armExtension < 0
                telemetry.addData("Arm Is:","Extending");
                if (hwMap.extendArmFrontStop.getState() == false) {

                    hwMap.armExtend.setPower(armExtensionScaling * armExtension);
                }
                else
                hwMap.armExtend.setPower(0);
            }

            telemetry.addData("BackStop", hwMap.extendArmBackStop.getState());
            telemetry.addData("FrontStop", hwMap.extendArmFrontStop.getState());


            // Read the triggers and roll the Mineral Servos
            mineralServosIn = gamepad2.right_trigger;
            mineralServosOut = gamepad2.left_trigger;
            if (mineralServosIn > mineralServosOut) {
                hwMap.leftMineral.setPower(-mineralServosIn); // These don't look right, how do I get them to rotate continuously
                hwMap.rightMineral.setPower(mineralServosIn);
            }
            else
            {
                hwMap.leftMineral.setPower(mineralServosOut);
                hwMap.rightMineral.setPower(-mineralServosOut);
            }


            /*

            // Use gamepad left & right Bumpers to open and close the claw
            if (gamepad1.right_bumper)
                clawOffset += CLAW_SPEED;
            else if (gamepad1.left_bumper)
                clawOffset -= CLAW_SPEED;

            // Move both servos to new position.  Assume servos are mirror image of each other.
            clawOffset = Range.clip(clawOffset, -0.5, 0.5);
            robot.leftClaw.setPosition(robot.MID_SERVO + clawOffset);
            robot.rightClaw.setPosition(robot.MID_SERVO - clawOffset);

            // Use gamepad buttons to move arm up (Y) and down (A)
            if (gamepad1.y)
                robot.leftArm.setPower(robot.ARM_UP_POWER);
            else if (gamepad1.a)
                robot.leftArm.setPower(robot.ARM_DOWN_POWER);
            else
                robot.leftArm.setPower(0.0);

            // Send telemetry message to signify robot running;
            telemetry.addData("claw",  "Offset = %.2f", clawOffset);
            telemetry.addData("left",  "%.2f", left);
            telemetry.addData("right", "%.2f", right); */
            telemetry.update();

            // Pace this loop so jaw action is reasonable speed.
            //sleep(50);
        }
    }

}


