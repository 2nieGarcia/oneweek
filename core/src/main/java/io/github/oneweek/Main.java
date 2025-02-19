package io.github.oneweek;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MenuScreen(this)); // Start with the Menu Screen
    }

    @Override
    public void render() {
        super.render(); // Delegate render to the active screen
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
