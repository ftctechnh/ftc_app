//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.robotcore.eventloop.opmode;

import com.borsch.sim.TelemetrySim;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.TelemetryMessage;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.TelemetryInternal;

import java.util.concurrent.TimeUnit;

public abstract class OpMode {
    OpModeServices opModeServices = null;
    public Gamepad gamepad1 = null;
    public Gamepad gamepad2 = null;
    public Telemetry telemetry = new TelemetrySim(this);
    public HardwareMap hardwareMap = null;
    public double time = 0.0D;
    private long startTime = 0L;
    protected int msStuckDetectInit = 5000;
    protected int msStuckDetectInitLoop = 5000;
    protected int msStuckDetectStart = 5000;
    protected int msStuckDetectLoop = 5000;
    protected int msStuckDetectStop = 1000;

    public OpMode() {
        this.startTime = System.nanoTime();
    }

    public abstract void init();

    public void init_loop() {
    }

    public void start() {
    }

    public abstract void loop();

    public void stop() {
    }

    public final void requestOpModeStop() {
        this.opModeServices.requestOpModeStop(this);
    }

    public double getRuntime() {
        double NANOSECONDS_PER_SECOND = (double)TimeUnit.SECONDS.toNanos(1L);
        return (double)(System.nanoTime() - this.startTime) / NANOSECONDS_PER_SECOND;
    }

    public void resetStartTime() {
        this.startTime = System.nanoTime();
    }

    public void updateTelemetry(Telemetry telemetry) {
        telemetry.update();
    }

    final void updateTelemetryNow(TelemetryMessage telemetry) {
        this.opModeServices.refreshUserTelemetry(telemetry, 0.0D);
    }

    protected void preInit() {
        if(this.telemetry instanceof TelemetryInternal) {
            ((TelemetryInternal)this.telemetry).resetTelemetryForOpMode();
        }

    }

    protected void postInitLoop() {
        this.telemetry.update();
    }

    protected void postLoop() {
        this.telemetry.update();
    }
}
