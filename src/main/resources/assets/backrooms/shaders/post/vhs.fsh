#version 330

// VHS post-processing effect, single-pass adaptation of
// https://www.shadertoy.com/view/sltBWM
//
// Host exposes only `In`, `texCoord`, `fragColor`. No time/frame/resolution
// uniform, so resolution comes from textureSize(In, 0), blurs use explicit
// taps (no mip pyramid), and time-animated glitches are static.
//
// This revision fixes the "all black" problem:
//   - alpha is carried through from the source instead of forced to 1.0
//   - the luminosity/color blends that could collapse to black are replaced
//     with a stable YIQ-style luma/chroma separation
//   - the vignette is softened and clamped so it can't crush the image
//   - a DEBUG_MODE switch lets you verify the pipeline step by step
//
// If you still see black, set DEBUG_MODE to 1 first: that outputs the raw
// input untouched. If THAT is black, the post effect isn't being fed a scene
// (a binding / JSON issue), not a shader-math issue.

uniform sampler2D InSampler;
in  vec2 texCoord;
out vec4 fragColor;

// 0 = full VHS effect
// 1 = raw passthrough (is `In` even giving us the scene?)
// 2 = effect but NO vignette (isolate the vignette as the culprit)
// 3 = effect but NO level clamp / tint (isolate colour balancing)
#define DEBUG_MODE 0

// ---- constants ------------------------------------------------------------

#define PI 3.1415926535897932384626433832795
#define W  vec3(0.2126, 0.7152, 0.0722)

// Static "time" surrogate. Add `uniform float Time;` and set this to Time to animate.
#define TIME 0.0

// Pass A
const float LUMA_SHRINK   = 0.5;
const float CHROMA_SHRINK = 1.0 / 32.0;

// Pass B
const float UNSHARP_AMOUNT   = 1.5;
const float BLACK_LEVEL      = 0.06;   // was 0.1 — lighter lift
const float WHITE_LEVEL      = 0.95;   // was 0.9  — keep more highlight
const float SATURATION_LEVEL = 0.85;
const vec3  SHADOW_TINT      = vec3(0.35, 0.0, 0.5); // gentler magenta tint

// Pass C
const float LINE_HEIGHT = 2.0;
const float NOISE_GRAIN = 4.0;
const float NOISE_BLEND = 0.05;

// Pass D
const float WAVE_FREQUENCY  = 70.0;
const float NOISE_LINE_OPAC = 0.30;

// Final
const float FINAL_BLUR    = 1.0;
const float VIGNETTE_AMT   = 0.65; // 0 = none, 1 = strong. Softened from original.

// ---- resolution helpers ---------------------------------------------------

vec2 gRes, gPx, gFrag;

float GetLum(vec3 c) { return dot(c, W); }

float GoldNoise(vec2 xy, float seed) {
    return fract(sin(dot(xy * seed, vec2(12.9898, 78.233))) * 43758.5453);
}

float DEFINE(float a) { return (gRes.y / 450.0) * a; }

vec4 sampleBlur(vec2 uv, float bias) {
    uv = clamp(uv, vec2(0.0), vec2(1.0));
    if (bias <= 0.0) return texture(InSampler, uv);
    vec2 s = gPx * bias * 1.5;
    vec4 c = vec4(0.0);
    c += texture(InSampler, clamp(uv + vec2(-s.x, -s.y), 0.0, 1.0));
    c += texture(InSampler, clamp(uv + vec2( s.x, -s.y), 0.0, 1.0));
    c += texture(InSampler, clamp(uv + vec2(-s.x,  s.y), 0.0, 1.0));
    c += texture(InSampler, clamp(uv + vec2( s.x,  s.y), 0.0, 1.0));
    c += texture(InSampler, uv) * 2.0;
    return c / 6.0;
}

// ---- PASS A: shrink luma / chroma bands (stable version) ------------------
// Instead of the fragile SetLum/ClipColor luminosity blend (which can go black
// on out-of-gamut inputs), we separate luma and chroma directly: sample a
// low-horizontal-res version for luma, an even lower-res version for chroma,
// and recombine. This can never collapse to black for a non-black input.

vec4 ShrinkSample(vec2 frag, float shrinkRatio, float bias) {
    float scale     = 1.0 / gRes.x;
    float numBands  = gRes.x * shrinkRatio;
    float bandWidth = gRes.x / numBands;
    float t = mod(frag.x, bandWidth) / bandWidth;

    frag.x = floor(frag.x * shrinkRatio) / shrinkRatio;
    vec2 uv = frag / gRes;
    vec4 a = sampleBlur(uv, bias);
    uv.x += bandWidth * scale;
    vec4 b = sampleBlur(uv, bias);
    return mix(a, b, t);
}

vec4 PassA(vec2 frag) {
    vec4 lumaS   = ShrinkSample(frag, LUMA_SHRINK,   0.0);
    vec4 chromaS = ShrinkSample(frag, CHROMA_SHRINK, 3.0);

    // luma from the half-res band, chroma (colour offset from grey) from the
    // very-low-res band. Recombine: grey level from luma + colour from chroma.
    float luma = GetLum(lumaS.rgb);
    vec3  chromaOffset = chromaS.rgb - vec3(GetLum(chromaS.rgb));
    vec3  outRgb = clamp(vec3(luma) + chromaOffset, 0.0, 1.0);

    // carry alpha from the luma sample (which is the sharpest source here)
    return vec4(outRgb, lumaS.a);
}

// ---- PASS B: unsharp mask + balancing -------------------------------------

vec4 ClampLevels(vec4 p, float black, float white) {
    p.rgb = mix(p.rgb, vec3(0.0), 1.0 - white);
    p.rgb = mix(p.rgb, vec3(1.0), black);
    return p;
}
vec4 Saturation(vec4 p, float adj) {
    vec3 i = vec3(GetLum(p.rgb));
    return vec4(mix(i, p.rgb, adj), p.a);
}
vec4 TintShadows(vec4 p, vec3 color) {
    const float POWER = 1.5;
    if (color.r > 0.0) p.r = mix(p.r, 1.0 - pow(abs(p.r - 1.0), POWER), color.r);
    if (color.g > 0.0) p.g = mix(p.g, 1.0 - pow(abs(p.g - 1.0), POWER), color.g);
    if (color.b > 0.0) p.b = mix(p.b, 1.0 - pow(abs(p.b - 1.0), POWER), color.b);
    return p;
}

vec4 PassB(vec2 frag) {
    vec4 pixel = PassA(frag);
    vec4 blur = ( PassA(frag + vec2( 2.0, 0.0))
    + PassA(frag + vec2(-2.0, 0.0))
    + PassA(frag + vec2( 0.0, 2.0))
    + PassA(frag + vec2( 0.0,-2.0)) ) * 0.25;

    pixel.rgb = pixel.rgb + (pixel.rgb - blur.rgb) * UNSHARP_AMOUNT;
    pixel.rgb = clamp(pixel.rgb, 0.0, 1.0);

    #if DEBUG_MODE != 3
    pixel = ClampLevels(pixel, BLACK_LEVEL, WHITE_LEVEL);
    pixel = TintShadows(pixel, SHADOW_TINT);
    #endif
    pixel = Saturation(pixel, SATURATION_LEVEL);
    return pixel;
}

// ---- PASS C: interlace + grain --------------------------------------------

float BlendSoftLight(float b, float s) {
    return (s < 0.5) ? (2.0 * b * s + b * b * (1.0 - 2.0 * s))
    : (sqrt(b) * (2.0 * s - 1.0) + 2.0 * b * (1.0 - s));
}

vec4 PassC(vec2 frag) {
    vec4 pixel = PassB(frag);

    bool isOdd = mod(floor(frag.y), 2.0 * LINE_HEIGHT) >= LINE_HEIGHT;
    if (isOdd) pixel.rgb *= 0.96;

    float g = GoldNoise(floor(frag / NOISE_GRAIN) + 1.0, TIME + 1.0);
    vec3 noise = vec3(g);
    vec3 sl = vec3(BlendSoftLight(pixel.r, g),
            BlendSoftLight(pixel.g, g),
            BlendSoftLight(pixel.b, g));
    pixel.rgb = mix(pixel.rgb, sl, NOISE_BLEND);
    return pixel;
}

// ---- PASS D: glitches (static) --------------------------------------------

vec2 Wave(float frequency, float offset, vec2 frag, vec2 uv) {
    float phase = floor(frag.y / (gRes.y / frequency));
    float nMod  = GoldNoise(vec2(1.0 + phase, phase), 10.0);
    float o = sin((uv.y + fract(TIME * 0.05)) * PI * 2.0 * frequency)
    * ((offset * nMod) / gRes.x);
    return uv + vec2(o, 0.0);
}

vec4 WhiteNoise(float thickness, float opacity, vec4 pixel, vec2 frag) {
    if (GoldNoise(vec2(600.0, 500.0), fract(TIME) * 10.0) > 0.97) {
        float start = floor(GoldNoise(vec2(800.0, 50.0), fract(TIME)) * gRes.y);
        float end   = floor(start + thickness);
        if (floor(frag.y) >= start && floor(frag.y) < end) {
            float freq   = GoldNoise(vec2(850.0, 50.0), fract(TIME)) * 3.0 + 1.0;
            float offset = GoldNoise(vec2(900.0, 51.0), fract(TIME));
            float x = floor(frag.x) / floor(gRes.x) + offset;
            float white = pow(cos(PI * fract(x * freq) / 2.0), 10.0) * opacity;
            float grit  = GoldNoise(vec2(floor(frag.x / 3.0), 800.0), fract(TIME));
            white = max(white - grit * 0.3, 0.0);
            pixel.rgb += white;
        }
    }
    return pixel;
}

vec4 PassD(vec2 frag) {
    float WAVE_OFFSET  = DEFINE(1.0);
    float NOISE_HEIGHT = DEFINE(6.0);

    vec2 uv = frag / gRes;
    uv = Wave(WAVE_FREQUENCY, WAVE_OFFSET, frag, uv);

    vec4 pixel = PassC(uv * gRes);
    pixel = WhiteNoise(NOISE_HEIGHT, NOISE_LINE_OPAC, pixel, frag);
    return pixel;
}

// ---- FINAL: blur + vignette -----------------------------------------------

vec4 Televisionfy(vec4 pixel, vec2 uv) {
    float raw = pow(uv.x * (1.0 - uv.x) * uv.y * (1.0 - uv.y), 0.25) * 2.2;
    float v = clamp(raw, 0.0, 1.0);
    // blend toward 1.0 so the vignette only darkens edges, never the centre to black
    v = mix(1.0, v, VIGNETTE_AMT);
    pixel.rgb *= v;
    return pixel;
}

vec4 FinalBlur(vec2 frag) {
    vec2 s = gPx * FINAL_BLUR * gRes; // pixel step
    vec4 c = vec4(0.0);
    c += PassD(frag + vec2(-s.x, -s.y)) * 1.0;
    c += PassD(frag + vec2( 0.0, -s.y)) * 2.0;
    c += PassD(frag + vec2( s.x, -s.y)) * 1.0;
    c += PassD(frag + vec2(-s.x,  0.0)) * 2.0;
    c += PassD(frag)                    * 4.0;
    c += PassD(frag + vec2( s.x,  0.0)) * 2.0;
    c += PassD(frag + vec2(-s.x,  s.y)) * 1.0;
    c += PassD(frag + vec2( 0.0,  s.y)) * 2.0;
    c += PassD(frag + vec2( s.x,  s.y)) * 1.0;
    return c / 16.0;
}

// ---- main -----------------------------------------------------------------

void main() {
    gRes  = vec2(textureSize(InSampler, 0));
    gPx   = 1.0 / gRes;
    gFrag = texCoord * gRes;

    vec4 src = texture(InSampler, texCoord);

    #if DEBUG_MODE == 1
    fragColor = src;                 // raw passthrough
    return;
    #endif

    vec4 col = FinalBlur(gFrag);

    #if DEBUG_MODE != 2
    col = Televisionfy(col, texCoord);
    #endif

    // Preserve source alpha so the effect can be transparent where the scene is.
    fragColor = vec4(col.rgb, src.a);
}
