package org.firstinspires.ftc.teamcode.opmodes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.controller.Controller;
import org.firstinspires.ftc.teamcode.Hardware.controller.Handler;
import org.firstinspires.ftc.teamcode.Hardware.controller.StepButton;
import org.firstinspires.ftc.teamcode.opmodes.debuggers.TeleOpModeDebugger;
import org.firstinspires.ftc.teamcode.systems.arm.ArmState;
import org.firstinspires.ftc.teamcode.systems.arm.ArmSystem;
import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;

/**
 * Created by idiot on 10/11/17.
 */
@TeleOp(name = "TeleOp", group="TeleOp")
public class TeleOpMode extends TeleOpModeDebugger {
    //protected final ConfigParser config;
    private Controller controller1;
    //private Controller controller2;
    private MecanumDriveSystem driveSystem;
    private ArmSystem armSystem;

    public TeleOpMode() {
        msStuckDetectLoop = 1000000000;
    }

    @Override
    public void init()
    {
        this.controller1 = new Controller(gamepad1);
        armSystem = new ArmSystem(this);
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
        controller1.dPadDown.pressedHandler = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                armSystem.addState(ArmState.ROTATE_PICKUP);
            }
        };
        controller1.dPadUp.pressedHandler = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                armSystem.addState(ArmState.ROTATE_DROP);
            }
        };
        controller1.dPadUpShifted.pressedHandler = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                armSystem.addState(ArmState.ROTATE_LATCH);
            }
        };
        addWinchButton();
    }

    private void addWinchButton() {
        final StepButton<ArmState> winchButton = new StepButton<ArmState>(
                controller1.y,
                controller1.a,
                ArmState.WINCH_BOTTOM,
                ArmState.WINCH_LOAD,
                ArmState.WINCH_TOP
        );
        winchButton.setOffset(1);
        winchButton.incrementAction = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                armSystem.addState(winchButton.getCurrentState());
            }
        } ;
        winchButton.decrementAction = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                armSystem.addState(winchButton.getCurrentState());
            }
        };
        controller1.addButton(winchButton);
    }

    private void addRotateButton() {
        final StepButton<ArmState> rotateButton = new StepButton<ArmState>(
                controller1.y,
                controller1.a,
                ArmState.ROTATE_PICKUP,
                ArmState.ROTATE_LATCH,
                ArmState.ROTATE_DROP
        );
        rotateButton.setOffset(1);
        rotateButton.incrementAction = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                armSystem.addState(rotateButton.getCurrentState());
            }
        } ;
        rotateButton.decrementAction = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                armSystem.addState(rotateButton.getCurrentState());
            }
        };
        controller1.addButton(rotateButton);
    }

    @Override
    public void run(){
        controller1.handle();
        armSystem.run();

        float rx = controller1.gamepad.right_stick_x;
        float ry = controller1.gamepad.right_stick_y;
        float lx = controller1.gamepad.left_stick_x;
        float ly = controller1.gamepad.left_stick_y;

        driveSystem.mecanumDrive(rx, ry, lx, ly, false);
    }
}
