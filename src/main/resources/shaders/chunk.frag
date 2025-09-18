#version 330 core
in vec2 texCoord;
flat in int texLayer;

uniform sampler2DArray texArray;
out vec4 FragColor;

void main() {
    FragColor = texture(texArray, vec3(texCoord, texLayer));
}
