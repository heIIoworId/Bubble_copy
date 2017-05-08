precision mediump float;

uniform mat4 uProjMatrix;
uniform mat4 uModelViewMatrix;
uniform mat4 uModelMatrix;
uniform mat4 uViewMatrix;
uniform mat4 uNormalMatrix;
uniform vec3 campos;

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
  vec4 wPosition = uModelMatrix * vec4(aPosition, 1.0);
  vec4 wNormal = uModelMatrix * vec4(aNormal, 0.0);
  gl_Position = uProjMatrix * tPosition;
  vec4 camera = uViewMatrix * vec4(campos, 1.0);
  vec3 toPos = normalize(camera.xyz - wPosition.xyz);
//  RefractDir = -refract(toPos, wNormal, 1.0/1.52);
//  RefractDir = -reflect(toPos, wNormal.xyz);
//  RefractDir = -reflect(normalize(aPosition), vNormal);
  RefractDir = -refract(-toPos, wNormal.xyz, 1.0/1.52);

  vTex_Coord = aTex_Coord;
}