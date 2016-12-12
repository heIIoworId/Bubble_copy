precision mediump float;

uniform vec3 uLight, uLight2, uColor;
uniform sampler2D uTextureUnit;
uniform vec2 uTime;

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
    // vec3 intensity = uColor * diffuse;

    // texture

    vec2 hello = vTextureCoor + uTime;
    if(hello.x >= 1.0f){
        hello.x -= 1.0f;
    }
    if(hello.y >= 1.0f){
        hello.y -= 1.0f;
    }
    vec4 textureColor = texture2D(uTextureUnit, hello);
    vec3 intensity = textureColor.xyz * diffuse;

    // haze
    vec4 haze = vec4(0.5, 0.5, 0.5, 0.7);
    float ratio = 1.0 + vPosition.z/87.0;

    gl_FragColor = ratio * vec4(intensity, 0.7) + (1.0 - ratio) * haze;
}
