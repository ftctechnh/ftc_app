package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Joseph Liang on 12/3/2017.
 */

@TeleOp (name = "30minTeleOp", group = "Pushbot")
//@Disabled
public class ThirtyMinTeleOp extends OpMode{

    GlyphArm gilgearmesh = new GlyphArm();
    RelicDrive robot       = new RelicDrive();
    private ElapsedTime     runtime = new ElapsedTime();
    JewelSystem sensArm = new JewelSystem();

    @Override
    public void init() {
        robot.init(hardwareMap);
        gilgearmesh.init(hardwareMap);
        sensArm.init(hardwareMap);

        robot.leftMid.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightMid.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        gilgearmesh.armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        double right;
        double left;
        double armPower;

        left = gamepad1.left_stick_y;
        right = gamepad1.right_stick_y;

        if (gamepad1.left_trigger > 0) {
            left = gamepad1.left_stick_y;
            right = gamepad1.right_stick_y;
        } else if (gamepad1.left_bumper = true) {
            left = gamepad1.left_stick_y/2.5;
            right = gamepad1.right_stick_y/2.5;
        } else if (gamepad1.right_bumper = true) {
            left = gamepad1.left_stick_y/4;
            right = gamepad1.right_stick_y/4;
        } else if (gamepad1.left_trigger > 0) {
            left = gamepad1.left_stick_y/6;
            right = gamepad1.right_stick_y/6;
        } else {
            left = gamepad1.left_stick_y/2;
            right = gamepad1.right_stick_y/2;}

        robot.controlDrive(left, right);

        armPower = -gamepad2.left_stick_y;

        gilgearmesh.armPower(armPower);

        if (gamepad2.a){gilgearmesh.armPos(25,.7);}
        else if (gamepad2.x){gilgearmesh.armPos(50,.7);}
        else if (gamepad2.y){gilgearmesh.armPos(75,.7);}
        else if (gamepad2.b){gilgearmesh.armPos(100,.7);}
        else if (gamepad2.right_stick_button){gilgearmesh.armPos(0,.7);}

        if (gamepad2.right_bumper) {
            gilgearmesh.clawPos(1);
        } else if (gamepad2.left_bumper) {
            gilgearmesh.clawPos(0);
        }
        if (gamepad2.dpad_down == true){sensArm.armPos(.444);}
        else if (gamepad2.dpad_up ==true){sensArm.armPos(1);}

        telemetry.addData("Arm Pos","%7d",gilgearmesh.getArmPosition());
        telemetry.addData("Left Mid","%7d",robot.getLMencoder());
        telemetry.addData("Right Mid","%7d",robot.getRMencoder());
        telemetry.addData("Left Back","%7d",robot.getLBencoder());
        telemetry.addData("Right Back","%7d",robot.getRBencoder());
        telemetry.update();
    }
}
