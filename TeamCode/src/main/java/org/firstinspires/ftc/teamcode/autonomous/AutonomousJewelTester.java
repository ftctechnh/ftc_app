package org.firstinspires.ftc.teamcode.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robotplus.hardware.ColorSensorWrapper;

/**
 * Tests the JewelIdentifier
 * @author Alex Migala, Nick Clifford, Blake Abel
 * @since 9/14/17
 */

@Disabled
@Autonomous(name = "Color Tester", group = "Testing")
public class AutonomousJewelTester extends LinearOpMode {

    private ColorSensorWrapper sensorWrapper;
    //private IMUWrapper imuWrapper;

    @Override
    public void runOpMode() {
        // init
        this.sensorWrapper = new ColorSensorWrapper(hardwareMap);
        //this.imuWrapper = new IMUWrapper(hardwareMap);

        waitForStart();

        telemetry.addData("Status", "Ready");
        telemetry.update();

        while (opModeIsActive()) {
            telemetry.addData("Colors", this.sensorWrapper.getFormattedTelemetryMessage());
            //telemetry.addData("Accleration", "");

            this.sensorWrapper.getRelativeLayout().post(new Runnable() {
                @Override
                public void run() {
                    sensorWrapper.getRelativeLayout().setBackgroundColor(Color.HSVToColor(0xff, sensorWrapper.getHsvValues()));
                }
            });
            telemetry.update();
        }

        this.sensorWrapper.getRelativeLayout().post(new Runnable() {
            @Override
            public void run() {
                sensorWrapper.getRelativeLayout().setBackgroundColor(Color.WHITE);
            }
        });
    }
}
