package org.firstinspires.ftc.teamcode.Shane;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
/**
 * Created by team on 7/19/2017.
 */

@TeleOp(name = "Shane's TeleOp", group = "TeleOp")
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
        super.myTelemetry();

    }   //end loop

    private void padControls() {
        if (gamepad1.a) {
            padCofig = 0;
        }
        if (gamepad1.b) {

            padCofig = 1;
        }
        double drivePower[] = new double[2];
        if (padCofig == 0) {
            TankDrive tank = new TankDrive();
            drivePower = tank.drive(gamepad1);
        }
        if (padCofig == 1) {
            TurnDrive turn = new TurnDrive();
            drivePower = turn.drive(gamepad1);
        }
        rightPower = drivePower[0];
        leftPower = drivePower[1];
        liftPower = gamepad1.right_trigger - gamepad1.left_trigger;
        if (gamepad1.left_bumper) {
            armPower = 1;
        } else if (gamepad1.right_bumper) {
            armPower = -1;
        } else {
            armPower = 0;
        }
    }

    private void setMotorPower() {
        rightMotor.setPower(rightPower);
        leftMotor.setPower(leftPower);
        liftMotor.setPower(liftPower);
        liftMotor.setPower(armPower);
    }

    private void configTele() {
        telemetry.addData("gamepad Configuration", padCofig);
    }
}
