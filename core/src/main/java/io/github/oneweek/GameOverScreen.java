package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOverScreen implements Screen {
    private Main game;
    private Stage stage;
    private String difficulty;  // Stored difficulty for the retry option
    private OrthographicCamera camera;

    public GameOverScreen(Main game, String difficulty) {
        this.game = game;
        this.difficulty = difficulty;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        camera = new OrthographicCamera(1920, 1080);

        Sound openSound = Gdx.audio.newSound(Gdx.files.internal("fx/ui_open.mp3"));
        Sound closeSound = Gdx.audio.newSound(Gdx.files.internal("fx/ui_close.mp3"));

        // Load fonts
        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("fonts/title.fnt"));
        BitmapFont menuFont = new BitmapFont(Gdx.files.internal("fonts/menu.fnt"));

        // Load button textures (using your skin assets)
        Texture buttonTexture = new Texture(Gdx.files.internal("skin/menu-button.png"));
        Texture buttonPressedTexture = new Texture(Gdx.files.internal("skin/menu-button-pressed.png"));
        Texture buttonHoveredTexture = new Texture(Gdx.files.internal("skin/menu-button-hovered.png"));

        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        TextureRegionDrawable buttonPressed = new TextureRegionDrawable(new TextureRegion(buttonPressedTexture));
        TextureRegionDrawable buttonHovered = new TextureRegionDrawable(new TextureRegion(buttonHoveredTexture));

        // Set up the button style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.up = buttonDrawable;
        buttonStyle.down = buttonPressed;
        buttonStyle.over = buttonHovered;
        buttonStyle.up.setMinWidth(250);
        buttonStyle.up.setMinHeight(70);

        // Create the title label
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label titleLabel = new Label("Game Over", titleStyle);

        // Create the buttons
        TextButton mainMenuButton = new TextButton("Main Menu", buttonStyle);
        TextButton retryButton = new TextButton("Retry", buttonStyle);
        TextButton exitButton = new TextButton("Exit", buttonStyle);

        // Add button listeners
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
                closeSound.play(game.getAudioVolume());
            }
        });

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Retry by creating a new GameScreen using the stored difficulty
                game.setScreen(new GameScreen(game, difficulty));
                openSound.play(game.getAudioVolume());
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Layout with a Table
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(titleLabel).padBottom(50).row();
        table.add(mainMenuButton).padBottom(20).row();
        table.add(retryButton).padBottom(20).row();
        table.add(exitButton);

        stage.addActor(table);
    }

    @Override
    public void show() {
        // Nothing additional on show
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.047f, 0.067f, 0.133f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Handle pause if needed
    }

    @Override
    public void resume() {
        // Handle resume if needed
    }

    @Override
    public void hide() {
        // Handle hide if needed
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
