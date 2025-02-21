package io.github.oneweek;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Main extends Game {
    public SpriteBatch batch;
    private TextureAtlas atlas;
    private Skin skin;
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();

        atlas = new TextureAtlas(Gdx.files.internal("atlas/game_atlas.atlas"));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        font = new BitmapFont(Gdx.files.internal("skin/default.fnt"));

        this.setScreen(new MenuScreen(this));
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public TextureAtlas getTextureAtlas() {
        return atlas;
    }

    public Skin getSkin() {
        return skin;
    }

    public BitmapFont getFont() {
        return font;
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
