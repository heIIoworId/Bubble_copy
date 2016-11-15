precision mediump float;

uniform vec3 uLight, uLight2, uColor;
uniform sampler2D uTextureUnit;

varying vec3 vNormal;
varying vec3 vPosition;
varying vec2 vTextureCoor;

void main() {
    vec3 tolight = normalize(uLight - vPosition);
    vec3 tolight2 = normalize(uLight2 - vPosition);
    vec3 normal = normalize(vNormal);

    // diffuse
    float diffuse = max(0.0, dot(normal, tolight));
    diffuse += max(0.0, dot(normal, tolight2));
    vec3 intensity = uColor * diffuse;

    // haze
    vec4 haze = vec4(0.5, 0.5, 0.5, 1.0);
    float ratio = 1 + vPosition.z/35;

    gl_FragColor = ratio * vec4(intensity, 1.0) + (1 - ratio) * haze;
}
