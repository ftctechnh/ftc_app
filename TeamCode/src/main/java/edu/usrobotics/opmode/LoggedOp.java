package edu.usrobotics.opmode;

import com.borsch.DataType;
import com.borsch.TelemetryData;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.HashMap;
import java.util.Map;

import edu.usrobotics.telemetry.TelemetryWriter;

/**
 * Created by Max on 9/14/2016.
 */
public class LoggedOp extends TrackedOp {

    Map<HardwareDevice, TelemetryData> loggedDevices = new HashMap<>(16); // Maximum logged devices
    int loggedDeviceCount = 0;

    TelemetryData trackingData = new TelemetryData(0, DataType.UPDATE_MAP, 0, null);



    public String[] getDeviceData (HardwareDevice device) {
        if (device instanceof DcMotor) {
            DcMotor motor = (DcMotor) device;
            return new String[]{Integer.toString(motor.getCurrentPosition()), Double.toString(motor.getPower())};
        }

        return new String[0];
    }

    /*
     * 1. HashMap lookup last TelemetryData by HardwareDevice
     * 2. Convert current device data to String[]
     * 3. If deviceDataString[] != lastDeviceDataString[] then send.
     */
    public void LogDevice (HardwareDevice device) {
        TelemetryData data = loggedDevices.get(device);

        if (data == null) {
            data = new TelemetryData(TelemetryWriter.getTimestamp(), DataType.UPDATE_DEVICE, loggedDeviceCount, getDeviceData(device));
            loggedDeviceCount++;
            loggedDevices.put(device, data);
            TelemetryWriter.writeData(data);

        } else {
            data.timestamp = TelemetryWriter.getTimestamp();
            String[] newData = getDeviceData(device);

            if (data.data != newData) { // Send changes only.
                data.data = newData;
                TelemetryWriter.writeData(data);
            }
        }
    }

    public void LogTracking () {
        OpenGLMatrix transform = getRobotTransform();
        if (transform == null)
            return;

        trackingData.timestamp = TelemetryWriter.getTimestamp();
        VectorF tran = transform.getTranslation();
        Orientation rot = getRobotOrientation();

        trackingData.data = new String[]{
                Float.toString(tran.get(0)),
                Float.toString(tran.get(1)),
                Float.toString(tran.get(2)),

                Float.toString(rot.firstAngle),
                Float.toString(rot.secondAngle),
                Float.toString(rot.thirdAngle),
        };
    }

    public void LogState () {
        telemetry.addData("State", getState().name());
    }

    @Override public void loop () {
        super.loop();

        LogTracking();
        LogState();
    }

}
