package org.firstinspires.ftc.teamcode.Demos;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.CompleteOldAutonomous;

/**
 * Created by guberti on 1/16/2018.
 */
@TeleOp(name="Test Ultrasonic Sensors", group="Demo")
public class ReadUltrasonicSensors extends CompleteOldAutonomous {

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        double heading = 0;

        while(opModeIsActive()) {
            double speed = 0;
            if (gamepad1.dpad_left) {
                heading += Math.PI/2;
                turnToPos(heading);
                robot.sleep(500);
            } else if (gamepad1.dpad_right) {
                heading -= Math.PI/2;
                turnToPos(heading);
                robot.sleep(500);
            } else if (gamepad1.dpad_up) {
                speed = 1;
            } else if (gamepad1.dpad_down) {
                speed = -1;
            }

            robot.setMotorSpeeds(new double []{speed, speed, speed, speed});

            telemetry.addData("Front distance: ", robot.frontUltrasonic.getVoltage());
            telemetry.update();
        }
    }
}
