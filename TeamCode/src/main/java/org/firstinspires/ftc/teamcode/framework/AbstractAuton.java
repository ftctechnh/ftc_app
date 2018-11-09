package org.firstinspires.ftc.teamcode.framework;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractAuton extends AbstractOpMode {

    private boolean threadRunning = false;

    private ArrayList<Exception> exceptions = new ArrayList<>();

    public AbstractAuton(){
    }

    @Override
    public void runOpMode() {

        ExecutorService service = Executors.newSingleThreadExecutor();

        Thread InitThread = new Thread(new initThread());
        Thread InitLoopThread = new Thread(new initloopThread());
        Thread RunThread = new Thread(new runThread());

        threadRunning = true;
        service.execute(InitThread);

        while(!isStopRequested() && !isStarted()){
            checkException();
            if(!threadRunning) {
                threadRunning = true;
                service.execute(InitLoopThread);
            }
        }

        while(!isStopRequested() && opModeIsActive() && threadRunning);

        threadRunning = true;
        service.execute(RunThread);

        while(!isStopRequested() && opModeIsActive() && threadRunning){
            checkException();
        }

        if((isStopRequested() || !opModeIsActive()) && threadRunning){
            service.shutdownNow();
        }

        while(!service.isTerminated()){
            service.shutdownNow();
            checkException();
        }

        Stop();
    }

    public abstract void Init();

    public void InitLoop(){

    }

    public abstract void Run();

    public void Stop(){

    }

    private void throwException(Exception e){
        exceptions.add(e);
    }

    private void checkException() {
        try {
            for (Exception e : exceptions) {
                telemetry.update();
                for (StackTraceElement element : e.getStackTrace()) {
                    if (element.toString().contains("org.firstinspires.ftc.teamcode")) {
                        telemetry.addData(element.toString().replace("org.firstinspires.ftc.teamcode.",""));
                        break;
                    }
                }
                telemetry.update();
                AbstractOpMode.delay(500);
                switch (e.getClass().getSimpleName()) {
                    case "NullPointerException": {
                        throw new NullPointerException(e.getMessage());
                    }
                    case "IllegalArgumentException": {
                        throw new IllegalArgumentException(e.getMessage());
                    }
                    case "ArrayIndexOutOfBoundsException": {
                        throw new ArrayIndexOutOfBoundsException(e.getMessage());
                    }
                    default: {
                        telemetry.addData(e.toString());
                    }
                }
            }
        } catch (ConcurrentModificationException e){}
    }

    class initThread implements Runnable{
        public void run(){
            try {
                Init();
            } catch (Exception e){
                throwException(e);
            }
            threadRunning = false;
        }
    }

    class initloopThread implements Runnable{
        public void run(){
            try {
                InitLoop();
            } catch (Exception e){
                throwException(e);
            }

            threadRunning = false;
        }
    }

    class runThread implements Runnable{
        public void run(){
            try {
                Run();
            } catch (Exception e){
                throwException(e);
            }
            threadRunning = false;
        }
    }
}