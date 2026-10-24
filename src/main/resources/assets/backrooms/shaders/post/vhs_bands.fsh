#version 330
#extension GL_ARB_separate_shader_objects : require

uniform sampler2D InSampler;

layout(location = 0) in vec2 texCoord;
layout(location = 0) out vec4 fragColor;

// VHS pass 1/4: luma / chroma bandwidth loss.
// Luma is kept at half horizontal resolution, chroma at 1/32, then recombined.
// The chroma blur is two diagonal bilinear taps (each already a 2x2 average).
// Needs `bilinear: true` on its input.

#define W vec3(0.2126, 0.7152, 0.0722)

const float LUMA_SHRINK   = 0.5;
const float CHROMA_SHRINK = 1.0 / 32.0;
const float CHROMA_BLUR   = 3.0; // pixels

vec2 gRes;

vec4 Fetch(vec2 frag, float blurPx) {
    vec2 uv = frag / gRes;
    if (blurPx <= 0.0) return texture(InSampler, uv);
    vec2 d = vec2(blurPx) / gRes;
    return 0.5 * (texture(InSampler, uv + d) + texture(InSampler, uv - d));
}

vec4 ShrinkSample(vec2 frag, float shrinkRatio, float blurPx) {
    float bandWidth = 1.0 / shrinkRatio; // pixels
    float t = mod(frag.x, bandWidth) / bandWidth;
    vec2 p0 = vec2(floor(frag.x * shrinkRatio) / shrinkRatio, frag.y);
    vec2 p1 = p0 + vec2(bandWidth, 0.0);
    return mix(Fetch(p0, blurPx), Fetch(p1, blurPx), t);
}

void main() {
    gRes = vec2(textureSize(InSampler, 0));
    vec2 frag = texCoord * gRes;

    vec4 lumaS   = ShrinkSample(frag, LUMA_SHRINK, 0.0);
    vec4 chromaS = ShrinkSample(frag, CHROMA_SHRINK, CHROMA_BLUR);

    float luma = dot(lumaS.rgb, W);
    vec3 chromaOffset = chromaS.rgb - vec3(dot(chromaS.rgb, W));
    fragColor = vec4(clamp(vec3(luma) + chromaOffset, 0.0, 1.0), lumaS.a);
}
