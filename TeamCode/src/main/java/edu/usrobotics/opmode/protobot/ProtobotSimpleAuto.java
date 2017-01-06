package edu.usrobotics.opmode.protobot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by dsiegler19 on 11/7/16.
 */
@Autonomous(name="Protobot Auto SIMPLE", group="Protobot")
@Disabled
public class ProtobotSimpleAuto extends OpMode{

    ProtobotHardware robot = new ProtobotHardware();

    @Override
    public void init(){

        robot.init(hardwareMap);
        robot.setDirection(ProtobotHardware.MovementDirection.NORTH);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void loop(){

        if(robot.frontRight.getCurrentPosition() >= robot.inchesToEncoderTicks(36)){

            robot.setDrivePower(0f);

        }

        else{

            robot.setDrivePower(1f);

        }

    }

}

enum State{

    FORWARD1,
    TURN1,
    CRAB1

}
