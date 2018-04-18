package org.firstinspires.ftc.teamcode.opmodes.pointer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.ADPSAuto;

/**
 * Created by Noah on 12/31/2017.
 */

@Autonomous(name="Blue Rear Auto", group="test")
public class BlueRearAPDSAuto extends ADPSAuto {
    @Override
    public void _flipBits() {
        red = false;
        rear = true;
    }
}