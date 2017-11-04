package org.firstinspires.ftc.teamcode.opmodes.pointer;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.opmodes.demo.VumarkHiJackVideo;

/**
 * Created by Noah on 11/3/2017.
 */

@Autonomous(name="Blue Auto", group ="Auto")
public class VuMarkAutoBlue extends VumarkHiJackVideo {
    @Override
    public void init() {
        this.RED = false;
        super.init();
    }
}
