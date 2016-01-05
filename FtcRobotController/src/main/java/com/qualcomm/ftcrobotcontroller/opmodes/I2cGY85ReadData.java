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

  public static final int ACC_DEVICE_ID = 0xE5;
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
  public static final int ACC_FIFO_STATUS = 0x39;
  public static final int TOTAL_MEMORY_LENGTH = 0x06;

  int port = 5;

  private DataLogger dl;
  private Wire ds;
  private int readCount = 0;
  private int distance;
  private long accTimeStamp;
  private int Xaxis = 0;
  private int Yaxis = 0;
  private int Zaxis = 0;
  private int counter = 0;

  public void init() {
    DbgLog.msg("=====Start Init=====");
    dl = new DataLogger("ADXL345_Z_Accel");
    ds = new Wire(hardwareMap, "GY85", 2 * ADXL345_I2C_ADDRESS);
    DbgLog.msg("=====Initialized Items=====");

    dl.addField("Micros");      //Sensor reading time in microseconds
    dl.addField("X_Accel");
    dl.addField("Y_Accel");
    dl.addField("Z_Accel");
    dl.newLine();
  }

  public void start() {
    DbgLog.msg("=====Start Acc Setup=====");
    ds.write(ACC_DATA_FORMAT, ACC_SET_8G_MODE);
    DbgLog.msg("=====Data Format Done=====");
    ds.write(ACC_POWER_CTL, ACC_DISABLE_PM);
    DbgLog.msg("=====PM Done=====");
    ds.requestFrom(ACC_DEVICE_ID, 1);
  }

  @Override
  public void loop() {
    if (isACCUpdate()) {
      dl.addField(accTimeStamp / 1e6);
      dl.addField(Xaxis);
      dl.addField(Yaxis);
      dl.addField(Zaxis);
      dl.newLine();

      readCount++;
      telemetry.addData("ACC", "X-Axis: " + Xaxis + " Y-Axis: " + Yaxis + " Z-Axis: " + Zaxis);
      String XaxisHex = Integer.toHexString(Xaxis);
      String YaxisHex = Integer.toHexString(Yaxis);
      String ZaxisHex = Integer.toHexString(Zaxis);
      telemetry.addData("ACCHex", "X-Axis: " + XaxisHex + " Y-Axis: " + YaxisHex + " Z-Axis: " + ZaxisHex);

      telemetry.addData("Time", accTimeStamp / 1e6);
    }

  }

  private boolean isACCUpdate() {
    boolean isNew = false;
    if ((System.currentTimeMillis() - accTimeStamp) > 500) {
      ds.write(ACC_FIFO_STATUS);
      ds.requestFrom(ACC_FIFO_STATUS, 2);
      ds.write(ACC_DATAX0);
      ds.requestFrom(ACC_DATAX0, 6);
      accTimeStamp = System.currentTimeMillis();


      if ((ds.responseCount() > 0) && ds.getResponse()) {
        int regNumber = ds.registerNumber();
        if (ds.isRead()) {
          int regCount = ds.available();
          switch (regNumber) {
            case ACC_DEVICE_ID:
              if (regCount == 1) {
                int acc_did = ds.read();
                if ((acc_did & 0xFF) != ACC_DEVICE_ID) {
                  DbgLog.msg(String.format("=====  DID 0x%02X =====", acc_did));
                } else {
                  ds.requestFrom(ACC_DATAX0, 6);             // Request Data
                  DbgLog.msg(String.format("=====  GOT DID =====", acc_did));
                }
              } else {
                telemetry.addData("Error", regNumber + " length 1 != " + regCount);
                DbgLog.msg(String.format("ERROR reg 0x%02X Len = 0x%02X (!= 1)", regNumber, regCount));
              }
              break;
            case ACC_DATAX0:
              if (regCount == 6) {                        // Check register count
                accTimeStamp = ds.micros();              // Reading time
                Xaxis = ds.readLH();              // Read X axis
                Yaxis = ds.readLH();              // Read Y axis
                Zaxis = ds.readLH();              // Read Z axis
                isNew = true;
                DbgLog.msg(String.format("=====  GOT DATAX0 6 regs" + counter + " ====="));
                DbgLog.msg(String.format("=====  DataX " + Xaxis + " ====="));
              } else {
                telemetry.addData("Error", regNumber + " length 6 != " + regCount);
                DbgLog.msg(String.format("ERROR reg 0x%02X Len = 0x%02X (!= 1)", regNumber, regCount));
              }
              break;
            case ACC_FIFO_STATUS:
              if (regCount == 2) {
                accTimeStamp = ds.micros();
                int fifo0 = ds.read();
                int fifo1 = ds.read();
                DbgLog.msg(String.format("===== GOT FIFO_Stat 2 regs 0x%2X%2X =====", fifo0,fifo1));
              }
              break;
            default:
              telemetry.addData("Error", "Unexpected register " + regNumber);
              break;
          }
        }
      }
      counter++;
    }
    return isNew;
  }

  public void stop() {
    dl.closeDataLogger();
    ds.close();
  }
}