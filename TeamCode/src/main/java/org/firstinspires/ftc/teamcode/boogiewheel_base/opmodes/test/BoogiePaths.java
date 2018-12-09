package org.firstinspires.ftc.teamcode.boogiewheel_base.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.RobotState;
import org.firstinspires.ftc.teamcode.framework.opModes.AbstractAuton;
import org.upacreekrobotics.dashboard.Dashboard;

@Autonomous(name = "BoogieWheel Paths", group = "New")
//@Disabled

public class BoogiePaths extends AbstractAuton {
    private Robot robot;

    @Override
    public void Init() {

        robot = new Robot();
    }

    @Override
    public void Run() {
        switch ((int) Dashboard.getInputValueDouble("Create(0) of Run(1)?")) {
            case 0: {
                createPath();
                break;
            }
            case 1: {
                runPath();
                break;
            }
        }
    }

    public void createPath() {
        Dashboard.getInputValueDouble("Press Enter to continue");

        //printArrays(robot.recordPath(40,250));

        printArray(robot.recordPathWithHeading(300, 10));
    }

    public void runPath() {
        /*
        int[] left = {0,0,1,159,348,542,769,1005,1205,1476,1783,2114,2406,2689,2866,3064,3293,3481,3712,3975,4193,4373,4535,4697,4884,5039,5205,5404,5580,5709,5829,5950,6014,6014,6014,6014,6014,6014,6014,6014};
        int[] right = {0,0,23,185,364,558,784,1016,1213,1479,1770,2100,2419,2734,3056,3344,3644,3959,4240,4526,4807,5069,5352,5609,5868,6124,6399,6656,6934,7133,7381,7617,7676,7676,7676,7676,7676,7676,7676,7676};

        robot.runPath(left, right, 1000);
        */
        //robot.runPathWithHeading(,10,1);
    }

    public void printArrays(int[][] values) {
        String message = "";
        message = message + "int[] left = {";
        for (int i = 0; i < values[0].length - 1; i++) {
            message = message + values[0][i] + ",";
        }
        message = message + values[0][values[0].length - 1] + "};";
        telemetry.addDataDB(message);

        message = "";
        message = message + "int[] right = {";
        for (int i = 0; i < values[1].length - 1; i++) {
            message = message + values[1][i] + ",";
        }
        message = message + values[1][values[1].length - 1] + "};";
        telemetry.addDataDB(message);
    }

    public void printArray(int[] values) {
        String message = "int[] NAME = {";
        for (int i = 0; i < values.length - 1; i++) {
            message = message + values[i] + ",";
        }
        message = message + values[values.length - 1] + "};";
        telemetry.addDataDB(message);
    }
}
