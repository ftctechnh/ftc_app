package org.firstinspires.ftc.teamcode.opmodes.pointer;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.RedBallAuto;

/**
 * Created by Noah on 11/18/2017.
 */

//@Autonomous(name="Blue Ball")
public class BlueBallAuto extends RedBallAuto {
    @Override
    public void init() {
        this.isRed = false;
        super.init();
    }

}
