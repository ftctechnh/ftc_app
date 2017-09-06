package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by vineelavandanapu on 9/4/17.
 */

@Autonomous(name="AutonTest", group="Pushbot")


public class AutonTest extends OpMode{

    TestAutonHardware robot       = new TestAutonHardware();
    float hsvValues[] = {0F,0F,0F};
    int state = 1;

    @Override
    public void init() {
        robot.init(hardwareMap);

        telemetry.addData("Say", "Hello Driver");
    }

    @Override
    public void loop() {

        Color.RGBToHSV(robot.color.red() * 8, robot.color.green() * 8, robot.color.blue() * 8, hsvValues);

        if (state == 1) {
            robot.MotorFrontRight.setPower(.15);
            robot.MotorFrontLeft.setPower(-.15);
            robot.MotorRearRight.setPower(.15);
            robot.MotorRearLeft.setPower(-.15);
        }else if(state == 2){
            robot.MotorFrontRight.setPower(-.1);
            robot.MotorFrontLeft.setPower(.1);
            robot.MotorRearRight.setPower(-.1);
            robot.MotorRearLeft.setPower(.1);
        }else{
            robot.MotorFrontRight.setPower(0);
            robot.MotorFrontLeft.setPower(0);
            robot.MotorRearRight.setPower(0);
            robot.MotorRearLeft.setPower(0);
        }

        if ( hsvValues[1] > .30 &&  hsvValues [2] > .30){
            state += 1;
        }

        telemetry.addLine()
                .addData("Clear", robot.color.alpha())
                .addData("Red  ", robot.color.red())
                .addData("Green", robot.color.green())
                .addData("Blue ", robot.color.blue());
        telemetry.addLine()
                .addData("Hue", hsvValues[0])
                .addData("Saturation", hsvValues[1])
                .addData("Value", hsvValues[2]);

    }
}
