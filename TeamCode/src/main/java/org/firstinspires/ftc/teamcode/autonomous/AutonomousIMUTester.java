package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robotplus.hardware.IMUWrapper;

/**
 * Tests the REV's onboard IMU
 * @author Nick Clifford, Alex Migala, Blake Abel
 * @since 9/28/17
 */

@Disabled
@Autonomous(name = "IMU Tester", group = "Testing")
public class AutonomousIMUTester extends LinearOpMode {

    private IMUWrapper imuWrapper;

    @Override
    public void runOpMode() {
        this.imuWrapper = new IMUWrapper(hardwareMap);

        waitForStart();

        telemetry.addData("Status", "Ready");
        telemetry.update();

        while (opModeIsActive()) {
            telemetry.addData("Orientation", this.imuWrapper.getOrientation().toString());
            telemetry.update();
        }
    }

}
