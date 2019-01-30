package org.firstinspires.ftc.teamcode;

import android.hardware.Sensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp (name = "DriverControl" , group = "testOp")
//@Disabled


public class DriverControl extends LinearOpMode
{
    //DcMotors
    private DcMotor frontLeft;      //1     Hub1 P0
    private DcMotor rearLeft;       //2     Hub1 P2
    private DcMotor frontRight;     //3     Hub1 P1
    private DcMotor rearRight;      //4     Hub1 P3
    private DcMotor liftMotor;      //5     Hub2 P0
    private DcMotor flipperMotor;       //6     Hub2 P1
    private DcMotor pulleyMotor;    //7     Hub2 P3
    private DcMotor revLift;        //8     Hub2 P2

    //Servos
    private Servo armServo;
    private Servo depositServo;     //2     Hub2 P?


    @Override
    public void runOpMode () throws InterruptedException

    {
//Declare DcMotors
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        rearLeft = hardwareMap.dcMotor.get("rearLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        rearRight = hardwareMap.dcMotor.get("rearRight");
        liftMotor = hardwareMap.dcMotor.get("liftMotor");
        flipperMotor = hardwareMap.dcMotor.get("flipprMotor");
        pulleyMotor = hardwareMap.dcMotor.get("pulleyMotor");
        revLift = hardwareMap.dcMotor.get("revLift");
//Declare Servos
        armServo = hardwareMap.servo.get("armServo");
        depositServo = hardwareMap.servo.get("depositServo");
//Declare DcMotor Directions
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        rearLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        liftMotor.setDirection(DcMotor.Direction.REVERSE);
        flipperMotor.setDirection(DcMotor.Direction.FORWARD);
        pulleyMotor.setDirection(DcMotor.Direction.FORWARD);
        revLift.setDirection(DcMotor.Direction.FORWARD);
//Declare Servo Directions
        armServo.setDirection(Servo.Direction.FORWARD);
        depositServo.setDirection(Servo.Direction.FORWARD);


//Declare Mecanum Drive Variables
        double drive;
        double strafe;
        double rotate;

        double front_left;
        double rear_left;
        double front_right;
        double rear_right;

//Declare Speed Variables(0 = slow)(1 = fast)
        int speedState = 1;
        double fast = 1.0;
        double slow = 0.6;

//Declare Direction Variable(s)
        int direction = 1;

//Declare Continuous Servo Variables
        int flipperState = 0;
        boolean buttonState = false;
        {
            waitForStart();
            while (opModeIsActive())
            {
//Speed
                if(gamepad1.x)
                {
                    speedState = 1;
                }
                else if(gamepad1.a)
                {
                    speedState = 0;
                }

                if(gamepad1.right_bumper)
                {
                    direction = 1;
                }
                else if(gamepad1.left_bumper)
                {
                    direction = -1;
                }

//Declare Values to Mecanum Variables
                drive = gamepad1.right_stick_y * direction;
                strafe = gamepad1.right_stick_x * direction;
                rotate = gamepad1.left_stick_x * direction;
//Mecanum direction calculation
                if(direction == -1) {
                    front_left = drive - strafe + rotate;
                    rear_left = drive + strafe + rotate;
                    front_right = drive + strafe - rotate;
                    rear_right = drive - strafe - rotate;
                }

                else
                {
                    front_left = drive - strafe - rotate;
                    rear_left = drive + strafe - rotate;
                    front_right = drive + strafe + rotate;
                    rear_right = drive - strafe + rotate;
                }
//-----------------------------------Gamepad 1 Start------------------------------------------------
//Mecanum Drive
                if(speedState == 1)
                {
                    frontLeft.setPower(limit(front_left)* fast);
                    rearLeft.setPower(limit(rear_left)* fast);
                    frontRight.setPower(limit(front_right)* fast);
                    rearRight.setPower(limit(rear_right)* fast);
                }
                else
                {
                    frontLeft.setPower(limit(front_left)* slow);
                    rearLeft.setPower(limit(rear_left)* slow);
                    frontRight.setPower(limit(front_right)* slow);
                    rearRight.setPower(limit(rear_right)* slow);
                }

//if(gamepad1.dpad_up) {
//    armServo.setPosition(0);
//}
//else if(gamepad1.dpad_down) {
//    armServo.setPosition(0.7);
//}
//------------------------------------Gamepad 1 End-------------------------------------------------


//-----------------------------------Gamepad 2 Start------------------------------------------------

//Flipper Servo
                if(gamepad2.x)
                {
                    flipperMotor.setPower(1);
                }
                else if(gamepad2.b)
                {
                    flipperMotor.setPower(-1);
                }
                else
                {
                    flipperMotor.setPower(0);
                }



//                if(gamepad2.x && !buttonState)
//                {
//                    if(flipperState == 1)
//                    {
//                        flipperState = 0;
//                    }
//                    else
//                    {
//                        flipperState = 1;
//                    }
//                    buttonState = true;
//                }
//                else if(!gamepad2.x && buttonState)
//                {
//                    buttonState = false;
//                }
//
//                if(flipperState == 1)
//                {
//                    flipperServo.setPosition(1);
//                }
//                else
//                {
//                    flipperServo.setPosition(0.5);
//                }
//Telemetry
//                telemetry.addData("buttonState",buttonState);
//                telemetry.addData("flipperState",flipperState);
//                telemetry.update();


//Deposit Servo
                if(gamepad2.right_bumper)
                {
                    depositServo.setPosition(180);
                }
                //Forward
                else if(gamepad2.left_bumper)
                {
                    depositServo.setPosition(0);
                }
                else
                {

                }
//Cascade Lift
                if(gamepad2.dpad_up)
                {
                    liftMotor.setPower(1);
                }
                else if(gamepad2.dpad_down)
                {
                    liftMotor.setPower(-1);
                }
                else
                {
                    liftMotor.setPower(0);
                }
//Pulley Motor
                pulleyMotor.setPower(gamepad2.right_stick_y);
//Rev Lift
                revLift.setPower(gamepad2.left_stick_x);
//------------------------------------Gamepad 2 End-------------------------------------------------
                idle();
            }
        }
    }
    public double limit(double number)
    {
        if(number < -1.0)
        {
            return -1.0;
        }
        else if(number > 1)
        {
            return 1;
        }
        else
        {
            return number;
        }
    }
}
