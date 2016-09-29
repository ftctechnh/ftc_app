package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Peter on 9/22/2016.
 */
public class OmniDriveBot implements DriveTrainInterface
{
    private HardwareMap hardwareMap = null;
    private DcMotor fL = null;
    private DcMotor fR = null;
    private DcMotor bL = null;
    private DcMotor bR = null;
    public float leftYIn;
    public float leftXIn;
    public float rightXIn;
    public float fRPower;
    public float fLPower;
    public float bRPower;
    public float bLPower;

    public OmniDriveBot()
    {

    }

    public void init(HardwareMap hwMap)
    {
        hardwareMap = hwMap;
        fL = hardwareMap.dcMotor.get("frontLeft");
        fR = hardwareMap.dcMotor.get("frontRight");
        bL = hardwareMap.dcMotor.get("backLeft");
        bR = hardwareMap.dcMotor.get("backRight");

        fL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        fL.setDirection(DcMotorSimple.Direction.FORWARD);
        fR.setDirection(DcMotorSimple.Direction.FORWARD);
        bL.setDirection(DcMotorSimple.Direction.FORWARD);
        bR.setDirection(DcMotorSimple.Direction.FORWARD);

        fL.setPower(0);
        fR.setPower(0);
        bL.setPower(0);
        bR.setPower(0);
    }

    public void drive()
    {
        fRPower = leftYIn - leftXIn - rightXIn;
        fLPower = -leftYIn - leftXIn - rightXIn;
        bRPower = leftYIn + leftXIn - rightXIn;
        bLPower = -leftYIn + leftXIn - rightXIn;
        fR.setMaxSpeed(2400);
        fL.setMaxSpeed(2400);
        bR.setMaxSpeed(2400);
        bL.setMaxSpeed(2400);
        float scaleFactor = 1.0f;

        if (Math.abs(fRPower) > scaleFactor)
        {
            scaleFactor = Math.abs(fRPower);
        }
        if (Math.abs(fLPower) > scaleFactor)
        {
            scaleFactor = Math.abs(fLPower);
        }
        if (Math.abs(bRPower) > scaleFactor)
        {
            scaleFactor = Math.abs(bRPower);
        }
        if (Math.abs(bLPower) > scaleFactor)
        {
            scaleFactor = Math.abs(bLPower);
        }
        fLPower = fLPower / scaleFactor;
        bRPower = bRPower / scaleFactor;
        bLPower = bLPower / scaleFactor;
        fRPower = fRPower / scaleFactor;

        fR.setPower(fRPower);
        fL.setPower(fLPower);
        bR.setPower(bRPower);
        bL.setPower(bLPower);
    }

    public DcMotor getfL()
    {
        return fL;
    }

    public DcMotor getfR()
    {
        return fR;
    }

    public DcMotor getbL()
    {
        return bL;
    }

    public DcMotor getbR()
    {
        return bR;
    }

    public float getfRPower()
    {
        return fRPower;
    }

    public float getfLPower()
    {
        return fLPower;
    }

    public float getbRPower()
    {
        return bRPower;
    }

    public float getbLPower()
    {
        return bLPower;
    }

    public void setLeftYIn(float lYI)
    {
        leftYIn = -lYI;
    }
    public void setLeftXIn(float lXI)
    {
        leftXIn = lXI;
    }
    public void setRightXIn(float rXI)
    {
        rightXIn = -rXI;
    }

}
