package com.qualcomm.ftcrobotcontroller.hardware;

import android.util.Log;

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

    final static String TAG = "Motor Runner";

    private static ArrayList<RunEvent> events = new ArrayList<RunEvent>();

    /**
     * Sets an array of {@link DcMotor} to a certain power.
     *
     * @param motors The motors to run
     * @param power  The power to run at
     */
    public static void setMotorPowers(DcMotor[] motors, double power) {
        for (DcMotor motor : motors) {
            motor.setPower(power);
            Log.w(TAG, "Set motor power:" + power);
        }
    }

    /**
     * Runs a {@link DcMotor} with an {@link Unit}
     * This function will block, only use in a {@link LinearOpMode}
     *
     * @param mode Your OpMode
     * @param motor The motor to run
     * @param power The power to run at
     * @param unit  The unit to run
     */
    public static void run(LinearOpMode mode, DcMotor motor, double power, Unit unit) throws InterruptedException {
        run(mode, new DcMotor[]{motor}, power, unit);
    }

    /**
     * Runs an array of {@link DcMotor} with an {@link Unit}
     * The the first element in the array must be a motor with an encoder
     * This function will block, only use in a {@link LinearOpMode}
     *
     * @param mode Your OpMode
     * @param motors The motors to run
     * @param power  The power to run at
     * @param unit   The unit to run
     */
    public static void run(LinearOpMode mode, DcMotor[] motors, double power, Unit unit) throws InterruptedException {
        if (motors[0] != null) {
            if (unit instanceof TimeUnit) {
                setMotorPowers(motors, power);
                Thread.sleep(unit.getValue());
                setMotorPowers(motors, 0);
            } else if (unit instanceof EncoderUnit) {
                mode.telemetry.addData("Log", "Encoder Unit:" + unit.getValue());
                motors[0].setMode(DcMotorController.RunMode.RESET_ENCODERS);
                mode.waitOneFullHardwareCycle();
                motors[0].setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                mode.waitOneFullHardwareCycle();
                motors[0].setTargetPosition((int) unit.getValue());
                mode.waitOneFullHardwareCycle();
                setMotorPowers(motors, power);
                while (motors[0].isBusy()) {
                    mode.waitOneFullHardwareCycle();
                    mode.telemetry.addData("Encoder Value", "Encoder Position:" + motors[0].getCurrentPosition());
                }
                mode.telemetry.addData("Log", "Done Waiting");
                setMotorPowers(motors, 0);
                motors[0].setMode(DcMotorController.RunMode.RESET_ENCODERS);
                mode.waitOneFullHardwareCycle();
                motors[0].setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
                mode.waitOneFullHardwareCycle();
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
     * @return Whether the motor is done or not.
     */
    public static boolean doneWith(DcMotor motor) {
        for (RunEvent event : events) {
            if (event.getMotor() == motor)
                return false;
        }
        return true;
    }

}
