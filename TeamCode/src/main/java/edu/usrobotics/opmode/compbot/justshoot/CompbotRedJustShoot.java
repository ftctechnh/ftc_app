package edu.usrobotics.opmode.compbot.justshoot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Max on 11/12/2016.
 */

@Autonomous(name="Compbot Auto Red JUST SHOOT", group="Compbot Round 1")
@Disabled
public class CompbotRedJustShoot extends CompbotAutoJustShoot {

    public CompbotRedJustShoot() {
        super(false);
    }
}