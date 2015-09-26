/*!@addtogroup mindsensors
 * @{
 * @defgroup MSIMU AbsoluteIMU Sensor
 * Mindsensors AbsoluteIMU Sensor (MSIMU) driver
 * @{
 */

/*
 * $Id: mindsensors-imu.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MSIMU_H__
#define __MSIMU_H__
/** \file mindsensors-imu.h
 * \brief Mindsensors AbsoluteIMU Sensor driver
 *
 * mindsensors-imu.h provides an API for the Mindsensors AbsoluteIMU Sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 *
 * Credits:
 * - Big thanks to Mindsensors for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 23 August 2012
 * \version 0.1
 * \example mindsensors-imu-test1.c
 * \example mindsensors-imu-test2.c
 * \example mindsensors-imu-test3.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define MSIMU_IMU_I2C_ADDR            0x22  /*!< IMU I2C address */

#define MSIMU_REG_CMD                 0x41  /*!< Command register */

#define MSIMU_REG_TILT_ALL_AXES       0x42  /*!< All Axes for Tilt */
#define MSIMU_REG_ACC_ALL_AXES        0x45  /*!< All Axes for Accel */
#define MSIMU_REG_COMPASS_HEADING     0x4B  /*!< Compass heading */
#define MSIMU_REG_COMPASS_ALL_FIELDS  0x4D  /*!< All magnetic fields for compass */
#define MSIMU_REG_GYRO_ALL_AXES       0x53  /*!< All Axes for Gyro */
#define MSIMU_REG_GYRO_FILTER         0x5A  /*!< Filter level for Gyro */

#define MSIMU_CMD_COMPASS_START_CAL   0x43  /*!< Accelerometer 2G range */
#define MSIMU_CMD_COMPASS_STOP_CAL    0x63  /*!< Accelerometer 2G range */

#define MSIMU_CMD_ACC_RANGE_2G        0x31  /*!< Accelerometer 2G range */
#define MSIMU_CMD_ACC_RANGE_4G        0x32  /*!< Accelerometer 4G range */
#define MSIMU_CMD_ACC_RANGE_8G        0x33  /*!< Accelerometer 8G range */
#define MSIMU_CMD_ACC_RANGE_16G       0x34  /*!< Accelerometer 16G range */

#define MSIMU_GYRO_FILTER_NONE        0     /*!< Gyro filter level: none */
#define MSIMU_GYRO_FILTER_LEVEL_1     1     /*!< Gyro filter level: 1 */
#define MSIMU_GYRO_FILTER_LEVEL_2     2     /*!< Gyro filter level: 2 */
#define MSIMU_GYRO_FILTER_LEVEL_3     3     /*!< Gyro filter level: 3 */
#define MSIMU_GYRO_FILTER_LEVEL_4     4     /*!< Gyro filter level: 4 (default) */
#define MSIMU_GYRO_FILTER_LEVEL_5     5     /*!< Gyro filter level: 5 */
#define MSIMU_GYRO_FILTER_LEVEL_6     6     /*!< Gyro filter level: 6 */
#define MSIMU_GYRO_FILTER_LEVEL_7     7     /*!< Gyro filter level: 7 (highest) */


#define MSIMU_TILT_X_AXIS           MSIMU_REG_TILT_X_AXIS
#define MSIMU_TILT_Y_AXIS           MSIMU_REG_TILT_Y_AXIS
#define MSIMU_TILT_Z_AXIS           MSIMU_REG_TILT_Z_AXIS

#define MSIMU_ACC_X_AXIS            MSIMU_REG_ACC_X_AXIS
#define MSIMU_ACC_Y_AXIS            MSIMU_REG_ACC_Y_AXIS
#define MSIMU_ACC_Z_AXIS            MSIMU_REG_ACC_Z_AXIS

#define MSIMU_GYRO_X_AXIS           MSIMU_REG_GYRO_X_AXIS
#define MSIMU_GYRO_Y_AXIS           MSIMU_REG_GYRO_Y_AXIS
#define MSIMU_GYRO_Z_AXIS           MSIMU_REG_GYRO_Z_AXIS

tByteArray MSIMU_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray MSIMU_I2CReply;      /*!< Array to hold I2C reply data */

bool _MSIMUsendCMD(tSensors link, ubyte cmd);
bool MSIMUreadTiltAxes(tSensors link, int &_x, int &_y, int &_z);
bool MSIMUreadGyroAxes(tSensors link, int &_x, int &_y, int &_z);
bool MSIMUreadAccelAxes(tSensors link, int &_x, int &_y, int &_z);
bool MSIMUreadMagneticFields(tSensors link,  int &_x, int &_y, int &_z);
int MSIMUreadHeading(tSensors link);
bool MSIMUsetGyroFilter(tSensors link, ubyte level);


/**
 * Send a command to the sensor
 *
 * Note: this is an internal function and should be called directly
 * @param link the port number
 * @param cmd the command to be sent
 * @return true if no error occured, false if it did
 */
bool _MSIMUsendCMD(tSensors link, ubyte cmd)
{
	MSIMU_I2CRequest[0] = 3;                        // Message size
  MSIMU_I2CRequest[1] = MSIMU_IMU_I2C_ADDR;      // I2C Address
	MSIMU_I2CRequest[2] = MSIMU_REG_CMD;  // Register address
	MSIMU_I2CRequest[3] = cmd;  // command

  return writeI2C(link, MSIMU_I2CRequest);
}


/**
 * Read all three tilt axes
 * @param link the port number
 * @param _x data for x axis in degrees per second
 * @param _y data for y axis in degrees per second
 * @param _z data for z axis in degrees per second
 * @return true if no error occured, false if it did
 */
bool MSIMUreadTiltAxes(tSensors link, int &_x, int &_y, int &_z){
	MSIMU_I2CRequest[0] = 2;                        // Message size
  MSIMU_I2CRequest[1] = MSIMU_IMU_I2C_ADDR;      // I2C Address
	MSIMU_I2CRequest[2] = MSIMU_REG_TILT_ALL_AXES;  // Register address

  if (!writeI2C(link, MSIMU_I2CRequest, MSIMU_I2CReply, 3))
    return false;

  _x = (MSIMU_I2CReply[0] >= 128) ? (int)MSIMU_I2CReply[0] - 256 : (int)MSIMU_I2CReply[0];
  _y = (MSIMU_I2CReply[1] >= 128) ? (int)MSIMU_I2CReply[1] - 256 : (int)MSIMU_I2CReply[1];
  _z = (MSIMU_I2CReply[2] >= 128) ? (int)MSIMU_I2CReply[2] - 256 : (int)MSIMU_I2CReply[2];

  return true;
}


/**
 * Read all three axes of the gyro
 * @param link the port number
 * @param _x data for x axis in degrees per second
 * @param _y data for y axis in degrees per second
 * @param _z data for z axis in degrees per second
 * @return true if no error occured, false if it did
 */
bool MSIMUreadGyroAxes(tSensors link, int &_x, int &_y, int &_z){
	MSIMU_I2CRequest[0] = 2;                        // Message size
  MSIMU_I2CRequest[1] = MSIMU_IMU_I2C_ADDR;      // I2C Address
	MSIMU_I2CRequest[2] = MSIMU_REG_GYRO_ALL_AXES;  // Register address

  if (!writeI2C(link, MSIMU_I2CRequest, MSIMU_I2CReply, 6))
    return false;

  _x = MSIMU_I2CReply[0] + ((int)(MSIMU_I2CReply[1]<<8));
  _y = MSIMU_I2CReply[2] + ((int)(MSIMU_I2CReply[3]<<8));
  _z = MSIMU_I2CReply[4] + ((int)(MSIMU_I2CReply[5]<<8));
  return true;
}


/**
 * Read all of the accelerometer axes
 * @param link the port number
 * @param _x variable to hold X axis data
 * @param _y variable to hold Y axis data
 * @param _z variable to hold Z axis data
 * @return true if no error occured, false if it did
 */
bool MSIMUreadAccelAxes(tSensors link, int &_x, int &_y, int &_z){
	MSIMU_I2CRequest[0] = 2;                        // Message size
  MSIMU_I2CRequest[1] = MSIMU_IMU_I2C_ADDR;      // I2C Address
	MSIMU_I2CRequest[2] = MSIMU_REG_ACC_ALL_AXES;  // Register address

  if (!writeI2C(link, MSIMU_I2CRequest, MSIMU_I2CReply, 6))
    return false;

  _x = MSIMU_I2CReply[0] + ((int)(MSIMU_I2CReply[1]<<8));
  _y = MSIMU_I2CReply[2] + ((int)(MSIMU_I2CReply[3]<<8));
  _z = MSIMU_I2CReply[4] + ((int)(MSIMU_I2CReply[5]<<8));
  return true;
}



/**
 * Read all the magnetic field strengths
 * @param link the port number
 * @param _x variable to hold X axis data
 * @param _y variable to hold Y axis data
 * @param _z variable to hold Z axis data
 * @return true if no error occured, false if it did
 */
bool MSIMUreadMagneticFields(tSensors link, int &_x, int &_y, int &_z){
	MSIMU_I2CRequest[0] = 2;                        // Message size
  MSIMU_I2CRequest[1] = MSIMU_IMU_I2C_ADDR;      // I2C Address
	MSIMU_I2CRequest[2] = MSIMU_REG_ACC_ALL_AXES;  // Register address

  if (!writeI2C(link, MSIMU_I2CRequest, MSIMU_I2CReply, 6))
    return false;

  _x = MSIMU_I2CReply[0] + ((int)(MSIMU_I2CReply[1]<<8));
  _y = MSIMU_I2CReply[2] + ((int)(MSIMU_I2CReply[3]<<8));
  _z = MSIMU_I2CReply[4] + ((int)(MSIMU_I2CReply[5]<<8));

  return true;
}


/**
 * Read the current magnetic compass heading
 * @param link the port number
 * @return the current heading
 */
int MSIMUreadHeading(tSensors link)
{
	MSIMU_I2CRequest[0] = 2;                        // Message size
  MSIMU_I2CRequest[1] = MSIMU_IMU_I2C_ADDR;      // I2C Address
	MSIMU_I2CRequest[2] = MSIMU_REG_COMPASS_HEADING;  // Register address

  if (!writeI2C(link, MSIMU_I2CRequest, MSIMU_I2CReply, 2))
    return 0;

  return MSIMU_I2CReply[0] + ((int)(MSIMU_I2CReply[1]<<8));
}


/**
 * Set the level of filtering of the Gryo readings.
 * @param link the port number
 * @param level the level of filtering (0-7) where 0 is none and 7 is the highest amount of filtering, 4 is the default level.
 * @return true if no error occured, false if it did
 */
bool MSIMUsetGyroFilter(tSensors link, ubyte level)
{
	MSIMU_I2CRequest[0] = 3;                        // Message size
  MSIMU_I2CRequest[1] = MSIMU_IMU_I2C_ADDR;      // I2C Address
	MSIMU_I2CRequest[2] = MSIMU_REG_GYRO_FILTER;  // Register address
	MSIMU_I2CRequest[3] = level;  // filtering level

  return writeI2C(link, MSIMU_I2CRequest);
}

#endif // __MSIMU_H__

/*
 * $Id: mindsensors-imu.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
