package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.LinkedList;

/**
 * Created by Steve on 6/29/2015.
 */
public class SwerveLRMotors {

    String name;
    private DcMotor leftmotor;
    private DcMotor rightmotor;
    private LinkedList<SwerveAction> queue = new LinkedList<SwerveAction>();

    int actioncounter = 0;//for generating unique debugging names for actions

    public SwerveLRMotors(String debugName, DcMotor targetLeftMotor, DcMotor targetRightMotor)
    {
        name = debugName;
        leftmotor = targetLeftMotor;
        rightmotor = targetRightMotor;
    }

    public void AddAcceleration(DcMotor.Direction targetDirection, double targetStartPower, double targetEndPower, double targetDurationSeconds)
    {
        queue.add( new SwerveActionLADualMotor(Integer.toString(actioncounter++), leftmotor, rightmotor, targetDirection, targetStartPower, targetEndPower, targetDurationSeconds));
    }

    public void AddTimedMotion(DcMotor.Direction targetDirection, double targetPower, double targetDurationSeconds)
    {
        queue.add( new SwerveActionTimedDualMotor(Integer.toString(actioncounter++), leftmotor, targetDirection, targetPower, rightmotor, targetDirection, targetPower, targetDurationSeconds));
    }

    public void AddDelay(double targetDurationSeconds)
    {
        queue.add( new SwerveActionDelay(Integer.toString(actioncounter++), targetDurationSeconds));
    }

    public void AddLineFollow(LightSensor targetSensor)
    {
        queue.add( new SwerveActionDualMotorLineFollow(Integer.toString(actioncounter++),leftmotor, rightmotor, targetSensor));
    }

    public String ToString()
    {
        String message = "";

        for (SwerveAction d : queue)
        {
            message += d.ToString() +"\n";
        }
        return message;
    }

    public int GetQueueLength()
    {
        return queue.size();
    }

    public void ClearQueue()
    {
        queue.clear();
    }

    public void Update(ElapsedTime currentTime)
    {
        if (queue.size() > 0)
        {
            //get the first item
            SwerveAction a = queue.getFirst();
            if (a!=null)  //don't really need this check since we looked at the queue size already
            {
                if (!a.IsStarted()) {
                    a.Start(currentTime);
                }
                a.Update(currentTime);
            }

            //remove any done actions from the queue
            //in c# you can't remove an item during an iterator because that messes up the iterator
            //I'm not sure if java handles that better, but to be safer I'll remove after iterating

            //To Do...since we only activate the top item in the queue,
            //...perhaps we only need to check the first item in the list?
            LinkedList<SwerveAction> removelist = new LinkedList<SwerveAction>();
            for (SwerveAction d : queue)
            {
                if (d.IsDone()) removelist.add(d);
            }
            for (SwerveAction r : removelist)
            {
                queue.remove(r);
            }

        }
    }


}
