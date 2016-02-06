package com.powerstackers.resq.opmodes.autonomous;

import com.powerstackers.resq.common.RobotAuto;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.swerverobotics.library.interfaces.Autonomous;

/**
 * Created by root on 2/5/16.
 */
@Autonomous(name = "Detect Hardware", group = "Powerstackers")
public class UBetterDetectMyHardwareOp  extends LinearOpMode {

    private class TestRobot {
        private DcMotor motorLeft;

        public TestRobot(OpMode mode) {
            motorLeft = mode.hardwareMap.dcMotor.get("motorFLeft");
        }
    }

    public UBetterDetectMyHardwareOp() {

    }

    @Override
    public void runOpMode() throws InterruptedException {
        TestRobot robot = new TestRobot(this);
    }



}
