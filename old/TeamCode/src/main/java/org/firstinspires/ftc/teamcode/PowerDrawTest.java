package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Nullbot: Test power draw", group="Diagnostics")
public class PowerDrawTest extends LinearOpMode {

    NullbotHardware robot   = new NullbotHardware();
    int direction;
    ElapsedTime totalTime;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, this, false, gamepad2);

        for (DcMotor m : robot.motorArr) {
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        telemetry.log().add("Power draw test");
        telemetry.log().add("--------------------------");
        telemetry.log().add("Robot will begin to rotate its wheels.");
        telemetry.log().add("Place robot on drydock, or hit STOP!");
        telemetry.update();
        direction = 1;
        totalTime = new ElapsedTime();

        waitForStart();

        totalTime.reset();

        while (opModeIsActive()) {
            for (DcMotor m : robot.motorArr) {
                m.setPower(direction);
            }
            direction *= -1;

            telemetry.addData("Seconds elapsed", totalTime.seconds());
            telemetry.update();

            robot.sleep(500);

            idle();
        }
    }
}
