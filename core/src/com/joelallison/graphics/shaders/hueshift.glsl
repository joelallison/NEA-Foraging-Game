// this was adapated from https://gist.github.com/mairod/a75e7b44f68110e1576d77419d608786 and https://www.yobowargames.com/libgdx-in-10-minute-slices-shaders-introduction/

vec3 hue_shift(vec3 color, float dhue) {
    float s = sin(dhue);
    float c = cos(dhue);
    return (color * c) + (color * s) * mat3(
        vec3(0.167444, 0.329213, -0.496657),
        vec3(-0.327948, 0.035669, 0.292279),
        vec3(1.250268, -1.047561, -0.202707)) + dot(vec3(0.299, 0.587, 0.114), color) * (1.0 - c);
}

#ifdef GL_ES
precision mediump float;
#endif

varying float hue;
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
    vec3 color = texture2D(u_texture, v_texCoords).rgb;
    vec3 shifted = vec3(hue_shift(color, hue));

    gl_FragColor = vec4(shifted, 1.0);
}