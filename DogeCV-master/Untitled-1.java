public Mat processFrame2(Mat frame)
    {
        // if the frame is not empty, process it
        if (!frame.empty())
        {
            // init
            //Mat flippedImage = new Mat();
            Mat resizedImage = new Mat();
            Mat blurredImage = new Mat();
            Mat hsvImage = new Mat();
            Mat maskRedLower = new Mat();
            Mat maskRedUpper = new Mat();
            Mat maskRedCombined = new Mat();
            Mat morphOutputRed = new Mat();
            Mat maskBlue = new Mat();
            Mat morphOutputBlue = new Mat();

            //Core.flip(frame, flippedImage, 1);

            //resize to lower resolution
            //Imgproc.resize(frame,resizedImage,new Size(640,480),0,0,Imgproc.INTER_CUBIC);

            // remove some noise
            Imgproc.blur(frame, blurredImage, new Size(7, 7));

            // convert the frame to HSV
            Imgproc.cvtColor(blurredImage, hsvImage, Imgproc.COLOR_RGB2HSV);

            // get thresholding values from the UI
            // remember: H ranges 0-180, S and V range 0-255
            Scalar minValuesRedUpper = new Scalar(HSVfilters.HueStartUpper_red, HSVfilters.SaturationStart_red, HSVfilters.ValueStart_red);
            Scalar maxValuesRedUpper = new Scalar(HSVfilters.HueStopUpper_red, HSVfilters.SaturationStop_red, HSVfilters.ValueStop_red);
            Scalar minValuesRedLower = new Scalar(HSVfilters.HueStartLower_red, HSVfilters.SaturationStart_red, HSVfilters.ValueStart_red);
            Scalar maxValuesRedLower = new Scalar(HSVfilters.HueStopLower_red, HSVfilters.SaturationStop_red, HSVfilters.ValueStop_red);
            Scalar minValuesBlue = new Scalar(HSVfilters.HueStart_blue, HSVfilters.SaturationStart_blue, HSVfilters.ValueStart_blue);
            Scalar maxValuesBlue = new Scalar(HSVfilters.HueStop_blue, HSVfilters.SaturationStop_blue, HSVfilters.ValueStop_blue);

            //threshold HSV image to select red ball
            Core.inRange(hsvImage, minValuesRedLower, maxValuesRedLower, maskRedLower);
            Core.inRange(hsvImage, minValuesRedUpper, maxValuesRedUpper, maskRedUpper);

            //Combine the two red masks
            Core.addWeighted(maskRedUpper, 1.0, maskRedLower, 1.0, 0.0, maskRedCombined);

            //threshold HSV image to select red ball
            Core.inRange(hsvImage, minValuesBlue, maxValuesBlue, maskBlue);

            // morphological operators
            // dilate with large element, erode with small ones
            Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24));
            Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 12));

            Imgproc.erode(maskRedCombined, morphOutputRed, erodeElement);
            Imgproc.erode(morphOutputRed, morphOutputRed, erodeElement);

            Imgproc.dilate(morphOutputRed, morphOutputRed, dilateElement);
            Imgproc.dilate(morphOutputRed, morphOutputRed, dilateElement);

            Imgproc.erode(maskBlue, morphOutputBlue, erodeElement);
            Imgproc.erode(morphOutputBlue, morphOutputBlue, erodeElement);

            Imgproc.dilate(morphOutputBlue, morphOutputBlue, dilateElement);
            Imgproc.dilate(morphOutputBlue, morphOutputBlue, dilateElement);

            // find the jewels contours and show them
            frame = this.findAndDrawBalls(morphOutputRed, morphOutputBlue, frame);

        }


        return frame;
    }

public Mat findAndDrawBalls(Mat maskedImageRed, Mat maskedImageBlue, Mat frame)
    {
        //init
        List<MatOfPoint> contoursRed = new ArrayList<>();
        List<MatOfPoint> contoursBlue = new ArrayList<>();
        Mat hierarchyRed = new Mat();
        Mat hierarchyBlue = new Mat();
        Rect blueRect = null;
        Rect redRect = null;
        boolean MTOROSC = false;

        // find contours
        Imgproc.findContours(maskedImageRed, contoursRed, hierarchyRed, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(maskedImageBlue, contoursBlue, hierarchyBlue, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

        //filter(contoursRed,contoursRed);

        //if any contours exist...
        if (hierarchyRed.size().height > 0 && hierarchyRed.size().width > 0)
        {
            // for each contour, display it in blue
            for (int idx = 0; idx >= 0; idx = (int) hierarchyRed.get(0, idx)[0])
            {
                Imgproc.drawContours(frame, contoursRed, idx, new Scalar(255, 0, 0));
            }

            MatOfPoint2f approxCurve = new MatOfPoint2f();

            Rect chosenRect = null;
            int chosenDiffrence = 99999;

            //For each contour found
            for (int i = 0; i < contoursRed.size(); i++)
            {
                if (i >= 1)
                {
                    MTOROSC = true;
                }

                //Convert contoursRed(i) from MatOfPoint to MatOfPoint2f
                MatOfPoint2f contour2f = new MatOfPoint2f(contoursRed.get(i).toArray());

                //Processing on mMOP2f1 which is in type MatOfPoint2f
                double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
                Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

                //Convert back to MatOfPoint
                MatOfPoint points = new MatOfPoint(approxCurve.toArray());

                // Get bounding rect of contour
                Rect rect = Imgproc.boundingRect(points);
                redRect = rect;

                //System.out.println("X: " + rect.x);
                //System.out.println("Y: " + rect.y);

                // You can find this by printing the area of each found rect, then looking and finding what u deem to be perfect.
                // Run this with the bot, on a balance board, with jewels in their desired location. Since jewels should mostly be
                // in the same position, this hack could work nicely.
                double perfectArea = 750; 
                
                double area = Imgproc.contourArea(contoursRed.get(i));
                double areaDiffrence = Math.abs(perfectArea - area);
                double areaWeight = 0.1; // Since we're dealing with 100's of pixels


                // Just declaring vars to make my life eassy
                double x = rect.x;
                double y = rect.y;
                double w = rect.width;
                double h = rect.height;
                Point centerPoint = new Point(x + ( w/2), y + (h/2));

                double cubeRatio = Math.max(Math.abs(h/w), Math.abs(w/h)); // Get the ratio. We use max in case h and w get swapped??? it happens when u account for rotation
                double ratioDiffrence = Math.abs(1- cubeRatio);
                double ratioWeight = 10; // Since most of the time the area diffrence is a decimal place

                double finalDiffrence = (ratioDiffrence * ratioWeight) + (areaDiffrence * areaWeight)


                // Optional to ALWAYS return a result.
                if(chosenRect == null){
                    chosenRect = redRect;
                    chosenDiffrence = finalDiffrence;
                }

                // Update the chosen rect if the diffrence is lower then the curreny chosen
                // Also can add a condition for min diffrence to filter out VERY wrong answers
                // Think of diffrence as score. 0 = perfect
                if(finalDiffrence < chosenDiffrence){
                    chosenDiffrence = finalDiffrence;
                    chosenRect = redRect;
                }

                // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
                
            }
            Imgproc.rectangle(frame, new Point(chosenRect.x, chosenRect.y), new Point(chosenRect.x + chosenRect.width, chosenRect.y + rect.height), new Scalar(0, 255, 0, 255), 2);
            Imgproc.putText(frame, "R", new Point(chosenRect.x - 5, chosenRect.y - 10), Core.FONT_HERSHEY_PLAIN, 2, new Scalar(0, 255, 0), 2);
        } else {
            redRect = null;
        }

        //if any contours exist...
        if (hierarchyBlue.size().height > 0 && hierarchyBlue.size().width > 0)
        {
            // for each contour, display it in blue
            for (int idx = 0; idx >= 0; idx = (int) hierarchyBlue.get(0, idx)[0])
            {
                Imgproc.drawContours(frame, contoursBlue, idx, new Scalar(0, 0, 255));
            }

            MatOfPoint2f approxCurve = new MatOfPoint2f();

            //For each contour found
            for (int i = 0; i < contoursBlue.size(); i++)
            {
                if (i >= 1)
                {
                    MTOROSC = true;
                }

                //Convert contoursRed(i) from MatOfPoint to MatOfPoint2f
                MatOfPoint2f contour2f = new MatOfPoint2f(contoursBlue.get(i).toArray());
                //Processing on mMOP2f1 which is in type MatOfPoint2f
                double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
                Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

                //Convert back to MatOfPoint
                MatOfPoint points = new MatOfPoint(approxCurve.toArray());

                // Get bounding rect of contour
                Rect rect = Imgproc.boundingRect(points);
                blueRect = rect;

                //System.out.println("X: " + rect.x);
                //System.out.println("Y: " + rect.y);

                // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
                Imgproc.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0, 255), 2);
                Imgproc.putText(frame, "B", new Point(rect.x - 5, rect.y - 10), Core.FONT_HERSHEY_PLAIN, 2, new Scalar(0, 255, 0), 2);
            }
        } else
        {
            blueRect = null;
        }

        if ((blueRect != null) && (redRect != null))
        {
            if (!MTOROSC)
            {
                long confidence;

                if (blueRect.y < redRect.y)
                {
                    confidence = Math.round(((blueRect.y - redRect.y) * .66) + 100);
                } else
                {
                    confidence = Math.round(((redRect.y - blueRect.y) * .66) + 100);
                }

                if (confidence > 100)
                {
                    confidence = 100;
                } else if (confidence < 0)
                {
                    confidence = 0;
                }

                if (blueRect.y > redRect.y)
                {
                    Imgproc.putText(frame, "Order: red,blue", new Point(5, 25), Core.FONT_HERSHEY_PLAIN, 2, new Scalar(0, 255, 0), 2);
                    order = "Red,Blue";
                    jewelsOrder = jewelsOrder.redFirst;
                } else
                {
                    Imgproc.putText(frame, "Order: blue,red", new Point(5, 25), Core.FONT_HERSHEY_PLAIN, 2, new Scalar(0, 255, 0), 2);
                    order = "Blue,Red";
                    jewelsOrder = jewelsOrder.blueFirst;

                }

                Imgproc.putText(frame, "Confidence: " + confidence + "%", new Point(5, 50), Core.FONT_HERSHEY_PLAIN, 2, new Scalar(0, 255, 0), 2);
            } else
            {
                Imgproc.putText(frame, "Err - MTOROSC", new Point(5, 25), Core.FONT_HERSHEY_PLAIN, 2, new Scalar(0, 255, 0), 2);
                jewelsOrder = jewelsOrder.unknown;
            }
        } else
        {
            if (!MTOROSC)
            {
                Imgproc.putText(frame, "Order: ???", new Point(5, 25), Core.FONT_HERSHEY_PLAIN, 2, new Scalar(0, 255, 0), 2);
                jewelsOrder = jewelsOrder.unknown;
            } else
            {
                Imgproc.putText(frame, "Err - MTOROSC", new Point(5, 25), Core.FONT_HERSHEY_PLAIN, 2, new Scalar(0, 255, 0), 2);
                jewelsOrder = jewelsOrder.unknown;
            }
        }

        MTOROSC = false;

        //Imgproc.putText(frame, "FPS: " + Math.round(1000 / lastTimeMills), new Point(5, 75), Core.FONT_HERSHEY_PLAIN, 2, new Scalar(0, 255, 0), 2);

        return frame;
    }