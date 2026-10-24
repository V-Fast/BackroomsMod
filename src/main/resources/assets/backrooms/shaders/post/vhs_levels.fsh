#version 330
#extension GL_ARB_separate_shader_objects : require

uniform sampler2D InSampler;

layout(location = 0) in vec2 texCoord;
layout(location = 0) out vec4 fragColor;

// VHS pass 2/4: unsharp mask, levels, shadow tint, desaturation.
// Reads the output of vhs_bands (nearest filtering).

#define W vec3(0.2126, 0.7152, 0.0722)

const float UNSHARP_AMOUNT   = 1.5;
const float BLACK_LEVEL      = 0.06;
const float WHITE_LEVEL      = 0.95;
const float SATURATION_LEVEL = 0.85;
const vec3  SHADOW_TINT      = vec3(0.35, 0.0, 0.5);

vec4 ClampLevels(vec4 p, float black, float white) {
    p.rgb = mix(p.rgb, vec3(0.0), 1.0 - white);
    p.rgb = mix(p.rgb, vec3(1.0), black);
    return p;
}

vec4 Saturation(vec4 p, float adj) {
    vec3 i = vec3(dot(p.rgb, W));
    return vec4(mix(i, p.rgb, adj), p.a);
}

vec4 TintShadows(vec4 p, vec3 color) {
    const float POWER = 1.5;
    if (color.r > 0.0) p.r = mix(p.r, 1.0 - pow(abs(p.r - 1.0), POWER), color.r);
    if (color.g > 0.0) p.g = mix(p.g, 1.0 - pow(abs(p.g - 1.0), POWER), color.g);
    if (color.b > 0.0) p.b = mix(p.b, 1.0 - pow(abs(p.b - 1.0), POWER), color.b);
    return p;
}

void main() {
    vec2 px = 1.0 / vec2(textureSize(InSampler, 0));

    vec4 pixel = texture(InSampler, texCoord);
    vec4 blur = (texture(InSampler, texCoord + vec2( 2.0, 0.0) * px)
               + texture(InSampler, texCoord + vec2(-2.0, 0.0) * px)
               + texture(InSampler, texCoord + vec2( 0.0, 2.0) * px)
               + texture(InSampler, texCoord + vec2( 0.0,-2.0) * px)) * 0.25;

    pixel.rgb = clamp(pixel.rgb + (pixel.rgb - blur.rgb) * UNSHARP_AMOUNT, 0.0, 1.0);
    pixel = ClampLevels(pixel, BLACK_LEVEL, WHITE_LEVEL);
    pixel = TintShadows(pixel, SHADOW_TINT);
    fragColor = Saturation(pixel, SATURATION_LEVEL);
}
