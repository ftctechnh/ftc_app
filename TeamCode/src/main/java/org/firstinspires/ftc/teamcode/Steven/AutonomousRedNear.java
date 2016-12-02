package org.firstinspires.ftc.teamcode.Steven;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by inspirationteam on 12/1/2016.
 */
@Autonomous(name = "RedAutonomousNear", group = "Pushbot")
@Disabled//since it isnt finished i disabled it for now;
public class AutonomousRedNear extends OpMode {

    //final double wheelGearRatio;
    final double axleLength=26.67;
    final double wheelCircum=29;
    final double encodervalueperwheelrotation = (56./24)*1440;


    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;
    DcMotor ballCollectorMotor;
    DcMotor ballShooterMotor;

    @Override
    public void init(){
        leftWheelMotorFront = hardwareMap.dcMotor.get("leftWheelMotorFront");
        leftWheelMotorBack = hardwareMap.dcMotor.get("leftWheelMotorBack");
        rightWheelMotorFront = hardwareMap.dcMotor.get("rightWheelMotorFront");
        rightWheelMotorBack = hardwareMap.dcMotor.get("rightWheelMotorBack");

        ballCollectorMotor = hardwareMap.dcMotor.get("ballCollectorMotor");
        ballShooterMotor = hardwareMap.dcMotor.get("ballShooterMotor");

            /* lets reverse the direction of the right wheel motor*/
        rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);

        //set mode so that the motors use encoder
        leftWheelMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftWheelMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightWheelMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightWheelMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void init_loop(){

    }

    @Override
    public void start(){

    }

    @Override
    public void loop(){

    }

    @Override
    public void stop(){

    }

    public void move_motors(double right_dist, double left_motor, double power){


    }
}
