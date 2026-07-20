#version 330

uniform sampler2D In;

in vec2 texCoord;

out vec4 fragColor;

// RECTANGLE
bool rect(vec2 p, vec2 minPos, vec2 maxPos) {
  return p.x >= minPos.x && p.x <= maxPos.x && p.y >= minPos.y &&
         p.y <= maxPos.y;
}

// TRIANGLE
bool triangle(vec2 p, vec2 a, vec2 b, vec2 c) {
  float area =
      abs((a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)) * 0.5);

  float area1 =
      abs((p.x * (a.y - b.y) + b.x * (p.y - a.y) + a.x * (b.y - p.y)) * 0.5);

  float area2 =
      abs((p.x * (b.y - c.y) + c.x * (p.y - b.y) + b.x * (c.y - p.y)) * 0.5);

  float area3 =
      abs((p.x * (c.y - a.y) + a.x * (p.y - c.y) + c.x * (a.y - p.y)) * 0.5);

  return abs(area - (area1 + area2 + area3)) < 0.001;
}

// TWO TRIANGLE STAR
bool star(vec2 p) {
  // up
  bool up = triangle(p, vec2(0.5, 0.15), vec2(0.10, 0.70), vec2(0.90, 0.70));

  // down
  bool down = triangle(p, vec2(0.10, 0.30), vec2(0.90, 0.30), vec2(0.5, 0.85));

  return up || down;
}

void main() {
  vec2 p = texCoord;

  // Background
  vec4 color = vec4(0.4, 0.7, 1.0, 1.0);

  bool text = false;
  bool starShape = false;

  // STAR

  if (star(p)) {
    starShape = true;
  }

  // BOOM

  // B
  if (rect(p, vec2(0.22, 0.42), vec2(0.24, 0.58)) ||
      rect(p, vec2(0.24, 0.56), vec2(0.29, 0.58)) ||
      rect(p, vec2(0.24, 0.50), vec2(0.29, 0.52)) ||
      rect(p, vec2(0.24, 0.42), vec2(0.29, 0.44)) ||
      rect(p, vec2(0.29, 0.42), vec2(0.31, 0.50)) ||
      rect(p, vec2(0.29, 0.52), vec2(0.31, 0.58))) {
    text = true;
  }

  // O
  if (rect(p, vec2(0.35, 0.42), vec2(0.37, 0.58)) ||
      rect(p, vec2(0.44, 0.42), vec2(0.46, 0.58)) ||
      rect(p, vec2(0.37, 0.56), vec2(0.44, 0.58)) ||
      rect(p, vec2(0.37, 0.42), vec2(0.44, 0.44))) {
    text = true;
  }

  // O
  if (rect(p, vec2(0.50, 0.42), vec2(0.52, 0.58)) ||
      rect(p, vec2(0.59, 0.42), vec2(0.61, 0.58)) ||
      rect(p, vec2(0.52, 0.56), vec2(0.59, 0.58)) ||
      rect(p, vec2(0.52, 0.42), vec2(0.59, 0.44))) {
    text = true;
  }

  // M
  if (rect(p, vec2(0.65, 0.42), vec2(0.67, 0.58)) ||
      rect(p, vec2(0.75, 0.42), vec2(0.77, 0.58)) ||
      rect(p, vec2(0.70, 0.48), vec2(0.72, 0.58)) ||
      rect(p, vec2(0.67, 0.55), vec2(0.72, 0.58)) ||
      rect(p, vec2(0.72, 0.55), vec2(0.76, 0.58))) {
    text = true;
  }

  if (starShape) {
    color = vec4(1.0, 0.0, 0.0, 1.0);  // red star
  }

  if (text) {
    color = vec4(1.0, 1.0, 0.0, 1.0);  // yellow text
  }

  fragColor = color;
}