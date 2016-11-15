precision mediump float;

uniform vec3 uLight, uLight2, uColor;
uniform sampler2D sampler;
uniform samplerCube cubemap;

varying vec3 vNormal;
varying vec3 vPosition;
varying vec2 vTex_Coord;
varying vec3 RefractDir;

void main() {
  vec3 tolight = normalize(uLight - vPosition);
  vec3 tolight2 = normalize(uLight2 - vPosition);
  vec3 normal = normalize(vNormal);

  vec3 Kd = vec3(1.0, 1.0, 1.0);

  Kd = texture2D(sampler, vTex_Coord).rgb;
  float diffuse = max(0.0, dot(normal, tolight));
  diffuse += max(0.0, dot(normal, tolight2));
  vec3 intensity = uColor * diffuse + Kd;
//  gl_FragColor = vec4(intensity, 0.3);

 // vec4 envColor = textureCube(cubemap, RefractDir);

  //gl_FragColor = texture2D(sampler, vTex_Coord);
  //gl_FragColor = textureCube(cubemap, vec3(1,0,0));
  vec4 envColor = textureCube(cubemap, RefractDir);
  gl_FragColor = envColor;

//  gl_FragColor = vec4(uColor, 1.0);

}
