package org.firstinspires.ftc.teamcode.opmode.misc;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.HolonomicRobot;

import java.util.ArrayList;

/**
 * Created by 292486 on 11/30/2016.
 */

@TeleOp(name = "Hardware Diagnostics")
public class HardwareTest extends OpMode {

    private HolonomicRobot robot = new HolonomicRobot();
    private String menu = "Motors";

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        switch(menu)
        {
            case "Motors":
        }
    }
}
