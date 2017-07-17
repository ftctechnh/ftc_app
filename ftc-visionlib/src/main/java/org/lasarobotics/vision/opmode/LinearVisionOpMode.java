/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 *
 * Some code from FIRST library, Copyright (C) Qualcomm
 *
 * Thank you to Russell Coleman (LASA).
 */
package org.lasarobotics.vision.opmode;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.lasarobotics.vision.util.color.Color;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Linear version of the Vision OpMode
 * This includes code from the FIRST library (C) Qualcomm as of 1/23/2016
 */
public abstract class LinearVisionOpMode extends VisionOpMode {
    private final ElapsedTime timer = new ElapsedTime();
    private Threader threader = null;
    private Thread thread = null;
    private volatile boolean opModeStarted = false;
    private Mat rgba;
    private Mat gray;
    private boolean hasNewFrame = false;

    public LinearVisionOpMode() {

    }

    @Override
    public final Mat frame(Mat rgba, Mat gray) {
        if (!opModeStarted) return rgba;
        this.rgba = super.frame(rgba, gray);
        Imgproc.cvtColor(rgba, this.gray, Imgproc.COLOR_RGBA2GRAY);
        hasNewFrame = true;
        return rgba;
    }

    public final Mat getFrameRgba() {
        return rgba;
    }

    public final Mat getFrameGray() {
        return gray;
    }

    public boolean hasNewFrame() {
        return hasNewFrame;
    }

    public void discardFrame() {
        hasNewFrame = false;
    }

    public abstract void runOpMode() throws InterruptedException;

    public final void waitForVisionStart() throws InterruptedException {
        //Give some status info
        //telemetry.addData("Vision Status", "Initializing...\r\n" +
          //      "Please wait, do not stop the OpMode.");

        while (!this.isInitialized()) {
            synchronized (this) {
                this.wait();
            }
        }
    }

    public synchronized void waitForStart() throws InterruptedException {
        while (!this.opModeStarted) {
            synchronized (this) {
                this.wait();
            }
        }
    }

    public void waitOneFullHardwareCycle() throws InterruptedException {
        this.waitForNextHardwareCycle();
        Thread.sleep(1L);
        this.waitForNextHardwareCycle();
    }

    private void waitForNextHardwareCycle() throws InterruptedException {
        synchronized (this) {
            this.wait();
        }
    }

    public void sleep(long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }

    public boolean opModeIsActive() {
        return this.opModeStarted;
    }

    @Override
    public final void init() {
        super.init();
        hasNewFrame = false;
        this.rgba = Color.createMatRGBA(width, height);
        this.gray = Color.createMatGRAY(width, height);
        this.threader = new Threader(this);
        this.thread = new Thread(this.threader, "Linear OpMode Helper");
        this.thread.start();
    }

    @Override
    public final void init_loop() {
        this.notifyOrThrowError();
    }

    @Override
    public final void start() {
        this.opModeStarted = true;
        synchronized (this) {
            this.notifyAll();
        }
    }

    @Override
    public final void loop() {
        super.loop();
        this.notifyOrThrowError();
    }

    @Override
    public final void stop() {
        super.stop();
        this.opModeStarted = false;
        this.rgba.release();
        this.gray.release();

        if (!this.threader.isReady()) {
            this.thread.interrupt();
        }

        this.timer.reset();

        while (!this.threader.isReady() && this.timer.time() < 0.5D) {
            Thread.yield();
        }

        if (!this.threader.isReady()) {
            RobotLog.e("*****************************************************************");
            RobotLog.e("User Linear Op Mode took too long to exit; emergency killing app.");
            RobotLog.e("Possible infinite loop in user code?");
            RobotLog.e("*****************************************************************");
            System.exit(-1);
        }
    }

    private void notifyOrThrowError() {
        if (this.threader.hasError()) {
            throw this.threader.getLastError();
        } else {
            synchronized (this) {
                this.notifyAll();
            }
        }
    }

    private static class Threader implements Runnable {
        private final LinearVisionOpMode opModeReference;
        private RuntimeException lastError = null;
        private boolean ready = false;

        public Threader(LinearVisionOpMode var1) {
            this.opModeReference = var1;
        }

        public void run() {
            this.lastError = null;
            this.ready = false;

            try {
                this.opModeReference.runOpMode();
            } catch (InterruptedException var6) {
                RobotLog.d("LinearOpMode received an Interrupted Exception; shutting down this linear op mode");
            } catch (RuntimeException var7) {
                this.lastError = var7;
            } finally {
                this.ready = true;
            }

        }

        public boolean hasError() {
            return this.lastError != null;
        }

        public RuntimeException getLastError() {
            return this.lastError;
        }

        public boolean isReady() {
            return this.ready;
        }
    }
}
