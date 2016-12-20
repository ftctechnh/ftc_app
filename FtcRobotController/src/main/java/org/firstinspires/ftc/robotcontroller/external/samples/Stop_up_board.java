/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

/*package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Stop_up_board extends OpMode {
  private String startDate;
  private SensorManager mSensorManager;
  private Sensor accelerometer;
  private Sensor magnetometer;

  // orientation values
  private float azimuth = 0.0f;      // value in radians
  private float pitch = 0.0f;        // value in radians
  private float roll = 0.0f;         // value in radians

  private float[] mGravity;       // latest sensor values
  private float[] mGeomagnetic;   // latest sensor values




  @Override
  public void init() {
    mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
    accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    azimuth = 0.0f;      // value in radians
    pitch = 0.0f;        // value in radians
    roll = 0.0f;

  }

  @Override
  public void start() {
    startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

    // delay value is SENSOR_DELAY_UI which is ok for telemetry, maybe not for actual robot use
    mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);

  }

  @Override
  public void loop() {
    telemetry.addData("azimuth", Math.round(Math.toDegrees(azimuth)));
    telemetry.addData("pitch", Math.round(Math.toDegrees(pitch)));
    telemetry.addData("roll", Math.round(Math.toDegrees(roll)));

  }

  @Override
  public void stop() {
    mSensorManager.unregisterListener(this);
  }
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // not sure if needed, placeholder just in case
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
*/
