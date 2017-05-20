package org.firstinspires.ftc.teamcode.Libs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HardWareMaps.Hardware_Omnidirectional_Platform_Exp;

public abstract class Core_Omnidirectional_Platform extends LinearOpMode {

    public Hardware_Omnidirectional_Platform_Exp robot = new Hardware_Omnidirectional_Platform_Exp();

    // ------------------------------------通用-----------------------------------------------
    public void initRobot(){

    }

    public abstract String goal();

    protected boolean isAnyKeyDown(){
        return gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_up || gamepad1.dpad_right ||
                gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y ||
                gamepad1.left_bumper || gamepad1.right_bumper ||
                gamepad1.left_stick_button || gamepad1.right_stick_button ||
                gamepad2.dpad_down || gamepad2.dpad_left || gamepad2.dpad_up || gamepad2.dpad_right ||
                gamepad2.a || gamepad2.b || gamepad2.x || gamepad2.y ||
                gamepad2.left_bumper || gamepad2.right_bumper ||
                gamepad2.left_stick_button || gamepad2.right_stick_button;
    }

    protected boolean isAnyKeyDownOnPadA(){
        return gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_up || gamepad1.dpad_right ||
                gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y ||
                gamepad1.left_bumper || gamepad1.right_bumper ||
                gamepad1.left_stick_button || gamepad1.right_stick_button;
    }

    protected boolean isAnyKeyDownOnPadB(){
        return gamepad2.dpad_down || gamepad2.dpad_left || gamepad2.dpad_up || gamepad2.dpad_right ||
                gamepad2.a || gamepad2.b || gamepad2.x || gamepad2.y ||
                gamepad2.left_bumper || gamepad2.right_bumper ||
                gamepad2.left_stick_button || gamepad2.right_stick_button;
    }
}
