package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by NDHSB-Emma on 9/8/17.
 */

@Autonomous(name="AutonTurningTest", group="Pushbot")
public class AutonTurningTest extends OpMode {

    TestAutonHardware robot = new TestAutonHardware();
    float hsvValues[] = {0F, 0F, 0F};
    int state = 1;
    public double startingheading = 0;

    @Override
    public void init() {
        robot.init(hardwareMap);

        telemetry.addData("Say", "Hello Driver");

        telemetry.addData("heading", robot.heading);

        startingheading = robot.heading;
    }

    @Override
    public void loop() {


        if (state == 1) {

            robot.MotorFrontRight.setPower(.15);
            robot.MotorFrontLeft.setPower(.15);
            robot.MotorRearRight.setPower(.15);
            robot.MotorRearLeft.setPower(.15);

            if ((startingheading+40 <= robot.heading && robot.heading <= startingheading +50) ||
                    (startingheading+40 <= robot.heading+360 && robot.heading+360 <= startingheading +50)){

                state=2;
            }

        }

        if (state == 2) {

            robot.MotorFrontRight.setPower(0);
            robot.MotorFrontLeft.setPower(0);
            robot.MotorRearRight.setPower(0);
            robot.MotorRearLeft.setPower(0);
        }

        telemetry.addLine()
                .addData("heading", robot.heading);
    }
}
