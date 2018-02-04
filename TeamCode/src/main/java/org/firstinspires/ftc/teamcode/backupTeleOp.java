package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by anshnanda on 30/12/17.
 */

@TeleOp(name = "Backup tele op", group = "agroup")

public class backupTeleOp extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motorFrontLeft;
    private DcMotor motorBackLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackRight;




    @Override
    public void runOpMode() throws InterruptedException
    {

//        OLD HARDWARE VARIBALE INITIALISATION
//        motorFrontLeft = hardwareMap.dcMotor.get("MC1M1");
//        motorBackLeft = hardwareMap.dcMotor.get("MC1M2");
//        motorFrontRight = hardwareMap.dcMotor.get("MC2M1");
//        motorBackRight = hardwareMap.dcMotor.get("MC2M2");

        //NEW HARDWARE VARIBALE INITIALISATION
        motorFrontLeft  = hardwareMap.get(DcMotor.class, "MC1M1");
        motorBackLeft = hardwareMap.get(DcMotor.class, "MC1M2");
        motorFrontRight  = hardwareMap.get(DcMotor.class, "MC2M1");
        motorBackRight = hardwareMap.get(DcMotor.class, "MC2M2");


        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        runtime.reset();

        while(opModeIsActive())
        {
            //GAMEPAD 1 : DRIVER STUFF

            //CARDINAL DIRECTIONS

            //STOP
            if(!gamepad1.dpad_left && !gamepad1.dpad_right && !gamepad1.dpad_down &&
                    !gamepad1.dpad_up && !gamepad1.a && !gamepad1.b && !gamepad1.x &&
                    !gamepad1.y && (gamepad1.right_stick_x>-0.1 && gamepad1.right_stick_x<0.1)
                    && (gamepad1.left_stick_x>-0.1 && gamepad1.left_stick_x<0.1)
                    && (gamepad1.left_stick_y>-0.1 && gamepad1.left_stick_y<0.1))
            {
                motorFrontLeft.setPower(0);
                motorFrontRight.setPower(0);
                motorBackLeft.setPower(0);
                motorBackRight.setPower(0);
            }

            //Left Trigger Emergency Break
            if(gamepad1.left_trigger>0)
            {
                motorFrontLeft.setPower(0);
                motorFrontRight.setPower(0);
                motorBackLeft.setPower(0);
                motorBackRight.setPower(0);
            }

            // FORWARD
            if(gamepad1.dpad_up && !gamepad1.dpad_left && !gamepad1.dpad_right &&
                    !gamepad1.dpad_down && !gamepad1.a && !gamepad1.b && !gamepad1.x &&
                    !gamepad1.y && (gamepad1.right_stick_x>-0.1 && gamepad1.right_stick_x<0.1)
                    && (gamepad1.left_stick_x>-0.1 && gamepad1.left_stick_x<0.1) &&
                    (gamepad1.left_stick_y>-0.1 && gamepad1.left_stick_y<0.1)
                    && gamepad1.left_trigger==0)
            {
                motorFrontLeft.setPower(gamepad1.right_trigger);
                motorFrontRight.setPower(gamepad1.right_trigger);
                motorBackLeft.setPower(gamepad1.right_trigger);
                motorBackRight.setPower(gamepad1.right_trigger);
            }

            // BACKWARD
            if(gamepad1.dpad_down &&!gamepad1.dpad_up && !gamepad1.dpad_left &&
                    !gamepad1.dpad_right && !gamepad1.a && !gamepad1.b && !gamepad1.x && !gamepad1.y
                    && (gamepad1.right_stick_x>-0.1 && gamepad1.right_stick_x<0.1) &&
                    (gamepad1.left_stick_x>-0.1 && gamepad1.left_stick_x<0.1) &&
                    (gamepad1.left_stick_y>-0.1 && gamepad1.left_stick_y<0.1)
                    && gamepad1.left_trigger==0)
            {
                motorFrontLeft.setPower(-gamepad1.right_trigger);
                motorFrontRight.setPower(-gamepad1.right_trigger);
                motorBackLeft.setPower(-gamepad1.right_trigger);
                motorBackRight.setPower(-gamepad1.right_trigger);
            }

            // AXIS LEFT
            if(gamepad1.dpad_left && !gamepad1.dpad_down &&!gamepad1.dpad_up &&
                    !gamepad1.dpad_right && !gamepad1.a && !gamepad1.b && !gamepad1.x && !gamepad1.y
                    && (gamepad1.right_stick_x>-0.1 && gamepad1.right_stick_x<0.1) &&
                    (gamepad1.left_stick_x>-0.1 && gamepad1.left_stick_x<0.1) &&
                    (gamepad1.left_stick_y>-0.1 && gamepad1.left_stick_y<0.1)
                    && gamepad1.left_trigger==0)
            {
                motorFrontLeft.setPower(-gamepad1.right_trigger);
                motorBackLeft.setPower(-gamepad1.right_trigger);
                motorFrontRight.setPower(gamepad1.right_trigger);
                motorBackRight.setPower(gamepad1.right_trigger);
            }

            // AXIS RIGHT
            if(gamepad1.dpad_right && !gamepad1.dpad_down &&!gamepad1.dpad_up &&
                    !gamepad1.dpad_left && !gamepad1.a && !gamepad1.b && !gamepad1.x && !gamepad1.y
                    && (gamepad1.right_stick_x>-0.1 && gamepad1.right_stick_x<0.1) &&
                    (gamepad1.left_stick_x>-0.1 && gamepad1.left_stick_x<0.1) &&
                    (gamepad1.left_stick_y>-0.1 && gamepad1.left_stick_y<0.1)&&
                    gamepad1.left_trigger==0)
            {
                motorFrontLeft.setPower(gamepad1.right_trigger);
                motorBackLeft.setPower(gamepad1.right_trigger);
                motorFrontRight.setPower(-gamepad1.right_trigger);
                motorBackRight.setPower(-gamepad1.right_trigger);
            }

            // SWAYING LEFT AND RIGHT


            //RIGHT SWAY
            if(gamepad1.right_stick_x>0.1 && !gamepad1.dpad_right && !gamepad1.dpad_down
                    &&!gamepad1.dpad_up
                    && !gamepad1.dpad_left && !gamepad1.a && !gamepad1.b && !gamepad1.x &&
                    !gamepad1.y && (gamepad1.left_stick_x>-0.1 && gamepad1.left_stick_x<0.1) &&
                    (gamepad1.left_stick_y>-0.1 && gamepad1.left_stick_y<0.1)&&
                    gamepad1.left_trigger==0)
            {
                motorFrontLeft.setPower(gamepad1.right_trigger);
                motorBackLeft.setPower(-gamepad1.right_trigger);
                motorFrontRight.setPower(-gamepad1.right_trigger);
                motorBackRight.setPower(gamepad1.right_trigger);
            }

            //LEFT SWAY
            if(gamepad1.right_stick_x<-0.1 && !gamepad1.dpad_right && !gamepad1.dpad_down
                    &&!gamepad1.dpad_up && !gamepad1.dpad_left && !gamepad1.a && !gamepad1.b &&
                    !gamepad1.x && !gamepad1.y && (gamepad1.left_stick_x>-0.1 &&
                    gamepad1.left_stick_x<0.1) && (gamepad1.left_stick_y>-0.1 &&
                    gamepad1.left_stick_y<0.1)&& gamepad1.left_trigger==0)
            {
                motorFrontLeft.setPower(-gamepad1.right_trigger);
                motorBackLeft.setPower(gamepad1.right_trigger);
                motorFrontRight.setPower(gamepad1.right_trigger);
                motorBackRight.setPower(-gamepad1.right_trigger);
            }


            // DIAGONALS

            if (gamepad1.left_stick_y < -0.1 && !gamepad1.dpad_right &&
                    !gamepad1.dpad_up && !gamepad1.dpad_left
                    && !gamepad1.dpad_down && gamepad1.left_trigger == 0 &&
                    !gamepad1.a && !gamepad1.b && !gamepad1.x && !gamepad1.y)
            {

                if (gamepad1.left_stick_x > 0.1) //DIAGONAL FORWARD RIGHT
                {
                    motorFrontLeft.setPower(gamepad1.right_trigger);
                    motorBackLeft.setPower(0);
                    motorFrontRight.setPower(0);
                    motorBackRight.setPower(gamepad1.right_trigger);
                }

                else if (gamepad1.left_stick_x < -0.1)  //DIAGONAL FORWARD LEFT
                {
                    motorFrontLeft.setPower(0);
                    motorBackLeft.setPower(gamepad1.right_trigger);
                    motorFrontRight.setPower(gamepad1.right_trigger);
                    motorBackRight.setPower(0);
                }

                else //FALSE ALARM
                {
                    motorFrontLeft.setPower(0);
                    motorBackLeft.setPower(0);
                    motorFrontRight.setPower(0);
                    motorBackRight.setPower(0);
                }
            }

            if (gamepad1.left_stick_y > 0.1 && !gamepad1.dpad_right &&
                    !gamepad1.dpad_up && !gamepad1.dpad_left
                    && !gamepad1.dpad_down && gamepad1.left_trigger == 0 &&
                    !gamepad1.a && !gamepad1.b && !gamepad1.x && !gamepad1.y)
            {
                if (gamepad1.left_stick_x > 0.1) //DIAGONAL BACKWARD RIGHT
                {
                    motorFrontLeft.setPower(0);
                    motorBackLeft.setPower(-gamepad1.right_trigger);
                    motorFrontRight.setPower(-gamepad1.right_trigger);
                    motorBackRight.setPower(0);
                }

                else if (gamepad1.left_stick_x < -0.1) //DIAGONAL BACKWARD LEFT
                {
                    motorFrontLeft.setPower(-gamepad1.right_trigger);
                    motorBackLeft.setPower(0);
                    motorFrontRight.setPower(0);
                    motorBackRight.setPower(-gamepad1.right_trigger);
                }

                else //FALSE ALARM
                {
                    motorFrontLeft.setPower(0);
                    motorBackLeft.setPower(0);
                    motorFrontRight.setPower(0);
                    motorBackRight.setPower(0);
                }
            }


            idle();
        }
    }
}
