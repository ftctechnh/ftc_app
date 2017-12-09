
width=12;
holeOffset = 8;
holeRadius = 2.1;

facets = 144;

height=10;
wireRadius = 12;
screwRadius=4;
screwHeight=2;
wallThickness = 1;
baseThickness = 3;

cutOffset=wireRadius*0.01;
cutSize=20;
cutThickness=1.5;

yScale = 0.75;


   
difference() {
    union() {
        translate([0,(baseThickness+wireRadius*yScale)/2,0])
            cube([(wireRadius + wallThickness)*2,
                  baseThickness+wireRadius*yScale, height],center=true);
        
        scale([1,yScale,1])
            cylinder(r=wireRadius + wallThickness, h=height, center=true, $fn = facets);
    }  

    scale([1,yScale,1])
        cylinder(r=wireRadius, h=height+2, center=true, $fn = facets);


    translate([0,(wireRadius*yScale+screwHeight),0])
        rotate([90,0,0])
            cylinder(r=screwRadius, h=wireRadius*yScale+screwHeight,$fn=facets);
    
    translate([0,baseThickness+wireRadius+2,0])
        rotate([90,0,0])
            cylinder(r=holeRadius, h=baseThickness+wireRadius+2,$fn=facets); 


    translate([-cutOffset,0,0])
        rotate([0,45,0]) {
            translate([0,-cutSize/2,(cutSize-cutThickness)/2])
                cube([cutThickness,cutSize,cutSize], center=true);

            translate([(cutSize-cutThickness)/2,-cutSize/2,0])
                cube([cutSize,cutSize,cutThickness], center=true);
    }
}
