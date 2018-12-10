package org.firstinspires.ftc.teamcode.framework.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.framework.userHardware.DoubleTelemetry;
import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.Logger;
import org.upacreekrobotics.dashboard.Dashboard;

public abstract class AbstractOpMode extends LinearOpMode {

    //Setup OpMode instance to allow other classes to access hardwareMap and Telemetry
    private static OpMode opmode;
    private static LinearOpMode linearOpMode;
    public static DoubleTelemetry telemetry;

    public static DoubleTelemetry getTelemetry() {
        return telemetry;
    }

    public static HardwareMap getHardwareMap() {
        return opmode.hardwareMap;
    }

    public static OpMode getOpModeInstance() {
        return opmode;
    }

    public AbstractOpMode() {
        opmode = this;
        linearOpMode = this;
        telemetry = new DoubleTelemetry(super.telemetry, Dashboard.getInstance().getTelemetry(), new Logger(Dashboard.getCurrentOpMode()));
    }

    @Override
    public abstract void runOpMode();

    public static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static boolean isOpModeActive() {
        return !linearOpMode.isStopRequested();
    }

    protected static void stopRequested() {
        Dashboard.onOpModePreStop();
    }
}
