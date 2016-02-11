package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class EasyDcMotor extends DcMotor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    protected final LastKnown<Double>                       lastKnownPower;
    protected final LastKnown<DcMotor.Direction>            lastKnownDirection;
    protected final LastKnown<DcMotorController.RunMode>    lastKnownMode;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public EasyDcMotor(DcMotorController controller, int portNumber, DcMotor.Direction direction)
        {
        super(controller, portNumber, direction);
        this.lastKnownPower     = new LastKnown<Double>();
        this.lastKnownDirection = new LastKnown<Direction>();
        this.lastKnownMode      = new LastKnown<DcMotorController.RunMode>();
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    public synchronized void setDirection(DcMotor.Direction direction)
        {
        // super writes direction
        if (!lastKnownDirection.isValue(direction))
            {
            super.setDirection(direction);
            }
        }

    public synchronized void setPower(double power)
        {
        // super reads direction, mode
        if (!lastKnownPower.isValue(power))
            {
            super.setPower(power);
            }
        }

    public synchronized double getPower()
        {
        // super reads direction
        return super.getPower();
        }

    @Deprecated
    public synchronized void setChannelMode(DcMotorController.RunMode mode)
        {
        // super writes mode
        if (!lastKnownMode.isValue(mode))
            {
            super.setChannelMode(mode);
            }
        }

    public synchronized void setMode(DcMotorController.RunMode mode)
        {
        // super writes mode
        if (!lastKnownMode.isValue(mode))
            {
            super.setMode(mode);
            }
        }

    @Deprecated
    public DcMotorController.RunMode getChannelMode()
        {
        return super.getChannelMode();
        }

    public DcMotorController.RunMode getMode()
        {
        return super.getMode();
        }

    }
