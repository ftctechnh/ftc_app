package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.LearningHardwareClass;
import org.firstinspires.ftc.teamcode.MasterHardwareClass;

/**
 * ☺ Welcome to your first teleop code! ☺
 */
@TeleOp(name = "My Servo Control Teleop", group = "Learning")
//@Disabled
public class PositionServoLearningTeleop extends LinearOpMode {

    /* This says to use the LearningHardwareClass */
    MasterHardwareClass robot = new MasterHardwareClass();

    /* This is my power variable */
    double servoPosition;

    @Override
    public void runOpMode() {

        /* telemetry sends text & data to the phone */
        telemetry.addLine("Ready to Run!");
        telemetry.update();
        waitForStart();

    /* While OpMode is Active Loop */
        while (opModeIsActive()) {
            if(gamepad1.x){
                if(servoPosition != 0){
                    servoPosition = 0;
                    robot.myServo.setPosition(0);
                }
            }
            if(gamepad1.b){
                if(servoPosition != 1){
                    servoPosition = 1;
                    robot.myServo.setPosition(1);
                }
            }
        }
    }
}
