

holePadding = 2;
holeDepth = 8;
holeOffset = 8;
holeRadius = 2;
bigHoleRadius=2.5;

connectorThickness = 1;
connectorDepth=2*(holePadding+holeOffset + holeRadius);
connectorHeight=30;
connectorOffset=20;

armHeight= 10;
armLength = 40;
armAngle = 30;
armThickness=1;

armX=sin(armAngle)*armLength;
armY=-cos(armAngle)*armLength;

gripperLength=2*armX + connectorOffset;
gripperThickness=1;
gripperHeight = 20;

holeFacets = 48;

difference() {
    union() {
        translate([-armThickness/2,armY,0])
            cube([armThickness,-armY,armHeight]);
       
       
        translate([connectorOffset-armThickness/2,armY,0])
            cube([armThickness,-armY,armHeight]);
        
        translate([-connectorThickness/2,0,0])
            cube([connectorThickness,
                  connectorDepth,connectorHeight]);
        translate([connectorOffset-connectorThickness/2,0,0])
            cube([connectorThickness,
                  connectorDepth,connectorHeight]);
        
        rotate([0,0,15])
        cylinder(r=armThickness,h=armHeight,$fn=12);
        
        rotate([0,0,180-armAngle])
            translate([-armThickness/2,0,0])
                cube([armThickness,armLength,armHeight]);
        
        translate([0,-armThickness/2,0])
        cube([connectorOffset,armThickness,armHeight]);
        
        translate([connectorOffset,0,0])
            rotate([0,0,15])
                cylinder(r=armThickness,h=armHeight,$fn=12);

        translate([connectorOffset,0,0])
        rotate([0,0,armAngle-180])
            translate([-armThickness/2,0,0])
                cube([armThickness,armLength,armHeight]);

        translate([-armX,armY-gripperThickness/2,0])
        cube([gripperLength,gripperThickness,gripperHeight]);
        
        translate([-armX,armY,0])
            rotate([0,0,15])
                cylinder(r=armThickness,h=armHeight,$fn=12);
        translate([connectorOffset+armX,armY,0])
            rotate([0,0,15])
                cylinder(r=armThickness,h=armHeight,$fn=12);

     }

    translate([0,connectorDepth/2,
               connectorHeight-(holePadding+holeOffset +
                                holeRadius)])
        rotate([0,90,0])
            Holes();
     
    translate([connectorOffset,connectorDepth/2,
              connectorHeight-(holePadding+holeOffset +
                                holeRadius)])
        rotate([0,90,0])
            Holes();
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
