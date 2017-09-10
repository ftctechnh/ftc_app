package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;


/**
 * Created by Team 10464 on 9/21/16.
 */
@TeleOp(name="Protobot Tank", group="Protobot")
public class ProtoBot extends OpMode {

    private DcMotor motorUp;
    private DcMotor motorDown;
    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor motorRightShooter;
    private DcMotor motorLeftShooter;
    private DcMotor motorConveyer;
    private Servo servoCollector;
    private Servo servoLeftButton;
    private Servo servoRightButton;
    private TouchSensor touchRight;
    private GyroSensor gyro;
    private double resetTime;
    private int cDist, lDist, dDist, tDist;

    public void init(){
        motorUp = hardwareMap.dcMotor.get("front");
        motorDown = hardwareMap.dcMotor.get("back");
        motorLeft = hardwareMap.dcMotor.get("left");
        motorRight = hardwareMap.dcMotor.get("right");
        
        motorRightShooter = hardwareMap.dcMotor.get("r_shoot");
        motorLeftShooter = hardwareMap.dcMotor.get("l_shoot");
        motorConveyer = hardwareMap.dcMotor.get("conveyor");

        servoCollector = hardwareMap.servo.get("collector");
        servoLeftButton = hardwareMap.servo.get("l_button");
        servoRightButton = hardwareMap.servo.get("r_button");
        
        touchRight = hardwareMap.touchSensor.get("right_touch");
        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();

        motorUp.setDirection(DcMotor.Direction.REVERSE);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRightShooter.setDirection(DcMotor.Direction.REVERSE);
    }
    public void loop(){
        int theta = gyro.getHeading();
        // Used to get spin speed of shooting motors
        // lDist - last read encoder value
        // cDist - current encoder value
        // dDist - encoder degrees between lDist and cDist (distance traveled)
        // tDist - total distance traveled since we reset
        // resetTime - time since reset
        lDist = cDist;
        cDist = motorRightShooter.getCurrentPosition();
        dDist = cDist - lDist; 
        tDist += dDist;
        // motorspeed = dx/dt * (60 seconds/1 minute) * (1 rotation/1120 encoder degrees) = (rotations/minute)
        double motorSpeed = 60*tDist/(getRuntime() - resetTime)/1120;

        // Drivetrain controls
        if(gamepad1.left_trigger > .1 || gamepad1.right_trigger > .1){
            if(gamepad1.left_trigger > gamepad1.right_trigger){
                motorUp.setPower(gamepad1.left_trigger);
                motorLeft.setPower(gamepad1.left_trigger);
                motorDown.setPower(-gamepad1.left_trigger);
                motorRight.setPower(-gamepad1.left_trigger);
            }else{
                motorUp.setPower(-gamepad1.right_trigger);
                motorLeft.setPower(-gamepad1.right_trigger);
                motorDown.setPower(gamepad1.right_trigger);
                motorRight.setPower(gamepad1.right_trigger);
            }
        }else{
          motorUp.setPower(-gamepad1.left_stick_x);
          motorDown.setPower(-gamepad1.left_stick_x);
          motorLeft.setPower(gamepad1.left_stick_y);
          motorRight.setPower(gamepad1.left_stick_y);
        }
        
        // Activates shooters
        if(gamepad1.b){
            motorRightShooter.setPower(1);
            motorLeftShooter.setPower(1);
            motorConveyer.setPower(1);
        }else if(gamepad1.a){
            motorRightShooter.setPower(-1);
            motorLeftShooter.setPower(-1);
            motorConveyer.setPower(-1);
        }else{
            motorRightShooter.setPower(0);
            motorLeftShooter.setPower(0);
            motorConveyer.setPower(0);
        }

        // Activates collectors
        if(gamepad1.x){
            servoCollector.setPosition(1);
        }else if(gamepad1.y){
            servoCollector.setPosition(0);
        }else{
            servoCollector.setPosition(.5);
        }
        
        if(gamepad1.right_bumper){
            servoLeftButton.setPosition(0);
            servoRightButton.setPosition(0);
        }else if(gamepad1.left_bumper){
            servoLeftButton.setPosition(1);
            servoRightButton.setPosition(1);
        }else{
            servoLeftButton.setPosition(.5);
            servoRightButton.setPosition(.5);
        }
        
        // Put telemetry here
        telemetry.addData("motor speed", motorSpeed);
        telemetry.addData("theta", theta);
    }
}
