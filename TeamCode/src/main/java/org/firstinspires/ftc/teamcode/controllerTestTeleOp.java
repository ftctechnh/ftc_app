package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by anshnanda on 30/12/17.
 */

@TeleOp(name = "Old code", group = "agroup")

public class controllerTestTeleOp extends LinearOpMode
{
    private DcMotor motorFrontLeft;
    private DcMotor motorBackLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackRight;


    @Override
    public void runOpMode() throws InterruptedException
    {
        motorFrontLeft = hardwareMap.dcMotor.get("MC1M1");
        motorBackLeft = hardwareMap.dcMotor.get("MC1M2");
        motorFrontRight = hardwareMap.dcMotor.get("MC2M1");
        motorBackRight = hardwareMap.dcMotor.get("MC2M2");

        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive())
        {
            //STOP
            if(gamepad1.left_stick_x<0.1 && gamepad1.left_stick_x>-0.1 && gamepad1.left_stick_y<0.1 && gamepad1.left_stick_y>-0.1){
                motorFrontLeft.setPower(0);
                motorFrontRight.setPower(0);
                motorBackLeft.setPower(0);
                motorBackRight.setPower(0);
            }
            //FORWARD AND BACK
            if((gamepad1.left_stick_x<0.1 && gamepad1.left_stick_x>-0.1) && (gamepad1.left_stick_y >0.1 || gamepad1.left_stick_y<-0.1))
            {
                motorFrontLeft.setPower(-gamepad1.left_stick_y);
                motorFrontRight.setPower(-gamepad1.left_stick_y);
                motorBackLeft.setPower(-gamepad1.left_stick_y);
                motorBackRight.setPower(-gamepad1.left_stick_y);
            }

            //SIDE TO SIDE
            if((gamepad1.left_stick_x>0.1 || gamepad1.left_stick_x <-0.1)&&(gamepad1.left_stick_y<0.1 && gamepad1.left_stick_y>-0.1)) {

                motorFrontLeft.setPower(-gamepad1.left_stick_y);
                motorBackLeft.setPower(gamepad1.left_stick_y);
                motorFrontRight.setPower(gamepad1.left_stick_y);
                motorBackRight.setPower(-gamepad1.left_stick_y);
            }

            //DIAGONAL FORWARD LEFT
            if(gamepad1.left_stick_x<-0.1 && gamepad1.left_stick_y <-0.1)   {
                double val = (Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.left_stick_y))/2;

                motorFrontLeft.setPower(0);
                motorBackLeft.setPower(val);
                motorFrontRight.setPower(val);
                motorBackRight.setPower(0);
            }

            //DIAGONAL FORWARD RIGHT
            if(gamepad1.left_stick_x>0.1 && gamepad1.left_stick_y<-0.1) {
                double val = (Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.left_stick_y))/2;

                motorFrontLeft.setPower(val);
                motorBackLeft.setPower(0);
                motorFrontRight.setPower(0);
                motorBackRight.setPower(val);

            }

            //DIAGONAL BACKWARD LEFT
            if(gamepad1.left_stick_x<-0.1 && gamepad1.left_stick_y>0.1 )    {
                double val = (Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.left_stick_y))/2;

                motorFrontLeft.setPower(-val);
                motorBackLeft.setPower(0);
                motorFrontRight.setPower(0);
                motorBackRight.setPower(-val);

            }

            //DIAGONAL BACKWARD RIGHT
            if(gamepad1.left_stick_x>0.1 && gamepad1.left_stick_y>0.1)  {
                double val = (Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.left_stick_y))/2;

                motorFrontLeft.setPower(0);
                motorBackLeft.setPower(-val);
                motorFrontRight.setPower(-val);
                motorBackRight.setPower(0);

            }

            idle();
        }
    }
}
