package org.firstinspires.ftc.teamcode.opmodes.pointer;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.RedDriveAuto;

/**
 * Created by Noah on 11/18/2017.
 */

@Autonomous(name="Blue Drive")
public class BlueDriveAuto extends RedDriveAuto {
    @Override
    public void init() {
        this.isRed = false;
        super.init();
    }
}
