package io.github.oneweek;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Main extends Game {
    public SpriteBatch batch;
    public Music backgroundMusic;
    public Preferences preferences;

    private TextureAtlas atlas;
    private Skin skin;
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();

        atlas = new TextureAtlas(Gdx.files.internal("atlas/game_atlas.atlas"));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        font = new BitmapFont(Gdx.files.internal("skin/default.fnt"));


        // Load preferences
        preferences = Gdx.app.getPreferences("GameSettings");
        float savedVolume = preferences.getFloat("volume", 1.0f);

        // Load and play background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/01. Aerial City.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(savedVolume);
        backgroundMusic.play();


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

    public void setMusicVolume(float volume) {
        backgroundMusic.setVolume(volume);
        preferences.putFloat("volume", volume);
        preferences.flush();
    }

    @Override
    public void render() {
        super.render(); // Delegate render to the active screen
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundMusic.dispose();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }
}
