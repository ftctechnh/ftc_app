package edu.usrobotics.opmode.protobot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Max on 11/12/2016.
 */

@Autonomous(name="Protobot Red", group="Protobot")
@Disabled
public class ProtobotRed extends ProtobotAuto {

    public ProtobotRed () {
        super(false);
    }
}