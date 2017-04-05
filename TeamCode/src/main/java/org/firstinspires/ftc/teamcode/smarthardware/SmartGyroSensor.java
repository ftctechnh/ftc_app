package org.firstinspires.ftc.teamcode.smarthardware;

import com.qualcomm.robotcore.hardware.GyroSensor;

import org.firstinspires.ftc.teamcode.threading.ProgramFlow;

public class SmartGyroSensor
{
    public final GyroSensor gyroSensor;

    public SmartGyroSensor(GyroSensor gyroSensor) throws InterruptedException
    {
        this.gyroSensor = gyroSensor;

        //Start gyroscope calibration.
        this.gyroSensor.calibrate();

        //Pause to prevent odd errors in which it says it's configured but is actually LYING.
        ProgramFlow.pauseForMS (1000);

        //Wait for gyro to finish calibrating.
        while (gyroSensor.isCalibrating())
            ProgramFlow.pauseForMS (50);

        //Zero gyro heading.
        zeroHeading();
    }

    //Just resets the gyro.
    public void zeroHeading() throws InterruptedException
    {
        ProgramFlow.pauseForMS (400);
        gyroSensor.resetZAxisIntegrator();
        ProgramFlow.pauseForMS (400);
    }

    //The gyroscope value goes from 0 to 360: when the bot turns left, it immediately goes to 360.
    public int getValidGyroHeading(int desiredHeading)
    {
        //Get the heading.
        int heading = gyroSensor.getHeading ();

        //Determine the actual heading on a logical basis (which makes sense with the calculations).
        if (heading > 180 && heading < 360)
            heading -= 360;

        //What this does is enable the 180 degree turn to be effectively made without resulting in erratic movement.
        if (desiredHeading > 160 && heading < 0)
            heading += 360;
        else if (desiredHeading < -160 && heading > 0)
            heading -= 360;

        return heading;
    }
}
