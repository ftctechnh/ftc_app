package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;


/**
 * Created by Team 10464 on 9/21/16.
 */
@Autonomous(name="Protobot Tank", group="Protobot")
public class ProtoBot extends OpMode {

    private DcMotor LT;
    private DcMotor LB;
    private DcMotor RT;
    private DcMotor RB;
    private DcMotor RS;
    private DcMotor LS;
    private Servo C;
    private TouchSensor right_touch;
    private double resetTime;
    private int cDist, lDist, dDist, tDist;

    public void init(){
        LT = hardwareMap.dcMotor.get("l_front");
        LB = hardwareMap.dcMotor.get("l_back");
        RT = hardwareMap.dcMotor.get("r_front");
        RB = hardwareMap.dcMotor.get("r_back");
        
        RS = hardwareMap.dcMotor.get("r_shoot");
        LS = hardwareMap.dcMotor.get("l_shoot");
        
        C = hardwareMap.servo.get("collector");
        
        right_touch = hardwareMap.touchSensor.get("right_touch");

        LT.setDirection(DcMotor.Direction.REVERSE);
        RT.setDirection(DcMotor.Direction.REVERSE);
        RS.setDirection(DcMotor.Direction.REVERSE);
    }
    public void loop(){
        // Lets do some math :)
        
        lDist = cDist;
        cDist = (
                  RS.getCurrentPosition() +
                  LS.getCurrentPosition()
                ) / 2;
        dDist = cDist - lDist;
        tDist += dDist;

        LT.setPower(gamepad1.left_stick_x);
        LB.setPower(gamepad1.left_stick_x);

        RT.setPower(gamepad1.left_stick_y);
        RB.setPower(gamepad1.left_stick_y);
        if(gamepad1.left_bumper){
          resetTime = getRuntime();
          tDist = 0;
        }

        if(Math.abs(gamepad1.left_stick_y) < .1 && Math.abs(gamepad1.left_stick_x) < .1){
            LT.setPower(0);
            RT.setPower(0);
            LB.setPower(0);
            RB.setPower(0);
        }
        
        if(gamepad1.left_trigger > .1 || gamepad1.right_trigger > 1){
            if(gamepad1.left_trigger > gamepad1.right_trigger){
                LT.setPower(gamepad1.left_trigger);
                RT.setPower(gamepad1.left_trigger);
                LB.setPower(gamepad1.left_trigger);
                RB.setPower(gamepad1.left_trigger);
            }else{
                LT.setPower(-gamepad1.right_trigger);
                RT.setPower(-gamepad1.right_trigger);
                LB.setPower(-gamepad1.right_trigger);
                RB.setPower(-gamepad1.right_trigger);
            }
        }
        
        if(gamepad1.a){
            RS.setPower(1);
            LS.setPower(-1);
        }else if(gamepad1.b){
            RS.setPower(1);
            LS.setPower(-1);
        }else{
            RS.setPower(0);
            LS.setPower(0);
        }
        if(gamepad1.x){
            C.setPosition(1);
        }else if(gamepad1.y){
            C.setPosition(0);
        }else{
            C.setPosition(.5);
        }
        
        telemetry.addData("Left Stick Y", gamepad1.left_stick_y);
        telemetry.addData("Right Stick Y", gamepad1.right_stick_y);
        telemetry.addData("motor speed", lDist/(getRuntime()-resetTime));
    }
}
