/*!@addtogroup mindsensors
 * @{
 * @defgroup nxtcam NXTCam Vision System
 * NXTCam Vision System
 * @{
 */

/*
 * $Id: mindsensors-nxtcam.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __NXTCAM_H__
#define __NXTCAM_H__
/** \file mindsensors-nxtcam.h
 * \brief Mindsensors NXTCam driver
 *
 * mindsensors-nxtcam.h provides an API for the Mindsensors NXTCam.  This version is an extensive rewrite of
 * Gordon Wyeth's driver. The blob information is no longer kept in seperate array,
 * but in an array of structs.
 *
 * Changelog:
 * - 1.0: Partial rewrite of original driver, using structs instead of arrays to hold blob info
 * - 1.1: Further rewrite to use common.h functions to further align with standard driver framework
 * - 1.2: Added NXTCAMgetCenter() to calculate the center of a single blob
 * - 1.3: Added NXTCAMinitTL() to enable line tracking mode<br>
 *        Fixed bug in NXTCAMinit() that did not configure object tracking properly<br>
 *        Added extra wait times after each issued command in init functions
 * - 1.4: Removed printDebugLine from driver
 * - 1.5: Added ability to specify I2C address with optional argument.  Defaults to 0x02 when not specified.
 *
 * License: You may use this code as you wish, provided you give credit where it's due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat
 * \author Gordon Wyeth
 * \date 03 Dec 2010
 * \version 1.5
 * \example mindsensors-nxtcam-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define MAX_BLOBS         8     /*!< Maximum number of blobs returned by the NXTCam */
#define NXTCAM_I2C_ADDR   0x02  /*!< I2C address used by the NXTCam */
#define NXTCAM_CMD_REG    0x41  /*!< Register used for issuing commands */
#define NXTCAM_COUNT_REG  0x42  /*!< Register used to hold number of blobs detected */
#define NXTCAM_DATA_REG   0x43  /*!< Register containing data pertaining to blobs */

#define SIDE_CENTER(X1, X2) ((X1 + X2) / 2)  /*!< Returns the center of a side */

/*! Blob struct, contains all the data for a blob. */
typedef struct {
  int x1;       /*!< left */
  int y1;       /*!< top */
  int x2;       /*!< right */
  int y2;       /*!< bottom */
  int colour;   /*!< Blob colour */
  int size;     /*!< Blob size */
} blob;

/*! Array of blob as a typedef, this is a work around for RobotC's inability to pass an array to a function */
typedef blob blob_array[MAX_BLOBS];

tByteArray NXTCAM_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray NXTCAM_I2CReply;      /*!< Array to hold I2C reply data */

// "public" functions
bool NXTCAMinit(tSensors link, ubyte address = NXTCAM_I2C_ADDR);
bool NXTCAMinitTL(tSensors link, ubyte address = NXTCAM_I2C_ADDR);
int NXTCAMgetBlobs(tSensors link, blob_array &blobs, bool mergeBlobs, ubyte address = NXTCAM_I2C_ADDR);
int NXTCAMgetBlobs(tSensors link, blob_array &blobs, ubyte address = NXTCAM_I2C_ADDR);

// internal functions, used by the above
bool _camera_cmd(tSensors link, byte cmd, ubyte address);
int _mergeBlobs(int blob1, int blob2, int nblobs, blob_array &blobs);
int _merge(int nblobs, blob_array &blobs);
void _sortBlobs(int nblobs, blob_array &blobs);
void NXTCAMgetAverageCenter(blob_array &blobs, int nblobs, int colourindex, int &x, int &y);
void NXTCAMgetCenter(blob_array &blobs, int index, int &x, int &y);

/*! boolean to signal if there are still blobs that might qualify for merging */
bool still_merging = false;

/**
 * This function sends a command to the camera over I2C.
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the sensor port number
 * @param cmd the command to be sent
 * @param address the I2C address to use, optional, defaults to 0x02
 * @return true if no error occured, false if it did
 */
bool _camera_cmd(tSensors link, byte cmd, ubyte address) {
  NXTCAM_I2CRequest[0] = 3;                 // Message size
  NXTCAM_I2CRequest[1] = address;           // I2C Address
  NXTCAM_I2CRequest[2] = NXTCAM_CMD_REG;    // Register used for issuing commands
  NXTCAM_I2CRequest[3] = cmd;               // Command to be executed

  return writeI2C(link, NXTCAM_I2CRequest);
}

/**
 * This function initialises camera ready to find blobs and sort them according to size.
 * @param link the sensor port number
 * @param address the I2C address to use, optional, defaults to 0x02
 * @return true if no error occured, false if it did
 */
bool NXTCAMinit(tSensors link, ubyte address) {
  if (!_camera_cmd(link, 'D', address)) // Stop object tracking
    return false;
  wait1Msec(500);

  if (!_camera_cmd(link, 'A', address)) // Sort by size
    return false;
  wait1Msec(500);

  if (!_camera_cmd(link,'B', address))  // Set object tracking mode
    return false;
  wait1Msec(500);

  if (!_camera_cmd(link,'E', address))  // Start object tracking
    return false;
  wait1Msec(500);

  return true;
}


/**
 * This function initialises camera ready to track lines.
 * @param link the sensor port number
 * @param address the I2C address to use, optional, defaults to 0x02
 * @return true if no error occured, false if it did
 */
bool NXTCAMinitTL(tSensors link, ubyte address) {
  if (!_camera_cmd(link, 'D', address)) // Stop object tracking
    return false;
  wait1Msec(500);

  if (!_camera_cmd(link, 'X', address)) // Do not sort objects
    return false;
  wait1Msec(500);

  if (!_camera_cmd(link,'L', address))  // Set tracking line mode
    return false;
  wait1Msec(500);

  if (!_camera_cmd(link,'E', address))  // Start tracking
    return false;

  wait1Msec(500);
  return true;
}


/**
 * This function fetches the blob data from the camera and merges the colliding ones.
 * @param link the sensor port number
 * @param blobs the array of blobs
 * @param mergeBlobs whether or not to merge the colliding blobs
 * @param address the I2C address to use, optional, defaults to 0x02
 * @return the number of blobs detected, -1 if an error occurred
 */
int NXTCAMgetBlobs(tSensors link, blob_array &blobs, bool mergeBlobs, ubyte address) {
  int _nblobs = NXTCAMgetBlobs(link, blobs, address);
  if (mergeBlobs == true)
    return _merge(_nblobs, blobs);
  return _nblobs;
}

/**
 * This function fetches the blob data from the camera.
 * @param link the sensor port number
 * @param blobs the array of blobs
 * @param address the I2C address to use, optional, defaults to 0x02
 * @return the number of blobs detected, -1 if an error occurred
 */
int NXTCAMgetBlobs(tSensors link, blob_array &blobs, ubyte address) {
  int _nblobs = 0;

  // clear the array used for the blobs
  memset(blobs, 0, sizeof(blob_array));

  // Request number of blobs from the count register
  NXTCAM_I2CRequest[0] = 2;                 // Message size
  NXTCAM_I2CRequest[1] = address;           // I2C Address
  NXTCAM_I2CRequest[2] = NXTCAM_COUNT_REG;  // Register used to hold number of blobs detected

  if (!writeI2C(link, NXTCAM_I2CRequest, NXTCAM_I2CReply, 1))
    return -1;

  _nblobs = NXTCAM_I2CReply[0];
  if (_nblobs > MAX_BLOBS) {
    return -1;
  }

  // Get nblobs of blob data from the camera
  for (int _i = 0; _i < _nblobs; _i++) {

    // Request blob data
    NXTCAM_I2CRequest[0] = 2;                         // Message size
    NXTCAM_I2CRequest[1] = address;           // I2C Address
    NXTCAM_I2CRequest[2] = NXTCAM_DATA_REG + _i * 5;  // Register containing data pertaining to blob

    if (!writeI2C(link, NXTCAM_I2CRequest, NXTCAM_I2CReply, 5))
      return -1;

    // Put the I2C data into the blob
    blobs[_i].colour    = (int)NXTCAM_I2CReply[0];
    blobs[_i].x1        = (int)NXTCAM_I2CReply[1];
    blobs[_i].y1        = (int)NXTCAM_I2CReply[2];
    blobs[_i].x2        = (int)NXTCAM_I2CReply[3];
    blobs[_i].y2        = (int)NXTCAM_I2CReply[4];
    blobs[_i].size      = abs(blobs[_i].x2 - blobs[_i].x1) * abs(blobs[_i].y2 - blobs[_i].y1);
  }
  return _nblobs;
}

/**
 * Go through the blobs and calls _mergeBlobs.
 *
 * Note: this is an internal function and should not be called directly.
 * @param nblobs the number of blobs
 * @param blobs the array of blobs
 * @return the number of blobs detected
 */
int _merge(int nblobs, blob_array &blobs) {
  still_merging = true;
  while (still_merging == true) {
    still_merging = false;
    for(int i = 0; i < nblobs; i++) {
      for(int j = i+1; j < nblobs; j++) {
        nblobs = _mergeBlobs(i,j, nblobs, blobs);
      }
    }
  }
  return nblobs;
}

/**
 * Check if two blobs can be merged into one.  blob1 will be replaced with the
 * new merged blob and blob2 will be destroyed.
 *
 * Note: this is an internal function and should not be called directly.
 * @param blob1 the index number of the first blob
 * @param blob2 the index number of the second blob
 * @param nblobs the number of blobs
 * @param blobs the array of blobs
 * @return the number of blobs detected
 */
int _mergeBlobs(int blob1, int blob2, int nblobs, blob_array &blobs) {
  int _blob1_center;
  int _blob2_center;
  int _blob1_proj;
  int _blob2_proj;
  bool _overlapx = false;
  bool _overlapy = false;

  // If either pf the blobs are size 0, just skip them
  if (blobs[blob1].size == 0 || blobs[blob2].size == 0)
    return nblobs;

  // If the colours don't match, don't _merge them
  if (blobs[blob1].colour != blobs[blob2].colour)
    return nblobs;

  // Find the center of the top sides of each blob and their projections onto
  // the X plane from center to right corner
  _blob1_center = SIDE_CENTER(blobs[blob1].x1, blobs[blob1].x2);
  _blob2_center = SIDE_CENTER(blobs[blob2].x1, blobs[blob2].x2);
  _blob1_proj = blobs[blob1].x2 - _blob1_center;
  _blob2_proj = blobs[blob2].x2 - _blob2_center;

  // Check if the projections overlap
  if ((_blob1_proj + _blob2_proj) > abs(_blob1_center - _blob2_center))
    _overlapx = true;

  // Find the center of the left sides of each blob and their projections onto
  // the Y plane from center to bottom corner
  _blob1_center = SIDE_CENTER(blobs[blob1].y1, blobs[blob1].y2);
  _blob2_center = SIDE_CENTER(blobs[blob2].y1, blobs[blob2].y2);
  _blob1_proj = blobs[blob1].y2 - _blob1_center;
  _blob2_proj = blobs[blob2].y2 - _blob2_center;

  // Check if the projections overlap
  if ((_blob1_proj + _blob2_proj) > abs(_blob1_center - _blob2_center))
    _overlapy = true;

  // If both projections are overlapping, then the blobs are _merged.
  // Find the smallest bounding box for both blobs and replace blob1
  // with this new information.  Blob2 gets set to 0 size and
  // recursively replaced by the next in line.
  if ((_overlapx == true) && (_overlapy == true)) {
    blobs[blob1].x1 = min2(blobs[blob1].x1, blobs[blob2].x1);
    blobs[blob1].y1 = min2(blobs[blob1].y1, blobs[blob2].y1);
    blobs[blob1].x2 = max2(blobs[blob1].x2, blobs[blob2].x2);
    blobs[blob1].y2 = max2(blobs[blob1].y2, blobs[blob2].y2);
    blobs[blob1].size = abs(blobs[blob1].x2 - blobs[blob1].x1) * abs(blobs[blob1].y2 - blobs[blob1].y1);

    for (int _i = blob2; _i < nblobs - 1; _i++) {
      memcpy(blobs[_i], blobs[_i+1], sizeof(blob));
    }

    nblobs--;
    memset(blobs[nblobs], 0, sizeof(blob));
    still_merging = true;
  }
  _sortBlobs(nblobs, blobs);

  // return the new number of blobs
  return nblobs;
}

/**
 * This function sorts the blobs in the array using insertion sort.
 *
 * Note: this is an internal function and should not be called directly.
 * @param nblobs the number of blobs
 * @param blobs the array of blobs
 */
void _sortBlobs(int nblobs, blob_array &blobs) {
  blob _tmpBlob;
  int i, j;

  // Outer boundary definition
  for (i = 1; i < nblobs; i++) {
    if (blobs[i-1].size >= blobs[i].size )
      continue;

    memcpy(_tmpBlob, blobs[i], sizeof(blob));

    // inner loop: elements shifted down until insertion point found
    for (j = i-1; j >= 0; j--) {
      if ( blobs[j].size >= _tmpBlob.size )
        break;
      memcpy(blobs[j+1], blobs[j], sizeof(blob));
    }
    // insert the blob
    memcpy(blobs[j+1], _tmpBlob, sizeof(blob));
  }
}

/**
 * Calculate the average center of all the blobs of a specific colour.
 *
 * Note: this is an experimental function and may not function properly yet.
 * @param blobs the array of blobs
 * @param nblobs the number of blobs
 * @param colourindex the colour of the blobs of which the average center is to be calculated
 * @param x x-coordinate of the center
 * @param y y-coordinate of the center
 */
void NXTCAMgetAverageCenter(blob_array &blobs, int nblobs, int colourindex, int &x, int &y) {
  long _totalX = 0;
  long _totalY = 0;
  int _counter = 0;

  for (int i = 0; i < nblobs; i++){
    if ((blobs[i].colour == colourindex) && (blobs[i].size > 400)) {
      _totalX += SIDE_CENTER(blobs[i].x1, blobs[i].x2);
      _totalY += SIDE_CENTER(blobs[i].y1, blobs[i].y2);
      _counter++;
    }
  }
  x = _totalX / (_counter - 1);
  y = _totalY / (_counter  -1);
}


/**
 * Calculate the center of a specified blob.
 *
 * Note: this is an experimental function and may not function properly yet.
 * @param blobs the array of blobs
 * @param index the blob that you want to calculate the center of
 * @param x x-coordinate of the center
 * @param y y-coordinate of the center
 */
void NXTCAMgetCenter(blob_array &blobs, int index, int &x, int &y) {
  x = SIDE_CENTER(blobs[index].x1, blobs[index].x2);
  y = SIDE_CENTER(blobs[index].y1, blobs[index].y2);
}

#endif // __NXTCAM_H__

/*
 * $Id: mindsensors-nxtcam.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
