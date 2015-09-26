/*!@addtogroup mindsensors
 * @{
 * @defgroup msll LineLeader Sensor
 * LineLeader Sensor
 * @{
 */

/*
 * $Id: mindsensors-lineleader.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MSLL_H__
#define __MSLL_H__

/** \file mindsensors-lineleader.h
 * \brief Mindsensors Line Tracking Sensor
 *
 * mindsensors-lineleader.h provides an API for the Mindsensors Line Tracking Sensor.
 *
 * Changelog:
 *  - 0.1 Initial	TFR<br>
 *  - 0.2 Added ability to read multi-ubyte values for raw, white and black thresholds.<br>
 *  - 0.3 Added PID factor registers (Read/Write), Added new commands 'S'napShot, 'R'eset,
 *      and changed function and method names to be more C++ like.<br>
 *  - 0.4 Modified to conform to new "naming standard" and be part of the driver suite.<br>
 *       changed all (int) casts to ubyteToInt() calls.<br>
 *       all direct I2C calls changed to readI2C() and writeI2C() calls.
 *  - 0.5 Bug in LLreadSteering fixed
 *  - 0.6 Added LLreadSensorUncalibrated
 *
 * License: You may use this code as you wish, provided you give credit where it's due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Thom Roach
 * \author Xander Soldaat (version 0.4+)
 * \date 29 October 2009
 * \version 0.6
 * \example mindsensors-lineleader-test2.c
 * \example mindsensors-lineleader-test3.c
 * \example mindsensors-lineleader-test4.c
 */

#pragma systemFile

#ifndef __COMMON_H__
	#include "common.h"
#endif

//*******************************************************************************
// REGISTER LOCATIONS AND COMMANDS for the Line Leader Sensor
//*******************************************************************************
#define LL_I2C_ADDR   		0x02  /*!< I2C address used by the LL */
#define LL_CMD_REG    		0x41  /*!< Register used for issuing commands */

#define LL_SETPOINT       0x45  /*!< average value considered center of line (def=45) */
#define LL_KP_VALUE 			0x46  /*!< P value of PID control */
#define LL_KI_VALUE 			0X47  /*!< I value of PID control */
#define LL_KD_VALUE 			0X48  /*!< D value of PID control */
#define LL_KP_FACTOR      0x61  /*!< P factor for P value of PID control */
#define LL_KI_FACTOR      0X62  /*!< I factor for I value of PID control */
#define LL_KD_FACTOR      0X63  /*!< D factor for D value of PID control */

#define LL_READ_STEERING 	0x42  /*!< steering value for simple mode line following */
#define LL_READ_AVERAGE  	0X43  /*!< weighted average value for all sensors in array */
#define LL_READ_RESULT   	0X44  /*!< 1 or 0 for line or no line for all 8 sensors */
#define LL_SENSOR_RAW 		0X49  /*!< ubyte array (8) with raw value for each sensor */
#define LL_WHITE_LIMIT 		0X51  /*!< ubyte array (8) with raw value of white calibration for each sensor */
#define LL_BLACK_LIMIT		0X59  /*!< ubyte array (8) with raw value of black calibration for each sensor */
#define LL_SENSOR_UNCAL   0x74  /*!< ubyte array (16) with uncalibrated sensor data */

tByteArray LL_I2CRequest;       /*!< Array to hold I2C command data */
tByteArray LL_I2CReply;         /*!< Array to hold I2C reply data */
ubyte oneByte;

//*******************************************************************************
// PUBLIC Line Leader functions
//*******************************************************************************
bool LLinit(tSensors link);		                            /*!< Set up Line Leader sensor type */
bool LLwakeUp(tSensors link);					                    /*!< Wake sensor from sleep mode */
bool LLsleep(tSensors link);					                    /*!< Sleep to conserve power when not in use */

bool LLinvertLineColor(tSensors link);	                  /*!< Inverts detected line color */
bool LLresetLineColor(tSensors link);                     /*!< Resets line color to dark on light bkgrnd */
bool LLtakeSnapshot(tSensors link);                       /*!< takes a snapshot to determine line color */

bool LLcalWhite(tSensors link);                           /*!< Set white threshold for light area */
bool LLcalBlack(tSensors link);                           /*!< Set black threshold for dark area */

bool LLsetPoint(tSensors link, ubyte data);                /*!< WRITE mid-point or center of line value */
int LLsetPoint(tSensors link);                            /*!< READ SetPoint value */

bool LLsetKp(tSensors link, ubyte data, ubyte factor);      /*!< WRITE Kp value */
int LLreadKp(tSensors link);                              /*!< READ Kp value */
int LLreadKpFactor(tSensors link);                        /*!< READ p factor value */

bool LLsetKi(tSensors link, ubyte data, ubyte factor);      /*!< WRITE Ki value */
int LLreadKi(tSensors link);                              /*!< READ Ki value */
int LLreadKiFactor(tSensors link);                        /*!< READ i factor value */

bool LLsetKd(tSensors link, ubyte data, ubyte factor);      /*!< WRITE Kd value */
int LLreadKd(tSensors link);                              /*!< READ Kd value */
int LLreadKdFactor(tSensors link);                        /*!< READ d factor value */

int LLreadSteering(tSensors link);                        /*!< Read internally calculated steering value */
int LLreadAverage(tSensors link);                         /*!< Read weighted sensor array average value */
int LLreadResult(tSensors link);			                    /*!< Read boolean sensor array values for all sensors */
bool LLreadSensorRaw(tSensors link, tByteArray &pMsg); 	  /*!< Return array of raw light values (8 bytes) */
bool LLreadSensorUncalibrated (tSensors link, tIntArray &sensorValues);  /*!< Return array of raw uncalibrated light values (8 ints) */
bool LLreadWhiteThresh(tSensors link, tByteArray &pMsg);  /*!< Return array of white thresholds (8 bytes) */
bool LLreadBlackThresh(tSensors link, tByteArray &pMsg);  /*!< Return array of black thresholds (8 bytes) */

//*******************************************************************************
// INTERNAL USE ONLY - Line Leader functions - used by the above
//*******************************************************************************
bool _lineLeader_cmd(tSensors link, ubyte cmd);                      /*!< Send a command to the Line Leader */
bool _lineLeader_write(tSensors link, ubyte regToWrite, ubyte data);  /*!< Write to a Line Leader Register */
bool _lineLeader_read(tSensors link, ubyte regToRead, ubyte &retval); /*!< Read one ubyte from a Line Leader Register */
bool _lineLeader_read(tSensors link, ubyte regToRead, int numBytes, tByteArray &pDataMsg);  /*!< Read data from a Line Leader Register */

//*******************************************************************************
// FUNCTION DEFINITIONS
//*******************************************************************************
/**
 * This function sends a command to the lineleader over I2C.
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the sensor port number
 * @param cmd the command to be sent
 * @return true if no error occured, false if it did

PRELIMINARY COMMANDS FROM NXC LIB CODE
 - A American frequency compensation
 - B for black calibration
 - D sensor power down
 - E European frequency compensation
 - I invert line color
 - P power on sensor
 - R reset line color to dark
 - S snapshot to determine line color
 - U Universal frequency compensation (default)
 - S setpoint based on snapshot (automatically set's invert if needed)
 - W White balance
 */
bool _lineLeader_cmd(tSensors link, ubyte cmd) {
  LL_I2CRequest[0] = 3;             // Message size
  LL_I2CRequest[1] = LL_I2C_ADDR;   // I2C Address
  LL_I2CRequest[2] = LL_CMD_REG;    // Register used for issuing commands
  LL_I2CRequest[3] = cmd;           // Command to be executed

  return writeI2C(link, LL_I2CRequest);
}


/**
 * This function writes data to a register in the LL sensor over I2C.
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the sensor port number
 * @param regToWrite the register to write to
 * @param data the value to write to the register
 * @return true if no error occured, false if it did
 */

bool _lineLeader_write(tSensors link, ubyte regToWrite, ubyte data) {

  memset(LL_I2CRequest, 0, sizeof(tByteArray));
	LL_I2CRequest[0] = 3;             // Message size
  LL_I2CRequest[1] = LL_I2C_ADDR;   // I2C Address
  LL_I2CRequest[2] = regToWrite;    // Register address to set
  LL_I2CRequest[3] = data;          // value to place in register address

  return writeI2C(link, LL_I2CRequest);
}


/**
 * This function reads one ubyte from a register in the LL sensor over I2C.
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the sensor port number
 * @param regToRead the register to read from
 * @param retval the ubyte in which to store the reply
 * @return true if no error occured, false if it did
 */
bool _lineLeader_read(tSensors link, ubyte regToRead, ubyte &retval) {
	memset(LL_I2CRequest, 0, sizeof(LL_I2CRequest));
	LL_I2CRequest[0] =  2;
	LL_I2CRequest[1] =  LL_I2C_ADDR;
	LL_I2CRequest[2] =  regToRead;

  if (!writeI2C(link, LL_I2CRequest, LL_I2CReply, 1))
    return false;

  retval = (int)LL_I2CReply[0];

  return true;
}


/**
 * This function reads multiple bytes from a register in the LL sensor over I2C.
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the sensor port number
 * @param regToRead the register to read from
 * @param numBytes the number of bytes to read
 * @param pDataMsg tByteArray to store reply
 * @return true if no error occured, false if it did
 */
bool _lineLeader_read(tSensors link, ubyte regToRead, int numBytes, tByteArray &pDataMsg) {
	memset(LL_I2CRequest, 0, sizeof(tByteArray));
	memset(pDataMsg, 0, sizeof(tByteArray));

	LL_I2CRequest[0] =  2;
	LL_I2CRequest[1] =  LL_I2C_ADDR;
	LL_I2CRequest[2] =  regToRead;


  if (!writeI2C(link, LL_I2CRequest, LL_I2CReply, numBytes))
    return false;

  // copy the result into the array to be returned.
  memcpy(pDataMsg, LL_I2CReply, sizeof(tByteArray));
  return true;
}


/**
 * This function initializes the line leader to prepare for use.
 * Issuing a command also wakes the line leader as needed.
 * @param link the sensor port number
 * @return true if no error occured, false if it did
 */
bool LLinit(tSensors link) {
	nI2CBytesReady[link] = 0;
	SensorType[link] = sensorI2CCustom9V;
	if (!LLwakeUp(link))
	  return false;
	if (!LLresetLineColor(link))
	  return false;
	return true;
}


/**
 * This function wakes the line leader to prepare for use.
 * Issuing a command also wakes the line leader as needed.
 * @param link the sensor port number
 * @return true if no error occured, false if it did
 */
bool LLwakeUp(tSensors link) {
  if (!_lineLeader_cmd(link, 'P')) // Sort by size
    return false;
  return true;
}


/**
 * This function puts the line leader to sleep conserve power.
 * @param link the sensor port number
 * @return true if no error occured, false if it did
 */
bool LLsleep(tSensors link) {
  if (!_lineLeader_cmd(link, 'D')) // Sort by size
    return false;
  return true;
}


/**
 * the function toggles from dark line on light to light line on dark and back.
 * @param link the sensor port number
 * @return true if no error occured, false if it did
 */
bool LLinvertLineColor(tSensors link) {
  if (!_lineLeader_cmd(link, 'I')) // invert motors
    return false;
  return true;
}


/**
 * the function resets to default of sensing a dark line on light background
 * @param link the sensor port number
 * @return true if no error occured, false if it did
 */
bool LLresetLineColor(tSensors link) {
  if (!_lineLeader_cmd(link, 'R')) // reset the color to black
    return false;
  return true;
}


/**
 * This function takes a snapshot of the line under the sensor
 * and tracks that position in subsequent tracking operations.
 * Also this function will set inversion if it sees white line
 * on dark background
 * @param link the sensor port number
 * @return true if no error occured, false if it did
 */
bool LLtakeSnapshot(tSensors link) {
  if (!_lineLeader_cmd(link, 'S')) // take a snapshot
    return false;
  return true;
}


/**
 * This function calibrates the white threshold for each sensor in the array.
 * Place the array over the white surface with all sensors on the white
 * area.  Execute this command to set white values internally.
 * @param link the sensor port number
 * @return true if no error occured, false if it did
 */
bool LLcalWhite(tSensors link) {
  if (!_lineLeader_cmd(link, 'W')) // calibrate white
    return false;
  return true;
}


/**
 * This function calibrates the black threshold for each sensor in the array.
 * Place the array over the white surface with all sensors on the black
 * area.  Execute this command to set black values internally.
 * @param link the sensor port number
 * @return true if no error occured, false if it did
 */
bool LLcalBlack(tSensors link) {
  if (!_lineLeader_cmd(link, 'B')) // calibrate black
    return false;
  return true;
}


/**
 * The set point is used by internally (or externally) by the sensor to
 * determine the middle of the sensor over a line.  This value is compared to
 * the average value to help the robot know if it is left or right of center.
 * @param link the sensor port number
 * @param data - the value to set the set point to
 * @return true if no error occured, false if it did
 */
bool LLsetPoint(tSensors link, ubyte data){
	return _lineLeader_write(link, LL_SETPOINT, data);
}


/**
 * This function reads the setpoint value from the sensor
 * @param link the sensor port number
 * @return value of setpoint
 */
int LLsetPoint(tSensors link){
	_lineLeader_read(link, LL_KP_VALUE, oneByte);
	return (int)oneByte;
}


/*
 * The following parameters (kp,ki,kd) are specific
 * to your robot. They will change based on robot design,
 * it's traction with mat, weight, wheel size, wheel spacing,
 * sensor distance from wheels, etc, etc.
 *
 * Tune them as best as you can for your robot.<br>
 * Note, in general,<br>
 *  kp will be a high value near 1.0 (30/32)<br>
 *  ki will be zero or very low value (0.01), and<br>
 *  kd will be a low value near 0.25 (8/32)<br>
 *
 * If you wish to learn more about Kp, Ki, Kd, please
 * read the user guide.<br>
 * An excellent explanation of PID is also offered at:
 * http://en.wikipedia.org/wiki/PID_controller
 *
 * p:25/32,i:0, d:8/32
 */



/**
 * Set the "Kp" value for the sensor's internal PID calculations.<br>
 * This value is usually set close to 1.0  default 25/32<br>
 * EXPECTED VALUES: 0 to 255<br>
 * DEFAULT VALUE: 25<br>
 * EXPECTED FACTORS: 1 to 255<br>
 * DEFAULT FACTOR: 32
 * @param link the sensor port number
 * @param data - the value to set Kp
 * @param factor - the Kp factor where p = Kp/Kpfactor
 * @return true if no error occured, false if it did
 */
bool LLsetKp(tSensors link, ubyte data, ubyte factor){
	_lineLeader_write(link, LL_KP_VALUE, data);
	if ( factor == 0 ) factor = 1;
	_lineLeader_write(link, LL_KP_FACTOR, factor);
  return true;
}


/**
 * Read the "Kp" value from the sensor.
 * @param link the sensor port number
 * @return Kp value from the sensor
 */
int LLreadKp(tSensors link) {
	_lineLeader_read(link, LL_KP_VALUE, oneByte);
	return (int) oneByte;
}


/**
 * Read the "Kp factor" value from the sensor.
 * @param link the sensor port number
 * @return Kp factor value from the sensor
 */
int LLreadKpFactor(tSensors link) {
	if (_lineLeader_read(link, LL_KP_FACTOR, oneByte))
		return (int)oneByte;
	else
	  return 0;
}


/**
 * Set the "Ki" value for the sensor's internal PID calculations.<br>
 * This value is usually set close to 0  default 0/1<br>
 * EXPECTED VALUES: 0 to 255<br>
 * DEFAULT VALUE: 0<br>
 * EXPECTED FACTORS: 1 to 255<br>
 * DEFAULT FACTOR: 1
 * @param link the sensor port number
 * @param data - the value to set Ki
 * @param factor - the Ki factor where i = Ki/Kifactor
 * @return true if no error occured, false if it did
 */
bool LLsetKi(tSensors link, ubyte data, ubyte factor){
  _lineLeader_write(link, LL_KI_VALUE, data);
	if ( factor == 0 ) factor = 1;
	_lineLeader_write(link, LL_KI_FACTOR, factor);
  return true;
}


/**
 * Read the "Ki" value from the sensor.
 * @param link the sensor port number
 * @return Ki value from the sensor
 */
int LLreadKi(tSensors link) {
	_lineLeader_read(link, LL_KI_VALUE, oneByte);
	return (int)oneByte;
}


/**
 * Read the "Ki factor" value from the sensor.
 * @param link the sensor port number
 * @return Ki factor value from the sensor
 */
int LLreadKiFactor(tSensors link) {
	if (_lineLeader_read(link, LL_KI_FACTOR, oneByte))
	  return (int)oneByte;
	else
	  return 0;
}


/**
 * Set the "Kd" value for the sensor's internal PID calculations.<br>
 * This value is usually set lower to stabilize default 8/32<br>
 * EXPECTED VALUES: 0 to 255<br>
 * DEFAULT VALUE: 8<br>
 * EXPECTED VALUES: 1 to 255<br>
 * DEFAULT FACTOR: 32
 * @param link the sensor port number
 * @param data - the value to set Kd
 * @param factor - the Kd factor where d = Kd/Kdfactor
 * @return true if no error occured, false if it did
 */
bool LLsetKd(tSensors link, ubyte data, ubyte factor){
  _lineLeader_write(link, LL_KD_VALUE, data);
	if ( factor == 0 ) factor = 1;
	_lineLeader_write(link, LL_KD_FACTOR, factor);
  return true;
}


/**
 * Read the "Kd" value from the sensor.
 * @param link the sensor port number
 * @return Kd value from the sensor
 */
int LLreadKd(tSensors link) {
	_lineLeader_read(link, LL_KD_VALUE, oneByte);
	return (int)oneByte;
}


/**
 * Read the "Kd factor" value from the sensor.
 * @param link the sensor port number
 * @return Kd factor value from the sensor
 */
int LLreadKdFactor(tSensors link) {
	if (_lineLeader_read(link, LL_KD_FACTOR, oneByte))
	  return (int)oneByte;
	else
	  return 0;
}


/**
 * Read the "Steering" value from the sensor.  This value is calculated internally
 * and can directly be used to set turning values for the robot's motors.<br>
 * EXPECTED VALUES: -100 to 100 (-101=ERROR)
 * @param link the sensor port number (range: -100 to 100)
 * @return steering value from the sensor, -101 for error
 */
int LLreadSteering(tSensors link) {
	if (_lineLeader_read(link, LL_READ_STEERING, oneByte))
	  return (int)(0xFF & oneByte);
	else
	  return -101;
}


/**
 * Read the Weighted "Average" value from the sensor.  This value is calculated
 * internally by the sensor where each of the eight sensors is either triggered or not
 * and multiplied by a a factor to help determine if the line is left, right or
 * on center of the line (according to the set point).<br>
 * EXPECTED VALUES: 0-80 (-1=ERROR)<br>
 * <pre> SENSOR:       0    1    2    3    4    5    6    7
 * MULTIPLIER:  10   20   30   40   50   60   70   80</pre>
 * FORMULA: Sum(Weighted Values)/Number sensors on line<br>
 * Ex. if sensor 0 and 1 are over a line, the average is:<br>
 *     (10 + 20 + 0 + 0 + 0 + 0 + 0 + 0)/2 = 15<br>
 *     in this case 30 < 45 (set point) so the bot is left of center
 * @param link the sensor port number
 * @return average sensor value or -1 for error
 */
int LLreadAverage(tSensors link) {
	if (_lineLeader_read(link, LL_READ_AVERAGE, oneByte))
	  return (int)oneByte;
	else
	  return -1;
}


/**
 * Read a ubyte with each bit equal to a sensor.<br>
 * 1 = Line<br>
 * 0 = No Line<br>
 * <pre> SENSOR:      0    1    2    3    4    5    6    7
 * MULTIPLIER:  1    2    4    8    16   32   64   128</pre>
 * To determine if a given sensor is over a line or not, use binary math
 * to test each bit.<br>
 * A returned value of 3 means sensor 0 and 1 are over a line.
 * @param link the sensor port number
 * @return RESULT value from the sensor with 8 bits of data; NO ERROR CODE
 */
int LLreadResult(tSensors link) {
  _lineLeader_read(link, LL_READ_RESULT, oneByte);
  return (int)oneByte;
}


/**
 * Read the "Raw Sensor" values from the Line Leader.  Amount of light or dark
 * each sensor sees.  Typically between 0-20.  0=black, 100=white
 * @param link the sensor port number
 * @param &pMsg is 8 bytes returned.  One for each sensor with raw value.
 * @return true if no error occured, false if it did
 */
bool LLreadSensorRaw(tSensors link, tByteArray &pMsg) {
	return _lineLeader_read(link, LL_SENSOR_RAW, 8, pMsg);
}


/**
 * Read the uncalibrated sensor values from the Line Leader.  Each sensor returns a 16 bit
 * value.
 * @param link the sensor port number
 * @param sensorValues is 8 bytes returned.  One for each sensor with raw value.
 * @return true if no error occured, false if it did
 */
bool LLreadSensorUncalibrated (tSensors link, tIntArray &sensorValues) {
  tByteArray sensorData;

  if (!_lineLeader_read(link, LL_SENSOR_UNCAL, 16, sensorData)) {
    return false;
  }

  for (int i = 0; i < 8; i++) {
    sensorValues[i] = (0xFF & sensorData[(i*2)]) + ((0xFF & sensorData[((i*2)+1)]) << 8);
  }

  return true;
}


/**
 * Read the "White Threshold" values from the Line Leader for each sensor.<br>
 * Each of the eight sensors has a value.  Raw values greater then this threshold
 * equal white (area).<br>
 * The values are set when calibrating the white points for the sensor.
 * @param link the sensor port number
 * @param &pMsg is 8 bytes returned.  One for each sensor with Threshold.
 * @return true if no error occured, false if it did
 */
bool LLreadWhiteThresh(tSensors link, tByteArray &pMsg) {
	return _lineLeader_read(link, LL_WHITE_LIMIT, 8, pMsg);
}


/**
 * Read the "Black Threshold" values from the Line Leader for each sensor.<br>
 * Each of the eight sensors has a value.  Raw values less then this threshold
 * equal black (line).<br>
 * The values are set when calibrating the black points for the sensor.
 * @param link the sensor port number
 * @param &pMsg is 8 bytes returned.  One for each sensor with Threshold.
 * @return true if no error occured, false if it did
 */
bool LLreadBlackThresh(tSensors link, tByteArray &pMsg) {
	return _lineLeader_read(link, LL_BLACK_LIMIT, 8, pMsg);
}


#endif // __MSLL_H__

/*
 * $Id: mindsensors-lineleader.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
