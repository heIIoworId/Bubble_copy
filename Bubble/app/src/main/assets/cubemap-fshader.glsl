precision mediump float;

uniform vec3 uLight, uLight2, uColor;
uniform samplerCube cubemap;

varying vec3 vNormal;
varying vec3 vPosition;
varying vec3 RefractDir;


void main() {

  vec4 envColor = textureCube(cubemap, RefractDir);

  gl_FragColor = envColor;
  gl_FragColor.a = 0.5;

}
