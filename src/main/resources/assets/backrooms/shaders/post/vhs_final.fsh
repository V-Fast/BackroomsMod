#version 330
#extension GL_ARB_separate_shader_objects : require

uniform sampler2D InSampler;

layout(location = 0) in vec2 texCoord;
layout(location = 0) out vec4 fragColor;

// VHS pass 4/4: 3x3 binomial blur + vignette, written to the main target.
// [1 2 1] x [1 2 1] / 16 is exactly four bilinear taps at half-pixel offsets.
// Needs `bilinear: true` on its input.

const float VIGNETTE_AMT = 0.65;

void main() {
    vec2 h = 0.5 / vec2(textureSize(InSampler, 0));
    vec4 c = 0.25 * (texture(InSampler, texCoord + vec2(-h.x, -h.y))
                   + texture(InSampler, texCoord + vec2( h.x, -h.y))
                   + texture(InSampler, texCoord + vec2(-h.x,  h.y))
                   + texture(InSampler, texCoord + vec2( h.x,  h.y)));

    float raw = pow(texCoord.x * (1.0 - texCoord.x) * texCoord.y * (1.0 - texCoord.y), 0.25) * 2.2;
    c.rgb *= mix(1.0, clamp(raw, 0.0, 1.0), VIGNETTE_AMT);
    fragColor = c;
}
