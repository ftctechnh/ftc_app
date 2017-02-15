package org.firstinspires.ftc.teamcode.opmode.misc;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HolonomicRobot;

/**
 * Created by 292486 on 11/28/2016.
 */

@TeleOp(name = "Light Sensor Polling")
public class LightSensorPolling extends OpMode {

    private HolonomicRobot robot = new HolonomicRobot();

    private double minr = Integer.MAX_VALUE, maxr = Integer.MIN_VALUE;
    private double minv = Integer.MAX_VALUE, maxv = Integer.MIN_VALUE;
    private double avgr, avgv;
    private double y, x, l, r;

    private boolean stateB;
    private ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("Light Poll [RAW]: ", robot.lightFloor.getRawLightDetected());
        telemetry.addData("Light Poll [RMIN/MAX]: ", "%2.5f %2.5f", minr, maxr);
        telemetry.addData("Light Poll [RAVG]: ", avgr);
        telemetry.addData("Light Poll [VAL]: ", robot.lightFloor.getLightDetected());
        telemetry.addData("Light Poll [MAX/MIN]: ", "%2.5f %2.5f", minv, maxv);
        telemetry.addData("Light Poll [VAVG]: ", avgv);

        y = -gamepad1.left_stick_y; //For some reason, the left y-axis stick is negative when pushed forward
        x = gamepad1.right_stick_x;
        l = gamepad1.left_trigger;
        r = gamepad1.right_trigger;

        if(gamepad1.x)
        {
            y /= 2.0;
            x /= 2.0;
            l /= 2.0;
            r /= 2.0;
        }

        robot.arcade(y, x, l, r);    //Triggers doing wrong turn, fix it later!!

        if(gamepad1.a) {
            robot.lightFloor.enableLed(true);
        } else {
            robot.lightFloor.enableLed(false);
        }

        if(robot.lightFloor.getLightDetected() > maxv)
        {
            maxv = robot.lightFloor.getLightDetected();
        }
        if(robot.lightFloor.getLightDetected() < minv)
        {
            minv = robot.lightFloor.getLightDetected();
        }

        if(robot.lightFloor.getRawLightDetected() > maxr)
        {
            maxr = robot.lightFloor.getRawLightDetected();
        }
        if(robot.lightFloor.getRawLightDetected() < minr)
        {
            minr = robot.lightFloor.getRawLightDetected();
        }

        if(gamepad1.y)
        {
            maxv = Integer.MIN_VALUE;
            minv = Integer.MAX_VALUE;

            maxr = Integer.MIN_VALUE;
            minr = Integer.MAX_VALUE;
        }

        if(gamepad1.b)
        {
            avgr = (avgr + robot.lightFloor.getRawLightDetected())/2;
            avgv = (avgv + robot.lightFloor.getLightDetected())/2;
        } else {
            avgr = robot.lightFloor.getRawLightDetected();
            avgv = robot.lightFloor.getLightDetected();
        }
    }
}
