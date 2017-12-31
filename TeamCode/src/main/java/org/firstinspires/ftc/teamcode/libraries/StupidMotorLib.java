package org.firstinspires.ftc.teamcode.libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Noah on 12/30/2017.
 * Motor that do stupid things and win stupid prizes
 *
 * Turning a motor into a servo: why you shouldn't
 */

public class StupidMotorLib extends DcMotorImplEx {
    private int startCount;
    private int currentCount;
    private float speed;

    //duplicate!
    public StupidMotorLib(DcMotorEx motor, float speed) {
        super(motor.getController(), motor.getPortNumber(), motor.getDirection(), motor.getMotorType());
        this.speed = speed;
    }

    //setPosition in encoder counts
    public void setPosition(int counts) {
        if(startCount == 0) startCount = getCurrentPosition();
        if(super.direction == Direction.REVERSE)  {
            counts = -counts;
            speed = -Math.abs(speed);
        }
        currentCount = startCount + counts;
    }

    public void update() {
        /*
        double time = mode.getRuntime();
        setPower(-pid.loop(Math.abs(getCurrentPosition() - currentCount), (float)(time - lastTime)));
        lastTime = time;
        */
        if(getCurrentPosition() == currentCount) setVelocity(0, AngleUnit.DEGREES);
        else if (getCurrentPosition() > currentCount) setVelocity(-speed, AngleUnit.DEGREES);
        else setVelocity(speed, AngleUnit.DEGREES);
    }

    public int getCounts() {
        return currentCount;
    }
}
