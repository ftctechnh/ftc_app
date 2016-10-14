package edu.usrobotics.opmode.protobot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.usrobotics.opmode.LoggedOp;

/**
 * Created by dsiegler19 on 10/13/16.
 */
@Autonomous(name="Protobot Auto", group="Protobot")
public class ProtobotTele extends LoggedOp {
    ProtobotHardware robot = new ProtobotHardware();

    @Override
    public void init () {
        super.init();

        robot.init(hardwareMap);
    }

    @Override
    public void start () {
        super.start();


    }
}
