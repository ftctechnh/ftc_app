package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**BoardBot Teleop script. Used for running a FWD robot with Omni wheels on the back. Tank drive controls.
 * Created by Clayton Ramsey on 1/20/2017.
 */
@TeleOp(name = "BoardBot", group = "")
public class BoardBot extends OpMode {
    DcMotor motorLeft;
    DcMotor motorRight;

    @Override
    public void init() {
        motorLeft = hardwareMap.dcMotor.get("Left");
        motorRight = hardwareMap.dcMotor.get("Right");
    }

    @Override
    public void loop() {
        motorLeft.setPower(-gamepad1.left_stick_y);
        motorRight.setPower(gamepad1.right_stick_y);
    }
}