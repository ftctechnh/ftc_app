package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Noah Pietrafesa on 12/3/16.
 */
@TeleOp
public class NoahOpMode extends OpMode {

    DcMotor left1;
    DcMotor left2;
    DcMotor right1;
    DcMotor right2;

    @Override
    public void init() {
        left1 = hardwareMap.dcMotor.get("left1");
        left2 = hardwareMap.dcMotor.get("left2");
        right1 = hardwareMap.dcMotor.get("right1");
        right2 = hardwareMap.dcMotor.get("right2");
    }
    //Noah was here

    @Override
    public void loop()
    {
       // if(gamepad2.left_stick_x == 0)//Forward/Backwards
        if(gamepad1.right_bumper)
        {
            left1.setPower(gamepad1.right_stick_y);
            left2.setPower(gamepad1.right_stick_y);
            right2.setPower(-1 * (gamepad1.right_stick_y));
            right1.setPower(-1 * (gamepad1.right_stick_y));
       }
// Left/Right
//        else if(gamepad1.left_trigger == 0) {
//            left2.setPower(gamepad1.left_stick_y);
//            right1.setPower(-1 * (gamepad1.left_stick_y));
//        }
//        else if(gamepad1.a == false) {
//            left1.setPower(gamepad1.left_stick_x);
//            right2.setPower(-1 * (gamepad1.left_stick_x));
//        }
//

        else
        {
            left1.setPower(gamepad1.right_stick_x);
            left2.setPower(-1 * (gamepad1.right_stick_x));
            right1.setPower(-1 * (gamepad1.right_stick_x));
            right2.setPower(gamepad1.right_stick_x);
        }
        //Diagonals
//       else if(gamepad1.left_trigger ==0) {
//            left2.setPower(gamepad1.left_stick_y);
//            right1.setPower(-1 * (gamepad1.left_stick_y));
//        }
//        else {
//            left1.setPower(gamepad1.left_stick_x);
//            right2.setPower(-1 * (gamepad1.left_stick_x));
//        }

        //non mecanum wheels config
//        left1.setPower(gamepad1.left_stick_y);
//        left2.setPower(gamepad1.left_stick_y);
//        right2.setPower(-1*(gamepad1.right_stick_y));
//        right1.setPower(-1*(gamepad1.right_stick_y));
    }
}
