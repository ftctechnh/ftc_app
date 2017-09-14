package org.firstinspires.ftc.teamcode.autonomii;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robotplus.autonomous.JewelAnalyzer;

/**
 * Tests the JewelIdentifier
 * @author Alex Migala, Nick Clifford, Blake Abel
 * @since 9/14/17
 */
public class AutonomousJewelChecker extends LinearOpMode {

    private JewelAnalyzer analyzer;

    @Override
    public void runOpMode() {
        // init
        this.analyzer = new JewelAnalyzer(hardwareMap);

        waitForStart();

        telemetry.addData("Status", "Ready");
        telemetry.update();

        while (opModeIsActive()) {
            telemetry.addData("Colors", this.analyzer.getFormattedTelemetryMessage());
            telemetry.update();
        }
    }
}
