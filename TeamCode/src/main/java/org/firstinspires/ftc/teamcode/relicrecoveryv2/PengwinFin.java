//File: PengwinFin.java
//Class to control the pengwin fin, which has a color sensor and can be use to knock of the jewel in ftc run
package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * This is a model class to hold information to map and control the fin
 * @see Object
 * @author Eric and Nora
 */
public class PengwinFin {
    public static final double FIN_UP = 0.7;
    //
    public static final double FIN_DOWN = 0.12;
    //
    public static final double FIN_SENSE = 0.4;
    Servo fin;
    ColorSensor colorSensor;
    DistanceSensor distanceSensor;
    DistanceSensor distanceSensor2;
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
        colorSensor = hardwareMap.get(ColorSensor.class, "sitefy");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "sitefy");
        distanceSensor2 = hardwareMap.get(DistanceSensor.class, "sitefy2");
    }
    /**
     * The command to move the fin down to the position {@value #FIN_DOWN}
     * on the servo
     */
    public void moveFinDown(){
            fin.setPosition(FIN_DOWN);
    }
    public void moveFinUp(){
            fin.setPosition(FIN_UP);
    }
    public void moveFinSense(){
            fin.setPosition(FIN_SENSE);
    }
    //
    public boolean doesColorSensorSeeBlueJewel(){
        return colorSensor.blue() > colorSensor.red();
    }
    //
    public boolean approachCrypt(int key){
        if(key == 1){
            return distanceSensor.getDistance(DistanceUnit.INCH) <= 10;
        }else{
            return distanceSensor2.getDistance(DistanceUnit.INCH) <= 10;
        }
    }
}

