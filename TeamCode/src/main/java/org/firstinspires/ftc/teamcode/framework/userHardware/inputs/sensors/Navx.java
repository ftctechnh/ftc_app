package org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors;
/*
import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.navXPIDController;*///**************************************************************************************************//
//**************************************************************************************************//
//**************************************************************************************************//
//**************************************************************************************************//
//*******THIS IS JUST A QUICK TRANSFER IT IS NOT TESTED!!!!!!!!! THIS SHOULD BE REVIEWED ASAP********//
//**************************************************************************************************//
//**************************************************************************************************//
//**************************************************************************************************//
//**************************************************************************************************//

public class Navx {
    /*private HardwareMap hwMap;

    // This is the port on the Core Device Interface Module
    // in which the navX-Model Device is connected.  Modify this
    // depending upon which I2C port you are using.
    private final int NAVX_DIM_I2C_PORT = 0;
    private AHRS navx_device;
    private navXPIDController yawPIDController;

    private final byte NAVX_DEVICE_UPDATE_RATE_HZ = 50;

    private final double TARGET_ANGLE_DEGREES = 0.0;
    private final double TOLERANCE_DEGREES = 1.0;
    private final double MIN_MOTOR_OUTPUT_VALUE = -1.0;
    private final double MAX_MOTOR_OUTPUT_VALUE = 1.0;
    private final double YAW_PID_P = 0.005; //gain WAS 0.02 and now 0.01
    private final double YAW_PID_I = 0.0;
    private final double YAW_PID_D = 0.03; //WAS 0.06

    private boolean calibration_complete = false;

    navXPIDController.PIDResult yawPIDResult = new navXPIDController.PIDResult();
    int DEVICE_TIMEOUT_MS = 500;

    DeviceInterfaceModule dim;
    static final int    BLUE_LED    = 0;     // Blue LED channel on DIM
    static final int    RED_LED     = 1;     // Red LED Channel on DIM

    public Navx(){
        try {
            hwMap = AbstractAuton.getOpModeInstance().hardwareMap;
        }
        catch (NullPointerException e){
            hwMap = AbstractTeleop.getOpModeInstance().hardwareMap;
        }

        dim = this.hwMap.deviceInterfaceModule.get("dim");

        //navx
        navx_device = AHRS.getInstance(this.hwMap.deviceInterfaceModule.get("dim"),
                NAVX_DIM_I2C_PORT,
                AHRS.DeviceDataType.kProcessedData,
                NAVX_DEVICE_UPDATE_RATE_HZ);

        dim.setLED(RED_LED,   true); // Red for even
        while ( !calibration_complete ) {
            // navX-Micro Calibration completes automatically ~15 seconds after it is
            //powered on, as long as the device is still.  To handle the case where the
            //navX-Micro has not been able to calibrate successfully, hold off using
            //the navX-Micro Yaw value until calibration is complete.

            calibration_complete = !navx_device.isCalibrating();
            if (!calibration_complete) {
                //   telemetry.addData("navX-Micro", "Startup Calibration in Progress");
            }
        }
        dim.setLED(RED_LED,   false); // Red for even

        navx_device.zeroYaw();
        try {
            sleep(500);  // wait a small time for Yaw to update.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        float yawValue1 = navx_device.getYaw();
        float yawValue2 = navx_device.getYaw();
        // If the yawValue is not near 0, check if the yawValue is stuck
        //if (( yawValue1 > 5.0) || ( yawValue1 < -5.0 ))
        {
            // when zeroYaw is issued, saw it at 1.75
            while (   !( ( yawValue1 < 2.0) && ( yawValue1 > -2.0))
                    && ( yawValue1 == yawValue2 ) )
            {
                // Flash the LED in a loop and indicate failure
                dim.setLED(RED_LED,   true); // Red for even
                yawValue1 = navx_device.getYaw();
                try {
                    sleep(1000);               // optional pause after each move
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dim.setLED(RED_LED,   false); // Red for even
                yawValue2 = navx_device.getYaw();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        dim.setLED(RED_LED,   false); // Red for even

        yawPIDController = new navXPIDController( navx_device,
                navXPIDController.navXTimestampedDataSource.YAW);

        // yawPIDController may need to go into a function for
        //    each movement.
        // Configure the PID controller
        yawPIDController.setSetpoint(TARGET_ANGLE_DEGREES);		// THIS MAY NEED TO BE PLACE IN A FUNCTION AS INPUT
        yawPIDController.setContinuous(true);
        yawPIDController.setOutputRange(MIN_MOTOR_OUTPUT_VALUE, MAX_MOTOR_OUTPUT_VALUE);
        yawPIDController.setTolerance(navXPIDController.ToleranceType.ABSOLUTE, TOLERANCE_DEGREES);
        yawPIDController.setPID(YAW_PID_P, YAW_PID_I, YAW_PID_D);
    }

    public double getYawPID(int target_angle){
        double output = 0;

        yawPIDController.setSetpoint(target_angle);
        boolean BoolTurnComplete = false;

        try {
            while (!yawPIDController.waitForNewUpdate(yawPIDResult, DEVICE_TIMEOUT_MS)){}
            output = yawPIDResult.getOutput();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return output;
    }

    public double getAngle(){
        return navx_device.getYaw();
    }*/
}
