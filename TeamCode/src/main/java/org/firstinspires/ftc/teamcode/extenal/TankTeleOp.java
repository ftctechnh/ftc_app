package org.firstinspires.ftc.teamcode.extenal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by inspirationteam on 11/10/2016.
 */
@TeleOp(name = "Pushbot: TankTeleOp", group = "Pushbot")
public class TankTeleOp extends OpMode {
    DcMotor leftMotor;
    DcMotor leftMotorback;
    DcMotor rightMotor;
    DcMotor rightMotorback;
    @Override
    public void init() {
        leftMotor = hardwareMap.dcMotor.get("left_motor");//get references to the hardware installed on the robot
        leftMotorback = hardwareMap.dcMotor.get("leftmotorback");//names of the motors
        rightMotor = hardwareMap.dcMotor.get("right_motor");
        rightMotorback = hardwareMap.dcMotor.get("right_motorback");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);
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
        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;

        //set the power of the motors with the game pad values
        leftMotor.setPower(leftY);
        leftMotorback.setPower(leftY);
        rightMotor.setPower(rightY);
        rightMotorback.setPower(rightY);
    }
    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
