#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec2 aTexCoord;
layout (location=2) in float aTexLayer;

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix; // optional, if you move chunks
uniform mat4 viewMatrix;

out vec2 texCoord;
flat out int texLayer;

void main() {
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(aPos, 1.0);
    texCoord = aTexCoord;
    texLayer = int(aTexLayer);
}
