gripperLength=70;
gripperThickness = 120;
guideDepth=24;
gripperDepth=20;

gripperMax=4;
gripperMin=-4;

gripperBase= 10;

gripperY = guideDepth*guideDepth/(4*gripperLength);
gripperX = sqrt(guideDepth*guideDepth/4 - gripperY*gripperY);


holePadding = 2;
holeDepth = 128;
holeOffset = 8;
holeRadius = 2;
bigHoleRadius=2.5;


holeFacets = 24;


 
GripperOutline();


module GripperOutline() {
    rotate([0,-90,0])
    difference() {
        union() {
            translate([0,0,-gripperThickness/2]) {
                linear_extrude(height=gripperThickness)
                    polygon(points=[[-guideDepth/2,0],
                                    [-guideDepth/2,gripperLength],
                                    [-guideDepth/2+gripperDepth,gripperLength+gripperMax],
                                    [-guideDepth/2+gripperDepth+0.14,
                                     gripperLength+gripperMin+0.038],
                                    [0, gripperLength-gripperBase],
                                    [gripperX,gripperY]]);               

                
                cylinder(r=guideDepth/2,h=gripperThickness,$fn=holeFacets);
                translate([-guideDepth/2+gripperDepth+0,gripperLength,0])
                    scale([0.25,1,1])
                        cylinder(r=(gripperMax-gripperMin)/2,h=gripperThickness, $fn=144);
            }
        }
        rotate([0,0,45])
            Holes();
    }
}

module Guide() {
    
}


module Holes() {
    cylinder(r=bigHoleRadius,h=holeDepth,
             center=true, $fn=holeFacets);
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
