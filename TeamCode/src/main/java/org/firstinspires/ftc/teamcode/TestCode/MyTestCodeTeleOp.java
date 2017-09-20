package org.firstinspires.ftc.teamcode.TestCode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by team on 7/19/2017.
 */

@TeleOp(name = "Jill",group = "TeleOp" )
public class MyTestCodeTeleOp extends MyTestCodeTelemetry {

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {//D
        super.loop();
        padControls();
        setMotorPower();
        super.myTelemetry();

    }   //end loop

    private void padControls() {

        rightPower = gamepad1.right_stick_y; // gamepad right stick for right robot motor
        leftPower = gamepad1.left_stick_y; // gamepad left stick for left robot motor

        if (gamepad1.dpad_up) {              //  lift motor controlled by d-pad on controller
            liftPower = 0.75;
        } else if (gamepad1.dpad_down) {
            liftPower = -0.75;
        } else {
            liftPower = 0;
        }

        //  arm motor controlled by A/B buttons on controller

        // Gamepad h/w works like this:
        // bumper on is true, bumper off is 0 -- my comment says A/B buttons, but code below
        // uses triggers -- OOPS!!
        // gamepad1.left_trigger is a float, so trigger needs supply value for armPower
        /* this will not work --
        if (boolean!!){
            armPower = 1;
        }
        else if (gamepad1.right_trigger){
            armPower = -1;
        }
        else {
            armPower = 0;
        }
        */

        // switched armPower setting to the triggers  -- JCF 7/19/17
        armPower = gamepad1.right_trigger;
        armPower = armPower - gamepad1.right_trigger;
    }

    private void setMotorPower() {
        rightMotor.setPower(rightPower);
        leftMotor.setPower(leftPower);
        liftMotor.setPower(liftPower);
        liftMotor.setPower(armPower);
    }
}
