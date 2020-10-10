package org.firstinspires.ftc.teamcode.opmodes;

import android.os.SystemClock;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.ToggleButton;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.sql.Time;

/*
 * Made by Scoot, 10/4/2020
 **/
@TeleOp(name="Simple Drive TeleOp")
public class SimpleDriveTeleOp extends OpMode {
    private static final float slowPow = 0.33f;
    private static final float fastPow = 1.0f;
    private boolean robotSlow = false; //init value
    private boolean lastA = false;

    protected BotHardware bot = new BotHardware(this);

    @Override
    public void init(){
        bot.init();
        telemetry.addData("TeleOp Init", "");
    }

    @Override
    public void start(){
        gamepad1.setJoystickDeadzone(0.05f);
        gamepad2.setJoystickDeadzone(0.05f);
        bot.start();
        telemetry.addData("TeleOp Start", "");
    }

    @Override
    public  void loop(){
        float tx = 2*gamepad1.right_stick_x*gamepad1.right_stick_x*gamepad1.right_stick_x; // WTF??
        float ty = -1*gamepad1.left_stick_y*gamepad1.left_stick_y*gamepad1.left_stick_y;
        float left = (ty +tx /2 );
        float right = (ty -tx/2);

        float x = gamepad1.left_stick_x; //strafe
        float y = -gamepad1.right_stick_y;//forward & back
        x=x*x*x;
        y=y*y*y;

        double theta = Math.atan2(-x, y);
        double heading = theta * 180.0/Math.PI;

        AutoLib.MotorPowers mp = AutoLib.GetSquirrelyWheelMotorPowers(heading);
        double front = mp.Front();
        double back = mp.Back();
        double power = Math.sqrt(x*x + y*y);
        front *= power;
        back *= power;

        if(robotSlow && gamepad1.a){
            robotSlow = false;
            SystemClock.sleep(500);

        }
        else if(!robotSlow && gamepad1.a){
            robotSlow = true;
            SystemClock.sleep(500);
        }

        double fr = 0f;
        double br = 0f;
        double fl = 0f;
        double bl = 0f;

        if(robotSlow) {
            fr = (back + right) * slowPow; //switched right sides font and back
            br = (front + right) * slowPow; // WTF??
            fl = (front + left) * slowPow;
            bl = (back + left) * slowPow;
        }
        else if(!robotSlow){
            fr = (back + right) * fastPow; //switched right sides font and back
            br = (front + right) * fastPow; // WTF??
            fl = (front + left) * fastPow;
            bl = (back + left) * fastPow;
        }

        fr = Range.clip(fr, -1, 1);
        br = Range.clip(br, -1, 1);
        fl = Range.clip(fl, -1, 1);
        bl = Range.clip(bl, -1, 1);
        bot.setAllDrive(fr, br, fl, bl);

        telemetry.addData("Moto Pow", fr + ", " + br + ", " + fl +", " + bl);
        telemetry.addData("slow mode", robotSlow);
    }

    @Override
    public void stop(){
        bot.stopAll();
    }
}
