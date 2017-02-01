package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;


/**
 * Created by Team 10464 on 9/21/16.
 */
@TeleOp(name="Oriented Protobot Tank", group="Protobot")
public class OrientedProtoBot extends OpMode {

    private DcMotor motorUp;
    private DcMotor motorDown;
    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor motorRightShooter;
    private DcMotor motorLeftShooter;
    private DcMotor motorConveyer;
    private DcMotor motorCap;
    private Servo servoCollector;
    private Servo servoCapSqueeze;
    private Servo servoCapRelease;
    private Servo servoBeaconDeploy;
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
        motorCap = hardwareMap.dcMotor.get("cap_ball");

        servoCollector = hardwareMap.servo.get("collector");
        servoLeftButton = hardwareMap.servo.get("l_button");
        servoRightButton = hardwareMap.servo.get("r_button");
        servoCapSqueeze = hardwareMap.servo.get("cap_squeeze");
        servoCapRelease = hardwareMap.servo.get("cap_release");
        servoBeaconDeploy = hardwareMap.servo.get("b_servo");

        touchRight = hardwareMap.touchSensor.get("right_touch");
        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();

        motorUp.setDirection(DcMotor.Direction.REVERSE);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRightShooter.setDirection(DcMotor.Direction.REVERSE);
    }
    public void loop(){
        int heading = gyro.getHeading();
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
        }else { // Sets robot movement vector independent of robot heading.
            // Power coefficient
            double P = ((Math.abs(gamepad1.left_stick_y) + Math.abs(gamepad1.left_stick_x) / 2));
            // Robot heading
            double H = (heading * Math.PI) / 180;
            // heading of sticks
            double Ht = (Math.PI - Math.atan2(gamepad1.left_stick_x, gamepad1.left_stick_y));

            motorUp.setPower(P * Math.sin(H - Ht));
            motorDown.setPower(P * Math.sin(H - Ht));
            motorLeft.setPower(-P * Math.cos(H - Ht));
            motorRight.setPower(-P * Math.cos(H - Ht));
        }

        // Activates collectors
        if(gamepad2.x){
            servoCollector.setPosition(1);
        }else if(gamepad2.y){
            servoCollector.setPosition(0);
        }else{
            servoCollector.setPosition(.5);
        }

        // GAME PAD 2 CODE
        // Activates shooters
        if(gamepad2.left_trigger > .1){
            motorRightShooter.setPower(.33);
            motorLeftShooter.setPower(.33);
        }else if(gamepad2.right_trigger > .65){
            motorRightShooter.setPower(-.33);
            motorLeftShooter.setPower(-.33);
        }else{
            motorRightShooter.setPower(0);
            motorLeftShooter.setPower(0);
        }

        // Activates Conveyer
        if(gamepad2.a){
            motorConveyer.setPower(1);
        }
        else if(gamepad2.b){
            motorConveyer.setPower(-1);
        }
        else{
            motorConveyer.setPower(0);
        }
        // Cap Ball code
        if(gamepad2.dpad_up) {
            motorCap.setPower(1);
        } else if (gamepad2.dpad_down) {
            motorCap.setPower(-1);
        }else{
            motorCap.setPower(0);
        }
        if(gamepad2.right_bumper){
            servoCapSqueeze.setPosition(1);
        } else if(gamepad2.left_bumper) {
            servoCapSqueeze.setPosition(0);
        }
        if(gamepad2.dpad_right){
            servoCapRelease.setPosition(1);
        }else if(gamepad2.dpad_left){
            servoCapRelease.setPosition(0);
        }

        if(gamepad1.left_stick_button){
            gyro.calibrate();
        }
        
        // Put telemetry here
        telemetry.addData("motor speed", motorSpeed);
        telemetry.addData("theta", heading);
    }
}
