package io.github.oneweek;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Main extends Game {
    public SpriteBatch batch;
    public Music backgroundMusic;
    public Preferences preferences;
    private float audioVolume = 0.2f;  // Default full volume


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
        float savedVolume = preferences.getFloat("volume", 0.5f);

        // Load and play background music

        int trackNumber = MathUtils.random(1, 3);
        String musicPath = "audio/music" + trackNumber + ".mp3";

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
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

    public void setAudioVolume(float volume) {
        // Clamp the value between 0 and 1 to avoid out-of-range issues.
        this.audioVolume = MathUtils.clamp(volume, 0.0f, 1.0f);
        // If you have any currently playing sound effects and want to adjust their volume,
        // you could update them here. Otherwise, this value is stored and used when playing sounds.
    }

    // Getter for audio volume if needed in other parts of your game.
    public float getAudioVolume() {
        return this.audioVolume;
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
