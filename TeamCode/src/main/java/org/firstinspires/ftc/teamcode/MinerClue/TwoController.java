package org.firstinspires.ftc.teamcode.MinerClue;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by JL on 9/25/2017.
 */
@TeleOp(name = "Two User", group = "TeleOp")
public class TwoController extends LinearOpMode {
    private Gyroscope imu;
    private DcMotor rightDriveMotor;
    private DcMotor leftDriveMotor;
    private DcMotor liftMotor;
    private CRServo clampServo;
    private Servo colorServo;
    float precision = 1;

    @Override
    public void runOpMode() {
        imu = hardwareMap.get(Gyroscope.class, "imu");
        //rightmotor call and reverse
        rightDriveMotor = hardwareMap.get(DcMotor.class, "Rmotor");
        rightDriveMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        //left motor call
        leftDriveMotor = hardwareMap.get(DcMotor.class, "Lmotor");
        //lift motor call
        liftMotor = hardwareMap.get (DcMotor.class, "belt1");
        liftMotor = hardwareMap.get (DcMotor.class, "elevator");
        //grabber call
        clampServo = hardwareMap.get(CRServo.class, "servoTest");
        //Color Servo
        colorServo = hardwareMap.get(Servo.class, "colorServo");


        telemetry.addData("Status", "Initialized");;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // run until the end of the match (driver presses STOP)

        while (opModeIsActive()) {
            if(gamepad1.right_trigger  > 0 && gamepad1.left_trigger > 0)
                precision = 2/5;
            else
                precision = 1;

            rightDriveMotor.setPower(-gamepad1.right_stick_y * precision);
            leftDriveMotor.setPower(-gamepad1.left_stick_y * precision);
            telemetry.addData("MotorPower", "R: " + -gamepad1.right_stick_y + " , L: " + -gamepad1.left_stick_y);

            if(gamepad1.a && colorServo.getPosition() != 1.0)
                colorServo.setPosition(1.0);

            if(gamepad2.right_stick_y > 0)
                liftMotor.setPower(1);
            if(gamepad2.right_stick_y < 0)
                liftMotor.setPower(-1);
            if(gamepad2.right_stick_y == 0)
                liftMotor.setPower(0);

            if(gamepad2.left_stick_y > 0)
                liftMotor.setPower(0.3);
            if(gamepad2.left_stick_y < 0)
                liftMotor.setPower(-0.3);
            if(gamepad2.left_stick_y == 0)
                liftMotor.setPower(0);

            if(gamepad2.right_trigger > 0)
                clampServo.setPower(1);
            if(gamepad2.left_trigger > 0)
                clampServo.setPower(-1);
            if(gamepad2.right_trigger == 0 && gamepad2.left_trigger == 0)
                clampServo.setPower(0);

            if(gamepad2.right_bumper)
                clampServo.setPower(0.3);
            if(gamepad2.left_bumper)
                clampServo.setPower(-0.3);
            if(!gamepad2.right_bumper && !gamepad2.left_bumper)
                clampServo.setPower(0);


        }
    }
}