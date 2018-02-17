package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.general.Timer;
import org.firstinspires.ftc.teamcode.general.action.LinearAction;
import org.firstinspires.ftc.teamcode.general.action.LoopAction;
import org.firstinspires.ftc.teamcode.math.vector.Vec3;

import java.util.concurrent.Callable;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

/**
 * Created by Derek on 2/15/2018.
 */

public class RelicAutoSimple extends RelicAutoBase {
    @Override
    public void addActions() {
        //sure wish we could use java 8 ()->{} . . .
        queue.add(new LinearAction(new Runnable() {
            @Override
            public void run() {
                drivetrain.setZeroPowerBehaivor(BRAKE);
            }
        }));

        queue.add(new LoopAction(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Timer timer = new Timer();
                timer.start();

                drivetrain.setIntegrator(new Vec3(0,-0.25,0));

                return timer.getTime() >= 0.5;
            }
        }));

        queue.add(new LinearAction(new Runnable() {
            @Override
            public void run() {
                drivetrain.stop();
            }
        }));


    }
}
