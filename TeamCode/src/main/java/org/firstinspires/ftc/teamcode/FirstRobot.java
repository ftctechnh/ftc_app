package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class FirstRobot extends LinearOpMode {

    private DcMotor leftMotor;
    private DcMotor rightMotor;

    @Override
    public void runOpMode() throws InterruptedException {

        AnimatornicsRobot robot = new AnimatornicsRobot(hardwareMap, telemetry);

        waitForStart();
        double tgtLeftPower = 0;
        double tgtRightPower = 0;

        while (opModeIsActive()) {
            tgtLeftPower = this.gamepad1.left_stick_y;
            tgtRightPower = -this.gamepad1.right_stick_y;

            robot.setWheelPower(tgtLeftPower, tgtLeftPower, tgtRightPower, tgtRightPower);

            telemetry.addData("Status", "Running");
            telemetry.addData("X and Y",
                    "LX:"+this.gamepad1.left_stick_x +
                            "LY:"+this.gamepad1.left_stick_y +
                            "RX:"+this.gamepad1.right_stick_x +
                            "RY:"+this.gamepad1.right_stick_y);
            telemetry.update();
        }
    }
}
