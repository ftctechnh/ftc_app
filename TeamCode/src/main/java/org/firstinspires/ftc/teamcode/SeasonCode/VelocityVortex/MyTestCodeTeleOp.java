package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;

import org.firstinspires.ftc.teamcode.Components.DriveTrainMethods.TankDriveMethods;
import org.firstinspires.ftc.teamcode.Components.DriveTrainMethods.TurnDriveMethods;

/**
 * Created by team on 7/19/2017.
 */

//@TeleOp(name = "Practice Robot TeleOp", group = "TeleOp")
//@Autonomous(name = "Practice Robot", group = "Autonomous")
//@Autonomous(name = "Test", group = "Autonomous")
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
