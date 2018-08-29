package TestUtilities;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MockLinearOpMode extends LinearOpMode {
    Telemetry telemetry = null;
    HardwareMap hardwareMap = null;

    public MockLinearOpMode() {}

    @Override
    public void runOpMode() throws InterruptedException {}
}
