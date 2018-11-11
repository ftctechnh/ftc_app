package org.firstinspires.ftc.teamcode.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractAuton extends AbstractOpMode {

    private boolean threadRunning = false;

    private List<Exception> exceptions = Collections.synchronizedList(new ArrayList<Exception>());

    public AbstractAuton() {
    }

    @Override
    public void runOpMode() {

        ExecutorService service = Executors.newSingleThreadExecutor();

        Thread InitThread = new Thread(new initThread());
        Thread InitLoopThread = new Thread(new initloopThread());
        Thread RunThread = new Thread(new runThread());

        threadRunning = true;
        service.execute(InitThread);

        while (!isStopRequested() && !isStarted()) {
            checkException();
            if (!threadRunning) {
                threadRunning = true;
                service.execute(InitLoopThread);
            }
        }

        while (!isStopRequested() && opModeIsActive() && threadRunning) ;

        threadRunning = true;
        service.execute(RunThread);

        while (!isStopRequested() && opModeIsActive() && threadRunning) {
            checkException();
        }

        if ((isStopRequested() || !opModeIsActive()) && threadRunning) {
            service.shutdownNow();
        }

        while (!service.isTerminated()) {
            service.shutdownNow();
            checkException();
        }

        Stop();
    }

    public abstract void Init();

    public void InitLoop() {

    }

    public abstract void Run();

    public void Stop() {

    }

    private void throwException(Exception e) {
        exceptions.add(e);
    }

    private void checkException() {
        for (Exception e : exceptions) {
            telemetry.update();
            for (StackTraceElement element : e.getStackTrace()) {
                if (element.toString().contains("org.firstinspires.ftc.teamcode")) {
                    telemetry.addData(element.toString().replace("org.firstinspires.ftc.teamcode.", ""));
                    break;
                }
            }
            switch (e.getClass().getSimpleName()) {
                case "NullPointerException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    NullPointerException exception = (NullPointerException) e;
                    throw exception;
                }
                case "IllegalArgumentException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    IllegalArgumentException exception = (IllegalArgumentException) e;
                    throw exception;
                }
                case "ArrayIndexOutOfBoundsException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    ArrayIndexOutOfBoundsException exception = (ArrayIndexOutOfBoundsException) e;
                    throw exception;
                }
                case "ConcurrentModificationException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    ConcurrentModificationException exception = (ConcurrentModificationException) e;
                    throw exception;
                }
                default: {
                    telemetry.addData(e.getClass().getSimpleName());
                    telemetry.update();
                    AbstractOpMode.delay(2000);
                }
            }
        }
    }

    class initThread implements Runnable {
        public void run() {
            try {
                Init();
            } catch (Exception e) {
                throwException(e);
            }
            threadRunning = false;
        }
    }

    class initloopThread implements Runnable {
        public void run() {
            try {
                InitLoop();
            } catch (Exception e) {
                throwException(e);
            }

            threadRunning = false;
        }
    }

    class runThread implements Runnable {
        public void run() {
            try {
                Run();
            } catch (Exception e) {
                throwException(e);
            }
            threadRunning = false;
        }
    }
}