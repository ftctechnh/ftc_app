

holePadding = 2;
holeDepth = 8;
holeOffset = 8;
holeRadius = 2;
bigHoleRadius=2.5;

connectorThickness = 1;
connectorDepth=2*(holePadding+holeOffset + holeRadius);
connectorLength=45;
connectorOffset=80;

armHeight= 10;
armLength = 40;
armAngle = 30;
armThickness=1;

armX=sin(armAngle)*armLength;
armY=-cos(armAngle)*armLength;

gripperLength=2*armX + connectorOffset;
gripperThickness=1;
gripperHeight = 20;

holeFacets = 288;

difference() {
    union() {
        translate([-armThickness/2,armY,0])
            cube([armThickness,-armY,armHeight]);
        
        translate([connectorOffset-armThickness/2,armY,0])
            cube([armThickness,-armY,armHeight]);
        
        translate([-connectorThickness/2,0,0])
            cube([connectorThickness, connectorDepth/2,
                  connectorDepth/2]);
        translate([0,connectorDepth/2,connectorDepth/2])
        rotate([60,0,0])
                Connector();
        
    //    translate([0,connectorDepth/2,0])
    //    cube([connectorOffset, armThickness, connectorDepth*0.75]);
    //    translate([0,armY,0])
    //    cube([connectorOffset,connectorDepth/2 - armY, armThickness]);
       
       
        
        translate([-connectorThickness/2,0,0])
            cube([connectorThickness, connectorDepth/2,
                  connectorDepth/2]);
        translate([0,connectorDepth/2,connectorDepth/2])
        rotate([60,0,0])
                Connector();
        
        translate([connectorOffset-connectorThickness/2,0,0])
            cube([connectorThickness, connectorDepth/2,
                  connectorDepth/2]);
        translate([connectorOffset,connectorDepth/2,connectorDepth/2])
        rotate([60,0,0])
                Connector();
        
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
}

module Connector() {
    translate([0,-connectorDepth/2,-connectorDepth/2])
    difference() {
    union() {
      translate([-connectorThickness/2,connectorDepth/2,0])
        cube([connectorThickness,
              connectorLength - connectorDepth, connectorDepth]);
        translate([0,connectorDepth/2,connectorDepth/2])
            rotate([0,90,0])
                cylinder(r=connectorDepth/2,h=connectorThickness,
                     center=true, $fn=holeFacets);

        translate([0,
                   connectorLength-(holePadding+holeOffset +
                                      holeRadius),
                   connectorDepth/2,])
        rotate([0,90,0])
            cylinder(r=connectorDepth/2,h=connectorThickness,
                 center=true, $fn=holeFacets);
    }
        
        translate([0,
                   connectorLength-(holePadding+holeOffset +
                                      holeRadius),
                   connectorDepth/2,])       
        rotate([0,90,0])
            Holes();

    } 
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
