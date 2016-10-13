# FTC_Library
Common classes used in FIRST Tech Challenge OpModes.  The OpModes are stored in another repository (FTC_OpModes).

## ArrayQueue.java
High performance **FIFO** for intra-thread buffering.  When used for inter-thread buffering, external synchronization is required.

An example of usage can be seen at 
* http://olliesworkshops.blogspot.com/2015/11/arduino-wire-library-for-i2c-sensors.html
* http://olliesworkshops.blogspot.com/2015/11/fix-in-ftc-wire-library.html

## Bno055.java
Bno055 is a 9 degree of freedom (9 DOF) sensor from Bosch. It is also known as inertial measurement unit (IMU). In addition of the three core sensors, gyroscope, accelerometer, and magnetometer.

Additional descriptions can be found at
* http://olliesworkshops.blogspot.com/2015/11/bno055-imu-sensor-with-ftc-wire-library.html

## Box.java
Box is used as a wrapper class to enable parameter passing by reference.
In Bno055.java, this feature is used to implement a dynamic scheduler that allows
multiple entries for the same item.  For details see
* http://olliesworkshops.blogspot.com/2015/11/bno055-imu-sensor-with-ftc-wire-library.html

## DataLogger.java
With DataLogger an OpMode can collect unlimited amount of data items in a Comma Separated Values (CSV) file
that can be opened with Excel for further analysis.  There is no limit how many DataLoggers an OpMode can use.

Some examples and illustrations can be found at
* http://olliesworkshops.blogspot.com/2015/11/using-excel-to-analyze-ftc-opmode.html
* http://olliesworkshops.blogspot.com/2015/10/i2cxl-maxsonar-registers.html
* http://olliesworkshops.blogspot.com/2015/10/i2cxl-maxsonar-with-wire-library.html
* http://olliesworkshops.blogspot.com/2015/10/ftc-i2c-sensor-driver-architecture.html
* http://olliesworkshops.blogspot.com/2015/11/arduino-wire-library-for-i2c-sensors.html


## Wire.java
This is emulating the Arduino Wire library used to communicate with I2C devices.
Due to reasonable long response times (5 - 15 ms) through Device Interface Module (DIM),
all methods in FTC Wire library are non-blocking.

Some examples and illustrations can be found at
* http://olliesworkshops.blogspot.com/2015/11/arduino-wire-library-for-i2c-sensors.html
* http://olliesworkshops.blogspot.com/2015/11/fix-in-ftc-wire-library.html
* http://olliesworkshops.blogspot.com/2015/10/i2cxl-maxsonar-with-wire-library.html
* http://olliesworkshops.blogspot.com/2015/11/i2c-color-sensor-with-ftc-wire-library.html
* http://olliesworkshops.blogspot.com/2015/11/bno055-imu-sensor-with-ftc-wire-library.html
