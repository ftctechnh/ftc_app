package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
//testing push commands
/**
 * Created by Dolphinos on 11/2/2017.
 */
@TeleOp(name = "Mecanum", group = "DriveTrain")
public class MecanumDriveTrain extends LinearOpMode{
    //Declare variable such as motors here, example below
    // private DcMotor motorLeft;
    private DcMotor motorfrontleft;
    private DcMotor motorfrontright;
    private DcMotor motorbackleft;
    private DcMotor motorbackright;
    @Override
    public void runOpMode()  throws InterruptedException
    {

        motorfrontleft = hardwareMap.dcMotor.get("motorfrontLeft");
        motorfrontright = hardwareMap.dcMotor.get("motorfrontright");
        motorbackleft = hardwareMap.dcMotor.get("motorbackleft");
        motorbackright = hardwareMap.dcMotor.get("motorbackright");
        //Declare hardwareMap here, example below
        //motorLeft = hardwareMap.dcMotor.get("motorLeft");

        //Include any code to run only once here
        waitForStart();

        while(opModeIsActive()){
        if(gamepad1.left_stick_x<-.7) {
            //strafe left
            motorfrontleft.setDirection(DcMotorSimple.Direction.REVERSE);
            motorfrontright.setDirection(DcMotorSimple.Direction.FORWARD);
            motorbackleft.setDirection(DcMotorSimple.Direction.FORWARD);
            motorbackright.setDirection(DcMotorSimple.Direction.REVERSE);
            motorfrontleft.setPower(-gamepad1.left_stick_x);
            motorfrontright.setPower(-gamepad1.left_stick_x);
            motorbackleft.setPower(-gamepad1.left_stick_x);
            motorbackright.setPower(-gamepad1.left_stick_x);
        }//end if
            if(gamepad1.left_stick_x>.7) {
                //strafe right
                motorfrontleft.setDirection(DcMotorSimple.Direction.FORWARD);
                motorfrontright.setDirection(DcMotorSimple.Direction.REVERSE);
                motorbackleft.setDirection(DcMotorSimple.Direction.REVERSE);
                motorbackright.setDirection(DcMotorSimple.Direction.FORWARD);
                motorfrontleft.setPower(gamepad1.left_stick_x);
                motorfrontright.setPower(gamepad1.left_stick_x);
                motorbackleft.setPower(gamepad1.left_stick_x);
                motorbackright.setPower(gamepad1.left_stick_x);
            }//end if
                if(gamepad1.left_stick_y>.7) {
                    //forward
                    motorfrontleft.setDirection(DcMotorSimple.Direction.FORWARD);
                    motorfrontright.setDirection(DcMotorSimple.Direction.FORWARD);
                    motorbackleft.setDirection(DcMotorSimple.Direction.FORWARD);
                    motorbackright.setDirection(DcMotorSimple.Direction.FORWARD);
                    motorfrontleft.setPower(gamepad1.left_stick_y);
                    motorfrontright.setPower(gamepad1.left_stick_y);
                    motorbackleft.setPower(gamepad1.left_stick_y);
                    motorbackright.setPower(gamepad1.left_stick_y);
                }//end if
                    if(gamepad1.left_stick_y<-.7) {
                        //backward
                        motorfrontleft.setDirection(DcMotorSimple.Direction.REVERSE);
                        motorfrontright.setDirection(DcMotorSimple.Direction.REVERSE);
                        motorbackleft.setDirection(DcMotorSimple.Direction.REVERSE);
                        motorbackright.setDirection(DcMotorSimple.Direction.REVERSE);
                        motorfrontleft.setPower(-gamepad1.left_stick_y);
                        motorfrontright.setPower(-gamepad1.left_stick_y);
                        motorbackleft.setPower(-gamepad1.left_stick_y);
                        motorbackright.setPower(-gamepad1.left_stick_y);
                    }//end if
                            if(gamepad1.right_stick_x>.7) {
                            //turn right
                            motorfrontleft.setDirection(DcMotorSimple.Direction.REVERSE);
                            motorfrontright.setDirection(DcMotorSimple.Direction.FORWARD);
                            motorbackleft.setDirection(DcMotorSimple.Direction.REVERSE);
                            motorbackright.setDirection(DcMotorSimple.Direction.FORWARD);
                            motorfrontleft.setPower(gamepad1.right_stick_x);
                            motorfrontright.setPower(gamepad1.right_stick_x);
                            motorbackleft.setPower(gamepad1.right_stick_x);
                            motorbackright.setPower(gamepad1.right_stick_x);
                              }//end if

                                        if(gamepad1.right_stick_x<-.7) {
                                            //turn left
                                            motorfrontleft.setDirection(DcMotorSimple.Direction.FORWARD);
                                            motorfrontright.setDirection(DcMotorSimple.Direction.REVERSE);
                                            motorbackleft.setDirection(DcMotorSimple.Direction.FORWARD);
                                            motorbackright.setDirection(DcMotorSimple.Direction.REVERSE);
                                            motorfrontleft.setPower(-gamepad1.right_stick_x);
                                            motorfrontright.setPower(-gamepad1.right_stick_x);
                                            motorbackleft.setPower(-gamepad1.right_stick_x);
                                            motorbackright.setPower(-gamepad1.right_stick_x);
                                        }//end if
                                            if(gamepad1.right_stick_x<.7&& gamepad1.right_stick_x>0 && gamepad1.right_stick_y>0){
                                                //diagonal up right
                                                motorfrontleft.setDirection(DcMotorSimple.Direction.FORWARD);
                                                motorbackright.setDirection(DcMotorSimple.Direction.FORWARD);
                                                motorfrontleft.setPower(gamepad1.right_stick_y);
                                                motorbackright.setPower(gamepad1.right_stick_y);
                                            }//end if
                                                if(gamepad1.right_stick_x>-.7&& gamepad1.right_stick_x<0 && gamepad1.right_stick_y>0){
                                                    //diagonal up left
                                                    motorfrontright.setDirection(DcMotorSimple.Direction.FORWARD);
                                                    motorbackleft.setDirection(DcMotorSimple.Direction.FORWARD);
                                                    motorfrontright.setPower(gamepad1.right_stick_y);
                                                    motorbackleft.setPower(gamepad1.right_stick_y);
                                                }//end if
                                                    if(gamepad1.right_stick_x>-.7&& gamepad1.right_stick_x<0 && gamepad1.right_stick_y<0){
                                                        //diagonal down left
                                                        motorfrontleft.setDirection(DcMotorSimple.Direction.REVERSE);
                                                        motorbackright.setDirection(DcMotorSimple.Direction.REVERSE);
                                                        motorfrontleft.setPower(-gamepad1.right_stick_y);
                                                        motorbackright.setPower(-gamepad1.right_stick_y);
                                                    }//end if
                                                        if(gamepad1.right_stick_x<.7&& gamepad1.right_stick_x>0 && gamepad1.right_stick_y<0){
                                                            //diagonal down right
                                                            motorfrontright.setDirection(DcMotorSimple.Direction.REVERSE);
                                                            motorbackleft.setDirection(DcMotorSimple.Direction.REVERSE);
                                                            motorfrontright.setPower(-gamepad1.right_stick_y);
                                                            motorbackleft.setPower(-gamepad1.right_stick_y);

            //Include commands to run on controller presses here, example below
            //motorLeft.setPower(-gamepad1.left_stick_y);

            idle();

        }//end opmodeisactive


    }//end runopmode
//Methods below


}//end class
