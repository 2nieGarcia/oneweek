package io.github.oneweek.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;


public class TexturePackerTool {
    public static void main(String[] args) {
//        TexturePacker.process(
//            "assets/skin/raw", // Input folder containing PNGs
//            "assets/skin", // Output folder for uiskin.atlas & uiskin.png
//            "uiskin" // Atlas name
//        );

        TexturePacker.process(
            "assets/skin/quiz/quizpanel", // Input folder (where GUI images are)
            "assets/atlas",           // Output folder (where atlas & packed texture will be saved)
            "quiz-panel"                         // Atlas file name (output: pixel-ui.atlas & pixel-ui.png)
        );
        System.out.println("TexturePacker completed!");

    }
}
