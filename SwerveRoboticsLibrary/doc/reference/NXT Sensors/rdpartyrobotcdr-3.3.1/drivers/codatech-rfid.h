/*!@addtogroup other
 * @{
 * @defgroup CTRFID Codatex RFID Sensor
 * Codatex RFID Sensor
 * @{
 */

/*
 * $Id: codatech-rfid.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __CTRFID_driver_H__
#define __CTRFID_driver_H__

/** \file codatech-rfid.h
 * \brief Codatex RFID driver
 *
 * codatech-rfid.h provides an API for the Codatex RFID sensor.
 * Based on information given on http://www.codatex.com/picture/upload/en/RFID_I2C.pdf
 * and on Stephen Hall and Oliver Graff previous work on this sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Partial rewrite by Xander Soldaat to simplify API
 * - 0.3: Uses new array typedefs instead of structs
 *
 * Credits:
 * - Big thanks to Xander Soldaat for giving his work on other sensors's drivers code for ROBOTC.<br>
 *   (Seriously, I'd have given up without you !)
 * - Thanks to Stephen Hall and Oliver Graff for their work which made me understand the initialisation need !
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Sylvain CACHEUX (sylcalego@cacheux.info)
 * \author Xander Soldaat (mightor@gmail.com), version 0.2 and up
 * \date 20 february 2011
 * \version 0.3
 * \example codatech-rfid-test1.c
 * \example codatech-rfid-test2.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "drivers/common.h"
#endif

// I2C ADDRESS
#define CTRFID_I2C_ADDR         0x04      /*!< RFID I2C device address          */

// REGISTER ADDRESSES
#define CTRFID_FIRM_VER         0x00      /*!< RFID Firmware version            */
#define CTRFID_MAN_NAME         0x08      /*!< RFID Manufacturer name (CODATEX) */
#define CTRFID_SENS_TYP         0x10      /*!< RFID Sensor type (RFID)          */
#define CTRFID_STATUS           0x32      /*!< Sensor status info (0/1)         */
#define CTRFID_OFFSET           0x41      /*!< Offset for sensor's registers    */
#define CTRFID_CMD              0x00      /*!< Command to the RFID sensor       */
#define CTRFID_BYTE1            0x01      /*!< 1rst byte of transponder data    */
#define CTRFID_BYTE2            0x02      /*!< 2nd  byte of transponder data    */
#define CTRFID_BYTE3            0x03      /*!< 3rd  byte of transponder data    */
#define CTRFID_BYTE4            0x04      /*!< 4th  byte of transponder data    */
#define CTRFID_BYTE5            0x05      /*!< 5th  byte of transponder data    */
#define CTRFID_SERIAL           0xA0      /*!< address of serial number         */

// COMMANDS
#define CTRFID_CMD_STOP         0x00      /*!< Stop RFID sensor, will stop reading and
                                               put RFID sensor into sleep mode  */
#define CTRFID_CMD_SINGLESHOT   0x01      /*!< Will start a single read action, if any
                                               transponder is readable,
                                               the data will be read and stored in
                                               registers 42h to 46 h. Immediately
                                               after reading a transponder or after a
                                               max wait time of app. 0,5 seconds the sensor
                                               will enter the sleep mode */
#define CTRFID_CMD_CONTINUOUS   0x02      /*!< Continuous read, the sensor will continuously
                                               read and store data in registers 42h to 46h!
                                               After app. 2 sec. of inactivity on the I2C bus
                                               the sensor will enter the sleep mode */
#define CTRFID_CMD_BOOTLOADER   0x81      /*!< Start the bootloader */
#define CTRFID_CMD_STARTAPPFIRM 0x83      /*!< Start the application firmware */

// ---------------------------- Functions list -----------------------------------
/*!< Functions definition */
// Real RFID functions :
bool CTRFIDinit(tSensors link);
bool CTRFIDsetContinuous(tSensors link);
bool CTRFIDsetSingleShot(tSensors link);
bool CTRFIDreadTransponder (tSensors link, string &transponderID);

// Internal functions
bool _CTRFIDsendCommand(tSensors link, ubyte command);
bool _CTRFIDsendDummy(tSensors link);
bool _CTRFIDreadStatus(tSensors link, ubyte &_status);

// ---------------------------- Global variables ---------------------------------
/*!< Global variables */
tByteArray CTRFID_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray CTRFID_I2CReply;      /*!< Array to hold I2C reply data   */

bool CTRFIDreadContinuous[4] = {false, false, false, false};  /*!< Is the sensor configured for Continuous mode? */
bool CTRFIDinitialised[4] = {false, false, false, false};     /*!< Has the sensor been initialised? */


/**
 * Send a command to the RFID sensor.
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the CTRFID port number,
 * @param command the CTRFID command to be sent to the sensor
 * @return true if no error occured, false if it did
 */
bool _CTRFIDsendCommand(tSensors link, ubyte command) {
  memset(CTRFID_I2CRequest, 0, sizeof(tByteArray)); // init request

  if (!CTRFIDinitialised[link] && (command != CTRFID_CMD_STARTAPPFIRM))
    CTRFIDinit(link);

  CTRFID_I2CRequest[0] = 3;                          // Message size
  CTRFID_I2CRequest[1] = CTRFID_I2C_ADDR;            // I2C Address of RFID sensor
  CTRFID_I2CRequest[2] = CTRFID_OFFSET + CTRFID_CMD; // Address of cmd registry
  CTRFID_I2CRequest[3] = command;                    // Mode to set

  return writeI2C(link, CTRFID_I2CRequest);
}


/**
 * Send a dummy I2C packet to the sensor to wake it up
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the CTRFID port number,
 * @return true if no error occured, false if it did
 */
bool _CTRFIDsendDummy(tSensors link) {
  memset(CTRFID_I2CRequest, 0, sizeof(tByteArray)); // init request

  CTRFID_I2CRequest[0] = 1;                          // Message size
  CTRFID_I2CRequest[1] = CTRFID_I2C_ADDR;            // I2C Address of RFID sensor

  return writeI2C(link, CTRFID_I2CRequest);
  memset(CTRFID_I2CRequest, 0, sizeof(tByteArray));
}

/**
 * Check if the sensor is in continuous mode.
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the CTRFID port number,
 * @param _status the sensor's status (continuous mode : 1, idle : 0),
 * @return true if no error occured, false if it did
 */
bool _CTRFIDreadStatus(tSensors link, ubyte &_status) {
  memset(CTRFID_I2CRequest, 0, sizeof(tByteArray)); // init request

  if(!_CTRFIDsendDummy(link))
    return false;

  if (!CTRFIDinitialised[link])
    CTRFIDinit(link);

  if (CTRFIDreadContinuous[link])
    return true;

  CTRFID_I2CRequest[0] = 2;               // Message size
  CTRFID_I2CRequest[1] = CTRFID_I2C_ADDR; // I2C Address
  CTRFID_I2CRequest[2] = CTRFID_STATUS;   // Address for sensor's status

  if (!writeI2C(link, CTRFID_I2CRequest, CTRFID_I2CReply, 1))
    return false;

  _status = CTRFID_I2CReply[0];

  return true;
}


/**
 * Configure the sensor for reading transponders
 * @param link the CTRFID port number,
 * @return true if no error occured, false if it did
 */
bool CTRFIDinit(tSensors link) {
  if (_CTRFIDsendCommand(link, CTRFID_CMD_STARTAPPFIRM)) {
    CTRFIDinitialised[link] = true;
    return true;
  } else {
    return false;
  }
}


/**
 * Configure the sensor for reading transponders
 * in continuous mode
 * @param link the CTRFID port number,
 * @return true if no error occured, false if it did
 */
bool CTRFIDsetContinuous(tSensors link) {
  if(!_CTRFIDsendDummy(link))
    return false;

  if (_CTRFIDsendCommand(link, CTRFID_CMD_CONTINUOUS)) {
    CTRFIDreadContinuous[link] = true;
    return true;
  } else {
    return false;
  }
}


/**
 * Configure the sensor for reading transponders
 * in single shot mode
 * @return true if no error occured, false if it did
 */
bool CTRFIDsetSingleShot(tSensors link) {
  if(!_CTRFIDsendDummy(link))
    return false;

  if (_CTRFIDsendCommand(link, CTRFID_CMD_SINGLESHOT)) {
    CTRFIDreadContinuous[link] = false;
    return true;
  } else {
    return false;
  }
}


/**
 * Read transponder ID's bytes. This function needs that a reading mode to be selected before using it.
 * @param link the CTRFID port number,
 * @param transponderID the read transponder ID,
 * @return true if no error occured, false if it did
 */
bool CTRFIDreadTransponder(tSensors link, string &transponderID) {
  ubyte _status;

  // Always send a dummy packet to make sure the sensor is awake
  if(!_CTRFIDsendDummy(link)) {
    return false;
  }

  if (!CTRFIDinitialised[link]) {
    CTRFIDinit(link);
    // Wait for the initialisation to be done
    wait1Msec(100);
  }

  if(CTRFIDreadContinuous[link] == false) {
    // the mode has not been set, so set to single shot mode
    CTRFIDsetSingleShot(link);

    // Wait for RF setup time.
    wait1Msec(250);
  } else {
    _CTRFIDreadStatus(link, _status);
    if (_status == 0) {
      CTRFIDsetContinuous(link);
      // Wait for RF setup time.
      wait1Msec(250);
    }
  }

  // Retrieve the transponder's address
  memset(CTRFID_I2CRequest, 0, sizeof(tByteArray));
  CTRFID_I2CRequest[0] = 2;                                        // Message size
  CTRFID_I2CRequest[1] = CTRFID_I2C_ADDR;                     // I2C Address of RFID sensor
  CTRFID_I2CRequest[2] = CTRFID_OFFSET + CTRFID_BYTE1;   // Address byte1 registry
  if (!writeI2C(link, CTRFID_I2CRequest, CTRFID_I2CReply, 5))
    return false;

  // formating the value read into a string of 10 chars
  // first : clear the current value of TransponderID
  // (if not, the next instructions will concatenate as much IDs as read !)
  strcpy(transponderID, "");
  for (int i=0;i<5;i++) {
    // "%02x"  will pad the hex number with a zero to make it two digits long at all times.
    StringFormat(transponderID, "%s%02x", transponderID, CTRFID_I2CReply[i]);
  }
  return true;
}

#endif // __CODATEX_RFID_driver_H__

/*
 * $Id: codatech-rfid.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
