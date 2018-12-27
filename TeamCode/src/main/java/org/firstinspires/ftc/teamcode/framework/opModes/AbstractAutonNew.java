package org.firstinspires.ftc.teamcode.framework.opModes;

import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaException;
import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.RobotState;
import org.firstinspires.ftc.teamcode.framework.util.State;
import org.firstinspires.ftc.teamcode.framework.util.StateConfigurationException;
import org.firstinspires.ftc.teamcode.framework.util.StateMachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AbstractAutonNew extends AbstractOpMode {

    private List<Exception> exceptions = Collections.synchronizedList(new ArrayList<Exception>());

    private StateMachine stateMachine = new StateMachine();

    private static AbstractAutonNew abstractAutonNew;

    private int initLoops = 0;

    public AbstractAutonNew() {
        abstractAutonNew = this;
    }

    @Override
    public void runOpMode() {

        RobotState.currentMatchState = RobotState.MatchState.AUTONOMOUS;

        ExecutorService service = Executors.newSingleThreadExecutor();

        Callable<Boolean> InitThread = () -> {
            try {
                Init();
            } catch (Exception e) {
                throwException(e);
            }
            return true;
        };
        Callable<Boolean> InitLoopThread = () -> {
            try {
                InitLoop();
                InitLoop(initLoops);
            } catch (Exception e) {
                throwException(e);
            }
            return true;
        };

        Future<Boolean> CurrentFuture;

        //calls user init
        CurrentFuture = service.submit(InitThread);

        while (!isStopRequested() && !isStarted()) {
            checkException();

            if (CurrentFuture.isDone()) {
                initLoops++;
                CurrentFuture = service.submit(InitLoopThread);
            }
        }

        while (!isStopRequested() && !CurrentFuture.isDone()) checkException();

        RegisterStates();

        try {
            stateMachine.prepare();
        } catch (StateConfigurationException e) {
            exceptions.add(e);
        }

        addState(new State("run", "start", ()-> {
            Run();
            return true;
        }));

        boolean stateMachineActive = true;

        while (opModeIsActive() && stateMachineActive) {
            checkException();

            stateMachineActive = stateMachine.update();
        }

        AbstractOpMode.stopRequested();

        //TODO remake our shutdown procedure
        CurrentFuture.cancel(true);
        stateMachine.shutdown();

        while (!service.isTerminated()) {
            service.shutdownNow();
            checkException();
        }

        Stop();
        telemetry.stop();
    }

    public abstract void RegisterStates();

    public abstract void Init();

    public void InitLoop() {

    }

    public void InitLoop(int loops) {

    }

    public abstract void Run();

    public void Stop(){}

    public void addState(State state) {
        stateMachine.addState(state);
    }

    public static void addFinishedState(String state){
        abstractAutonNew.stateMachine.addFinishedState(state);
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
                case "IllegalStateException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    IllegalStateException exception = (IllegalStateException) e;
                    throw exception;
                }
                case "VuforiaException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    VuforiaException exception = (VuforiaException) e;
                    throw exception;
                }
                case "StateConfigurationException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    StateConfigurationException exception = (StateConfigurationException) e;
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
}