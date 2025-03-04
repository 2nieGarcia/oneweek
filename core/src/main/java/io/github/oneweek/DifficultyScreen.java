package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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

public class DifficultyScreen implements Screen {
    private Main game;
    private Stage stage;

    public DifficultyScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load fonts
        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("fonts/title.fnt"));
        BitmapFont buttonFont = new BitmapFont(Gdx.files.internal("fonts/menu.fnt"));

        // Load button textures
        Texture buttonTexture = new Texture(Gdx.files.internal("skin/menu-button.png"));
        Texture buttonPressedTexture = new Texture(Gdx.files.internal("skin/menu-button-pressed.png"));
        Texture buttonHoveredTexture = new Texture(Gdx.files.internal("skin/menu-button-hovered.png"));

        // Create button drawables
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        TextureRegionDrawable buttonPressed = new TextureRegionDrawable(new TextureRegion(buttonPressedTexture));
        TextureRegionDrawable buttonHovered = new TextureRegionDrawable(new TextureRegion(buttonHoveredTexture));

        // Button style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = buttonFont;
        buttonStyle.up = buttonDrawable;
        buttonStyle.down = buttonPressed;
        buttonStyle.over = buttonHovered;

        // Title label
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label titleLabel = new Label("Select Difficulty", titleStyle);

        // Create buttons for each difficulty
        TextButton easyButton = new TextButton("Easy", buttonStyle);
        TextButton mediumButton = new TextButton("Medium", buttonStyle);
        TextButton hardButton = new TextButton("Hard", buttonStyle);

        // Add listeners to handle difficulty selection
        easyButton.addListener(new ClickListener(){
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Transition to GameScreen with "Easy" difficulty
                game.setScreen(new GameScreen(game, "Easy"));
            }
        });

        mediumButton.addListener(new ClickListener(){
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, "Medium"));
            }
        });

        hardButton.addListener(new ClickListener(){
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, "Hard"));
            }
        });

        // Layout with Table
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(titleLabel).padBottom(50).row();
        table.add(easyButton).padBottom(20).row();
        table.add(mediumButton).padBottom(20).row();
        table.add(hardButton);

        stage.addActor(table);
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.047f, 0.067f, 0.133f, 1f);
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);
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
    }
}
