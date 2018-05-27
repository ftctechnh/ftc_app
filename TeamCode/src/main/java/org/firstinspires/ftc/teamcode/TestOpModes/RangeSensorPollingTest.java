package org.firstinspires.ftc.teamcode.TestOpModes;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pston on 2/11/2018
 */

public class RangeSensorPollingTest extends OpMode {

    private ModernRoboticsI2cRangeSensor rangeSensor;

    private Robot robot;

    private List<Integer> rangeList = new ArrayList<>();

    private boolean hasTimeBeenReset = false;

    private ElapsedTime time = new ElapsedTime();

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, false);

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range");
    }

    @Override
    public void loop() {
        if (!hasTimeBeenReset) {
            time.reset();
            hasTimeBeenReset = true;
        }

        if (time.seconds() <= 1) {
            rangeList.add(rangeSensor.rawUltrasonic());
        }

        telemetry.addData("List Length", rangeList.size());
    }
}
