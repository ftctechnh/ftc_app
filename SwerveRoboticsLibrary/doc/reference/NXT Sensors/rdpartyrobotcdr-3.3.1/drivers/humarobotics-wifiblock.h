/*!@addtogroup other
 * @{
 * @defgroup HRWB HumaRobotics WiFiBlock
 * HumaBotics WiFiBlock Sensor
 * @{
 */

/*
 * $Id: humarobotics-wifiblock.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HRWB_H__
#define __HRWB_H__
/** \file humarobotics-wifiblock.h
 * \brief HumaRobotics WiFiBlock driver
 *
 * humarobotics-wifiblock.h provides an API for the HumaRobotics WiFiBlock Sensor.  This program
 * demonstrates how to use that API.
 *
 * Changelog:
 * - 0.1: Initial release
 *
 * Credits:
 * - Big thanks to HumaRobotics for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 02 April 2012
 * \version 0.1
 * \example humarobotics-wifiblock-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif


#define HRWB_I2C_ADDR               0x20      /*!< WiFiBlock I2C address */
#define HRWB_SIMPLE_WRITE           0x01      /*!< Command for a simple write */
#define HRWB_MASKED_WRITE           0x02      /*!< Command for a masked write */

#define HRWB_SENSORTYPE             0x0008    /*!< Register for Sensortype - WIFI */
#define HRWB_BATTLVL                0x000C    /*!< Register for battery level in 0.1V */

#define HRWB_HTTP_RESULT_CODE       0x001F    /*!< Register for HTTP result code */

#define HRWB_RESET                  0x000E    /*!< Register to reset the WiFiblock */
#define HRWB_RESET_WIFIBLOCK        0x00      /*!< Command to reset the WiFiblock */
#define HRWB_RESET_ERRORCODE        0x01      /*!< Command to reset the error code WiFiblock */

// WiFi connection related
#define HRWB_WIFI_STATUS            0x0200    /*!< Register for WiFi Status - see HRWB_WIFI_STATUS_* for bit masks */
#define HRWB_WIFI_SIGNAL            0x0201    /*!< Register for signal strength */
#define HRWB_WIFI_SSID              0x0202    /*!< Register for SSID */
#define HRWB_WIFI_IP_ADDR           0x0222    /*!< Register for IPv4 address */
#define HRWB_WIFI_IP_MASK           0x0226    /*!< Register for Netmask */
#define HRWB_WIFI_IP_GATEWAY        0x022A    /*!< Register for default gateway */
#define HRWB_WIFI_SECURITY_TYPE     0x022E    /*!< Register for WiFi security type - see HRWB_SECURITY_*  for values */
#define HRWB_WIFI_SECURITY_KEY      0x022F    /*!< Register for WiFi key */
#define HRWB_WIFI_SCANRESULT        0x026F    /*!< Register for number of SSIDs found after a scan */
#define HRWB_WIFI_SCANSEL           0x0270    /*!< Register for selecting the SSID to get more info on */
#define HRWB_WIFI_SCANSIGNAL        0x0271    /*!< Register for the signal strength of the specified SSID */
#define HRWB_WIFI_SCANSEC           0x0272    /*!< Register for the security type of the specified SSID */
#define HRWB_WIFI_SCANSSID          0x0273    /*!< Register for specifying the SSID to scan */

#define HRWB_WIFI_STATUS_ENABLE     0x01      /*!< Bit mask for selecting WiFi status */
#define HRWB_WIFI_STATUS_SAVE_CONF  0x02      /*!< Bit mask for selecting command to save current configuration */
#define HRWB_WIFI_STATUS_START_SCAN 0x04      /*!< Bit mask for selecting command to start scanning network */
#define HRWB_WIFI_STATUS_SCAN_DONE  0x08      /*!< Bit mask for selecting whether scan is done */
#define HRWB_WIFI_STATUS_CONN_STAT  0x10      /*!< Bit mask for selecting status of the current WiFi connection */
#define HRWB_WIFI_STATUS_RESERVED   0x20      /*!< RESERVED */
#define HRWB_WIFI_STATUS_RECONN     0x40      /*!< Bit mask for selecting whether or not to reconnect when disconnected */
#define HRWB_WIFI_STATUS_DEL_CONF   0x80      /*!< Bit mask for selecting command to delete current configuration */

#define HRWB_SECURITY_OPEN          0x00      /*!< HRWB_WIFI_SECURITY_TYPE value for Open Network (no crypto) */
#define HRWB_SECURITY_WEP           0x01      /*!< HRWB_WIFI_SECURITY_TYPE value for WEP crypto */
#define HRWB_SECURITY_WPA           0x02      /*!< HRWB_WIFI_SECURITY_TYPE value for WPA */
#define HRWB_SECURITY_WPA2          0x03      /*!< HRWB_WIFI_SECURITY_TYPE value for WPA2 */

// HTTP GET related
#define HRWB_GET_STATUS             0x1000    /*!< Register for status of GET command - see HRWB_GET_STATUS_* for bit masks */
#define HRWB_GET_LENGTH             0x1001    /*!< Register for received data length */
#define HRWB_GET_IP_ADDR            0x1002    /*!< Register for IP address to send request to */
#define HRWB_GET_PORT               0x1006    /*!< Register for port to send request to */
#define HRWB_GET_URL                0x1008    /*!< Register for URL (everything after the host name) */
#define HRWB_GET_DATA               0x1048    /*!< Register for received data */

#define HRWB_GET_STATUS_READY       0x01      /*!< Bit mask for GET port readiness status */
#define HRWB_GET_STATUS_RESERVED1   0x02      /*!< RESERVED */
#define HRWB_GET_STATUS_SEND_REQ    0x04      /*!< Bit mask for sending the GET request*/
#define HRWB_GET_STATUS_REQ_DONE    0x08      /*!< Bit mask for checking whether GET request is done */
#define HRWB_GET_STATUS_RESERVED2   0x10      /*!< RESERVED */
#define HRWB_GET_STATUS_RESERVED3   0x20      /*!< RESERVED */
#define HRWB_GET_STATUS_RESERVED4   0x40      /*!< RESERVED */
#define HRWB_GET_STATUS_REQ_ERROR   0x80      /*!< Bit mask for checking if an error has occurred */

// HTTP POST related
#define HRWB_POST_STATUS            0x2000    /*!< Register for status of POST command - see HRWB_POST_STATUS_* for bit masks */
#define HRWB_POST_LENGTH            0x2001    /*!< Register for POST data length */
#define HRWB_POST_IP_ADDR           0x2002    /*!< Register for IP address to send request to */
#define HRWB_POST_PORT              0x2006    /*!< Register for port to send request to */
#define HRWB_POST_URL               0x2008    /*!< Register for URL (everything after the host name) */
#define HRWB_POST_DATA              0x2048    /*!< Register for POST data */

#define HRWB_POST_STATUS_READY      0x01      /*!< Bit mask for POST port readiness status */
#define HRWB_POST_STATUS_RESERVED1  0x02      /*!< RESERVED */
#define HRWB_POST_STATUS_SEND_REQ   0x04      /*!< Bit mask for sending the POST request*/
#define HRWB_POST_STATUS_REQ_DONE   0x08      /*!< Bit mask for checking whether POST request is done */
#define HRWB_POST_STATUS_EN_FL_STO  0x10      /*!< Bit mask to store POST requests in the dataflash in case of network loss */
#define HRWB_POST_STATUS_EN_FL_RTX  0x20      /*!< Bit mask to store POST requests in the DataFlash in case of network loss
                                                   and then automatically retransmits them when the network is up again */
#define HRWB_POST_STATUS_RESERVED2  0x40      /*!< RESERVED */
#define HRWB_POST_STATUS_REQ_ERROR  0x80      /*!< Bit mask for checking if an error has occurred */

// TCP related
#define HRWB_TCP_STATUS             0x4000    /*!< Register for status of TCP command - see HRWB_TCP_STATUS_* for bit masks */
#define HRWB_TCP_IP_ADDR            0x4001    /*!< Register for IP address to send request to */
#define HRWB_TCP_PORT               0x4005    /*!< Register for port to send request to */
#define HRWB_TCP_TX_LENGTH          0x4007    /*!< Register for tramsmitted data length */
#define HRWB_TCP_TX_DATA            0x4008    /*!< Register for tramsmitted data */
#define HRWB_TCP_RX_LENGTH          0x4028    /*!< Register for received data length */
#define HRWB_TCP_RX_DATA            0x4029    /*!< Register for received data */

#define HRWB_TCP_STATUS_READY       0x01      /*!< Bit mask for TCP port readiness status */
#define HRWB_TCP_STATUS_SEND_REQ    0x02      /*!< Bit mask for sending the TCP request*/
#define HRWB_TCP_STATUS_REQ_DONE    0x04      /*!< Bit mask for checking whether TCP request is done */
#define HRWB_TCP_STATUS_RESERVED1   0x08      /*!< RESERVED */
#define HRWB_TCP_STATUS_RESERVED2   0x10      /*!< RESERVED */
#define HRWB_TCP_STATUS_RESERVED3   0x20      /*!< RESERVED */
#define HRWB_TCP_STATUS_RESERVED4   0x40      /*!< RESERVED */
#define HRWB_TCP_STATUS_REQ_ERROR   0x80      /*!< Bit mask for checking if an error has occurred */

// UDP related
#define HRWB_UDP_STATUS             0x3000    /*!< Register for status of UDP command - see HRWB_UDP_STATUS_* for bit masks */
#define HRWB_UDP_IP_ADDR            0x3001    /*!< Register for IP address to send request to */
#define HRWB_UDP_DPORT              0x3005    /*!< Register for port to send request to */
#define HRWB_UDP_SPORT              0x3007    /*!< Register for port to send request from */
#define HRWB_UDP_TX_LENGTH          0x3009    /*!< Register for tramsmitted data length */
#define HRWB_UDP_TX_DATA            0x300A    /*!< Register for tramsmitted data */
#define HRWB_UDP_RX_LENGTH          0x302A    /*!< Register for received data length */
#define HRWB_UDP_RX_DATA            0x302B    /*!< Register for received data */

#define HRWB_UDP_STATUS_READY       0x01      /*!< Bit mask for UDP port readiness status */
#define HRWB_UDP_STATUS_SEND_REQ    0x02      /*!< Bit mask for sending the UDP request*/
#define HRWB_UDP_STATUS_REQ_DONE    0x04      /*!< Bit mask for checking whether UDP request is done */
#define HRWB_UDP_STATUS_RXD_RDY     0x08      /*!< Bit mask for checking whether UDP data is ready for reading */
#define HRWB_UDP_STATUS_RXD_READ    0x10      /*!< Bit mask start reading UDP data */
#define HRWB_UDP_STATUS_RESERVED1   0x20      /*!< RESERVED */
#define HRWB_UDP_STATUS_RESERVED2   0x40      /*!< RESERVED */
#define HRWB_UDP_STATUS_REQ_ERROR   0x80      /*!< Bit mask for checking if an error has occurred */

tByteArray HRWB_I2CRequest;       /*!< Array to hold I2C command data */
tByteArray HRWB_I2CReply;         /*!< Array to hold I2C reply data */
tByteArray HRWB_scratch;          /*!< Array to hold scratch data */
tHugeByteArray HRWB_HugeArray;    /*!< Array to hold scratch data, only more of it */


typedef struct                    /*!< Struct to hold network config information */
{
  string wifi_ssid;               /*!< SSID (network name) to connect to */
  string wifi_pass;               /*!< WiFi network pass */
  tIPaddr IP;                     /*!< IP address to use */
  tIPaddr netmask;                /*!< netmask to use */
  tIPaddr gateway;                /*!< defrault gateway to use */
  ubyte wifi_type;                /*!< Crypto used on the network, see HRWB_SECURITY_* for options */
} tNetworkInfo;

/*
typedef struct
{
  ubyte SSID[64];
  ubyte signalStrength;
  ubyte securityType;
} tSSIDInfo;
*/


typedef struct                    /*!< Struct to hold UDP connection info */
{
  tIPaddr IP;                     /*!< IP address to send request to */
  int port;                       /*!< port to send request to */
  tHugeByteArray URL;             /*!< URL (everything after the host name) */
  tBigByteArray RXData;           /*!< Received data */
  ubyte RXDataLen;                /*!< Amount of received data */
  ubyte result_code;              /*!< HTTP result code */
} tGetRequest;


typedef struct
{
  tIPaddr IP;                     /*!< IP address to send request to */
  int port;                       /*!< port to send request to */
  tHugeByteArray URL;             /*!< URL (everything after the host name) */
  tBigByteArray TXData;           /*!< Data to be transmitted */
  ubyte TXDataLen;                /*!< Amount of data to be transmitted */
  ubyte result_code;              /*!< HTTP result code */
} tPostRequest;


typedef struct
{
  tIPaddr IP;                     /*!< IP address to send request to */
  int port;                       /*!< port to send request to */
  tHugeByteArray TXData;          /*!< Data to be transmitted */
  ubyte TXDataLen;                /*!< Amount of data to be transmitted */
  tHugeByteArray RXData;          /*!< Data to be transmitted */
  ubyte RXDataLen;                /*!< Amount of data to be transmitted */
  ubyte result_code;              /*!< result code: 0 if everything was fine, 1 if there was an error */
} tTCPRequest;


typedef struct
{
  tIPaddr IP;                     /*!< IP address to send request to */
  int sport;                      /*!< port to send request from */
  int dport;                      /*!< port to send request to */
  tHugeByteArray TXData;          /*!< Data to be transmitted */
  ubyte TXDataLen;                /*!< Amount of data to be transmitted */
  tHugeByteArray RXData;          /*!< Data to be transmitted */
  ubyte RXDataLen;                /*!< Amount of data to be transmitted */
  ubyte result_code;              /*!< result code: 0 if everything was fine, 1 if there was an error */
} tUDPRequest;

// tSSIDInfo SSIDs[8];

/**
 * Calculate the size of a string in an array.  Counts up to the NULL.
 * @param array the array to check the length of
 * @return length of the string in the array
 */
int strsize(tHugeByteArray &array)
{
  for (int i = 0; i < sizeof(tHugeByteArray); i++)
  {
    if((ubyte)array[i] == 0)
      return i;
  }
  return sizeof(tHugeByteArray);
}


/**
 * Convert a string to a tIPaddr struct. This is an IPv4 address.
 * @param address string representation of the IPv4 address
 * @param addr tIPaddr struct to hold the IPv4 address
 * @return true if no error occured, false if it did
 */
void stringToIp(const string address, tIPaddr &addr)
{
  string octet;
  string copy;
  int index = 0;
  memcpy(copy, address, 20);

  for (int i = 0; i < 3; i++)
  {
    octet = "";
    index = StringFind(copy, ".");
    memcpy(octet, copy, index);
    addr[i] = atoi(octet);
    StringDelete(copy, 0, index+1);
  }
  addr[3] = atoi(copy);
}


/**
 * Perform a masked write, which allows you to select a single bit in a register.
 * @param link the port number
 * @param reg register to write to
 * @param data value to write to the register
 * @param mask to use when writing to the register
 * @return true if no error occured, false if it did
 */
bool HRWBwriteRegMasked(tSensors link, int reg, ubyte data, ubyte mask)
{
  // writeDebugStreamLine("writeRegMasked");
  memset(HRWB_I2CRequest, 0, sizeof(tByteArray));

  HRWB_I2CRequest[0] =  6;                  //Message Size, here 7 Bytes
  HRWB_I2CRequest[1] =  HRWB_I2C_ADDR;
  HRWB_I2CRequest[2] =  HRWB_MASKED_WRITE;
  HRWB_I2CRequest[3] =  (reg >> 8) & 0xFF;  //Read address register - Msb
  HRWB_I2CRequest[4] =  reg & 0xFF;         //Read address register - Lsb
  HRWB_I2CRequest[5] =  mask;               //data
  HRWB_I2CRequest[6] =  data;               //mask

  return writeI2C(link, HRWB_I2CRequest);
}


/**
 * Write a single byte to a register
 * @param link the port number
 * @param reg register to write to
 * @param data value to write to the register
 * @return true if no error occured, false if it did
 */
bool HRWBwriteReg(tSensors link, int reg, ubyte data)
{
  // writeDebugStreamLine("writeReg");
  memset(HRWB_I2CRequest, 0, sizeof(tByteArray));

  HRWB_I2CRequest[0] =  5;                  //Message Size, here 7 Bytes
  HRWB_I2CRequest[1] =  HRWB_I2C_ADDR;
  HRWB_I2CRequest[2] =  HRWB_SIMPLE_WRITE;
  HRWB_I2CRequest[3] =  (reg >> 8) & 0xFF;  //Read address register - Msb
  HRWB_I2CRequest[4] =  reg & 0xFF;         //Read address register - Lsb
  HRWB_I2CRequest[5] =  data;                //Msb SensorType Register

  return writeI2C(link, HRWB_I2CRequest);
}


/**
 * Write data array to a register
 *
 * Note: this is an internal function and shouldn't be used directly
 * @param link the port number
 * @param reg register to write to
 * @param data value to write to the register
 * @param size amount of data to send
 * @return true if no error occured, false if it did
 */
bool _HRWBwriteRegPrivate(tSensors link, int reg, tByteArray &data, int size)
{
  if (size > 12)
    return false;

  memset(HRWB_I2CRequest, 0, sizeof(tByteArray));

  HRWB_I2CRequest[0] =  4 + size;                  //Message Size, here 7 Bytes
  HRWB_I2CRequest[1] =  HRWB_I2C_ADDR;
  HRWB_I2CRequest[2] =  HRWB_SIMPLE_WRITE;
  HRWB_I2CRequest[3] =  (reg >> 8) & 0xFF;  //Read address register - Msb
  HRWB_I2CRequest[4] =  reg & 0xFF;         //Read address register - Lsb

  memcpy(&HRWB_I2CRequest[5], data, size);

  return writeI2C(link, HRWB_I2CRequest);
}


/**
 * Write a tByteArray struct to a register
 * @param link the port number
 * @param reg register to write to
 * @param data tByteArray to write to the register
 * @param size amount of data to send
 * @return true if no error occured, false if it did
 */
bool HRWBwriteReg(tSensors link, int reg, tByteArray &data, int size)
{
  tByteArray tmpBuff;
  // writeDebugStreamLine("writeReg tByteArray");
  int bytesleft = size;
  int datalen = 0;

  for (int i =  0; i < ((size/12) + 1); i++)
	{
	  datalen = (bytesleft > 12) ? 12 : bytesleft;
	  memset(tmpBuff, 0, sizeof(tmpBuff));
	  memcpy(tmpBuff, &data[i*12], datalen);

	  if (!_HRWBwriteRegPrivate(link, reg + (i*12), tmpBuff, datalen))
	    return false;
	  bytesleft -= 12;
	}
	return true;
}


/**
 * Write a tHugeByteArray struct to a register
 * @param link the port number
 * @param reg register to write to
 * @param data tHugeByteArray to write to the register
 * @param size amount of data to send
 * @return true if no error occured, false if it did
 */
bool HRWBwriteReg(tSensors link, int reg, tHugeByteArray &data, int size)
{
  tByteArray tmpBuff;
  int bytesleft = size;
  int datalen = 0;

  for (int i =  0; i < ((size/12) + 1); i++)
	{
	  datalen = (bytesleft > 12) ? 12 : bytesleft;
	  memset(tmpBuff, 0, sizeof(tmpBuff));
	  memcpy(tmpBuff, &data[i*12], datalen);

	  if (!_HRWBwriteRegPrivate(link, reg + (i*12), tmpBuff, datalen))
	    return false;
	  bytesleft -= 12;
	}
	return true;
}


/**
 * Write a tBigByteArray struct to a register
 * @param link the port number
 * @param reg register to write to
 * @param data tBigByteArray to write to the register
 * @param size amount of data to send
 * @return true if no error occured, false if it did
 */
bool HRWBwriteReg(tSensors link, int reg, tBigByteArray &data, int size)
{
  tByteArray tmpBuff;
  int bytesleft = size;
  int datalen = 0;

  for (int i =  0; i < ((size/12) + 1); i++)
	{
	  datalen = (bytesleft > 12) ? 12 : bytesleft;
	  memset(tmpBuff, 0, sizeof(tmpBuff));
	  memcpy(tmpBuff, &data[i*12], datalen);

	  if (!_HRWBwriteRegPrivate(link, reg + (i*12), tmpBuff, datalen))
	    return false;
	  bytesleft -= 12;
	}
	return true;
}


/**
 * Read a register.  Data read is stored in HRWB_I2CReply
 * @param link the port number
 * @param reg register to read
 * @param size amount of data to read
 * @return true if no error occured, false if it did
 */
bool HRWBreadReg(tSensors link, int reg, int size)
{
  // writeDebugStreamLine("readReg");
  memset(HRWB_I2CRequest, 0, sizeof(tByteArray));

  HRWB_I2CRequest[0] =  7;                  //Message Size, here 7 Bytes
  HRWB_I2CRequest[1] =  HRWB_I2C_ADDR;
  HRWB_I2CRequest[2] =  HRWB_SIMPLE_WRITE;
  HRWB_I2CRequest[3] =  0x01;               //Read address register - Msb
  HRWB_I2CRequest[4] =  0x00;               //Read address register - Lsb
  HRWB_I2CRequest[5] =  (reg >> 8) & 0xFF;  //Msb SensorType Register
  HRWB_I2CRequest[6] =  reg & 0xFF;         //Lsb SensorType Register
  HRWB_I2CRequest[7] =  size & 0xFF;        //Size of the returned message

  return writeI2C(link, HRWB_I2CRequest, HRWB_I2CReply, size);
}


/**
 * Read a register.  Data read is stored in HRWB_HugeArray
 * @param link the port number
 * @param reg register to read
 * @param size amount of data to read
 * @return true if no error occured, false if it did
 */
bool HRWBreadBigReg(tSensors link, int reg, int size)
{
  // writeDebugStreamLine("readBigReg");
  int bytesleft = size;
  int requestlen = 0;
  memset(HRWB_I2CRequest, 0, sizeof(tByteArray));
  memset(HRWB_HugeArray, 0, sizeof(HRWB_HugeArray));

  for (int i =  0; i < ((size/16) + 1); i++)
	{
	  requestlen = (bytesleft > 16) ? 16 : bytesleft;
	  if (!HRWBreadReg(link, reg + (i*16), requestlen))
	    return false;
	  memcpy(&HRWB_HugeArray[16 * i], HRWB_I2CReply, requestlen);
	  bytesleft -= 16;
	  wait1Msec(10);
	}
	return true;
}


/**
 * Read the HTTP result code of the last HTTP transaction.
 * @param link the port number
 * @return the HTTP result code
 */
int HRWBreadHTTPCode(tSensors link)
{
  string tmpResultCode;
  HRWBreadReg(WifiBlockPort, HRWB_HTTP_RESULT_CODE, 3);
  HRWB_I2CReply[3] = 0;
  memcpy(tmpResultCode, HRWB_I2CReply, 4);
  return atoi(tmpResultCode);
}


/**
 * Reset the WiFiBlock.
 * @param link the port number
 * @return true if no error occured, false if it did
 */
bool HRWBresetWifiBlock(tSensors link)
{
  // writeDebugStreamLine("resetWifiBlock");
  return HRWBwriteRegMasked(WifiBlockPort, HRWB_RESET, 1, HRWB_RESET_WIFIBLOCK);
}


/**
 * Read the current voltage level
 * @param link the port number
 * @return current voltage level
 */
float HRWBreadBatt(tSensors link)
{
  HRWBreadReg(link, HRWB_BATTLVL, 1);
  return (float)HRWB_I2CReply[0]/10.0;

}


/**
 * Read the type of sensor. Should be WIFI, unless there's
 * something really freaky going on
 * @param link the port number
 * @param sType string to hold the sensor type string.
 * @return true if no error occured, false if it did
 */
bool HRWBreadSensorType (tSensors link, string &sType)
{
  if (!HRWBreadReg(link, HRWB_SENSORTYPE, 4))
    return false;

  StringFromChars(sType, &HRWB_I2CReply);

  return true;
}


/**
 * Enable the WiFi
 * @param link the port number
 * @return true if no error occured, false if it did
 */
bool HRWBenableWifi(tSensors link)
{
  //writeDebugStreamLine("enableWifi");
  return HRWBwriteRegMasked(link, HRWB_WIFI_STATUS, 0x01, HRWB_WIFI_STATUS_ENABLE);
}


/**
 * Disable the WiFi
 * @param link the port number
 * @return true if no error occured, false if it did
 */
bool HRWBdisableWifi(tSensors link)
{
  //writeDebugStreamLine("disableWifi");
  return HRWBwriteRegMasked(link, HRWB_WIFI_STATUS, 0x00, HRWB_WIFI_STATUS_ENABLE);
}


/**
 * Save the current network configuration
 * @param link the port number
 * @return true if no error occured, false if it did
 */
bool HRWBsaveConfig(tSensors link)
{
  //writeDebugStreamLine("saveConfig");
  return HRWBwriteRegMasked(link, HRWB_WIFI_STATUS, 0x02, HRWB_WIFI_STATUS_SAVE_CONF);
}


/**
 * Erase the current network configuration\n
 * Use in combination with HRWBresetWifiBlock()
 * to start with a clean configuration
 * @param link the port number
 * @return true if no error occured, false if it did
 */
bool HRWBeraseConfig(tSensors link)
{
  //writeDebugStreamLine("eraseConfig");
  return HRWBwriteRegMasked(link, HRWB_WIFI_STATUS, 0x80, HRWB_WIFI_STATUS_DEL_CONF);
}


/*
bool HRWBscanChannel(tSensors link, ubyte channel) {
  string tmpString;
  char tmpCharArray[20];
  int SSIDcount = 0;
  memset(HRWB_I2CRequest, 0, sizeof(tByteArray));

  writeDebugStreamLine("channel: %d", channel);
  if (!HRWBwriteReg(link, HRWB_WIFI_SCANSEL, channel))
    return false;

  // Start scan
  HRWB_I2CRequest[0] =  6;                  //Message Size, here 7 Bytes
  HRWB_I2CRequest[1] =  HRWB_I2C_ADDR;
  HRWB_I2CRequest[2] =  HRWB_MASKED_WRITE;
  HRWB_I2CRequest[3] =  0x02;               //Read address register - Msb
  HRWB_I2CRequest[4] =  0x00;               //Read address register - Lsb
  HRWB_I2CRequest[5] =  0x04;               //Msb SensorType Register
  HRWB_I2CRequest[6] =  0x04;               //Lsb SensorType Register

  if (!writeI2C(link, HRWB_I2CRequest, 0))
    return false;

  // Wait for scan to be done
  while (true) {
	  HRWBreadReg(link, HRWB_WIFI_STATUS, 1);
	  if ((HRWB_I2CReply[0] & 0x08) == 0x08)
	    break;
	  wait1Msec(100);
	}

  HRWBwriteReg(link, HRWB_WIFI_SCANSEL, channel);
  wait1Msec(10);
  HRWBreadBigReg(link, HRWB_WIFI_SCANSSID, 64);
  StringFromChars(tmpString, HRWB_HugeArray);
  writeDebugStream("Found [%d]: ", channel);
  writeDebugStreamLine(tmpString);
  return true;
}




bool HRWBscanWifi(tSensors link)
{
  char tmpCharArray[20];
  int SSIDcount = 0;
  memset(HRWB_I2CRequest, 0, sizeof(tByteArray));

  if (!HRWBwriteRegMasked(link, HRWB_WIFI_STATUS, HRWB_WIFI_STATUS_START_SCAN, HRWB_WIFI_STATUS_START_SCAN))
    return false;

  // Wait for scan to be done
  while (true) {
	  HRWBreadReg(link, HRWB_WIFI_STATUS, 1);
	  if ((HRWB_I2CReply[0] &  HRWB_WIFI_STATUS_SCAN_DONE) == HRWB_WIFI_STATUS_SCAN_DONE)
	    break;
	  wait1Msec(100);
	}

	// Fetch number of SSIDs found.
  HRWBreadReg(link, HRWB_WIFI_SCANRESULT, 1);
  SSIDcount = HRWB_I2CReply[0];
  writeDebugStreamLine("SSID count: %d", SSIDcount);
  if (SSIDcount == 0)
    return false;

  writeDebugStreamLine("count: %d", SSIDcount);
  for (int i = 0; i < SSIDcount; i++) {
    if (!HRWBscanChannel(link, i))
      return false;
    wait1Msec(10);
  }
  return true;


}
*/


/**
 * Configure the IP and WiFi settings
 * @param link the port number
 * @param netInfo tNetworkInfo struct containing network connection parameters
 * @return true if no error occured, false if it did
 */
bool HRWBconfigNetwork(tSensors link, tNetworkInfo &netInfo) {
  bool done = false;
  ubyte status;
  memset(HRWB_I2CRequest, 0, sizeof(tByteArray));

  // switch off the wifi
  if (!HRWBdisableWifi(link))
    return false;

  if (!HRWBeraseConfig(link))
    return false;
  wait1Msec(100);

  PlaySound(soundBlip);
  memcpy(HRWB_scratch, netInfo.IP[0], sizeof(tIPaddr));
  if (!HRWBwriteReg(link, HRWB_WIFI_IP_ADDR, HRWB_scratch, sizeof(tIPaddr)))
    return false;

	memcpy(HRWB_scratch, netInfo.netmask[0], sizeof(tIPaddr));
	if (!HRWBwriteReg(link, HRWB_WIFI_IP_MASK, HRWB_scratch, sizeof(tIPaddr)))
	  return false;

	memcpy(HRWB_scratch, netInfo.gateway[0], sizeof(tIPaddr));
	if (!HRWBwriteReg(link, HRWB_WIFI_IP_GATEWAY, HRWB_scratch, sizeof(tIPaddr)))
	  return false;

	if (!HRWBwriteReg(link, HRWB_WIFI_SECURITY_TYPE, netInfo.wifi_type))
	  return false;

	memcpy(HRWB_scratch, netInfo.wifi_ssid, strlen(netInfo.wifi_ssid) + 1);
	if (!HRWBwriteReg(link, HRWB_WIFI_SSID, HRWB_scratch, strlen(netInfo.wifi_ssid) + 1))
	  return false;

  memcpy(HRWB_scratch, netInfo.wifi_pass, strlen(netInfo.wifi_pass) + 1);
	if (!HRWBwriteReg(link, HRWB_WIFI_SECURITY_KEY, HRWB_scratch, strlen(netInfo.wifi_pass) + 1))
	  return false;

	if (!HRWBwriteRegMasked(link, HRWB_WIFI_STATUS, HRWB_WIFI_STATUS_RECONN, HRWB_WIFI_STATUS_RECONN))
	  return false;

	if (!HRWBsaveConfig(link))
	  return false;

	if (!HRWBenableWifi(link))
	  return false;

	while(!done) {
	  if (!HRWBreadReg(link, HRWB_WIFI_STATUS, 1))
	    return false;
	  status = HRWB_I2CReply[0];

	  if ((status & HRWB_WIFI_STATUS_CONN_STAT) == HRWB_WIFI_STATUS_CONN_STAT)
	  {
	    done = true;
	  }
	  wait1Msec(100);
	}

  return true;
}


/**
 * Perform a HTTP GET transaction
 * @param link the port number
 * @param getrequest tGetRequest struct containing transaction parameters
 * @return true if no error occured, false if it did
 */
bool HRWBdoGET(tSensors link, tGetRequest &getrequest) {
  ubyte status = 0;
  bool done = false;
  ubyte tmpPort[2];

  tmpPort[0] = (getrequest.port >> 8) & 0xFF;
  tmpPort[1] = (getrequest.port >> 0) & 0xFF;

  HRWBreadReg(link, HRWB_GET_STATUS, 1);
  status = HRWB_I2CReply[0];

  if (status & HRWB_GET_STATUS_READY != HRWB_GET_STATUS_READY)
  {
    writeDebugStreamLine("NOT READY");
    return false;
  }

	memcpy(HRWB_scratch, getrequest.IP[0], sizeof(tIPaddr));
	if (!HRWBwriteReg(link, HRWB_GET_IP_ADDR, HRWB_scratch, sizeof(tIPaddr)))
	  return false;

  memcpy(HRWB_scratch, tmpPort, 2);
	if (!HRWBwriteReg(link, HRWB_GET_PORT, HRWB_scratch, 2))
	  return false;

	if (!HRWBwriteReg(link, HRWB_GET_URL, (tHugeByteArray)getrequest.URL, strsize(getrequest.URL) + 1))
	  return false;

	if (!HRWBwriteRegMasked(link, HRWB_GET_STATUS, HRWB_GET_STATUS_SEND_REQ, HRWB_GET_STATUS_SEND_REQ))
	  return false;

	while(!done) {
	  if (!HRWBreadReg(link, HRWB_GET_STATUS, 1))
	    return false;

	  status = HRWB_I2CReply[0];

	  if ((status & HRWB_GET_STATUS_REQ_ERROR) == HRWB_GET_STATUS_REQ_ERROR)
	  {
	    //writeDebugStreamLine("ERROR GET: %d", status);
	    getrequest.result_code = HRWBreadHTTPCode(link);
	    //writeDebugStreamLine("HTTP CODE: %d", getrequest.result_code);
	    done = true;
	  }
	  else if ((status & HRWB_GET_STATUS_REQ_DONE) == HRWB_GET_STATUS_REQ_DONE)
	  {
	    //writeDebugStreamLine("SUCCESS GET");
	    getrequest.result_code = HRWBreadHTTPCode(link);
	    //writeDebugStreamLine("HTTP CODE: %d", getrequest.result_code);
	    done = true;
	  }
	  wait1Msec(100);
	}

	wait1Msec(1000);
	if (!HRWBreadReg(link, HRWB_GET_LENGTH, 1))
	  return false;
  getrequest.RXDataLen = HRWB_I2CReply[0];

  writeDebugStreamLine("res length: %d", getrequest.RXDataLen);
  if (!HRWBreadBigReg(link, HRWB_GET_DATA, getrequest.RXDataLen))
    return false;

  memcpy(&getrequest.RXData[0], &HRWB_HugeArray[0], sizeof(tBigByteArray));
	return true;


}


/**
 * Perform a HTTP POST transaction
 * @param link the port number
 * @param postrequest tPostRequest struct containing transaction parameters
 * @return true if no error occured, false if it did
 */
bool HRWBdoPOST(tSensors link, tPostRequest &postrequest) {
  ubyte status = 0;
  bool done = false;
  ubyte tmpPort[2];

  tmpPort[0] = (postrequest.port >> 8) & 0xFF;
  tmpPort[1] = (postrequest.port >> 0) & 0xFF;

  HRWBreadReg(link, HRWB_POST_STATUS, 1);
  status = HRWB_I2CReply[0];

  if (status & HRWB_POST_STATUS_READY != HRWB_POST_STATUS_READY)
    return false;
    //writeDebugStreamLine("NOT READY");

	memcpy(HRWB_scratch, postrequest.IP[0], sizeof(tIPaddr));
	if (!HRWBwriteReg(link, HRWB_POST_IP_ADDR, HRWB_scratch, sizeof(tIPaddr)))
	  return false;

  memcpy(HRWB_scratch, tmpPort, 2);
	if (!HRWBwriteReg(link, HRWB_POST_PORT, HRWB_scratch, 2))
	  return false;

	if (!HRWBwriteReg(link, HRWB_POST_URL, (tHugeByteArray)postrequest.URL, strsize(postrequest.URL) + 1))
	  return false;

	if (!HRWBwriteReg(link, HRWB_POST_DATA, (tBigByteArray)postrequest.TXData[0], postrequest.TXDataLen))
	  return false;

	if (!HRWBwriteRegMasked(link, HRWB_POST_STATUS, HRWB_POST_STATUS_SEND_REQ, HRWB_POST_STATUS_SEND_REQ))
	  return false;

	while(!done) {
	  if (!HRWBreadReg(link, HRWB_POST_STATUS, 1))
	    return false;
	  status = HRWB_I2CReply[0];


	  if ((status & HRWB_POST_STATUS_REQ_ERROR) == HRWB_POST_STATUS_REQ_ERROR)
	  {
	    //writeDebugStreamLine("ERROR POST: %d", status);
	    postrequest.result_code = HRWBreadHTTPCode(link);
	    //writeDebugStreamLine("HTTP CODE: %d", postrequest.result_code);
	    done = true;
	  }
	  else if ((status & HRWB_POST_STATUS_REQ_DONE) == HRWB_POST_STATUS_REQ_DONE)
	  {
	    //writeDebugStreamLine("SUCCESS POST");
	    postrequest.result_code = HRWBreadHTTPCode(link);
	    //writeDebugStreamLine("HTTP CODE: %d", postrequest.result_code);
	    done = true;
	  }
	  wait1Msec(100);
	}
	return true;
}


/**
 * Perform a TCP transaction
 * @param link the port number
 * @param tcprequest tTCPRequest struct containing transaction parameters
 * @return true if no error occured, false if it did
 */
bool HRWBdoTCP(tSensors link, tTCPRequest &tcprequest) {
  ubyte status = 0;
  bool done = false;
  HRWBreadReg(link, HRWB_TCP_STATUS, 1);
  status = HRWB_I2CReply[0];
  ubyte tmpPort[2];

  tmpPort[0] = (tcprequest.port >> 8) & 0xFF;
  tmpPort[1] = (tcprequest.port >> 0) & 0xFF;

  if (status & HRWB_TCP_STATUS_READY != HRWB_TCP_STATUS_READY)
    return false;
    //writeDebugStreamLine("NOT READY");

	memcpy(HRWB_scratch, tcprequest.IP[0], sizeof(tIPaddr));
	if (!HRWBwriteReg(link, HRWB_TCP_IP_ADDR, HRWB_scratch, sizeof(tIPaddr )))
	  return false;

  memcpy(HRWB_scratch, tmpPort, 2);
	if (!HRWBwriteReg(link, HRWB_TCP_PORT, HRWB_scratch, 2))
	  return false;

	writeDebugStreamLine("port: %2x %2x", HRWB_scratch[0], HRWB_scratch[1]);

	if (!HRWBwriteReg(link, HRWB_TCP_TX_DATA, (tBigByteArray)tcprequest.TXData[0], tcprequest.TXDataLen))
	  return false;

	memcpy(HRWB_scratch, tcprequest.TXDataLen, 1);
	if (!HRWBwriteReg(link, HRWB_TCP_TX_LENGTH, HRWB_scratch, 1))
	  return false;

	if (!HRWBwriteRegMasked(link, HRWB_TCP_STATUS, HRWB_TCP_STATUS_SEND_REQ, HRWB_TCP_STATUS_SEND_REQ))
	  return false;

	while(!done) {
	  if (!HRWBreadReg(link, HRWB_TCP_STATUS, 1))
	    return false;
	  status = HRWB_I2CReply[0];

	  if ((status & HRWB_TCP_STATUS_REQ_ERROR) == HRWB_TCP_STATUS_REQ_ERROR)
	  {
	    tcprequest.result_code = status;
	    done = true;
	  }
	  else if ((status & HRWB_TCP_STATUS_REQ_DONE) == HRWB_TCP_STATUS_REQ_DONE)
	  {
	    tcprequest.result_code = 0;
	    done = true;
	  }
	  wait1Msec(100);
	}

	wait1Msec(100);
	if (!HRWBreadReg(link, HRWB_TCP_RX_LENGTH, 1))
	  return false;
  tcprequest.RXDataLen = HRWB_I2CReply[0];

  writeDebugStreamLine("res length: %d", tcprequest.RXDataLen);
  if (!HRWBreadBigReg(link, HRWB_TCP_RX_DATA, 32))
    return false;

  memcpy(&tcprequest.RXData[0], &HRWB_HugeArray[0], sizeof(tBigByteArray));

  //writeDebugStreamLine(" ");
	return true;
}


/**
 * Perform a UDP transaction
 * @param link the port number
 * @param udprequest tUDPRequest struct containing transaction parameters
 * @return true if no error occured, false if it did
 */
bool HRWBdoUDP(tSensors link, tUDPRequest &udprequest) {
  ubyte status = 0;
  bool done = false;
  HRWBreadReg(link, HRWB_UDP_STATUS, 1);
  status = HRWB_I2CReply[0];
  ubyte tmpsPort[2];
  ubyte tmpdPort[2];


  tmpsPort[0] = (udprequest.sport >> 8) & 0xFF;
  tmpsPort[1] = (udprequest.sport >> 0) & 0xFF;

  tmpdPort[0] = (udprequest.dport >> 8) & 0xFF;
  tmpdPort[1] = (udprequest.dport >> 0) & 0xFF;

  if (status & HRWB_UDP_STATUS_READY != HRWB_UDP_STATUS_READY)
    writeDebugStreamLine("NOT READY");

	memcpy(HRWB_scratch, udprequest.IP[0], sizeof(tIPaddr));
	if (!HRWBwriteReg(link, HRWB_UDP_IP_ADDR, HRWB_scratch, sizeof(tIPaddr )))
	  return false;

  memcpy(HRWB_scratch, tmpsPort, 2);
	if (!HRWBwriteReg(link, HRWB_UDP_SPORT, HRWB_scratch, 2))
	  return false;
	//writeDebugStreamLine("sport: %2x %2x", HRWB_scratch[0], HRWB_scratch[1]);

  memcpy(HRWB_scratch, tmpdPort, 2);
	if (!HRWBwriteReg(link, HRWB_UDP_DPORT, HRWB_scratch, 2))
	  return false;
	//writeDebugStreamLine("dport: %2x %2x", HRWB_scratch[0], HRWB_scratch[1]);

	if (!HRWBwriteReg(link, HRWB_UDP_TX_DATA, (tBigByteArray)udprequest.TXData[0], udprequest.TXDataLen))
	  return false;

	memcpy(HRWB_scratch, udprequest.TXDataLen, 1);
	if (!HRWBwriteReg(link, HRWB_UDP_TX_LENGTH, HRWB_scratch, 1))
	  return false;

	if (!HRWBwriteRegMasked(link, HRWB_UDP_STATUS, HRWB_UDP_STATUS_SEND_REQ, HRWB_UDP_STATUS_SEND_REQ))
	  return false;

	while(!done) {
	  if (!HRWBreadReg(link, HRWB_UDP_STATUS, 1))
	    return false;
	  status = HRWB_I2CReply[0];

	  if ((status & HRWB_UDP_STATUS_REQ_ERROR) == HRWB_UDP_STATUS_REQ_ERROR)
	  {
	    udprequest.result_code = status;
	    done = true;
	  }
	  else if ((status & HRWB_UDP_STATUS_REQ_DONE) == HRWB_UDP_STATUS_REQ_DONE)
	  {
	    udprequest.result_code = status;
	    done = true;
	  }
	  wait1Msec(100);
	}

	wait1Msec(100);
	if (!HRWBreadReg(link, HRWB_UDP_RX_LENGTH, 1))
	  return false;
  udprequest.RXDataLen = HRWB_I2CReply[0];

  //writeDebugStreamLine("res length: %d", udprequest.RXDataLen);
  if (!HRWBreadBigReg(link, HRWB_UDP_RX_DATA, 32))
    return false;

  memcpy(&udprequest.RXData[0], &HRWB_HugeArray[0], sizeof(tBigByteArray));

  //writeDebugStreamLine(" ");
	return true;
}


#endif // __HRWB_H__

/*
 * $Id: humarobotics-wifiblock.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
