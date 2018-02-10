package org.firstinspires.ftc.teamcode.Deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.teamcode.NullbotHardware.getAngleDifference;

/**
 * Created by guberti on 11/4/2017.
 */
@Disabled
@Autonomous(name="BACK BLUE parking", group="Autonomous")
public class CompBlueBackParking extends CompBlueGemOnlyAutonomous {

    final double ACCEPTABLE_HEADING_VARIATION = Math.PI / 90; // 1 degree
    final double INITIAL_DESIRED_HEADING = Math.PI / 2;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);

        super.runOpMode();

        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

        for (DcMotor m : robot.motorArr) {
            m.setPower(0.2);
        }

        robot.frontLeft.setTargetPosition(3360);
        robot.backLeft.setTargetPosition(3360);
        robot.frontRight.setTargetPosition(3360);
        robot.backRight.setTargetPosition(3360);

        robot.sleep(3000);
        turnToPos(INITIAL_DESIRED_HEADING);

        robot.frontLeft.setTargetPosition(robot.frontLeft.getTargetPosition() + 1120);
        robot.backLeft.setTargetPosition(robot.backLeft.getTargetPosition() + 1120);
        robot.frontRight.setTargetPosition(robot.frontRight.getTargetPosition() + 1120);
        robot.backRight.setTargetPosition(robot.backRight.getTargetPosition() + 1120);

        robot.openBlockClaw();
        robot.setLiftMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setPower(0.2);
        robot.lift.setTargetPosition(0);
        robot.sleep(2000);

    }
    public void turnToPos(double pos) {
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double difference = Double.MAX_VALUE;


        while(Math.abs(difference) > ACCEPTABLE_HEADING_VARIATION && opModeIsActive()) {
            telemetry.addData("Heading difference", difference);
            double heading = robot.getGyroHeading();

            difference = getAngleDifference(pos, heading);
            double turnSpeed = difference;
            turnSpeed = Math.max(-0.5, Math.min(0.5, turnSpeed));

            double[] unscaledMotorPowers = new double[4];
            telemetry.addData("Turnspeed", turnSpeed);

            for (int i = 0; i < unscaledMotorPowers.length; i++) {
                if (i % 2 == 0) {
                    unscaledMotorPowers[i] = turnSpeed;
                } else {
                    unscaledMotorPowers[i] = -turnSpeed;
                }
            }
            telemetry.addData("M1", unscaledMotorPowers[0]);
            telemetry.addData("M2", unscaledMotorPowers[1]);
            telemetry.addData("M3", unscaledMotorPowers[2]);
            telemetry.addData("M4", unscaledMotorPowers[3]);

            telemetry.update();

            robot.setMotorSpeeds(unscaledMotorPowers);
        }
        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        stopMoving();
    }

    public void stopMoving() {
        for (DcMotor m : robot.motorArr) {
            m.setPower(0);
        }
    }
}
