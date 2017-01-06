package edu.usrobotics.opmode.compbot.autosimple;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.usrobotics.opmode.compbot.autosimple.CompbotAutoSimple;

/**
 * Created by Max on 11/12/2016.
 */

@Autonomous(name="Compbot Auto Blue SIMPLE", group="Compbot")
public class CompbotBlueSimple extends CompbotAutoSimple {

    public CompbotBlueSimple() {
        super(true);
    }
}