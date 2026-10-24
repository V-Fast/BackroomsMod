#version 330
#extension GL_ARB_separate_shader_objects : require

uniform sampler2D InSampler;

layout(location = 0) in vec2 texCoord;
layout(location = 0) out vec4 fragColor;

// VHS pass 3/4: horizontal wobble, interlacing, grain and the white noise line.
// One texture tap. Animated with the game time from the `Globals` uniform, which
// wraps every 24000 ticks: every period below divides 24000 or is a random
// reseed, so the wrap is never visible. Frozen while the game is paused.

#include <minecraft:globals.glsl>

#define PI 3.1415926535897932384626433832795

#define TICKS (GameTime * 24000.0)

// continuous: wobble scrolls down the screen, one cycle per 20 ticks
#define WAVE_SCROLL fract(TICKS * 0.05)
// stepped: grain re-rolls 30 times per second, kept small to stay precise in sin()
#define GRAIN_SEED (1.0 + mod(floor(TICKS * 1.5), 97.0))
// stepped: a new roll every 3 ticks decides if (and where) the white noise line shows
#define NOISE_SEED fract(floor(TICKS / 3.0) * 0.61803398875)

const float LINE_HEIGHT = 2.0;
const float NOISE_GRAIN = 4.0;
const float NOISE_BLEND = 0.05;
const float WAVE_FREQUENCY  = 70.0;
const float NOISE_LINE_OPAC = 0.30;

vec2 gRes;

float GoldNoise(vec2 xy, float seed) {
    return fract(sin(dot(xy * seed, vec2(12.9898, 78.233))) * 43758.5453);
}

float BlendSoftLight(float b, float s) {
    return (s < 0.5) ? (2.0 * b * s + b * b * (1.0 - 2.0 * s))
                     : (sqrt(b) * (2.0 * s - 1.0) + 2.0 * b * (1.0 - s));
}

vec2 Wave(float frequency, float offset, vec2 frag, vec2 uv) {
    float phase = floor(frag.y / (gRes.y / frequency));
    float nMod  = GoldNoise(vec2(1.0 + phase, phase), 10.0);
    float o = sin((uv.y + WAVE_SCROLL) * PI * 2.0 * frequency) * ((offset * nMod) / gRes.x);
    return uv + vec2(o, 0.0);
}

vec4 WhiteNoise(float thickness, float opacity, vec4 pixel, vec2 frag) {
    if (GoldNoise(vec2(600.0, 500.0), NOISE_SEED * 10.0) > 0.97) {
        float start = floor(GoldNoise(vec2(800.0, 50.0), NOISE_SEED) * gRes.y);
        float end   = floor(start + thickness);
        if (floor(frag.y) >= start && floor(frag.y) < end) {
            float freq   = GoldNoise(vec2(850.0, 50.0), NOISE_SEED) * 3.0 + 1.0;
            float offset = GoldNoise(vec2(900.0, 51.0), NOISE_SEED);
            float x = floor(frag.x) / floor(gRes.x) + offset;
            float white = pow(cos(PI * fract(x * freq) / 2.0), 10.0) * opacity;
            float grit  = GoldNoise(vec2(floor(frag.x / 3.0), 800.0), NOISE_SEED);
            pixel.rgb += max(white - grit * 0.3, 0.0);
        }
    }
    return pixel;
}

void main() {
    gRes = vec2(textureSize(InSampler, 0));
    vec2 frag = texCoord * gRes;

    vec2 uv = Wave(WAVE_FREQUENCY, gRes.y / 450.0, frag, texCoord);
    vec2 wFrag = uv * gRes;
    vec4 pixel = texture(InSampler, uv);

    // interlacing
    if (mod(floor(wFrag.y), 2.0 * LINE_HEIGHT) >= LINE_HEIGHT) pixel.rgb *= 0.96;

    // grain
    float g = GoldNoise(floor(wFrag / NOISE_GRAIN) + 1.0, GRAIN_SEED);
    vec3 sl = vec3(BlendSoftLight(pixel.r, g), BlendSoftLight(pixel.g, g), BlendSoftLight(pixel.b, g));
    pixel.rgb = mix(pixel.rgb, sl, NOISE_BLEND);

    fragColor = WhiteNoise((gRes.y / 450.0) * 6.0, NOISE_LINE_OPAC, pixel, frag);
}
