package org.firstinspires.ftc.teamcode.autonomii;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robotplus.autonomous.ColorSensorWrapper;

/**
 * Tests the JewelIdentifier
 * @author Alex Migala, Nick Clifford, Blake Abel
 * @since 9/14/17
 */
public class AutonomousJewelChecker extends LinearOpMode {

    private ColorSensorWrapper sensorWrapper;

    @Override
    public void runOpMode() {
        // init
        this.sensorWrapper = new ColorSensorWrapper(hardwareMap);

        waitForStart();

        telemetry.addData("Status", "Ready");
        telemetry.update();

        while (opModeIsActive()) {
            telemetry.addData("Colors", this.sensorWrapper.getFormattedTelemetryMessage());
            telemetry.update();
        }
    }
}
