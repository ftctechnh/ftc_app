package org.firstinspires.ftc.team8745;
//1120

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by some guy "named" 8K Proggramming on 10/30/2016.
 */
@Autonomous(name="8k Auto NO MOVE Shoot Corner")
//@Disabled
public class A_NoMoveWithShooterNearCorner extends LinearOpMode {
    private static final int TICS_PER_REV =1120;

    private double WHEEL_DIAMETER = 4;

    DcMotor leftFRONT;
    DcMotor rightFRONT;
    DcMotor leftBACK;
    DcMotor rightBACK;

    DcMotor shooterLeft;
    DcMotor shooterRight;

    Servo shooterServo;

    public ElapsedTime runtime = new ElapsedTime();

    public int ticks = ticsForInches(60);

    final double kServoNullPosition = 0.83;
    final double kServoRange = 0.6;
    final double kShooterEnginePower = 0.7;


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
        shooterServo = hardwareMap.servo.get("shooter-servo");

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
        //servos in Up position
        shooterServo.setPosition(kServoNullPosition + kServoRange);

        runtime.reset();
    }

    public void waitNSeconds(int secondsToWait) {
        double startTime = runtime.time();
        while (runtime.time() - startTime < secondsToWait) {

        }
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

        // Shoot Loaded Balls
        shooterRight.setPower(kShooterEnginePower);
        shooterLeft.setPower(kShooterEnginePower);

        for (int i = 1; i <= 3; i++) {
            waitNSeconds(1);
            shooterServo.setPosition((kServoNullPosition + (-kServoRange)));
            waitNSeconds(1);
            shooterServo.setPosition(kServoNullPosition);
        }

    }
}
