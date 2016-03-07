/*
 * $Id: mightyboard.h 108 2012-09-16 09:06:13Z xander $
 */

#pragma systemFile            

#ifndef __MIGHTYBOARD_H__
#define __MIGHTYBOARD_H__

#ifndef __MCP23008_H__
#include "microchip-mcp23008.h"
#endif

#ifndef __MAX127_H__
#include "maxim-max127.h"
#endif

tSensors _board = sensorNone;
byte _enabledSensors = 0;

void initBoard(tSensors _link);
void enableSensors(byte _mask);
void enableSensors(tSensors _link, byte _mask);
int readSensor(byte _channel);
int readSensor(byte _channel, byte _precision);
int readSensor(tSensors _link, byte _channel);
int readSensor(tSensors _link, byte _channel, byte _precision);


// Initialise the board and configure the sensor port it is attached to
void initBoard(tSensors _link) {
	_board = _link;
}

// Allows for sensors to be enabled and disabled according to the mask, 1 is on, 0 is off
void enableSensors(byte _mask) {
	_enabledSensors = _mask;
  MCP23008setupIO(_board, MCP_I2C_ADDR, 0x0);
  MCP23008writeIO(_board, MCP_I2C_ADDR, _mask);
}

void enableSensors(tSensors _link, byte _mask) {
	_enabledSensors = _mask;
  MCP23008setupIO(_link, MCP_I2C_ADDR, 0x0);
  MCP23008writeIO(_link, MCP_I2C_ADDR, _mask);
}

// Get the ADC value of the associated channel, return -1 of the sensor was not enabled.
int readSensor(byte _channel) {
  return readSensor(_board, _channel, 12);
}

int readSensor(byte _channel, byte _precision) {
  return readSensor(_board, _channel, _precision);
}

int readSensor(tSensors _link, byte _channel) {
	return readSensor(_link, _channel, 12);
}

int readSensor(tSensors _link, byte _channel, byte _precision) {
	if ((_enabledSensors >> _channel) & 1) {
	  return MAX127readChan(_link, MAX127_I2C_ADDR, _channel) >> (12 - _precision);
	} else {
		return -1;
	}
}

#endif // __MIGHTYBOARD_H__

/*
 * $Id: mightyboard.h 108 2012-09-16 09:06:13Z xander $
 */

