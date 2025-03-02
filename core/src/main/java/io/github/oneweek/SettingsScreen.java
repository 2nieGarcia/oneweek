package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingsScreen implements Screen {
    private Main game;
    private Stage stage;
    private Slider volumeSlider;
    private TextButton backButton;
//    private Music backgroundMusic;
    private Preferences preferences;
    private TextureAtlas atlas;
    private Skin skin;

    public SettingsScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load the atlas that contains your UI textures.
        atlas = new TextureAtlas(Gdx.files.internal("atlas/game_atlas.atlas"));

        // Create a skin manually (without using a JSON file)
        skin = new Skin();
        skin.addRegions(atlas);

        // Create a default BitmapFont.
        BitmapFont font = new BitmapFont();  // Uses the built-in font
        skin.add("default", font);

        // Create and add a TextButton style.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button-up");
        textButtonStyle.down = skin.getDrawable("button-down");
        textButtonStyle.checked = skin.getDrawable("button-checked");
        textButtonStyle.font = font;
        skin.add("default", textButtonStyle);

// Create and add a Slider style.
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = skin.getDrawable("slider-background");
        sliderStyle.knob = skin.getDrawable("slider-knob");
        skin.add("default-horizontal", sliderStyle);  // Use "default-horizontal" as the key

        // Load preferences and set the saved volume.
        preferences = Gdx.app.getPreferences("GameSettings");
        float savedVolume = preferences.getFloat("volume", 1.0f);

        // Load background music.
//        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/01. Aerial City.mp3"));
//        backgroundMusic.setLooping(true);
//        backgroundMusic.setVolume(savedVolume);
//        backgroundMusic.play();

        // Create UI elements using the skin.
        volumeSlider = new Slider(0, 1, 0.01f, false, skin, "default-horizontal");
        volumeSlider.setValue(savedVolume);

        backButton = new TextButton("Back", skin);

        // Layout UI elements.
        Table table = new Table();
        table.setFillParent(true);
        table.add(volumeSlider).width(300).padBottom(20);
        table.row();
        table.add(backButton).width(200).height(50);
        stage.addActor(table);

//        // Listeners for UI elements.
//        volumeSlider.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
//                float volume = volumeSlider.getValue();
//                backgroundMusic.setVolume(volume);
//                preferences.putFloat("volume", volume);
//                preferences.flush();
//            }
//        });

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                float volume = volumeSlider.getValue();
                game.setMusicVolume(volume);  // Use the method from Main to adjust volume
            }
        });


        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });


    }



    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        atlas.dispose();
//        backgroundMusic.dispose();
    }
}
