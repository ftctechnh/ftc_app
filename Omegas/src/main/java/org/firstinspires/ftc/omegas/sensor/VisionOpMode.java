package org.firstinspires.ftc.omegas.sensor;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.omegas.HardwareOmegas;
import org.lasarobotics.vision.opmode.LinearVisionOpMode;

/*
 * Created by ethertyper on 11/14/16.
 *
 * Copyright (c) 2015 LASA Robotics and Contributors
 * MIT licensed
 */

@Autonomous(name = "Omegas: FTC-Vision Test", group = "Tests")
public class VisionOpMode extends LinearVisionOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    HardwareOmegas Ω = new HardwareOmegas();

    // IPS Units
    static final double FORWARD_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {

    }
}