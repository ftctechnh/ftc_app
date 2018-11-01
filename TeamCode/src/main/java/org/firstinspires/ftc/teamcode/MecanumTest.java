package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Recharged Orange on 9/27/2018.
 */
@TeleOp(name = "mecenum drive code")

public class MecanumTest extends superClass {

    double drive ;
    double strafe;
    double rotate;


    double frontLeftPower;
    double backLeftPower;
    double frontRightPower;
    double backRightPower;

    @Override
    public void runOpMode() {

        initialization(false);
        waitForStart();
        while (opModeIsActive()) {
            mecanumDrive();
            sweeper();
            servo();
            //REVServo();
            touchSen();
        }
    }


    public void mecanumDrive() {

        double lt = gamepad1.left_trigger;
        double rt = gamepad1.right_trigger;
        double ly = -gamepad1.left_stick_y;
        double ry = -gamepad1.right_stick_y;

        double d = (ly + ry) / 2;
        double s = rt - lt;
        double r = (ly - ry) / 2;


        drive = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        // You might have to play with the + or - depending on how your motors are installed
        /*frontLeftPower = drive + strafe + rotate;
        backLeftPower = drive - strafe + rotate;
        frontRightPower = drive - strafe - rotate;
        backRightPower = drive + strafe - rotate;*/

        frontLeftPower = d + s + r;
        backLeftPower = d - s + r;
        frontRightPower = d - s - r;
        backRightPower = d + s - r;

        leftBack.setPower(-backLeftPower);
        leftFront.setPower(-frontLeftPower);
        rightBack.setPower(backRightPower);
        rightFront.setPower(frontRightPower);

    }

public void sweeper(){

        if (gamepad1.right_bumper){
            sweeper.setPower(1);
        }
        else if (gamepad1.left_bumper){
            sweeper.setPower(-1);
        }
        else sweeper.setPower(0);

}

public void servo(){

    if (gamepad1.x){
        servo.setPosition(1);
    }
    else servo.setPosition(0);
}

/*public void REVServo(){

    if (gamepad1.a){
        REVServo.setPower(1);
    }

    else if (gamepad1.b){
        REVServo.setPower(-1);
    }



}*/

public void touchSen(){

    if (touchSensor.isPressed()){
        REVServo.setPower(0);
    }
    else REVServo.setPower(1);
}


    }
