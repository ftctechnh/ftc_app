package org.firstinspires.ftc.teamcode.libraries.hardware;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;

import static android.os.SystemClock.sleep;

// wrapper for BNO055IMU gyro that implements our HeadingSensor interface
// also does correction for IMUs that return other than 360 degrees per full turn

public class BNO055IMUHeadingSensor implements HeadingSensor {

    BNO055IMU mIMU;             // the physical IMU
    float mHeadingOffset = 0;   // client specified "initial heading" == IMU heading zero
    int mOrientation = 0;       // orientation of REV hub on vehicle -- see choices below

    float mDegreesPerTurn = 360.0f;     // measured empirically - set this if yours isn't 360

    float mPrevHeading;         // for accumulating multi-turn heading incrementally
    float mCumHeading;          // accumulated multi-turn heading for error correction

    OpMode mOpMode = null;      // for debugging

    public BNO055IMUHeadingSensor(BNO055IMU imu) {
        mIMU = imu;
    }

    public float getHeading() {
        // compute incremental heading change since last reading and add it to the cumulative
        // multi-turn heading used to compute error correction
        float imuZ = mIMU.getAngularOrientation().firstAngle;
        float delta = SensorLib.Utils.wrapAngle(imuZ - mPrevHeading);
        mCumHeading += delta;
        mPrevHeading = imuZ;

        // compute corrected cumulative heading
        float corrCumHeading = mCumHeading * 360.0f / mDegreesPerTurn;

        // compute sensor heading value corrected for IMU error (measured)
        // plus initial offset (heading at sensor zero) wrapped to -180..+180
        float result = SensorLib.Utils.wrapAngle(corrCumHeading + mHeadingOffset);

        // debug output
        if (mOpMode != null) {
            mOpMode.telemetry.addData("mCumHeading", mCumHeading);
            mOpMode.telemetry.addData("corrCumHeading", corrCumHeading);
            mOpMode.telemetry.addData("result", result);
        }

        return result;
    }

    public boolean haveHeading() { return true; }

    public void setDegreesPerTurn(float degreesPerTurn) { mDegreesPerTurn = degreesPerTurn; }
    public float getDegreesPerTurn() { return mDegreesPerTurn; }

    public void setHeadingOffset(float offset) { mHeadingOffset = offset; }
    public float getHeadingOffset() { return mHeadingOffset; }

    public float getPitch() {
        return mIMU.getAngularOrientation().thirdAngle;
    }

    public float getRoll() {
        return mIMU.getAngularOrientation().secondAngle;
    }

    public Orientation getAngularOrientation() {
        return mIMU.getAngularOrientation();
    }

    // get angular velocity about the vehicle Z axis in degrees/sec -- depends on RevHub orientation
    public float getHeadingVelocity() {
        AngularVelocity av = mIMU.getAngularVelocity();
        switch (mOrientation) {
            case 0: case 1: case 2: case 3: case 4: {
                return av.zRotationRate;
            }
            case 5: case 6: case 7: {
                return av.xRotationRate;
            }
            case 8: {
                return -av.zRotationRate;
            }
        }
        return 0;
    }

    public Position getPosition() {
        return mIMU.getPosition();
    }

    public void init(int orientation) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled       = true;
        parameters.useExternalCrystal   = true;
        parameters.mode                 = BNO055IMU.SensorMode.IMU;
        parameters.loggingTag           = "IMU";

        mIMU.initialize(parameters);

        // this may take too long to do in init -- if so, move to start() or loop() first time
        setOrientation(orientation);

        // initialize "previous" heading used for error correction
        mPrevHeading = mIMU.getAngularOrientation().firstAngle;
    }

    public void setOpMode(OpMode opmode) {
        mOpMode = opmode;
    }

    // used for testing changing to various orientations after initialization
    public boolean setOrientation(int orientation) {
        if (orientation >= 0 && orientation < numOrientations()) {
            mOrientation = orientation;
            setRevOrientation(mIMU, mOrientation);
            return true;        // legal
        }
        return false;       // not legal
    }

    // return current orientation index
    public int getOrientation() {
        return mOrientation;
    }

    // return number of implemented orientations (see setRevOrientation below)
    public int numOrientations() {
        return config.length;
    }


    // this function sets CONFIG and SIGN bytes for REV mounted in various orientations.
    // the default orientation of the IMU axes in the REV hub is like this:
    //       __________________________
    //      |                          |
    //      |  REV                     |
    //      |           Z@------ Y     |
    //      |            |             |        ---------> front of vehicle
    //      |            |             |
    //      |            X             |
    //      |__________________________|

    // we will use the convention PITCH = +X, ROLL = +Y, YAW(Heading) = +Z
    // which corresponds to mounting the REV hub laying flat on the vehicle as shown above.

    // other orientations need these settings:
    private byte[] config = { 0x24, 0x24, 0x21, 0x21, 0x6,  0x6, 0x9, 0x9, 0x21 };
    private byte[] sign   = { 0x0,  0x6,  0x4,  0x2,  0x1,  0x4, 0x5, 0x3, 0x7  };
    // 0: flat with top of REV label on right side of vehicle (default):
    // byte AXIS_MAP_CONFIG_BYTE = 0x24;    // Z=Z Y=Y X=X
    // byte AXIS_MAP_SIGN_BYTE = 0x0;       // X Y Z
    // 1: flat with top of REV label on left side of vehicle:
    // byte AXIS_MAP_CONFIG_BYTE = 0x24;    // Z=Z Y=-Y X=-X
    // byte AXIS_MAP_SIGN_BYTE = 0x6;       // -X -Y Z
    // 2: flat with top of REV label along front side of vehicle:
    // byte AXIS_MAP_CONFIG_BYTE = 0x21;    // Z=Z Y=-X X=Y
    // byte AXIS_MAP_SIGN_BYTE = 0x4;       // -X Y Z
    // 3: flat with top of REV label along back side of vehicle:
    // byte AXIS_MAP_CONFIG_BYTE = 0x21;    // Z=Z Y=X X=-Y
    // byte AXIS_MAP_SIGN_BYTE = 0x2;       // X -Y Z
    // 4: upright longitudinally with R nearest the front of vehicle:
    // byte AXIS_MAP_CONFIG_BYTE = 0x6;     // Z=-X Y=-Y X=-Z
    // byte AXIS_MAP_SIGN_BYTE = 0x1;       // X Y -Z     ?? if 0x7 -X -Y -Z, X and Y are LH rotations
    // 5: upright longitudinally with V nearest the front of vehicle:
    // byte AXIS_MAP_CONFIG_BYTE = 0x6;     // Z=X Y=-Y X=Z
    // byte AXIS_MAP_SIGN_BYTE = 0x4;       // -X Y Z, X is RH, but around +=180 ??
    // 6: upright crosswise with REV facing backward:
    // byte AXIS_MAP_CONFIG_BYTE = 0x9;     // Z=-X Y=-Z X=Y
    // byte AXIS_MAP_SIGN_BYTE = 0x5;       // -X Y -Z
    // 7: upright crosswise with REV facing forward:
    // byte AXIS_MAP_CONFIG_BYTE = 0x9;     // Z=-X Y=Z X=-Y
    // byte AXIS_MAP_SIGN_BYTE = 0x3;       // X -Y -Z    ?? if 0x5 -X Y -Z, X and Y are LH rotations
    // 8: flat face DOWN with top of REV label nearest the back of vehicle:
    // byte AXIS_MAP_CONFIG_BYTE = 0x21;    // Z=-Z Y=-X X=-Y
    // byte AXIS_MAP_SIGN_BYTE = 0x7;       // -X -Y -Z

    private void setRevOrientation(BNO055IMU imu, int orientation) {

        //Need to be in CONFIG mode to write to registers
        imu.write8(BNO055IMU.Register.OPR_MODE, BNO055IMU.SensorMode.CONFIG.bVal);
        sleep(19); //Changing to CONFIG mode require a delay of 19ms before doing anything else

        //Write to the AXIS_MAP_CONFIG register
        imu.write8(BNO055IMU.Register.AXIS_MAP_CONFIG, config[orientation]);

        //Write to the AXIS_MAP_SIGN register
        imu.write8(BNO055IMU.Register.AXIS_MAP_SIGN, sign[orientation]);

        //Need to change back into the IMU mode to use the gyro
        imu.write8(BNO055IMU.Register.OPR_MODE, BNO055IMU.SensorMode.IMU.bVal);
        sleep(7); //Changing back to any operating mode requires a delay of 7ms
    }
}