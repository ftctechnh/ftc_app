![](https://i.imgur.com/QfRkLt4.jpg)
**STATUS:** _70% Competition Ready_   
**LAST UPDATED:** _1/1/2018 with DogeCV 1.1_

# About
The Cryptobox detector is used to get positions of each column inside a cryptobox. This can be used to align your robot with a certain collumn of the cryptobox.

### Todo
- Better Motion tracking 
- Col. Label Memory
### Known Issues
- Jeans
### Credits
- Levi

# Details
The cryptobox detector uses a mix of the Levi color filter and Levi line detector in order to extract each column plastic. First the detector runs one of the two color filters to extract either the red or blue in the image. Using this mask, it finds lines in the image.

Once lines are find close and similar ones are merged. Then checks between parallel lines are done. These checks involve checking if there is a certain amount of a color between then. If this is true (IE: There is alot of red between two lines) the detector assumes these lines are part of the same column and groups them, else it deems them different. Using this we get the position of all the columns. We then memorize the last _x_ positions of each column and avg them. We do this to remove noise and smooth out the result. How many previous positions it memorizes can be tuned.

# Usage
The detector returns doubles that represent the position of each column along the x-axis. This can be then plugged into a PID or other control loop and allow the bot to navigate to a desired column.

## Tuning
Currently the Cryptobox Detector is still in development so many options are not yet available to it such as down-scaling and quality modes.

### Position Memory
As previously described the detector memorizes a number of previous locations of the column to average and smooth it's results. This can tuned via the `trackableMemory` parameter. The higher this value the smoother the results but the further behind they will be. This also depends on FPS since a higher FPS results in more positions per second. For 7-10 FPS we recommend values between 3-5;

## Parameters
- `colorFilterRed` - Color filter to use for red
- `colorFilterBlue` - Color filter to use for blue
- `detectionMode` - Mode used to detect, `RED` and `BLUE` are currently only implemented modes, each representing which color you what to detect.
- `trackableMemory` - How many positions to keep in memory and average.
- `rotateMat` - Rotate the image when processing (wont be visible on preview, change this if you see detections working horizontally) [Usually: Landscape = false, Portrait = true]

## Returned Data
Currently this detector returns the following:
- `isCryptoBoxDetected()` - Is the full box detected?
- `isColumnDetected()` - Is at least one column detected?
- `getCryptoBoxLeftPosition()` - Get the left column position (int on x-axis)
- `getCryptoBoxCenterPosition()` - Get the center column position (int on x-axis)
- `getCryptoBoxRightPosition()` - Get the right column position (int on x-axis)
- `getCryptoBoxPositions()` - Array on Ints that represent columns found, in order from left to right

# Changelogs
- **1.1**
  - Now uses Color Filter API