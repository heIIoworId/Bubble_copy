precision mediump float;

uniform mat4 uProjMatrix;
uniform mat4 uModelViewMatrix;
uniform mat4 uNormalMatrix;
uniform mat4 uModelMatrix;

attribute vec3 aPosition;
attribute vec3 aNormal;
attribute vec2 aTextureCoor;

varying vec3 vNormal;
varying vec3 wNormal;
varying vec3 vPosition;
varying vec3 wPosition;
varying vec2 vTextureCoor;

void main() {
  vNormal = vec3(uNormalMatrix * vec4(aNormal, 0.0));

  // pass position to fragment shader
  vec4 tPosition = uModelViewMatrix * vec4(aPosition, 1.0);
  vPosition = vec3(tPosition);
  wPosition = (uModelMatrix * vec4(aPosition, 1.0)).xyz;
  wNormal = (uModelMatrix * vec4(aNormal, 1.0)).xyz;
  gl_Position = uProjMatrix * tPosition;

  // pass texture coor. to fragment shader
  vTextureCoor = aTextureCoor;
}