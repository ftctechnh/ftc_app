---
layout: post
title:  "First Night"
---
Alex: Tonight I messed with updating last year’s open-source vision library to this year’s game. I have so far identified upwards of 4 vision tasks that my library should be able to complete, and due to the nature of the game elements being bright shapes, this should be easy. The tasks or “Detectors” I have chosen to work on are the following:
Sampling Order (Returns the location of the Gold Element in Auton. CENTER, LEFT, RIGHT
Sampling Alignment (Returns true/false when the bot is aligned with the Gold element, used for Auton)
Generic Game Element Detector (Returns locations of game elements)
Top Down Game Element Detector (Returns the angle, distance, and location of gameelemts from a top down view)
I have started work on the Sampling Order detector, as this is just a modification of last year’s jewel detector. With just some basic modifications I got the detector returning the proper order of game elements for the sampling section of auton. However it is far from perfect as it does not yet check for distance between the elements, or ignore white noise very well. Although it in most likelihood already good enough for competition usage, I will be investing much more time into it to achieve the 100% success rate seen last year.

So far this year is seeming very promising for CV and I can't wait to get to release the full update.
