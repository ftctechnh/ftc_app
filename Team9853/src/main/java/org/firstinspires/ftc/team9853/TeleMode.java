package org.firstinspires.ftc.team9853;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.chathamrobotics.ftcutils.OmniWheelDriver;

import java.util.Map;

/**
 * teleop opmode
 */
@TeleOp(name = "Driving", group = "General")

public class TeleMode extends OpMode {
    /*
     * Config
     */
    private double maxSpeedModifier = .7;
    private double minSpeedModifier = .1;

    private OmniWheelDriver driver;

    /*
     * initializes robot
     */
    @Override
    public void init() {
        driver = new OmniWheelDriver(
                hardwareMap.dcMotor.get("FrontLeft"),
                hardwareMap.dcMotor.get("FrontRight"),
                hardwareMap.dcMotor.get("BackLeft"),
                hardwareMap.dcMotor.get("BackRight"),
                telemetry
        );
    }

    /*
     * Starts robot
     */
    @Override
    public void start() {
        super.start();
    }

    /*
     * Called continuously while opmode is active
     */
    @Override
    public void loop() {
        // Drive
        if(gamepad1.dpad_up){driver.offsetAngle = 0;}
        if(gamepad1.dpad_left){driver.offsetAngle = Math.PI/2;}
        if(gamepad1.dpad_down){driver.offsetAngle = Math.PI;}
        if(gamepad1.dpad_right){driver.offsetAngle = (3*Math.PI)/2;}

        driver.move(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

        telemetryData();
    }

    /*
     * Called when opmode is stopped
     */
    @Override
    public void stop() {
        super.stop();

        // Stop all motors
        for (Map.Entry<String, DcMotor> entry : hardwareMap.dcMotor.entrySet()) {
            entry.getValue().setPower(0);
        }
    }

    private void telemetryData() {
        // For each motor
        for (Map.Entry<String, DcMotor> entry : hardwareMap.dcMotor.entrySet()) {
            telemetry.addData("Motor Power", entry.getKey() + "="
                    + entry.getValue().getController().getMotorPower(entry.getValue().getPortNumber()));
        }
    }
}
