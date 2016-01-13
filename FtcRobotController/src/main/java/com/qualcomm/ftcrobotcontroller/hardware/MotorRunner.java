package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.ftcrobotcontroller.CycleTimer;
import com.qualcomm.ftcrobotcontroller.units.EncoderUnit;
import com.qualcomm.ftcrobotcontroller.units.TimeUnit;
import com.qualcomm.ftcrobotcontroller.units.Unit;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Runs motors with {@link Unit}
 */
public class MotorRunner {

    private static ArrayList<RunEvent> events = new ArrayList<RunEvent>();

    /**
     * Runs a {@link DcMotor} with an {@link Unit}
     * This function will block, only use in a {@link LinearOpMode}
     *
     * @param motor The motor to run
     * @param power The power to run at
     * @param unit  The unit to run
     */
    public static void run(DcMotor motor, double power, Unit unit) throws InterruptedException {
        if (motor != null) {
            if (unit instanceof TimeUnit) {
                motor.setPower(power);
                Thread.sleep(unit.getValue());
                motor.setPower(0);
            } else if (unit instanceof EncoderUnit) {
                motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                motor.setTargetPosition((int) unit.getValue());
                motor.setPower(power);
                while (motor.isBusy()) ;
                motor.setPower(0);
                motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                motor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            }
        }
    }

    /**
     * Runs a {@link DcMotor} with an {@link Unit}
     * This function will not block and can be used in TeleOp.
     * Call {@link #update()} and {@link CycleTimer#update()} each loop cycle for this to work right.
     *
     * @param motor The motor to run
     * @param power The power to run at
     * @param unit  The unit to run
     */
    public static void runEvent(DcMotor motor, double power, Unit unit) {
        if (unit instanceof EncoderUnit) {
            motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
            motor.setTargetPosition((int) unit.getValue());
        }
        motor.setPower(power);
        events.add(new RunEvent(motor, unit));
    }

    /**
     * Updates all run events. Must be called every cycle for {@link #runEvent(DcMotor, double, Unit) to work.}
     * If you use this with {@link TimeUnit}, you must also call {@link CycleTimer#update()}
     */
    public static void update() {
        Iterator<RunEvent> iterator = events.iterator();
        while (iterator.hasNext()) {
            RunEvent event = iterator.next();
            event.update();
            if (event.isDone())
                iterator.remove();
        }
    }

    /**
     * Checks if a {@link DcMotor} is still in use from {@link #runEvent(DcMotor, double, Unit)}
     * If this returns false, it is unsafe to use the motor
     *
     * @param motor The motor to test
     * @return Wheter the motor is done or not.
     */
    public static boolean doneWith(DcMotor motor) {
        for (RunEvent event : events) {
            if (event.getMotor() == motor)
                return false;
        }
        return true;
    }

}
