package io.github.oneweek;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Preferences;

public class Main extends Game {
    public SpriteBatch batch;
    public Music backgroundMusic;
    public Preferences preferences;


    @Override
    public void create() {
        batch = new SpriteBatch();

        // Load preferences
        preferences = Gdx.app.getPreferences("GameSettings");
        float savedVolume = preferences.getFloat("volume", 1.0f);

        // Load and play background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/01. Aerial City.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(savedVolume);
        backgroundMusic.play();


        this.setScreen(new MenuScreen(this)); // Start with the Menu Screen
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
}
