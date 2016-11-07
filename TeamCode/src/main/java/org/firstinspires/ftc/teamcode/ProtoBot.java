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

    private DcMotor LT;
    private DcMotor LB;
    private DcMotor RT;
    private DcMotor RB;
    private DcMotor RS;
    private DcMotor LS;
    private DcMotor MC;
    private Servo C;
    private Servo BPL;
    private Servo BPR;
    private TouchSensor right_touch;
    private GyroSensor gyro;
    private double resetTime;
    private int cDist, lDist, dDist, tDist;

    public void init(){
        LT = hardwareMap.dcMotor.get("front");
        LB = hardwareMap.dcMotor.get("back");
        RT = hardwareMap.dcMotor.get("left");
        RB = hardwareMap.dcMotor.get("right");
        
        RS = hardwareMap.dcMotor.get("r_shoot");
        LS = hardwareMap.dcMotor.get("l_shoot");
        MC = hardwareMap.dcMotor.get("conveyor");

        C = hardwareMap.servo.get("collector");
        BPL = hardwareMap.servo.get("l_button");
        BPR = hardwareMap.servo.get("r_button");
        
        right_touch = hardwareMap.touchSensor.get("right_touch");
        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();

        LT.setDirection(DcMotor.Direction.REVERSE);
        RT.setDirection(DcMotor.Direction.REVERSE);
        RS.setDirection(DcMotor.Direction.REVERSE);
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
        cDist = RS.getCurrentPosition();
        dDist = cDist - lDist; 
        tDist += dDist;
        // motorspeed = dx/dt * (60 seconds/1 minute) * (1 rotation/1120 encoder degrees) = (rotations/minute)
        double motorSpeed = 60*tDist/(getRuntime() - resetTime)/1120;

        // Drivetrain controls
        if(gamepad1.left_trigger > .1 || gamepad1.right_trigger > .1){
            if(gamepad1.left_trigger > gamepad1.right_trigger){
                LT.setPower(gamepad1.left_trigger);
                RT.setPower(gamepad1.left_trigger);
                LB.setPower(-gamepad1.left_trigger);
                RB.setPower(-gamepad1.left_trigger);
            }else{
                LT.setPower(-gamepad1.right_trigger);
                RT.setPower(-gamepad1.right_trigger);
                LB.setPower(gamepad1.right_trigger);
                RB.setPower(gamepad1.right_trigger);
            }
        }else{
          LT.setPower(-gamepad1.left_stick_x);
          LB.setPower(-gamepad1.left_stick_x);
          RT.setPower(gamepad1.left_stick_y);
          RB.setPower(gamepad1.left_stick_y);
        }
        
        // Activates shooters
        if(gamepad1.a){
            RS.setPower(1);
            LS.setPower(1);
            MC.setPower(1);
        }else if(gamepad1.b){
            RS.setPower(-1);
            LS.setPower(-1);
            MC.setPower(-1);
        }else{
            RS.setPower(0);
            LS.setPower(0);
            MC.setPower(0);
        }

        // Activates collectors
        if(gamepad1.x){
            C.setPosition(1);
        }else if(gamepad1.y){
            C.setPosition(0);
        }else{
            C.setPosition(.5);
        }
        
        if(gamepad1.right_bumper){
            BPL.setPosition(0);
            BPR.setPosition(0);
        }else if(gamepad1.left_bumper){
            BPL.setPosition(1);
            BPR.setPosition(1);
        }else{
            BPL.setPosition(.5);
            BPR.setPosition(.5);
        }
        
        // Put telemetry here
        telemetry.addData("motor speed", motorSpeed);
        telemetry.addData("theta", theta);
    }
}
