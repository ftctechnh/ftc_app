

gridThickness = 1;
gridDepth = 34+32;

gridLength = 200;
gridSideLength = 32;
gridSpacing = gridSideLength*cos(30);

perimiterWidth = 2*gridSpacing;
perimiterHeight = 3 * gridSideLength;
perimiterThickness = 2;



holeShift=5+16;
holeDepth = 6;
holeOffset = 8;
holeRadius = 2;


holeFacets = 144;
echo(gridSpacing);
echo(perimiterWidth);
scale([1,59/perimiterWidth,1])
difference() {
    scale([1,1,1]) {
        intersection() {
            union() {
               scale([1,1,1])
                Grid();

                translate([gridSideLength,0,0])
                    cylinder(r=6,h=gridDepth, center=true, $fn=6);
                translate([-(perimiterHeight-perimiterThickness)/2,0,0])
                   cube([perimiterThickness,
                         perimiterWidth,
                         gridDepth], center=true);
                    
                translate([0,(perimiterWidth-perimiterThickness)/2,0])
                    cube([perimiterHeight-1.2, perimiterThickness, gridDepth], center=true);
                translate([0,-(perimiterWidth-perimiterThickness)/2,0])
                    cube([perimiterHeight-1.2, perimiterThickness, gridDepth], center=true);
                

            }
            cube([perimiterHeight, perimiterWidth,gridDepth],center=true);
        }
    }
                translate([gridSideLength,0,holeShift])
                    rotate_extrude(convexity = 10, $fn=holeFacets)
                        translate([10, 0, 0])
                            circle(r = 5, $fn=holeFacets);
    
    translate([0,perimiterWidth/2,holeShift])
        rotate([90,0,0])
            Holes();
    translate([0,-perimiterWidth/2,holeShift])
        rotate([90,0,0])
            Holes();
    
    translate([32,perimiterWidth/2,holeShift])
        rotate([90,0,0])
            Holes();
    translate([-32,-perimiterWidth/2,holeShift])
        rotate([90,0,0])
            Holes();
    
    translate([32,-perimiterWidth/2,holeShift])
        rotate([90,0,0])
            Holes();
    translate([-32,perimiterWidth/2,holeShift])
        rotate([90,0,0])
            Holes();
}

module Grid() {
    difference() {
        union() {
            GridSet();
            rotate([0,0,60])
                GridSet();
            rotate([0,0,-60])
                GridSet();
        }
        translate([gridSideLength*4,0,0])
        cube([gridLength,gridThickness*3,gridDepth*3], center=true);
    }
}

module GridSet() {
    GridSlice();
    translate([0,gridSpacing,0])
        GridSlice();
    translate([0,2*gridSpacing,0])
        GridSlice();
     translate([0,-gridSpacing,0])
        GridSlice();
    translate([0,-2*gridSpacing,0])
        GridSlice();    
}    
module GridSlice() {
        cube([gridLength,gridThickness,gridDepth], center=true);
}

module Holes() {
    translate([holeOffset,0,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([-holeOffset,0,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([0,holeOffset,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([0,-holeOffset,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
}
