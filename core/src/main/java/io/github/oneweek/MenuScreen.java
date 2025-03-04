package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MenuScreen implements Screen {
    private Camera camera;
    private SpriteBatch batch;
    private boolean isPlaying = false;
    private ParallaxLayer[] layers;
    private Stage stage;
    private Main game;

    public MenuScreen(Main game) {
        this.game = game;
        this.batch = game.batch;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        this.camera = new OrthographicCamera(1920, 1080);

        // Initialize Parallax Layers
        layers = new ParallaxLayer[10];
        for (int i = 0; i < 10; i++) {
            layers[i] = new ParallaxLayer(new Texture("Background/" + i + ".png"), 0.1f * (i + 1), true, false);
            layers[i].setCamera(camera);
        }

        // Load Fonts
        BitmapFont title = new BitmapFont(Gdx.files.internal("fonts/title.fnt"));
        BitmapFont menu = new BitmapFont(Gdx.files.internal("fonts/menu.fnt"));


        // Load button textures
        Texture buttonTexture = new Texture(Gdx.files.internal("skin/menu-button.png"));
        Texture buttonPressedTexture = new Texture(Gdx.files.internal("skin/menu-button-pressed.png"));
        Texture buttonHoveredTexture = new Texture(Gdx.files.internal("skin/menu-button-hovered.png"));

        // Create drawables for different states
        TextureRegionDrawable button = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        TextureRegionDrawable buttonPressed = new TextureRegionDrawable(new TextureRegion(buttonPressedTexture));
        TextureRegionDrawable buttonHovered = new TextureRegionDrawable(new TextureRegion(buttonHoveredTexture));

        // Set up button style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menu;
        buttonStyle.up = button;       // Default state
        buttonStyle.down = buttonPressed;   // When clicked
        buttonStyle.over = buttonHovered;  // When hovered

        // Create title label
        Label.LabelStyle labelStyle = new Label.LabelStyle(title, com.badlogic.gdx.graphics.Color.WHITE);
        Label titleLabel = new Label("Ronin's Trial", labelStyle);

        // Create buttons
        TextButton playButton = new TextButton("Play", buttonStyle);
        TextButton settingsButton = new TextButton("Settings", buttonStyle);
        TextButton exitButton = new TextButton("Exit", buttonStyle);

        Sound openSound = Gdx.audio.newSound(Gdx.files.internal("fx/ui_open.mp3"));
        Sound closeSound = Gdx.audio.newSound(Gdx.files.internal("fx/ui_close.mp3"));


        // Add listeners
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                if (!isPlaying) {
                    isPlaying = true;
                    game.setScreen(new DifficultyScreen(game));
                    openSound.play(game.getAudioVolume());
                }
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                if (!isPlaying) {
                    game.setScreen(new SettingsScreen(game));
                    openSound.play(game.getAudioVolume());
                }
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                if (!isPlaying) {
                    Gdx.app.exit();
                }
            }
        });

        // Arrange buttons using a table layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(titleLabel).padBottom(70).row();
        table.add(playButton).padBottom(20).row();
        table.add(settingsButton).padBottom(20).row();
        table.add(exitButton);

        buttonStyle.up.setMinWidth(250);
        buttonStyle.up.setMinHeight(70);
        stage.addActor(table);

    }


    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.047f, 0.067f, 0.133f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        for (ParallaxLayer layer : layers) {
            layer.render(batch);
        }
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));




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
    }
}
