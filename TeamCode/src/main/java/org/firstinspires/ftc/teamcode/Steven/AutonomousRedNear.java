package org.firstinspires.ftc.teamcode.Steven;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by inspirationteam on 12/1/2016.
 */
@Autonomous(name = "RedAutonomousNear", group = "Pushbot")
@Disabled//since it isnt finished i disabled it for now;
public class AutonomousRedNear extends OpMode {


    final double axleLength=26.67;
    final double wheelCircum=29;
    final double ticksperrev = 757;// 1440;//number of ticks the encoder has per revolution
    final double wheelgearratio = (56./24);

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

    public void move_motors(double right_dist, double left_dist, double power){
        //leftWheelMotorFront.setMode(DcMotor.RunMode.RUN_TO_POSITION); lets you set a target position
        //isBusy() returns true when the motors are still running to a certain position
        //gear ratio =  # of teeth of wheel attached to motor/ # of teeth of wheel attached to wheel
        //# of rotations of motor = (distance/Circumference of wheel)* gear ratio
        //# of ticks = # of rotations of motor * (ticks per rotation)

        //reset encoder value to 0
        rightWheelMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheelMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftWheelMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftWheelMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //calculate the encoder position that correlates to the wanted distance
        double rotright = ((right_dist/wheelCircum)*wheelgearratio) * ticksperrev;
        double rotleft =  ((left_dist/wheelCircum)*wheelgearratio) * ticksperrev;


        rightWheelMotorFront.setTargetPosition((int) rotright);
        rightWheelMotorBack.setTargetPosition((int) rotright);
        leftWheelMotorFront.setTargetPosition((int) rotleft);
        leftWheelMotorBack.setTargetPosition((int) rotleft);

        //set mode so that you can run to the target position
        rightWheelMotorFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightWheelMotorBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);;
        leftWheelMotorFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftWheelMotorBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while(rightWheelMotorFront.isBusy() && rightWheelMotorBack.isBusy() && leftWheelMotorFront.isBusy() && leftWheelMotorBack.isBusy()){
            //wait until target position is reached
        }

    }


}
