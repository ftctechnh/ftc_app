package org.firstinspires.ftc.team8745;
//1120

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by some guy "named" 8K Proggramming on 10/30/2016.
 */
@Autonomous(name="8k Autonomous shooter 1")
//@Disabled
public class AutonomousWithShooter8K extends LinearOpMode {
    private static final int TICS_PER_REV =1120;

    private double WHEEL_DIAMETER = 4;

    DcMotor leftFRONT;
    DcMotor rightFRONT;
    DcMotor leftBACK;
    DcMotor rightBACK;

    DcMotor shooterLeft;
    DcMotor shooterRight;

    Servo lifter;

    public ElapsedTime runtime = new ElapsedTime();

    public  int ticks = ticsForInches(60);


    private int ticsForInches(double inches){
        return (int)((inches*TICS_PER_REV)/(Math.PI*WHEEL_DIAMETER));
    }




    // 4 Inches
    public void initmybot() {
        //Front Motors
        leftFRONT = hardwareMap.dcMotor.get("motor-left");
        rightFRONT = hardwareMap.dcMotor.get("motor-right");

        //Back Motors
        leftBACK = hardwareMap.dcMotor.get("motor-leftBACK");
        rightBACK = hardwareMap.dcMotor.get("motor-rightBACK");

       //Shooter Motors
        shooterLeft = hardwareMap.dcMotor.get("shooter-left");
        shooterRight = hardwareMap.dcMotor.get("shooter-right");

        //servos
        lifter = hardwareMap.servo.get("shooter-servo");
        //Running with encoder
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFRONT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBACK.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFRONT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBACK.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //stopping with Encoder
        rightFRONT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBACK.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFRONT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBACK.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //setting direction
        rightFRONT.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBACK.setDirection(DcMotorSimple.Direction.FORWARD);
        leftFRONT.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBACK.setDirection(DcMotorSimple.Direction.REVERSE);
        //Shooter directions
        shooterRight.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //servos
        lifter.setPosition(1);

        runtime.reset();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initmybot();
        waitForStart();
        /*
        if (!opModeIsActive()){
            stop();
            return;
        }
        */
        shooterRight.setPower(1);
        shooterLeft.setPower(1);

        lifter.setPosition(.75);



        if (leftFRONT.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
            //Run to posiiton
            rightFRONT.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBACK.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftFRONT.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBACK.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //Our ticks for the motors

            rightFRONT.setTargetPosition(ticks);
            rightBACK.setTargetPosition(ticks);
            leftFRONT.setTargetPosition(ticks);
            leftBACK.setTargetPosition(ticks);

        }



        //Waiting for robot to reach position.
        while (true) {
            telemetry.addData("Ticks:",rightFRONT.getCurrentPosition());
            telemetry.addData("Target:",rightFRONT.getTargetPosition());
            telemetry.addData("Time elapsed:", runtime);
            if (runtime.time() > 10) {
                rightFRONT.setPower(.5);
                rightBACK.setPower(.5);
                leftFRONT.setPower(.5);
                leftBACK.setPower(.5);
            }

            if (leftFRONT.getCurrentPosition() > ticks){
                break;
            }

        }
    }
}
