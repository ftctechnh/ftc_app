package org.firstinspires.ftc.teamcode.opmodes.pointer;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.opmodes.old2017.RedPilliar;

/**
 * Created by Noah on 12/2/2017.
 */

@Autonomous(name="Blue Drive", group="test")
@Disabled
public class BluePilliar extends RedPilliar {
    public void init() {
        this.red = false;
        super.init();
    }
}
