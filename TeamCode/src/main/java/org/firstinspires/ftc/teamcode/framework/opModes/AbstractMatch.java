package org.firstinspires.ftc.teamcode.framework.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaException;
import org.firstinspires.ftc.teamcode.framework.util.StateConfigurationException;
import org.upacreekrobotics.dashboard.Dashboard;
import org.upacreekrobotics.eventloop.OurEventLoop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AbstractMatch extends AbstractOpMode {

    private List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

    private String auton, teleop;

    public ElapsedTime runTime;

    private OpModeManagerImpl manager;

    public AbstractMatch() {
    }

    @Override
    public void runOpMode() {
        runTime = new ElapsedTime();
        manager = OurEventLoop.createUserOpModeManager();

        InitMatch();
        telemetry.addData("Init");
        telemetry.update();
        manager.initActiveOpMode(auton);
        waitForStart();
        telemetry.addData("Start");
        telemetry.update();
        manager.startActiveOpMode();
        delay(30000);
        manager.stopActiveOpMode();

        manager.initActiveOpMode(teleop);
        manager.startActiveOpMode();
        delay(150000);
        manager.stopActiveOpMode();
        StopMatch();
    }

    public abstract void InitMatch();

    public abstract void StopMatch();

    public void SetupMatch(String abstractAuton, String abstractTeleop) {
        auton = abstractAuton;

        teleop = abstractTeleop;
    }
}
