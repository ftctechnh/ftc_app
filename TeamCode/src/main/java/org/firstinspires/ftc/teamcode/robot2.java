package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by jxfio on 11/17/2017.
 */

@TeleOp(name="robot2", group="robot2")
public class robot2 extends LinearOpMode {

    // Declare OpMode members.
    private DcMotor Left;
    private DcMotor Right;
    private DcMotor Tower;
    private CRServo Slack;
    private Servo RF;
    private Servo LF;
    @Override
    public void runOpMode() {

        Left = hardwareMap.get(DcMotor.class, "left");
        Right = hardwareMap.get(DcMotor.class, "right");
        Tower = hardwareMap.get(DcMotor.class, "tower");
        Slack = hardwareMap.get(CRServo.class, "slack");
        RF = hardwareMap.get(Servo.class, "right finger");
        LF = hardwareMap.get(Servo.class, "left finger");

        waitForStart();
        // run until the end of the match (driver presses STOP)
        boolean limitoff=false;
        boolean y = false;
        double Fingeroffset = 0;
        boolean b = false;
        boolean x = false;
        double factor=1;
        double bonus = 0;
        double position = 0;
        double towerpower;
        double max = 4416;
        while (opModeIsActive()) {
            bonus = 0;
            towerpower = -gamepad2.left_stick_y;
            /*if(towerpower>0){
                if (position>0&&position<max){
                    position= Tower.getCurrentPosition();
                }else if(position<0){
                    position=0;
                }else{
                    position=max;
                }
                //factor = Range.clip((2/(1+(3^(Tower.getCurrentPosition()-max))))-1,-1,1);
            }else if (Math.abs(towerpower) < .01){
                bonus = .01*(position-Tower.getCurrentPosition());
            }else{
                if (position>0&&position<max){
                    position= Tower.getCurrentPosition();
                }else if(position<0){
                    position=0;
                }else{
                    position=max;
                }
                //factor = Range.clip((2/(1+(3^(-Tower.getCurrentPosition()))))-1,-1,1);
            }
            */
            if (gamepad2.y && !y){
                limitoff = !limitoff;
            }
            y = gamepad2.y;
            if (Tower.getCurrentPosition()<0 && !limitoff){
                towerpower= Range.clip(towerpower,0,1) + .01*(0-Tower.getCurrentPosition());
            }else if(Tower.getCurrentPosition()>max && !limitoff ){
                towerpower = -1;
            }
            /*
            //towerpower = (bonus+factor*towerpower);*/
            //towerpower += bonus;
            Left.setPower(.5*Range.clip(gamepad1.left_stick_y, -1, 1));
            Right.setPower(.5*Range.clip(-gamepad1.right_stick_y, -1, 1));
            Tower.setPower(.25 * Range.clip(towerpower, -1, 1));
            Slack.setPower(-.25 *Range.clip((towerpower), -1, 1));
            //finger adjustment statements
            if(gamepad2.b && !b && Fingeroffset <= .4){
                Fingeroffset += .1;
            }
            if(gamepad2.x && !x && Fingeroffset >= .1 ){
                Fingeroffset -= .1;
            }
            RF.setPosition(Range.clip(0.5 - Fingeroffset, -1,1));
            LF.setPosition(Range.clip(0.5 + Fingeroffset, -1,1));
            x = gamepad2.x;
            b = gamepad2.b;
            //Finger controls
            if (limitoff){
                telemetry.addData("path 0","limits off");
            }else {
                telemetry.addData("path 0","limits on");
            }
            telemetry.addData("Path1","Tower position: " + String.valueOf(Tower.getCurrentPosition())+" factor: " + String.valueOf(factor)+"bonus: " + String.valueOf(bonus) + "tower power: " + String.valueOf(towerpower)+ " position: " + String.valueOf(position));
            telemetry.addData("Path2","Finger offset: " + String.valueOf(Fingeroffset) + " y: "+ String.valueOf(y));
            telemetry.update();
        }
    }
}
