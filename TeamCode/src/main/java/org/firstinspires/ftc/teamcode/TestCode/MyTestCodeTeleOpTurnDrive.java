package org.firstinspires.ftc.teamcode.TestCode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by team on 7/19/2017.
 */

@TeleOp(name = "jTurnZero" , group = "TeleOp")
public class MyTestCodeTeleOpTurnDrive extends MyTestCodeTelemetry {


    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {
        super.loop();
        padControlsTurnMove();
        setMotorPower();
        super.myTelemetry();

    }   //end loop

    /* padControlsTurnMove assigns turning to the left stick and
       moving forward and back to the right stick.  How fast will depend on how much you press
       the stick. it will turn with a ZERO  radius.
     */
    private void padControlsTurnMove(){

        // GO STRAIGHT on right stick:  right stick up = fwd, right stick down = backwards
        rightPower = gamepad1.right_stick_y; // gamepad right stick y for both right and left robot  motor
        leftPower = rightPower;              // gamepad right stick for left robot motor too

        // TURN with Left Stick - if push left stick up, go left; if left stick down, go right

        // to turn left, left motor goes backward, right motor goes forward. ZERO radius turn.
        // to turn right, left motor will now go forward, right motor goes backward, ZERO radius turn.
        leftPower  = -gamepad1.left_stick_y; // gamepad left stick for left robot motor
        rightPower = gamepad1.left_stick_y; // gamepad left stick for left robot motor

        if (gamepad1.dpad_up){              //  lift motor controlled by d-pad on controller
            liftPower = 1;
        }
        else if (gamepad1.dpad_down){
            liftPower = -1;
        }
        else {
            liftPower = 0;
        }
/*
        if (gamepad1.left_trigger){              //  arm motor controlled by A/B buttons on controller
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
        armPower = armPower - gamepad1.left_trigger;


    }

    private void setMotorPower() {

        rightMotor.setPower(rightPower);
        leftMotor.setPower(leftPower);
        liftMotor.setPower(liftPower);
        liftMotor.setPower(armPower);
    }

}
