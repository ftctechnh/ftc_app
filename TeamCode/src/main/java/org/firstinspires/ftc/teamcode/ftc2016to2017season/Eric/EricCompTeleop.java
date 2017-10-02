package org.firstinspires.ftc.teamcode.ftc2016to2017season.Eric;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by inspirationteam on 11/10/2016.
 */
@TeleOp(name = "#11183:Eric TeleOp Competition", group = "Robot")
@Disabled
public class EricCompTeleop extends OpMode {
    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;
    DcMotor intakeMotor;
    DcMotor flickerMotor;
    @Override
    public void init() {
        leftWheelMotorFront = hardwareMap.dcMotor.get("leftWheelMotorFront");
        leftWheelMotorBack = hardwareMap.dcMotor.get("leftWheelMotorBack");
        rightWheelMotorFront = hardwareMap.dcMotor.get("rightWheelMotorFront");
        rightWheelMotorBack = hardwareMap.dcMotor.get("rightWheelMotorBack");
        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");
        flickerMotor = hardwareMap.dcMotor.get("flickerMotor");

        /* lets reverse the direction of the right wheel motor*/
        rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);
    }
    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }


    @Override
    public void loop(){
        fourwheelmove();
        intake();
        flickershoot();
    }
    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    public void fourwheelmove(){
        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;

        //set the power of the motors with the game pad values
        leftWheelMotorFront.setPower(leftY);
        leftWheelMotorBack.setPower(leftY);
        rightWheelMotorFront.setPower(rightY);
        rightWheelMotorBack.setPower(rightY);
    }
    public void intake(){
        boolean intake = gamepad1.right_bumper;
        boolean outtake = gamepad1.left_bumper;

        if(intake){
            intakeMotor.setPower(1);
        }
        else if(outtake){
            intakeMotor.setPower(-1);
        }
        else{
            intakeMotor.setPower(0);
        }
    }
    public void flickershoot(){
        boolean intake = gamepad1.a;
        boolean outtake = gamepad1.x;

        if (intake) {
            flickerMotor.setPower(1);
        }
        else if(outtake) {
            flickerMotor.setPower(-1);
        }
        else {
            flickerMotor.setPower(0);
        }
        }
    }

