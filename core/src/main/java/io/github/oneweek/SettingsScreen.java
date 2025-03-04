package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingsScreen implements Screen {
    private Main game;
    private Stage stage;
    private Slider musicVolumeSlider, audioVolumeSlider;
    private TextButton backButton;
    private Preferences preferences;
    private TextureAtlas atlas;
    private Skin skin;

    public SettingsScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Sound openSound = Gdx.audio.newSound(Gdx.files.internal("fx/ui_open.mp3"));
        Sound closeSound = Gdx.audio.newSound(Gdx.files.internal("fx/ui_close.mp3"));

        // Load the atlas that contains your UI textures.
        atlas = new TextureAtlas(Gdx.files.internal("atlas/game_atlas.atlas"));

        // Create a skin manually (without using a JSON file)
        skin = new Skin();
        skin.addRegions(atlas);

        // Create a default BitmapFont.
        BitmapFont font = new BitmapFont();  // Built-in font
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
        skin.add("default-horizontal", sliderStyle);  // Key for horizontal sliders

        // Load preferences from Main's config.
        preferences = Gdx.app.getPreferences("GameSettings");
        float savedMusicVolume = preferences.getFloat("musicVolume", 1.0f);
        float savedAudioVolume = preferences.getFloat("audioVolume", 1.0f);

        // Create UI elements using the skin.
        musicVolumeSlider = new Slider(0, 1, 0.01f, false, skin, "default-horizontal");
        musicVolumeSlider.setValue(savedMusicVolume);

        audioVolumeSlider = new Slider(0, 1, 0.01f, false, skin, "default-horizontal");
        audioVolumeSlider.setValue(savedAudioVolume);

        backButton = new TextButton("", skin);

        // Create labels for the sliders.
        Label musicLabel = new Label("Music Volume", new Label.LabelStyle(font, Color.WHITE));
        Label audioLabel = new Label("Audio Volume", new Label.LabelStyle(font, Color.WHITE));

        // Layout UI elements using a table.
        Table table = new Table();
        table.setFillParent(true);
        table.pad(20);
        table.add(musicLabel).padBottom(10);
        table.row();
        table.add(musicVolumeSlider).width(300).padBottom(20);
        table.row();
        table.add(audioLabel).padBottom(10);
        table.row();
        table.add(audioVolumeSlider).width(300).padBottom(20);
        table.row();
        table.add(backButton).width(200).height(50);
        stage.addActor(table);

        // Listener for music volume changes.
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                float volume = musicVolumeSlider.getValue();
                game.setMusicVolume(volume); // Update Main's music volume.
                preferences.putFloat("musicVolume", volume);
                preferences.flush();
            }
        });

        // Listener for audio volume changes.
        audioVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                float volume = audioVolumeSlider.getValue();
                game.setAudioVolume(volume); // Update Main's audio (SFX) volume.
                preferences.putFloat("audioVolume", volume);
                preferences.flush();
            }
        });

        // Listener for back button to return to the menu.
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new MenuScreen(game));
                closeSound.play(game.getAudioVolume());
            }
        });
    }

    @Override
    public void show() { }

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
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        atlas.dispose();
    }
}
