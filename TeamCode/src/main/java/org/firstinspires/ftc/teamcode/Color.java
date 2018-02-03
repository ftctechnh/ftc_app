package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 1/2/2018.
 */

public enum Color {
    RED, BLUE, UNKNOWN;
    Color not(Color target) {
        Color notTarget = null;
        switch (target) {
            case RED: notTarget = BLUE;
            case BLUE: notTarget = RED;
        }
        return notTarget;
    }
}