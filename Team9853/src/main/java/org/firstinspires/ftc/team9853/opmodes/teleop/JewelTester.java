package org.firstinspires.ftc.team9853.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.chathamrobotics.common.Controller;
import org.chathamrobotics.common.IsBusyException;
import org.firstinspires.ftc.team9853.Robot9853;
import org.firstinspires.ftc.team9853.systems.JewelDisplacer;

/**
 * Created by carsonstorm on 11/1/2017.
 */

@TeleOp(name = "Jewel Tester", group = "Test")
public class JewelTester extends OpMode {
    private Robot9853 robot;
    private JewelDisplacer jewelDisplacer;
    private Controller controller1;

    @Override
    public void init() {
        robot = Robot9853.build(this);
        robot.init();
        jewelDisplacer = JewelDisplacer.build(robot);
        controller1 = new Controller(gamepad1);
    }

    @Override
    public void start() {
        super.start();
        robot.start();
        jewelDisplacer.raise();
    }

    @Override
    public void loop() {
        controller1.update();

        try {
            if (controller1.aState == Controller.ButtonState.TAPPED) {
                jewelDisplacer.shiftLeft();
            }

            if (controller1.bState == Controller.ButtonState.TAPPED) {
                jewelDisplacer.shiftRight();
            }

            if (controller1.xState == Controller.ButtonState.TAPPED) {
                jewelDisplacer.raise();
            }
            
            if (controller1.yState == Controller.ButtonState.TAPPED) {
                jewelDisplacer.drop();
            }
        } catch (IsBusyException e) {}
    }
}
