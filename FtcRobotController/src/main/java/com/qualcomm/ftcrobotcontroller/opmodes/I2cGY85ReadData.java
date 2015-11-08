/* Copyright (c) 2015 Qualcomm Technologies Inc

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

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import org.usfirst.FTC5866.library.*;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;

/**
 * Example to read data from GY85 9DOF sensor
 */


public class I2cGY85ReadData extends OpMode {


  public static final int ADXL345_I2C_ADDRESS = 0x53;


  public static final int ACC_DATA_FORMAT = 0x31;
  public static final int ACC_SET_8G_MODE = 0x02;
  public static final int ACC_POWER_CTL = 0x2D;
  public static final int ACC_DISABLE_PM = 0x08;
  public static final int ACC_DATAX0 = 0x32;
  public static final int ACC_DATAX1 = 0x33;
  public static final int ACC_DATAY0 = 0x34;
  public static final int ACC_DATAY1 = 0x35;
  public static final int ACC_DATAZ0 = 0x36;
  public static final int ACC_DATAZ1 = 0x37;
  public static final int TOTAL_MEMORY_LENGTH = 0x06;

  int port = 5;

  private DataLogger dl;
  private Wire ds;
  private int readCount = 0;
  private int distance;
  private long pingTime;

  public void init() {
    dl = new DataLogger("ADXL345_Z_Accel");
    ds = new Wire(hardwareMap, "GY85", ADXL345_I2C_ADDRESS);

    dl.addField("Micros");      //Sensor reading time in microseconds
    dl.addField("X_Accel");
    dl.addField("Y_Accel");
    dl.addField("Z_Accel");
    dl.newLine();
  }

  public void start() {
    ds.beginWrite(ADXL345_I2C_ADDRESS);
    ds.write(ACC_DATA_FORMAT);
    ds.write(ACC_SET_8G_MODE);
    ds.endWrite();
    ds.beginWrite(ADXL345_I2C_ADDRESS);
    ds.write(ACC_POWER_CTL);
    ds.write(ACC_DISABLE_PM);
    ds.endWrite();
    pingTime = System.currentTimeMillis();
  }

  @Override
  public void loop() {
    if ((System.currentTimeMillis() - pingTime) > 100) {
      ds.beginWrite(ADXL345_I2C_ADDRESS);
      ds.write(ACC_DATAX0);
      ds.endWrite();
      ds.beginWrite(ADXL345_I2C_ADDRESS);
      ds.requestFrom(ADXL345_I2C_ADDRESS, 6);
      pingTime = System.currentTimeMillis();
    }

    if (ds.responseCount() > 0) {
      ds.getResponse();
      if (ds.isRead()) {
        long micros = ds.micros();
        int Xaxis = ds.read() << 8 | ds.read();
        int Yaxis = ds.read() << 8 | ds.read();
        int Zaxis = ds.read() << 8 | ds.read();
        dl.addField(micros);
        dl.addField(Xaxis);
        dl.addField(Yaxis);
        dl.addField(Zaxis);

        readCount++;
        telemetry.addData("Count", readCount);
        telemetry.addData("Time", micros / 1000);
        telemetry.addData("X_Acc", Xaxis);
        telemetry.addData("Y_Acc", Yaxis);
        telemetry.addData("Z_Acc", Zaxis);
      }
    }
  }

  public void stop() {
    dl.closeDataLogger();
    ds.close();
  }
}