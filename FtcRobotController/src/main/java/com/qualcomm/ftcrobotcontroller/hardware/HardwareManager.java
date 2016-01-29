package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.ftcrobotcontroller.hardware.nullware.NullDcMotor;
import com.qualcomm.ftcrobotcontroller.hardware.nullware.NullHardware;
import com.qualcomm.ftcrobotcontroller.hardware.nullware.NullServo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Gets hardware, or equivalant {@link NullHardware}
 */
public class HardwareManager {

    HardwareMap map;

    /**
     * Creates a new {@link HardwareManager} with your {@link HardwareMap}
     *
     * @param map Your hardware map
     * @see OpMode#hardwareMap
     */
    public HardwareManager(HardwareMap map) {
        this.map = map;
    }

    /**
     * Gets the specified {@link DcMotor}, or an equivalent {@link NullDcMotor}
     *
     * @param name The name of the motor
     * @return A DcMotor
     */
    public DcMotor getMotor(String name) {
        try {
            DcMotor motor = map.dcMotor.get(name);
            if (motor == null)
                motor = new NullDcMotor();
            return motor;
        } catch (IllegalArgumentException e) {
            return new NullDcMotor();
        }
    }

    /**
     * Gets the specified {@link Servo}, or an equivalent {@link NullServo}
     *
     * @param name The name of the servo
     * @return A servo
     */
    public Servo getServo(String name) {
        try {
            Servo servo = map.servo.get(name);
            if (servo == null)
                servo = new NullServo();
            return servo;
        } catch (IllegalArgumentException e) {
            return new NullServo();
        }

    }

}
