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
            //maxDrive = Math.max(Math.max((drive+strafe+rotate),(drive - strafe + rotate)));
            hwMap.leftFrontDrive.setPower(drive + strafe + rotate); //= drive + strafe + rotate;
            hwMap.leftRearDrive.setPower(drive - strafe + rotate); //= drive - strafe + rotate;
            hwMap.rightFrontDrive.setPower(drive - strafe - rotate); //= drive - strafe - rotate;
            hwMap.rightRearDrive.setPower(drive + strafe - rotate); //= drive + strafe - rotate;

            // Read the second gamepad and move the arm
            armExtension = gamepad2.right_stick_y;
            armRotation = gamepad2.left_stick_y;
            hwMap.armRotate.setPower(armRotation);
            hwMap.armExtend.setPower(armExtension);


            mineralServosIn = gamepad2.right_trigger;
            mineralServosOut = gamepad2.left_trigger;
            if (mineralServosIn > mineralServosOut) {
                hwMap.leftMineral.setPosition(mineralServosIn); // These don't look right, how do I get them to rotate continuously
                hwMap.rightMineral.setPosition(mineralServosIn);
            }
            else
            {
                hwMap.leftMineral.setPosition(mineralServosOut);
                hwMap.rightMineral.setPosition(mineralServosOut);
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


