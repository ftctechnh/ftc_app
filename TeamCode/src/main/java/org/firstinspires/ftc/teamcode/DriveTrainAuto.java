package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by 111 on 9/29/2016.
 */

public class DriveTrainAuto implements DriveTrainInterface
{
    OmniDriveBot OmniDrive = new OmniDriveBot();

    public void init(HardwareMap hardwareMap)
    {

    }

    public void drive()
    {

    }

    void driveStraight(double distanceInches, int degree)
    {

        OmniDrive.getfL().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        OmniDrive.getfR().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        OmniDrive.getbL().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        OmniDrive.getbR().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double degToRad = (180/3.1415926) * degree;

        float leftXIn = (float)Math.sin(degToRad);
        float leftYIn = (float)Math.cos(degToRad);
        float fRPower = leftYIn - leftXIn;
        float fLPower = -leftYIn - leftXIn;
        float bRPower = leftYIn + leftXIn;
        float bLPower = -leftYIn + leftXIn;

        OmniDrive.getbL().setPower(bLPower);
        OmniDrive.getbR().setPower(bRPower);
        OmniDrive.getfL().setPower(fLPower);
        OmniDrive.getfR().setPower(fRPower);



    }

    void spin(int degree)
    {

    }

}
