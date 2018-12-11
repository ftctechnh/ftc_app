package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.I2cDevice;

//@Disabled
@Autonomous(name="colorSenseTest")
public class colorsenseTest extends LinearOpMode {

    ModernRoboticsI2cColorSensor2 colorx;
    NewHardware robot = new NewHardware();
    private ElapsedTime runtime = new ElapsedTime();

    static final double FORWARD_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    float hsvValues[] = {0F, 0F, 0F};
    final float values[] = hsvValues;

    @Override
    public void runOpMode() {
        I2cDevice colori2c = hardwareMap.i2cDevice.get("sensorColor");
        colorx = new ModernRoboticsI2cColorSensor2(colori2c.getI2cController(),colori2c.getPort());


        int b = 1;

        waitForStart();
        while (opModeIsActive()) {
            int cnumber = colorx.colorNumber();
            if (cnumber > 12) {

            }else if (cnumber < 10 && cnumber > 5) {

            }else {

            }
            telemetry.addData("Colornumber: ",cnumber);
            telemetry.update();
        }
    }
}
