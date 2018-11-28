package org.firstinspires.ftc.teamcode.framework.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaException;
import org.firstinspires.ftc.teamcode.framework.util.StateConfigurationException;

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

    private AbstractAutonNew auton;
    private AbstractTeleop teleop;

    public ElapsedTime runTime;

    public AbstractMatch() {
        runTime = new ElapsedTime();
    }

    @Override
    public void runOpMode() {
        InitMatch();
        //auton.start();
        auton.runOpMode();
        //auton.stop();
        auton.opModeIsActive();
        teleop.runOpMode();
        StopMatch();
    }

    public abstract void InitMatch();

    public abstract void StopMatch();

    public void SetupMatch(AbstractAutonNew abstractAuton, AbstractTeleop abstractTeleop) {
        auton = abstractAuton;

        teleop = abstractTeleop;
    }
}
