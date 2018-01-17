package org.firstinspires.ftc.teamcode.Demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MainTeleOp;
import org.firstinspires.ftc.teamcode.NullbotHardware;

/**
 * Created by guberti on 1/16/2018.
 */
@Autonomous(name="Test Ultrasonic Sensors", group="Demo")
public class ReadUltrasonicSensors extends MainTeleOp {
    private NullbotHardware robot = new NullbotHardware();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);

        waitForStart();

        while(opModeIsActive()) {
            telemetry.addData("Front distance: ", robot.frontUltrasonic.getVoltage());
            telemetry.update();
            robot.sleep(100);
        }
    }
}
