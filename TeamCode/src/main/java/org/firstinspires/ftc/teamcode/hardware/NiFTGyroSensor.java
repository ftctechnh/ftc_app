/**
 * Encapsulates the gyro and includes easier access to main gyro features.
 */

package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.GyroSensor;

import org.firstinspires.ftc.teamcode.threads.NiFTFlow;

public class NiFTGyroSensor
{
    public final GyroSensor sensor;

    public NiFTGyroSensor (String gyroSensorName) throws InterruptedException
    {
        this.sensor = NiFTInitializer.initialize (GyroSensor.class, gyroSensorName);

        calibrate (true);
    }

    public void calibrate(boolean zeroHeading) throws InterruptedException
    {
        //Pause to prevent odd errors in which it says it's configured but is actually LYING.
        NiFTFlow.pauseForMS (1000);

        //Wait for gyro to finish calibrating.
        while (sensor.isCalibrating())
            NiFTFlow.pauseForMS (50);

        //Zero gyro heading.
        if (zeroHeading)
            zeroHeading();
    }

    //Just resets the gyro.
    public void zeroHeading() throws InterruptedException
    {
        NiFTFlow.pauseForMS (400);
        sensor.resetZAxisIntegrator();
        NiFTFlow.pauseForMS (400);
    }

    //The gyroscope value goes from 0 to 360: when the bot turns left, it immediately goes to 360.
    public int getValidGyroHeading()
    {
        //Get the heading.
        int heading = sensor.getHeading ();

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

    //The desired heading of the gyro.
    private int desiredHeading = 0;
    public void setDesiredHeading(int desiredHeading)
    {
        this.desiredHeading = desiredHeading;
    }
    public int getDesiredHeading() { return desiredHeading; }

    public int getOffFromHeading()
    {
        return desiredHeading - getValidGyroHeading ();
    }
}
