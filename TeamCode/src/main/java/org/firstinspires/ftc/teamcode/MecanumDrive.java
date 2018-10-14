package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.HardwareBruinBot;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "MecanumDrive", group = "Rohan")
public class MecanumDrive extends LinearOpMode {

      HardwareBruinBot hwMap = new HardwareBruinBot();
    public void runOpMode() {
        float drive;
        float strafe;
        float rotate;
        float maxDrive;
        float frontMax;
        float rearMax;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        hwMap.init(hardwareMap);



        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
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
            /*
            // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
            // This way it's also easy to just drive straight, or just turn.
            //
            drive = -gamepad1.left_stick_y;
            turn  =  gamepad1.right_stick_x;

            // Combine drive and turn for blended motion.
            left  = drive + turn;
            right = drive - turn;

            // Normalize the values so neither exceed +/- 1.0
            max = Math.max(Math.abs(left), Math.abs(right));
            if (max > 1.0)
            {
                left /= max;
                right /= max;
            }

            // Output the safe vales to the motor drives.
            robot.leftDrive.setPower(left);
            robot.rightDrive.setPower(right);

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
