/*!@addtogroup common_includes
 * @{
 * @defgroup common-rs485 RS485 functions
 * Commonly used RS485-related functions used by drivers
 * @{
 */

/*
 * $Id: common-rs485.h 133 2013-03-10 15:15:38Z xander $
 */

/** \file common-rs485.h
 * \brief Commonly used RS485-related functions used by drivers.
 *
 * common-rs485.h provides a number of frequently used RS485-related functions that are
 * useful for writing drivers.
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 *
 * Changelog:
 * - 0.1: Initial release
 *
 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 15 October 2012
 * \version 0.1
 */

#pragma systemFile

#ifndef __RS485_H__
#define __RS485_H__

#ifndef __TMR_H__
#include "timer.h"
#endif

int rxTimer;            /*!< timer for receiving timeouts */
//const string newline = "\n";
const ubyte newline[1] = '\n';

tMassiveArray RS485rxbuffer; /*!< 128 bit array for receiving */
tMassiveArray RS485txbuffer; /*!< 128 bit array for transmission */

/**
 * Write a message to the NXT2WIFI sensor
 * @param buf the buffer to be transmitted
 * @param len the length of the data to be transmitted
 * @return true if no error occured, false if it did
 */
bool RS485write(tMassiveArray &buf, ubyte len)
{
  TFileIOResult res;

  // Clear the read buffer
  if (nxtGetAvailHSBytes() > 0)
  {
    nxtReadRawHS(&RS485rxbuffer[0], nxtGetAvailHSBytes());
    memset(RS485rxbuffer, 0, sizeof(RS485rxbuffer));
  }

  // Make sure we're not sending anymore
  while (nxtHS_Status != HS_RECEIVING) EndTimeSlice();

#ifdef __RS485_DEBUG__
	writeDebugStream("RS485write: ");
	for (ubyte datacounter = 0; datacounter < len; datacounter++)
	{
	  writeDebugStream("%c", buf[datacounter]);
	  EndTimeSlice();
	}
#endif // __RS485_DEBUG__

  res = nxtWriteRawHS(&buf[0], len);
  if (res != ioRsltSuccess)
    return false;

  while (nxtHS_Status != HS_RECEIVING) EndTimeSlice();
  return true;
}


/**
 * Read a message from the NXT2WIFI. An optional timeout can be specified.
 * @param buf the buffer in which to store the received data
 * @param len the amount of data received
 * @param timeout optional parameter to specify the timeout, defaults to 100ms
 * @return true if no error occured, false if it did
 */
bool RS485read(tMassiveArray &buf, int &len, int timeout = 100) {
	int bytesAvailable = 0;

	memset(buf, 0, sizeof(buf));

	TMRreset(rxTimer);
  TMRsetup(rxTimer, timeout);

	while(bytesAvailable == 0 && !TMRisExpired(rxTimer)) {
		bytesAvailable = nxtGetAvailHSBytes();
		wait1Msec(10);
	}

	nxtReadRawHS(&buf[0], bytesAvailable);
	len = bytesAvailable;

#ifdef __RS485_DEBUG__
	writeDebugStream("RS485read (%d): ", bytesAvailable);
	for (int datacounter = 0; datacounter < bytesAvailable; datacounter++)
	{
	  writeDebugStream("%c", buf[datacounter]);
	  EndTimeSlice();
	}
	writeDebugStream("\n");
#endif // __RS485_DEBUG__

  return true;
}


/**
 * Read a very large response from the NXT2WIFI sensor.  This breaks up the
 * reads into many smaller chunks to prevent weirdness on the bus.
 * @param buf the buffer in which to store the received data
 * @param len the amount of data received
 * @param timeout optional parameter to specify the timeout, defaults to 100ms
 * @return true if no error occured, false if it did
 */
bool RS485readLargeResponse(tMassiveArray &buf, int &len, int timeout = 100)
{
  const ubyte chunkSize = 10;
  int bytesAvailable;
	tByteArray tmpBuff;
	int bytesleft = len;
	int bytesToRead = 0;
	int index = 0;
	memset(buf, 0, sizeof(buf));

	TMRreset(rxTimer);
  TMRsetup(rxTimer, timeout);


	while ((bytesleft > 0) && !TMRisExpired(rxTimer))
	{
		memset(tmpBuff, 0, sizeof(tmpBuff));
		bytesAvailable = nxtGetAvailHSBytes();
		bytesToRead = (bytesAvailable > chunkSize) ? chunkSize: bytesAvailable;
		nxtReadRawHS(&tmpBuff[0], bytesToRead);

		memcpy(&buf[index], tmpBuff, bytesToRead);
		bytesleft -= bytesToRead;
		index += bytesToRead;
		wait1Msec(5);
	}

#ifdef __RS485_DEBUG__
	writeDebugStream("RS485readLargeResponse: ");
	for (int datacounter = 0; datacounter < len; datacounter++)
	{
	  writeDebugStream("%c", buf[datacounter]);
	  EndTimeSlice();
	}
#endif // __RS485_DEBUG__
	return (nxtGetAvailHSBytes() == 0);
}

/**
 * Append an array of bytes to a tMassiveArray buffer, starts at index in the buffer
 * and copies nLength bytes.
 * @param buf the buffer to copy to
 * @param index the position in buffer where to start appending to
 * @param pData the data to be appended to buf
 * @param nLength the length of the data to be appended
 * @return the new 'tail' position of buf at which to append.
 */
int RS485appendToBuff(tMassiveArray &buf, const short index, const ubyte *pData, const short nLength)
{
  if (index == 0) memset(buf, 0, sizeof(buf));

  memcpy(&buf[index], pData, nLength);
  return index + nLength;
}


/**
 * Append a string a tMassiveArray buffer, starts at index in the buffer
 * @param buf the buffer to copy to
 * @param index the position in buffer where to start appending to
 * @param pData the string to be appended to buf
 * @return the new 'tail' position of buf at which to append.
 */
int RS485appendToBuff(tMassiveArray &buf, const short index, string pData)
{
  if (index == 0) memset(buf, 0, sizeof(buf));

  memcpy(&buf[index], pData, strlen(pData));
  return index + strlen(pData);
}


/**
 * Append a string a tBigByteArray buffer, starts at index in the buffer
 * @param buf the buffer to copy to
 * @param index the position in buffer where to start appending to
 * @param pData the string to be appended to buf
 * @param nLength the length of the data to be appended
 * @return the new 'tail' position of buf at which to append.
 */
int RS485appendToBuff(tBigByteArray &buf, const short index, const ubyte *pData, const short nLength)
{
  if (index == 0) memset(buf, 0, sizeof(buf));

  memcpy(&buf[index], pData, nLength);
  return index + nLength;
}


/**
 * Append a string a tBigByteArray buffer, starts at index in the buffer
 * @param buf the buffer to copy to
 * @param index the position in buffer where to start appending to
 * @param pData the string to be appended to buf
 * @return the new 'tail' position of buf at which to append.
 */
int RS485appendToBuff(tBigByteArray &buf, const short index, const string pData)
{
  if (index == 0) memset(buf, 0, sizeof(buf));

  memcpy(&buf[index], &pData, strlen(pData));
  return index + strlen(pData);
}


/**
 * Initialise the port, setup buffers
 */
void RS485initLib(long baudrate=230400) {
  nxtEnableHSPort();
  nxtHS_Mode = hsRawMode;
  nxtSetHSBaudRate(baudrate);
  rxTimer = TMRnewTimer();
  memset(RS485rxbuffer, 0, sizeof(RS485rxbuffer));
  memset(RS485txbuffer, 0, sizeof(RS485txbuffer));
}



void RS485clearRead(bool sendnewline = false)
{
  ubyte nDymmyData[] = {13};
  if(sendnewline)
  {
	  nxtWriteRawHS(&nDymmyData[0], 1);   // Send the carriage return
	  wait1Msec(10);
	}
  while(nxtGetAvailHSBytes()> 0){
    nxtReadRawHS(&nDymmyData[0], 1);    // Read the response.  Probably an error.
    EndTimeSlice();
  }
}


bool RS485sendString(string &data)
{
  memcpy(RS485txbuffer, data, strlen(data));
  return RS485write(RS485txbuffer, strlen(data));
}

bool RS485sendString(char *data)
{
  memcpy(RS485txbuffer, data, strlen(data));
  return RS485write(RS485txbuffer, strlen(data));
}

#endif // __RS485_H__

/*
 * $Id: common-rs485.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
