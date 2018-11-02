package org.firstinspires.ftc.teamcode.opmodes.TeleOp;

import android.transition.Slide;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.controller.Controller;
import org.firstinspires.ftc.teamcode.hardware.controller.Handler;
import org.firstinspires.ftc.teamcode.hardware.controller.StepButton;
import org.firstinspires.ftc.teamcode.opmodes.debuggers.TeleOpModeDebugger;
import org.firstinspires.ftc.teamcode.systems.arm.ArmState;
import org.firstinspires.ftc.teamcode.systems.arm.ArmSystem;
import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;
import org.firstinspires.ftc.teamcode.systems.slide.SlideState;
import org.firstinspires.ftc.teamcode.systems.slide.SlideSystem;

/**
 * Created by idiot on 10/11/17.
 */
@TeleOp(name = "TeleOp", group="TeleOp")
public class TeleOpMode extends TeleOpModeDebugger {
    private Controller controller1;
    private MecanumDriveSystem driveSystem;
    private ArmSystem armSystem;
    private SlideSystem slideSystem;

    public TeleOpMode() {
        msStuckDetectLoop = 1000000000;
    }

    @Override
    public void init()
    {
        this.controller1 = new Controller(gamepad1);
        armSystem = new ArmSystem(this);
        slideSystem = new SlideSystem(this);
        this.driveSystem = new MecanumDriveSystem(this);
        initButton();
    }


    @Override
    public void initialize()
    {

    }

    public void initButton() {
        telemetry.addData("buttons", "initialize");
        telemetry.update();
        addWinchButton();
        addRotateButton();
    }

    private void addWinchButton() {
        final StepButton<SlideState> winchButton = new StepButton<>(
                controller1.y,
                controller1.a,
                SlideState.WINCHING_TO_BOTTOM,
                SlideState.WINCHING_TO_LOAD,
                SlideState.WINCHING_TO_TOP
        );
        winchButton.setOffset(1);
        winchButton.incrementAction = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                slideSystem.setState(winchButton.getCurrentState());
            }
        } ;
        winchButton.decrementAction = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                slideSystem.setState(winchButton.getCurrentState());
            }
        };
        controller1.addButton(winchButton);
    }

    private void addRotateButton() {
        final StepButton<ArmState> rotateButton = new StepButton<ArmState>(
                controller1.y,
                controller1.a,
                ArmState.ROTATING_DROP,
                ArmState.ROTATING_LATCH,
                ArmState.ROTATING_PICKUP
        );
        rotateButton.setOffset(1);
        rotateButton.incrementAction = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                armSystem.setState(rotateButton.getCurrentState());
            }
        } ;
        rotateButton.decrementAction = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                armSystem.setState(rotateButton.getCurrentState());
            }
        };
        controller1.addButton(rotateButton);
    }

    @Override
    public void run(){
        controller1.handle();
        armSystem.run();
        slideSystem.run();

        float rx = controller1.gamepad.right_stick_x;
        float ry = controller1.gamepad.right_stick_y;
        float lx = controller1.gamepad.left_stick_x;
        float ly = controller1.gamepad.left_stick_y;

        driveSystem.mecanumDrive(rx, ry, lx, ly, false);
    }
}
