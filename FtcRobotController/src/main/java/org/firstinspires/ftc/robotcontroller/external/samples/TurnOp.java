package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An op mode that uses the geomagnetic and accelerometer values to calculate device
 * orientation and return those values in telemetry.
 * It makes use of getRotationMatrix() and getOrientation(), but does not use
 * remapCoordinateSystem() which one might want.
 * see: http://developer.android.com/reference/android/hardware/SensorManager.html#remapCoordinateSystem(float[], int, int, float[])
 */
public class TurnOp extends OpMode implements SensorEventListener {
  private String startDate;
  private SensorManager mSensorManager;
  private Sensor accelerometer;
  private Sensor magnetometer;
  DcMotor motor1 ;
  DcMotor motor2 ;

  // orientation values
  private float azimuth = 0.0f;      // value in radians
  private float pitch = 0.0f;        // value in radians
  private float roll = 0.0f;         // value in radians

  private float[] mGravity;       // latest sensor values
  private float[] mGeomagnetic;   // latest sensor values

  static final double speed = 0.15;
  ElapsedTime elapsedTime = new ElapsedTime();

  /*
  * Constructor
  */
  public TurnOp() {

  }

  /*
  * Code to run when the op mode is first enabled goes here
  * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
  */
  @Override
  public void init() {
    mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
    accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    motor1 = hardwareMap.dcMotor.get("motor_1") ;
    motor2 = hardwareMap.dcMotor.get("motor_2") ;

    motor1.setDirection(DcMotor.Direction.REVERSE) ;

    azimuth = 0.0f;      // value in radians
    pitch = 0.0f;        // value in radians
    roll = 0.0f;         // value in radians
  }

  /*
* Code to run when the op mode is first enabled goes here
* @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
*/
  @Override
  public void start() {
    startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

    // delay value is SENSOR_DELAY_UI which is ok for telemetry, maybe not for actual robot use
    mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);

    elapsedTime.reset();
    elapsedTime.startTime();

     double driveTime ;
  }

  /*
  * This method will be called repeatedly in a loop
  * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
  */
  @Override
  public void loop() {
    double driveTime ;
    driveTime = elapsedTime.time();

    if (driveTime <= 5.0) {

      moveStraight();
      DbgLog.msg("===Moving Straight===");

    } else if (driveTime >= 5.0) {

      leftTurn();
      DbgLog.msg("===Turning Left===");

      if (Math.round(Math.toDegrees(azimuth)) % 90 == 0 && driveTime >= 5.5) {

        elapsedTime.reset();
        elapsedTime.startTime();
        DbgLog.msg("===Reseting Elapsed Time===");
      }
    }
//        telemetry.addData("1 Start", "OrientOp started at " + startDate);
//        telemetry.addData("2 note1", "values below are in degrees" );
//        telemetry.addData("3 note2", "azimuth relates to magnetic north" );
//        telemetry.addData("4 note3", " " );
    telemetry.addData("azimuth", Math.round(Math.toDegrees(azimuth)));
    telemetry.addData("pitch", Math.round(Math.toDegrees(pitch)));
    telemetry.addData("roll", Math.round(Math.toDegrees(roll)));
  }

  /*
  * Code to run when the op mode is first disabled goes here
  * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
  */
  @Override
  public void stop() {
    mSensorManager.unregisterListener(this);
  }

  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // not sure if needed, placeholder just in case
  }

  public void leftTurn() {
    motor1.setPower(-speed);
    motor2.setPower(speed);
  }

  public void moveStraight() {
    motor1.setPower(speed);
    motor2.setPower(speed);
  }

  public void onSensorChanged(SensorEvent event) {
    // we need both sensor values to calculate orientation
    // only one value will have changed when this method called, we assume we can still use the other value.
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
      mGravity = event.values;
    }
    if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
      mGeomagnetic = event.values;
    }
    if (mGravity != null && mGeomagnetic != null) {  //make sure we have both before calling getRotationMatrix
      float R[] = new float[9];
      float I[] = new float[9];
      boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
      if (success) {
        float orientation[] = new float[3];
        SensorManager.getOrientation(R, orientation);
        azimuth = orientation[0]; // orientation contains: azimuth, pitch and roll
        pitch = orientation[1];
        roll = orientation[2];
      }
    }
  }
}