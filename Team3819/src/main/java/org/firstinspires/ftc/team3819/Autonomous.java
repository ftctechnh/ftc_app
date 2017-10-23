package org.firstinspires.ftc.team3819;


import com.qualcomm.hardware.motors.TetrixMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by 200344191 on 10/14/2017.
 */
@TeleOp(name="test", group="test")
public class Autonomous extends LinearOpMode {
    public DcMotor topleft;
    public DcMotor topRight;
    public DcMotor bottomRight;
    public DcMotor bottomLeft;

    @Override
    public void runOpMode() throws InterruptedException {
        topleft=hardwareMap.dcMotor.get("topLeft");
        topRight=hardwareMap.dcMotor.get("topRight");
        bottomLeft=hardwareMap.dcMotor.get("bottomLeft");
        bottomRight=hardwareMap.dcMotor.get("bottomRight");
        while(opModeIsActive()){
            topleft.setPower(-gamepad1.left_stick_y);
            topRight.setPower(-gamepad1.right_stick_y);
        }
    }




    public void allForward(double speed,int seconds){

            topleft.setPower(-1*speed);
            topRight.setPower(speed);//inverted
            bottomRight.setPower(-1*speed);
            bottomLeft.setPower(-1*speed);


    }
    public void allBackwards(double speed,int seconds){

            topleft.setPower(1 * speed);
            topRight.setPower(-speed);
            bottomRight.setPower(1 * speed);
            bottomLeft.setPower(1 * speed);

    }
    public void pointTurnToRight(double speed){
        bottomLeft.setPower(0);
        topleft.setPower(speed);
        bottomRight.setPower(-speed);
        topRight.setPower(0);//inverted
    }

    public void pointTurnToLeft(double speed){
        bottomLeft.setPower(-speed);
        topleft.setPower(0);
        bottomRight.setPower(0);
        topRight.setPower(-speed);//inverted
    }
    public void turnRight(double speed){
        topRight.setPower(0);//inverted
        bottomRight.setPower(0);
        topleft.setPower(speed);
        bottomLeft.setPower(speed);
    }
    public void turnLeft(double speed){
        topRight.setPower(-speed);//inverted
        bottomRight.setPower(speed);
        topleft.setPower(0);
        bottomLeft.setPower(0);
    }
}
