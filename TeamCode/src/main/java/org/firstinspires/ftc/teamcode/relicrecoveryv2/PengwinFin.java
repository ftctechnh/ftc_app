//File: PengwinFin.java
//Class to control the pengwin fin, which has a color sensor and can be use to knock of the jewel in ftc run
package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * This is a model class to hold information to map and control the fin
 * @see Object
 * @author Eric and Nora
 */
public class PengwinFin {

    /**
     * The position for the servo to be for the fin to be up
     */
    public static final double FIN_UP_POSITION = .5;
    /**
     * The position for the servo to be for the fin to be in a place to move the jewel
     */
    public static final double FIN_DOWN_POSITION = 0.09;
    Servo fin;
    ColorSensor colorSensor;

    /**
     * This is the constructor of the pengwin class.  It will find the servo controlling
     * the fin, called <b>fin</b>, and the color sensor, called <b>sitefy</b>.
     * @param hardwareMap the object that has access to the actual hardware of the robot
     * @see HardwareMap
     * @see Servo
     * @see ColorSensor
    */
    public PengwinFin(HardwareMap hardwareMap){
        fin = hardwareMap.servo.get("fin");
        colorSensor = hardwareMap.colorSensor.get("sitefy");
    }


    /**
     * The command to move the fin down to the position {@value #FIN_DOWN_POSITION}
     * on the servo
     */
    public void moveFinDown(){
            fin.setPosition(FIN_DOWN_POSITION);
    }


    /**
     * The command to move the fun up to the position {@value #FIN_UP_POSITION}
     * on the servo
     * and the narwhal is Barney
     * and Unicorn Crossings
     * yaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaay-
     */
    public void moveFinUp(){
            fin.setPosition(FIN_UP_POSITION);
    }

    /**
     * Determine if the color sensor is pointed at something, hopefully a jewel, that is more blue then red
     * @return boolean if the sensor is seeing more blue then seeing red
     */
    public boolean doesColorSensorSeeBlueJewel(){
        return colorSensor.blue() > colorSensor.red();
    }
}

