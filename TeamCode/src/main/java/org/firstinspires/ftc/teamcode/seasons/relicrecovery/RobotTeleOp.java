package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.GlyphLift;

/**
 * This class is the competition robot tele-op program.
 */
@TeleOp(name = "TELEOP", group = "teleop")
public class RobotTeleOp extends LinearOpMode {
    private RelicRecoveryRobot robot;

    private static final float JOYSTICK_DEADZONE = 0.2f;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new RelicRecoveryRobot(this);

        gamepad1.setJoystickDeadzone(JOYSTICK_DEADZONE);
        gamepad2.setJoystickDeadzone(JOYSTICK_DEADZONE);

        robot.glyphLift.initializeGrippers();
        robot.intake.raiseIntake();
        robot.jewelKnocker.retractArm();

        waitForStart();

        double speedX;
        double speedY;
        double pivot;

        double liftMotorPower;
        double liftRotationMotorPower;

        while (opModeIsActive()) {
            speedX = -gamepad1.right_stick_x;
            speedY = gamepad1.right_stick_y;
            pivot = -gamepad1.left_stick_x;

            liftMotorPower = gamepad2.right_stick_y;
            liftRotationMotorPower = -gamepad2.left_stick_x;

            // slow down robot with right trigger
            if(gamepad1.right_trigger > 0) {
                speedY /= 3;
                pivot /= 3;
                speedX /= 2;
            }

            // Jewel arm control
            if (gamepad1.y){
                robot.jewelKnocker.extendArm();
            } else {
                robot.jewelKnocker.retractArm();
            }
            // intake raise/lower control
            if(gamepad1.left_bumper) {
                robot.intake.raiseIntake();
            } else if(gamepad1.left_trigger > 0) {
                robot.intake.lowerIntake();
            }

            // intake control
            if(gamepad1.dpad_up) {
                robot.intake.setIntakePower(1.0);
            } else if(gamepad1.dpad_down||gamepad1.left_trigger>0) {
                robot.intake.setIntakePower(-1.0);
            } else{
                robot.intake.setIntakePower(0);
            }

            // close/open blue gripper
            if(gamepad2.right_trigger > 0.1) {
                robot.glyphLift.openBlueGripper();
            } else {
                robot.glyphLift.closeBlueGripper();
            }

            //maneuver jewel arm
            if(gamepad1.b){
                robot.jewelKnocker.retractArm();
            }

            // close/open red gripper
            if(gamepad2.left_trigger > 0.1) {
                robot.glyphLift.openRedGripper();
            } else {
                robot.glyphLift.closeRedGripper();
            }

            // automatic lift rotation motor control
            if(gamepad2.dpad_up) {
                robot.glyphLift.setRotationMotorPosition(GlyphLift.RotationMotorPosition.UP);
            } else if(gamepad2.dpad_down) {
                robot.glyphLift.setRotationMotorPosition(GlyphLift.RotationMotorPosition.DOWN);
            } else {
                robot.glyphLift.setRotationMotorPower(liftRotationMotorPower);
            }

            telemetry.addData("Red Level", robot.jewelKnocker.getRed());
            telemetry.addData("Blue Level", robot.jewelKnocker.getBlue());
            telemetry.update();

            robot.hDriveTrain.pivot(pivot);
            robot.hDriveTrain.drive(speedX, speedY);

            robot.glyphLift.setLiftMotorPower(liftMotorPower);
        }
    }
}
