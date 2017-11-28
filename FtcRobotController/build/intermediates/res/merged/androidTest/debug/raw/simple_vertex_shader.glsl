uniform mat4 modelViewProjectionMatrix;
attribute vec4 vertexPosition;

void main()
{                                	  	  
   gl_Position = modelViewProjectionMatrix * vertexPosition;
}
