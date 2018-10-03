package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.LearningHardwareClass;
import org.firstinspires.ftc.teamcode.MasterHardwareClass;

/**
 * ☺ Welcome to your first teleop code! ☺
 */
@TeleOp(name = "My Motor Control Teleop", group = "Learning")
//@Disabled
public class MotorLearningTeleop extends LinearOpMode {

    /* This says to use the LearningHardwareClass */
    LearningHardwareClass robot = new LearningHardwareClass();

    /* This is my power variable */
    double motorPower;

    @Override
    public void runOpMode() {
        /* run the initialization method in the LearningHardwareClass */
        robot.init(hardwareMap);

        /* telemetry sends text & data to the phone */
        telemetry.addLine("Ready to Run!");
        telemetry.update();
        waitForStart();

    /* While OpMode is Active Loop */
        while (opModeIsActive()) {
            if(gamepad1.dpad_up) {
                if (motorPower != 1) {
                    robot.myMotor.setPower(1);
                    motorPower = 1;
                }
            }
            if(motorPower != 0){
                robot.myMotor.setPower(0);
                motorPower = 0;
            }
        }
    }
}
