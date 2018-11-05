package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class ServoTest extends LinearOpMode {

    private CRServo left_servo;
    private CRServo right_servo;

    @Override
    public void runOpMode() throws InterruptedException {

        //AnimatornicsRobot robot = new AnimatornicsRobot(hardwareMap, telemetry);

        left_servo = hardwareMap.crservo.get("left_servo");
        right_servo = hardwareMap.crservo.get("right_servo");

        waitForStart();
        double tgtLeftPower = 0;
        double tgtRightPower = 0;

        while (opModeIsActive()) {
            tgtLeftPower = this.gamepad1.left_stick_y;
            tgtRightPower = -this.gamepad1.right_stick_y;

            left_servo.setPower(tgtLeftPower);
            right_servo.setPower(tgtRightPower);

            //robot.setWheelPower(tgtLeftPower, tgtLeftPower, tgtRightPower, tgtRightPower);

            telemetry.addData("Status", "Running");
            telemetry.addData("X and Y are",
                    "LX:"+this.gamepad1.left_stick_x +
                            "LY:"+this.gamepad1.left_stick_y +
                            "RX:"+this.gamepad1.right_stick_x +
                            "RY:"+this.gamepad1.right_stick_y);
            telemetry.update();
        }
    }
}
