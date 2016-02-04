package org.swerverobotics.library.internal.tests;

/*
 * FTC Team 25: cmacfarl, December 10, 2015
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.swerverobotics.library.internal.EasyModernMotorController;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Team25DcMotor extends DcMotor
{
    public enum MotorLocation {
        UNKNOWN,
        FRONT_LEFT,
        FRONT_RIGHT,
        REAR_LEFT,
        REAR_RIGHT,
    }

    // protected Robot robot;
    protected double power;
    protected int targetPosition;
    protected Set<Team25DcMotor> slaves = null;
    protected MotorLocation location;
    private final static double powerMax = 1.0;
    private final static double powerMin = -1.0;

    /*
    protected PeriodicTimerTask ptt = new PeriodicTimerTask(null, 200) {
        @Override
        public void handleEvent(RobotEvent e)
        {
            Team25DcMotor.super.setPower(Team25DcMotor.this.power);
        }
    };*/

    public Team25DcMotor(Robot robot, DcMotorController controller, int portNumber)
    {
        super(controller, portNumber);
        // this.robot = robot;
        // this.ptt.setRobot(robot);
        this.power = 0.0;
    }

    public Team25DcMotor(Robot robot, DcMotorController controller, int portNumber, MotorLocation loc)
    {
        super(controller, portNumber);
        // this.robot = robot;
        // this.ptt.setRobot(robot);
        this.power = 0.0;
        this.location = loc;
    }

    public void stopPeriodic()
    {
        // this.robot.removeTask(ptt);
    }

    public void startPeriodic()
    {
        // this.robot.addTask(ptt);
    }

    /**
     * Sets the power for this motor and any of its slaves.
     *
     * @param power The power to apply to the motor.
     */
    public void setPower(double power)
    {
        power = Range.clip(power, powerMin, powerMax);

        if (slaves != null) {
            if ((slaves.size() == 1) && (isEasyController() == true)) {
                Team25DcMotor m = (Team25DcMotor) slaves.toArray()[0];
                DcMotorController mc = m.getController();
                if (mc == this.getController()) {
                    // ((EasyModernMotorController) mc).setMotorPower(power);
                    return;
                }
            }
            for (Team25DcMotor m : slaves) {
                m.setPower(power);
            }
        }

        this.power = power;
        super.setPower(power);
    }

    /**
     * Rotates two/four motors in opposite directions.  This motor must be
     * bound with one slave or three slaves.  If three, the user must
     * specify motor location in the constructor.
     *
     * @param power The motor power to apply to the master.  The slave is
     * applied a negated power value.
     */
    public void turn(double power)
    {
        if (slaves == null) {
            throw new UnsupportedOperationException("Turn must be called with at least one slave");
        }

        power = Range.clip(power, powerMin, powerMax);

        if (slaves.size() == 1) {
            this.power = power;
            super.setPower(power);
            ((Team25DcMotor)slaves.toArray()[0]).setPower(-power);
        } else if (slaves.size() == 3) {
            if (this.location == MotorLocation.UNKNOWN) {
                throw new UnsupportedOperationException("All motors must have location specified for the turn operation");
            }
            this.power = power;
            super.setPower(power);

            for (Team25DcMotor m : slaves) {
                if (this.location == MotorLocation.UNKNOWN) {
                    throw new UnsupportedOperationException("All motors must have location specified for the turn operation");
                } else if (this.location.toString().substring(this.location.toString().indexOf('_')) ==
                            m.location.toString().substring(m.location.toString().indexOf('_'))) {
                    m.setPower(power);
                } else {
                    m.setPower(-power);
                }
            }
        } else {
            throw new UnsupportedOperationException("Turn must be called with either one or three slaves");
        }
    }

    public void setTargetPosition(int position)
    {
        targetPosition = position;
    }

    public boolean isBusy()
    {
        int currentPosition = getCurrentPosition();

        return (Math.abs(currentPosition) < targetPosition);
    }

    /**
     * Bind a set of motors to this motor as slaves.  The setPower operation will
     * this apply to all of the slaves in the set.
     *
     * @param slaves A set of motors to bind to this master.
     */
    public void bind(Set<Team25DcMotor> slaves)
    {
        this.slaves = slaves;
    }

    /**
     * Bind three other motors to this one, making this one the master.
     * Assumes there's a second motor on the master's controller and two
     * motors on the controller passed in.
     *
     * Throws an exception if this is not running on an EasyModernMotorController
     *
     * @param mc The other two
     */
    public void bind(DcMotorController mc)
    {
        if ((isEasyController() == false) || (isEasyController(mc) == false)) {
            throw new UnsupportedOperationException("Binding only supported in conjunction with EasyModernMotorController");
        }

        this.slaves = new HashSet<Team25DcMotor>();

        /*
         * What port am I on?
         */
        int port = this.getPortNumber();
        if (port == 1) {
            // this.slaves.add((Team25DcMotor)((EasyModernMotorController) this.getController()).getMotor(2));
        } else {
            // this.slaves.add((Team25DcMotor)((EasyModernMotorController) this.getController()).getMotor(1));
        }

        /*
         * Add the other motor controller's motors.
         */
        // this.slaves.add((Team25DcMotor)((EasyModernMotorController)mc).getMotor(1));
        // this.slaves.add((Team25DcMotor)((EasyModernMotorController)mc).getMotor(2));
    }


    /**
     * A convenience function to bind a single motor to this master.  Will throw
     * away any previous binds/pairs.  If you want to bind multiple motors to
     * this motor use bind().
     *
     * @param slave A motor to bind to this master.
     */
    public void pair(Team25DcMotor slave)
    {
        this.slaves = new HashSet<Team25DcMotor>();
        this.slaves.add(slave);
    }

    public void unbind()
    {
        this.slaves = null;
    }

    private boolean isEasyController()
    {
        return isEasyController(this.getController());
    }

    private static boolean isEasyController(DcMotorController mc)
    {
        if (mc instanceof org.swerverobotics.library.internal.EasyModernMotorController) {
            return true;
        } else {
            return false;
        }
    }
}