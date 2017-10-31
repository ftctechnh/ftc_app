package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robot.Robot;

/**
 * Created by Derek on 10/31/2017.
 *
 * Base Op mode that all other op modes extend.
 *
 * @see OpMode
 *
 *
 * todo: finish Javadoc
 *
 */

public class BaseOp extends OpMode {

    private static Robot robot;

    @Override
    public void init() {
        robot = new Robot(hardwareMap);
    }

    @Override
    public void loop() {
        robot.update();
    }
}
