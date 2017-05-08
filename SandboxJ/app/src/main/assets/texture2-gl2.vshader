precision mediump float;

uniform mat4 uProjMatrix;
uniform mat4 uModelViewMatrix;
uniform mat4 uNormalMatrix;

attribute vec3 aPosition;
attribute vec3 aNormal;
attribute vec2 aTex_Coord;

varying vec3 vNormal;
varying vec3 vPosition;
varying vec2 vTex_Coord;
varying vec3 RefractDir;


void main() {
  vNormal = vec3(uNormalMatrix * vec4(aNormal, 0.0));

  // send position (eye coordinates) to fragment shader
  vec4 tPosition = uModelViewMatrix * vec4(aPosition, 1.0);
  vPosition = vec3(tPosition);
  gl_Position = uProjMatrix * tPosition;
//  RefractDir = -reflect(normalize(aPosition), vNormal);
  RefractDir = -refract(normalize(aPosition), vNormal, 1.0/1.52);

  vTex_Coord = aTex_Coord;
}