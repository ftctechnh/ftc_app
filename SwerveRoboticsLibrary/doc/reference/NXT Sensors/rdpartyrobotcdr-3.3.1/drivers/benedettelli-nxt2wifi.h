/*!@addtogroup other
 * @{
 * @defgroup NXT2WIFI NXT2WIFI Wifi Sensor
 * Dani's WiFi Sensor
 * @{
 */

/*
 * $Id: benedettelli-nxt2wifi.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __N2W_H__
#define __N2W_H__
/** \file benedettelli-nxt2wifi.h
 * \brief Dani's WiFi Sensor
 *
 * benedettelli-nxt2wifi.h provides an API for Dani's WiFi Sensor.\n
 *
 * Changelog:
 * - 0.1: Initial release
 *
 * Credits:
 * - Big thanks to Dani for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 12 June 2012
 * \version 0.1
 * \example benedettelli-nxt2wifi-test1.c
 * \example benedettelli-nxt2wifi-test2.c
 * \example benedettelli-nxt2wifi-test3.c
 * \example benedettelli-nxt2wifi-test4.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#ifndef __RS485_H__
#include "common-rs485.h"
#endif

#define WS_WEBLABEL                 1   /*!< Web page widget: label */
#define WS_WEBBUTTON                2   /*!< Web page widget: button */
#define WS_WEBCHECKBOX              3   /*!< Web page widget: checkbox */
#define WS_WEBSLIDER                4   /*!< Web page widget: slider */
#define WS_WEBBARGRAPH              5   /*!< Web page widget: bargraph */

#define WF_SEC_OPEN 					      0   /*!< Open Security (none) */
#define WF_SEC_WEP_40 				      1   /*!< 40 bit WEP  */
#define WF_SEC_WEP_104				      2   /*!< 104 bit WEP */
#define WF_SEC_WPA_KEY				      3   /*!< WPA using key */
#define WF_SEC_WPA_PASSPHRASE 	    4   /*!< WPA using passphrase */
#define WF_SEC_WPA2_KEY				      5   /*!< WPA2 using key */
#define WF_SEC_WPA2_PASSPHRASE 	    6   /*!< WPA2 using passphrase */
#define WF_SEC_WPA_AUTO_KEY 		    7   /*!< Automatically determine WPA type and use key */
#define WF_SEC_WPA_AUTO_PASSPHRASE  8   /*!< Automatically determine WPA type and use passphrase */

#define AD_HOC 1
#define INFRASTRUCTURE 0

#define N2WchillOut()  wait1Msec(50)                   /*!< Wait 50ms between messages, this allows transmission to be done */
#define N2WsetIPAddress(X)    _N2WsetPar("IPAD", X) /*!< Macro for setting the IP address */
#define N2WsetMask(X)         _N2WsetPar("MASK", X) /*!< Macro for setting the netmask */
#define N2WsetGateway(X)      _N2WsetPar("GWAY", X) /*!< Macro for setting the gateway IP address */
#define N2WsetDNS1(X)         _N2WsetPar("DNS1", X) /*!< Macro for setting the first DNS server IP address */
#define N2WsetDNS2(X)         _N2WsetPar("DNS2", X) /*!< Macro for setting the second DNS server IP address */
#define N2WsetSSID(X)         _N2WsetPar("SSID", X) /*!< Macro for setting the SSID to connect to */
#define N2WsetNetbiosName(X)  _N2WsetPar("NAME", X) /*!< Macro for setting the Netbios Name */



string N2WscratchString;   /*!< string for tmp formatting, scratch data */

intrinsic int StringFind(const char *sSrce, const char *pzFindString)	 asm(opcdStringOps, strOpFind, variableRefCharPtr(sSrce), functionReturn, variableRefCharPtr(pzFindString));


/**
 * Parse the buffer and return the number in the NXT2WIFI response
 * @param buf the buffer to pull the number from
 * @return the number or -1 if no number found.
 */
int N2WgetNumericResponse(tMassiveArray &buf)
{
  long retval = 0;
  int pos = 0;
  for (pos = 0; pos < sizeof(tMassiveArray); pos++)
  {
    memset(N2WscratchString, 0, 20);
    memcpy(N2WscratchString, buf, 19);
    pos = StringFind(N2WscratchString, "=");
    memset(N2WscratchString, 0, 20);
    memcpy(N2WscratchString, &buf[pos+1], sizeof(tMassiveArray) - (pos + 1));
    retval = atoi(N2WscratchString);
    return retval;
  }
  return 0;
}


/**
 * Parse the buffer and return the string in the NXT2WIFI response
 * @param buf the buffer to pull the string from
 * @param response the string to hold the response from the sensor
 */
void N2WgetStringResponse(const tMassiveArray &buf, string &response)
{
  int pos = 0;

  memset(N2WscratchString, 0, 20);
  memcpy(N2WscratchString, buf, 19);
  pos = StringFind(N2WscratchString, "=");
  memset(N2WscratchString, 0, 20);
  memcpy(response, &buf[pos+1], 19);

}


/**
 * Enable debugging
 * @param en whether or not to enable debugging
 * @return true if no error occured, false if it did
 */
bool N2WsetDebug(bool en)
{
  N2WscratchString = (en) ? "$DBG1\n" : "$DBG0\n";
  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  if (!RS485write(RS485txbuffer, strlen(N2WscratchString)))
    return false;
  wait1Msec(10);
  RS485clearRead();
  return true;
}


/**
 * Connect to the currently configured WiFi network
 * @param custom use the default or custom profile
 * @return true if no error occured, false if it did
 */
bool N2WConnect(bool custom)
{
  N2WscratchString = (custom) ? "$WFC1\n" : "$WFC0\n";
  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  if (!RS485write(RS485txbuffer, strlen(N2WscratchString)))
    return false;
  wait1Msec(10);
  RS485clearRead();
  return true;
}


/**
 * Disconnect from the current WiFi network
 * @return true if no error occured, false if it did
 */
bool N2WDisconnect() {
  N2WscratchString = "$WFX\n";
  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  if (!RS485write(RS485txbuffer, strlen(N2WscratchString)))
    return false;
  wait1Msec(10);
  RS485clearRead();
  return true;
}


/**
 * Stop reconnecting when disconnected
 * @return true if no error occured, false if it did
 */
bool N2WStopConnecting()
{
  if (!RS485sendString("$WFQ\n"))
    return false;
  //N2WscratchString = "$WFQ\n";
  //memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  //if (!RS485write(RS485txbuffer, strlen(N2WscratchString)))
  //  return false;
  wait1Msec(10);
  RS485clearRead();
  return true;
}


/**
 * Delete the currently configured custom profile
 * @return true if no error occured, false if it did
 */
bool N2WDelete()
{
  if(!RS485sendString("$WFKD\n"))
    return false;
  wait1Msec(10);
  RS485clearRead();
  return true;
}


/**
 * Save the currently configured custom profile
 * @return true if no error occured, false if it did
 */
bool N2WSave() {
  RS485sendString("$WFKS\n");
  wait1Msec(10);
  RS485clearRead();
  return true;
}


/**
 * Load the currently configured custom profile
 * @return true if no error occured, false if it did
 */
bool N2WLoad() {
  if (!RS485sendString("$WFKL\n"))
    return false;
  wait1Msec(10);
  RS485clearRead();
  return true;
}


/**
 * Reset the NXT2WIFI sensor.
 */
void N2WReset()
{
  RS485sendString("$RST\n");
}


/**
 * Confgure the security settings for the custom profile\n
 * Note: this is an internal function and shouldn't be used directly
 * @param mode the security mode to use
 * @param pKeypass the keypass to use
 * @param keylen the length of the key
 * @param keyind used for WEP, usually set to 0
 * @return true if no error occured, false if it did
 */
bool N2WSecurity(int mode, const ubyte *pKeypass, int keylen, int keyind) {
  int index = 0;
  writeDebugStreamLine("keylen: %d", keylen);
  index = RS485appendToBuff((tBigByteArray)RS485txbuffer, index, "$WFS?");
  sprintf(N2WscratchString, "%d:", mode);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  index = RS485appendToBuff(RS485txbuffer, index, pKeypass, keylen);
  sprintf(N2WscratchString, ":%d\n", keyind);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  return RS485write(RS485txbuffer, index);
}


/**
 * Set the WPA2 key
 * @param key the WPA2 key to use
 * @param len the length of the WPA2 key
 * @return true if no error occured, false if it did
 */
bool N2WSecurityWPA2Key(tBigByteArray &key, int len) {
	return N2WSecurity(WF_SEC_WPA2_KEY, &key[0], len, 0);
}


/**
 * Set the WPA2 passphrase
 * @param passphrase the WPA2 passphrase to use
 * @return true if no error occured, false if it did
 */
bool N2WSecurityWPA2Passphrase(const string passphrase) {
  tByteArray tmpArray;
	memcpy(tmpArray, passphrase, strlen(passphrase));
	return N2WSecurity(WF_SEC_WPA2_PASSPHRASE, &tmpArray[0], strlen(passphrase), 0);
}


/**
 * Set the WPA key
 * @param key the WPA key to use
 * @param len the length of the WPA key
 * @return true if no error occured, false if it did
 */
bool N2WSecurityWPAKey(tBigByteArray &key, int len) {
	return N2WSecurity(WF_SEC_WPA_KEY, &key[0], len, 0);
}


/**
 * Set the WPA passphrase
 * @param passphrase the WPA passphrase to use
 * @return true if no error occured, false if it did
 */
bool N2WSecurityWPAPassphrase(const string &passphrase) {
  tByteArray tmpArray;
	memcpy(tmpArray, passphrase, strlen(passphrase));
	return N2WSecurity(WF_SEC_WPA_PASSPHRASE, &tmpArray[0], strlen(passphrase), 0);
}


/**
 * Set the WEP passphrase.  Please don't use this, it's very insecure.
 * @param passphrase the WEP passphrase to use
 * @return true if no error occured, false if it did
 */
bool N2WSecurityWEP104(const string &passphrase) {
  tByteArray tmpArray;
  writeDebugStreamLine("wep104: %d:", strlen(passphrase));
  writeDebugStreamLine("phrase: %s", passphrase);
	return N2WSecurity(WF_SEC_WEP_104, passphrase, strlen(passphrase), 1);
}


/**
 * Use no security at all.  Just as effective as WEP but less annoying.
 * @return true if no error occured, false if it did
 */
bool N2WSecurityOpen() {
  tByteArray tmpArray;
	return N2WSecurity(WF_SEC_OPEN, &tmpArray[0] , 0, 0);
}


/**
 * Use ad-hoc network or infrastructure
 * @param adhoc if true, use adhoc, otherwise use infrastructure mode
 * @return true if no error occured, false if it did
 */
bool N2WsetAdHoc(bool adhoc) {
  if (adhoc)
    RS485sendString("$WFE?TYPE=1\n");
  else
    RS485sendString("$WFE?TYPE=0\n");
  RS485clearRead();
  return true;
}


/**
 * Set a specific parameter.\n
 * Note: this is an internal function and should not be used directly.
 * @param type the parameter type
 * @param param the value to pass to the parameter
 * @return true if no error occured, false if it did
 */
bool _N2WsetPar(const string type, const string param) {
  int index = 0;
	N2WscratchString = "$WFE?";
	index = RS485appendToBuff((tBigByteArray)RS485txbuffer, index, N2WscratchString);
	index = RS485appendToBuff((tBigByteArray)RS485txbuffer, index, type);
	index = RS485appendToBuff((tBigByteArray)RS485txbuffer, index, "=");
	index = RS485appendToBuff((tBigByteArray)RS485txbuffer, index, param);
	index = RS485appendToBuff((tBigByteArray)RS485txbuffer, index, "\n");
  return RS485write(RS485txbuffer, index);
}


/**
 * Configure to use DHCP.
 * @param yes if set to true, use DHCP
 * @return true if no error occured, false if it did
 */
bool N2WsetDHCP(bool yes) {
  N2WscratchString = (yes) ? "$WFE?DHCP=1\n" : "$WFE?DHCP=0\n";

  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  if (!RS485write(RS485txbuffer, strlen(N2WscratchString)))
    return false;
  wait1Msec(10);
  RS485clearRead();
  return true;
}


/**
 * Set the default profileto connect to after initial startup
 * @param profile The profile to connect to, 0 = none, 1 = custom profile, 2 = default
 * @return true if no error occured, false if it did
 */
bool N2WsetDefaultProfile(ubyte profile)
{
  ubyte len;
  sprintf(N2WscratchString, "$COS%d\n", profile);
  writeDebugStreamLine(N2WscratchString);
  memset(RS485txbuffer, 0, sizeof(RS485txbuffer));
  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  RS485write(RS485txbuffer, strlen(N2WscratchString));
  wait1Msec(10);
	RS485read(RS485rxbuffer, len, 100);
	return (N2WgetNumericResponse(RS485rxbuffer) == 1);
}


/**
 * Check if a the custom profile exists.
 * @return true if the profile exists, false if it does not or an error occured.
 */
bool N2WCustomExist()
{

  ubyte len;
  RS485sendString("$WFKE\n");
  wait1Msec(100);
	RS485read(RS485rxbuffer, len, 100);
	return (N2WgetNumericResponse(RS485rxbuffer) == 1);
}


/**
 * Enter or exit hibernation mode
 * @param hibernate enter hibernation mode if true, exit if false
 * @return true if no error occured, false if it did
 */
bool N2WsetHibernate(bool hibernate)
{
  N2WscratchString = (hibernate) ? "$WFH\n" : "WFO\n";
  if (!RS485sendString(N2WscratchString))
    return false;
  wait1Msec(10);
  RS485clearRead();
  return true;
}



/**
 * Enable or disable power saving
 * @param powersave enable powersaving if true, disable if false
 * @return true if no error occured, false if it did
 */
bool N2WsetPowerSave(bool powersave)
{
  N2WscratchString = (powersave) ? "$WFP1\n" : "WFP0\n";
  if (!RS485sendString(N2WscratchString))
    return false;
  wait1Msec(10);
  RS485clearRead();
  return true;
}


/**
 * Get the current connection status.
 * @return true if no error occured, false if it did
 */
int N2WStatus() {
  ubyte len;
  RS485sendString("$WFGS\n");
  N2WchillOut();
	RS485read(RS485rxbuffer, len, 100);
	return N2WgetNumericResponse(RS485rxbuffer);
}


/**
 * Are we connected to the WiFi network?
 * @return true if connected, false if not connected an error occured
 */
bool N2WConnected() {
	return (N2WStatus() == 2);
}


/**
 * Get the current IP address.  If the address could not be determined
 * 0.0.0.0 is returned.
 * @param IP the string to put the address into
 * @return true if no error occured, false if it did
 */
bool N2WgetIP(string &IP) {
  ubyte len;
  N2WscratchString = "$WFIP\n";
  memset(RS485txbuffer, 0, sizeof(RS485txbuffer));
  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  RS485write(RS485txbuffer, strlen(N2WscratchString));
	wait1Msec(100);
	RS485read(RS485rxbuffer, len, 100);

	if (len < 1)
	{
	  IP = "0.0.0.0";
	  return false;
	}
	else
	{
	  N2WgetStringResponse(RS485rxbuffer, IP);
	  return true;
	}
}


/**
 * Get the NXT2WIFI's MAC address.
 * @param mac string to hold MAC address
 */
void N2WgetMAC(string &mac)
{
  ubyte len;
  sprintf(N2WscratchString, "$MAC\n");
  memset(RS485txbuffer, 0, sizeof(RS485txbuffer));
  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  RS485write(RS485txbuffer, strlen(N2WscratchString));
	N2WchillOut();
	RS485read(RS485rxbuffer, len, 100);

	if (len < 1)
	{
	  mac = "00-00-00-00-00-00";
	}
	else
  {
    N2WgetStringResponse(RS485rxbuffer, mac);
  }
}


/**
 * Open a UDP datastream to a remote host on a port
 * @param id the connection ID to use, can be 1 to 4
 * @param ip the IP address of the remote host
 * @param port the port of the service on the remote host
 * @return true if no error occured, false if it did
 */
bool N2WUDPOpenClient(int id, string ip, int port) {
  int index = 0;
  int respOK = 0;
  ubyte len;
  sprintf(N2WscratchString, "$UDPOC%d?", id);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  index = RS485appendToBuff(RS485txbuffer, index, ip);
  sprintf(N2WscratchString, ",%d\n", port);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  RS485write(RS485txbuffer, index);
	N2WchillOut();
	RS485read(RS485rxbuffer, len, 100);
	respOK = N2WgetNumericResponse(RS485rxbuffer);
	return (respOK == 1) ? true : false;
}


/**
 * Open a listening UDP socket on the specified port
 * @param id the connection ID to use, can be 1 to 4
 * @param port the port on which to start listening
 * @return true if no error occured, false if it did
 */
bool N2WUDPOpenServer(int id, int port) {
  int index = 0;
  int respOK = 0;
  ubyte len;
  sprintf(N2WscratchString, "$UDPOS%d?%d\n", id, port);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  RS485write(RS485txbuffer, index);
	N2WchillOut();
	RS485read(RS485rxbuffer, len, 100);
	respOK = N2WgetNumericResponse(RS485rxbuffer);
	return (respOK == 1) ? true : false;
}


/**
 * Check if there are bytes available for reading on the specified connection.
 * @param id the connection ID to use, can be 1 to 4
 * @return the number of bytes available for reading
 */
int N2WUDPAvail(int id) {
  int index = 0;
  ubyte len;
  sprintf(N2WscratchString, "$UDPL%d\n", id);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  RS485write(RS485txbuffer, index);
	N2WchillOut();
	RS485read(RS485rxbuffer, len, 100);
  if (!RS485write(RS485txbuffer, strlen(N2WscratchString)))
    return false;
  wait1Msec(10);
  RS485clearRead();
  return true;
}


/**
 * Read the specified number of bytes from the connection ID.
 * Bytes are read into the RS485rxbuffer variable.
 * @param id the connection ID to use, can be 1 to 4
 * @param datalen the number of bytes to read
 * @return true if no error occured, false if it did
 */
int N2WUDPRead(int id, int datalen) {
  int index = 0;
  memset(N2WscratchString, 0, 20);
  ubyte offset;

  sprintf(N2WscratchString, "$UDPR%d?%d\n", id, datalen);
  offset = (datalen > 9) ? 8 : 7;
  datalen += offset;
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  RS485write(RS485txbuffer, index);
 	N2WchillOut();
 	//wait1Msec(100);
 	RS485readLargeResponse(RS485rxbuffer, datalen, 100);


 	// This part seperates the data from the pre-amble
	for (int i = 0; i < sizeof(RS485rxbuffer); i++)
	{
	  if (RS485rxbuffer[i] != ',')
	  {
	    continue;
	  }
	  else
	  {
	    N2WscratchString = "";
	    memmove(&RS485rxbuffer[0], &RS485rxbuffer[offset+1], datalen);
	    return atoi(N2WscratchString);
	  }
	}
	return 0;
}


/**
 * Write the specified number of bytes to the connection ID.
 * @param id the connection ID to use, can be 1 to 4
 * @param data the tHugeByteArray containing the data to be transmitted
 * @param datalen the number of bytes to read
 * @return true if no error occured, false if it did
 */
bool N2WUDPWrite(int id, tHugeByteArray &data, int datalen) {
  // writeDebugStreamLine("N2WUDPWrite");
  int index = 0;
  int respOK;
  memset(N2WscratchString, 0, 20);

  sprintf(N2WscratchString, "$UDPW%d?%d,", id, datalen);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  index = RS485appendToBuff((tBigByteArray)RS485txbuffer, index, &data[0], datalen);
  N2WscratchString = "\n";
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  RS485write(RS485txbuffer, index);
  N2WchillOut();
	respOK = N2WgetNumericResponse(RS485rxbuffer);
	return (respOK == 1) ? true : false;
}


/**
 * Close the specified connection.  Use 0 to close all connections
 * @param id the connection ID to use, can be 1 to 4 or 0 for all
 * @return true if no error occured, false if it did
 */
bool N2WUDPClose(int id) {
	StringFormat(N2WscratchString, "$UDPX%d\n", id);
  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  if (!RS485write(RS485txbuffer, strlen(N2WscratchString)))
    return false;
  RS485clearRead();
  return true;
}


/**
 * Flush the buffers of the specified connection
 * @param id the connection ID to use, can be 1 to 4 or 0 for all
 * @return true if no error occured, false if it did
 */
bool N2WUDPFlush(int id) {
	StringFormat(N2WscratchString, "$UDPF%d\n", id);
  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  if (!RS485write(RS485txbuffer, strlen(N2WscratchString)))
    return false;
  wait1Msec(10);
  RS485clearRead();
  return true;
}


/**
 * Open a TCP connection to a remote host on a port
 * @param id the connection ID to use, can be 1 to 4
 * @param host the IP address of name of the remote host
 * @param port the port of the service on the remote host
 * @return true if no error occured, false if it did
 */
bool N2WTCPOpenClient(int id, string host, int port) {
  int index = 0;
  int respOK = 0;
  ubyte len;
  sprintf(N2WscratchString, "$TCPOC%d?", id);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  index = RS485appendToBuff(RS485txbuffer, index, host);
  sprintf(N2WscratchString, ",%d\n", port);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  RS485write(RS485txbuffer, index);
	N2WchillOut();
	RS485read(RS485rxbuffer, len, 100);
	respOK = N2WgetNumericResponse(RS485rxbuffer);
	return (respOK == 1) ? true : false;
}


/**
 * Open a TCP connection to a remote host on a port
 * @param id the connection ID to use, can be 1 to 4
 * @param host the IP address of name of the remote host
 * @param port the port of the service on the remote host
 * @return true if no error occured, false if it did
 */
bool N2WTCPOpenClient(int id, char *host, int port) {
  writeDebugStreamLine("TCPOC pointer version");
  int index = 0;
  int respOK = 0;
  ubyte len;
  writeDebugStreamLine("host: %s", host);
  sprintf(N2WscratchString, "$TCPOC%d?", id);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  index = RS485appendToBuff(RS485txbuffer, index, host);
  sprintf(N2WscratchString, ",%d\n", port);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  RS485write(RS485txbuffer, index);
	N2WchillOut();
	RS485read(RS485rxbuffer, len, 100);
	respOK = N2WgetNumericResponse(RS485rxbuffer);
	return (respOK == 1) ? true : false;
}


/**
 * Open a listening UDP socket on the specified port
 * @param id the connection ID to use, can be 1 to 4
 * @param port the port on which to start listening
 * @return true if no error occured, false if it did
 */
bool N2WTCPOpenServer(int id, int port) {
  int index = 0;
  int respOK = 0;
  ubyte len;
  sprintf(N2WscratchString, "$TCPOS%d?%d\n", id, port);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  RS485write(RS485txbuffer, index);
	wait1Msec(500);
	RS485read(RS485rxbuffer, len, 100);
	respOK = N2WgetNumericResponse(RS485rxbuffer);
	return (respOK == 1) ? true : false;
}


/**
 * Closes a connection to a remote client
 * @param id the connection ID to use, can be 1 to 4
 * @return true if no error occured, false if it did
 */
bool N2WTCPDetachClient(int id) {
  int index = 0;
  int respOK = 0;
  ubyte len;
  sprintf(N2WscratchString, "$TCPD%d\n", id);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  RS485write(RS485txbuffer, index);
	N2WchillOut();
	RS485read(RS485rxbuffer, len, 100);
	respOK = N2WgetNumericResponse(RS485rxbuffer);
	return (respOK == 1) ? true : false;
}


/**
 * Close the specified connection
 * @param id the connection ID to use, can be 1 to 4 or 0 for all
 * @return true if no error occured, false if it did
 */
bool N2WTCPClose(int id) {
	StringFormat(N2WscratchString, "$TCPX%d\n", id);
  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  if (!RS485write(RS485txbuffer, strlen(N2WscratchString)))
    return false;
  RS485clearRead();
  return true;
}


/**
 * Flush the buffers of the specified connection
 * @param id the connection ID to use, can be 1 to 4 or 0 for all
 * @return true if no error occured, false if it did
 */
bool N2WTCPFlush(int id) {
	StringFormat(N2WscratchString, "$TCPF%d\n", id);
  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  if (!RS485write(RS485txbuffer, strlen(N2WscratchString)))
    return false;
  wait1Msec(10);
  RS485clearRead();
  return true;
}


/**
 * Check if there are bytes available for reading on the specified connection.
 * @param id the connection ID to use, can be 1 to 4
 * @return the number of bytes available for reading
 */
int N2WTCPAvail(int id) {
  int index = 0;
  ubyte len;
  sprintf(N2WscratchString, "$TCPL%d\n", id);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  RS485write(RS485txbuffer, index);
	N2WchillOut();
	RS485read(RS485rxbuffer, len, 100);
	return N2WgetNumericResponse(RS485rxbuffer);
}


/**
 * Read the specified number of bytes from the connection ID.
 * Bytes are read into the RS485rxbuffer variable.
 * @param id the connection ID to use, can be 1 to 4
 * @param datalen the number of bytes to read
 * @return true if no error occured, false if it did
 */
int N2WTCPRead(int id, int datalen) {
  int index = 0;
  memset(N2WscratchString, 0, 20);
  ubyte offset;

  sprintf(N2WscratchString, "$TCPR%d?%d\n", id, datalen);
  offset = (datalen > 9) ? 8 : 7;
  datalen += offset;
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  RS485write(RS485txbuffer, index);
 	N2WchillOut();
 	RS485readLargeResponse(RS485rxbuffer, datalen, 100);


	for (int i = 0; i < sizeof(RS485rxbuffer); i++)
	{
	  if (RS485rxbuffer[i] != ',')
	  {
	    continue;
	  }
	  else
	  {
	    N2WscratchString = "";
	    memmove(&RS485rxbuffer[0], &RS485rxbuffer[offset+1], datalen);
	    return atoi(N2WscratchString);
	  }
	}
	return 0;
}


/**
 * Write the specified number of bytes to the connection ID.
 * @param id the connection ID to use, can be 1 to 4
 * @param data the tHugeByteArray containing the data to be transmitted
 * @param datalen the number of bytes to read
 * @return true if no error occured, false if it did
 */
int N2WTCPWrite(int id, tHugeByteArray &data, int datalen)
{
  writeDebugStream("N2WTCPWrite: ");
  writeDebugStreamLine("datalen: %d", datalen);
  int index = 0;
  int respOK;
  memset(N2WscratchString, 0, 20);

  sprintf(N2WscratchString, "$TCPW%d?%d,", id, datalen);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  index = RS485appendToBuff(RS485txbuffer, index, &data[0], datalen);
  N2WscratchString = "\n";
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  RS485write(RS485txbuffer, index);
  N2WchillOut();
	respOK = N2WgetNumericResponse(RS485rxbuffer);
	return (respOK == 1) ? true : false;
}


/**
 * Get the remote client's IP address
 * @param id the connection ID to use, can be 1 to 4
 * @param ip string to hold IP address
 * @return true if no error occured, false if it did
 */
void N2WTCPClientIP(int id, string &ip)
{
  ubyte len;
  sprintf(N2WscratchString, "$TCPSI%d\n", id);
  memset(RS485txbuffer, 0, sizeof(RS485txbuffer));
  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  RS485write(RS485txbuffer, strlen(N2WscratchString));
	N2WchillOut();
	RS485read(RS485rxbuffer, len, 100);

	if (len < 1)
	{
	  ip = "0.0.0.0";
	}
	else
  {
    N2WgetStringResponse(RS485rxbuffer, ip);
  }
}


/**
 * Get the remote client's MAC address.  Only useful for local network client.
 * @param id the connection ID to use, can be 1 to 4
 * @param mac string to hold MAC address
 */
void N2WTCPClientMAC(int id, string &mac)
{
  ubyte len;
  sprintf(N2WscratchString, "$TCPSM%d\n", id);
  memset(RS485txbuffer, 0, sizeof(RS485txbuffer));
  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  RS485write(RS485txbuffer, strlen(N2WscratchString));
	N2WchillOut();
	RS485read(RS485rxbuffer, len, 100);

	if (len < 1)
	{
	  mac = "00-00-00-00-00-00";
	}
	else
  {
    N2WgetStringResponse(RS485rxbuffer, mac);
  }
}


/**
 * Enable the built-in webserver.  This cannot be used together with
 * normal TCP/UDP operations.
 * @param enable enables the web server if set to true, disables it if false
 * @return true if no error occured, false if it did
 */
bool N2WenableWS(bool enable) {
  N2WscratchString = (enable) ? "$SRV=1\n" : "$SRV=0\n";
  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  if (!RS485write(RS485txbuffer, strlen(N2WscratchString)))
    return false;
  wait1Msec(10);
  RS485clearRead();
  return true;
}

/**
 * Read data from the web server.  This cannot be used together with
 * normal TCP/UDP operations. \n
 * Type can be one of the following:
 * - WS_WEBLABEL
 * - WS_WEBBUTTON
 * - WS_WEBCHECKBOX
 * - WS_WEBSLIDER
 * @param type the type of widget
 * @param ID ID of the element that was pressed or submitted
 * @param state additional data from the UI element
 * @param value the value of the UI element
 * @return true if no error occured, false if it did
 */
bool N2WreadWS(ubyte &type, ubyte &ID, ubyte &state, ubyte &value)
{
  ubyte avail = nxtGetAvailHSBytes();
  if (avail >= 5)
  {
    nxtReadRawHS(&RS485rxbuffer[0], avail);
    writeDebugStream("N2WreadWS[%d]: ", avail);

    for (int i = 0; i < avail - 2; i++)
    {
      if ((RS485rxbuffer[i] == 0x00) && (RS485rxbuffer[i+1] == 0x80) && (RS485rxbuffer[i+2] == 0x14))
      {
        type = RS485rxbuffer[i+3];
      	ID = RS485rxbuffer[i+4];
        state = RS485rxbuffer[i+5];
        value = RS485rxbuffer[i+6];
        return true;
      }
    }
  }
  return false;
}


/**
 * Write data to the webserver. \n
 * Type can be one of the following:
 * - WS_WEBLABEL
 * - WS_WEBBUTTON
 * - WS_WEBCHECKBOX
 * - WS_WEBSLIDER
 * - WS_WEBBARGRAPH
 * @param type the type of webpage element
 * @param id the field number in the web page
 * @param wsmessage data to be transmitted
 * @param wsmsglen length of the data
 * @return true if no error occured, false if it did
 */
bool N2WwriteWS(ubyte type, ubyte id, tHugeByteArray &wsmessage, ubyte wsmsglen)
{
	writeDebugStream("N2WwriteWS: ");
	writeDebugStreamLine("datalen: %d, id: %d", wsmsglen, id);
	for (ubyte i = 0; i < wsmsglen; i++)
	{
	  writeDebugStream("0x%2X ", wsmessage[i]);
	}
	writeDebugStreamLine(" ");
	int index = 0;
	int respOK;
	memset(N2WscratchString, 0, 20);

	string web;
	switch (type)
	{
		case 1: web = "$WEBLBL%d?%d,"; break;
		case 2: web = "$WEBBTN%d?%d,"; break;
		case 3: web = "$WEBCHK%d?%d,"; break;
		case 4:	web = "$WEBSLD%d?%d,"; break;
		case 5: web = "$WEBBAR%d?%d,"; break;
		default: break;
	}

	sprintf(N2WscratchString, web, id, wsmsglen);
  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  index = RS485appendToBuff(RS485txbuffer, index, &wsmessage[0], wsmsglen);
  N2WscratchString = "\n";

  index = RS485appendToBuff(RS485txbuffer, index, N2WscratchString);
  RS485write(RS485txbuffer, index);
  N2WchillOut();
	respOK = N2WgetNumericResponse(RS485rxbuffer);
	return (respOK == 1) ? true : false;
}





/**
 * Clears all webpage fields.  This cannot be used together with
 * normal TCP/UDP operations.
 * @return true if no error occured, false if it did
 */
bool N2WclearFields() {
  N2WscratchString = "$RXD\n";
  memcpy(RS485txbuffer, N2WscratchString, strlen(N2WscratchString));
  if (!RS485write(RS485txbuffer, strlen(N2WscratchString)))
    return false;
  wait1Msec(10);
  RS485clearRead();
  return true;
}


#endif // __N2W_H__

/*
 * $Id: benedettelli-nxt2wifi.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
