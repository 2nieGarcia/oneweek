package io.github.oneweek.tools;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {
    public static void main(String[] args) {
        TexturePacker.process(
            "assets/skin/raw", // Input folder containing PNGs
            "assets/skin", // Output folder for uiskin.atlas & uiskin.png
            "uiskin" // Atlas name
        );
        System.out.println("TexturePacker completed!");
    }
}
