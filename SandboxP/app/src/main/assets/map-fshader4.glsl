precision mediump float;

uniform vec3 uLight, uLight2, uColor;
uniform sampler2D uTextureUnit; // texture
uniform sampler2D uTextureNormalUnit; // normal map of the texture

varying vec3 vNormal;
varying vec3 wNormal;
varying vec3 vPosition;
varying vec3 wPosition;
varying vec2 vTextureCoor;

void main() {
    vec3 tolight = normalize(uLight - vPosition);
    vec3 tolight2 = normalize(uLight2 - vPosition);
    vec3 normal = normalize(vNormal);

    // data from the normal map
    vec3 textureNormal = texture2D(uTextureNormalUnit, vTextureCoor).xyz;

    // diffuse
    float diffuse = max(0.0, dot(normal, tolight));
    diffuse += max(0.0, dot(normal, tolight2));
    // vec3 intensity = uColor * diffuse;

    // blinn-phong
    float lambertian = max(dot(tolight, normal), 0.0);
    float specular = 0.0;
    vec3 specColor = vec3(1.0, 1.0, 1.0);

    if (lambertian > 0.0) {
        float shininess = 15.0;
        vec3 viewDir = normalize(-vPosition);
        vec3 halfDir = normalize(tolight + viewDir);
        float specAngle = max(dot(halfDir, normal), 0.0);
        specular = pow(specAngle, shininess);
    }

    // tri-planar mapping
    vec3 blending = abs(wNormal);
    blending = normalize(max(blending, 0.00001));
    float b = (blending.x + blending.y + blending.z);
    blending /= vec3(b, b, b);

    float scale = 0.05;
    vec4 xaxis = texture2D(uTextureUnit, wPosition.yz * scale);
    vec4 yaxis = texture2D(uTextureUnit, wPosition.xz * scale);
    vec4 zaxis = texture2D(uTextureUnit, wPosition.xy * scale);
    vec4 tex = xaxis * blending.x + xaxis * blending.y + zaxis * blending.z;

    // color = texture + diffuse + blinn-phong
    vec3 intensity = tex.xyz * diffuse * lambertian + specular * specColor;

    // haze
    vec4 haze = vec4(0.7, 0.7, 0.7, 1.0);
    float ratio = 1.0 + vPosition.z/99.0;

    gl_FragColor = ratio * vec4(intensity, 1.0) + (1.0 - ratio) * haze;
}
