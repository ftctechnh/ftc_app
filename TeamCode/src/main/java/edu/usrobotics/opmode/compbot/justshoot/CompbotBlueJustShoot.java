package edu.usrobotics.opmode.compbot.justshoot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Max on 11/12/2016.
 */

@Autonomous(name="Compbot Auto Blue JUST SHOOT", group="Compbot Round 1")
@Disabled
public class CompbotBlueJustShoot extends CompbotAutoJustShoot {

    public CompbotBlueJustShoot() {
        super(true);
    }
}