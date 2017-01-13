package edu.usrobotics.opmode.compbot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import edu.usrobotics.opmode.compbot.CompbotAuto;
/**
 * Created by dsiegler19 on 1/5/17.
 */

@Autonomous(name="Compbot Auto Blue", group="Compbot")
public class CompbotAutoBlue extends CompbotAuto {

    public CompbotAutoBlue() {
        super(true);
    }
}
