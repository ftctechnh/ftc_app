package org.firstinspires.ftc.teamcode.TestCode.PracticeRobot;

import org.firstinspires.ftc.teamcode.Components.DriveTrain.TankDriveMethods;
import org.firstinspires.ftc.teamcode.Components.DriveTrain.TurnDriveMethods;

/**
 * Created by team on 7/19/2017.
 */

//@TeleOp(name = "Practice Robot TeleOp", group = "TeleOp")
public class MyTestCodeTeleOp extends MyTestCodeTelemetry {

    private int padCofig = 0;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {
        super.loop();
        padControls();
        setMotorPower();
        configTele();
        super.myTelemetry();
    }

    private void padControls() {
        if (gamepad1.a) {
            padCofig = 0;
        }
        if (gamepad1.b) {
            padCofig = 1;
        }
        double drivePower[] = new double[2];
        if (padCofig == 0) {
            TurnDriveMethods turn = new TurnDriveMethods();
            drivePower = turn.drive(gamepad1);
        }
        if (padCofig == 1) {
            TankDriveMethods tank = new TankDriveMethods();
            drivePower = tank.drive(gamepad1);
        }
        rightPower = drivePower[0];
        leftPower = drivePower[1];
    }

    private void setMotorPower() {
        rightMotor.setPower(rightPower);
        leftMotor.setPower(leftPower);
    }

    private void configTele() {
        telemetry.addData("0: Gamepad Configuration", padCofig);
    }
}
