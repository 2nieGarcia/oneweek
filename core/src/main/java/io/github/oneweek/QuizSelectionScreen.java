package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class QuizSelectionScreen implements Screen {

    private Main game;
    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private TextureAtlas atlas;

    public QuizSelectionScreen(Main game) {
        this.game = game;
        // Get assets from the main game class.
        this.skin = game.getSkin();
        this.font = game.getFont();
        this.atlas = game.getAtlas();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create a table for layout.
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // --- Category Selection ---
        Label categoryLabel = new Label("Select Category", skin);
        table.add(categoryLabel).padBottom(10);
        table.row();

        // Create a SelectBox with 15 placeholder categories.
        SelectBox<String> categorySelectBox = new SelectBox<>(skin);
        String[] categories = new String[] {
            "Category 1", "Category 2", "Category 3", "Category 4", "Category 5",
            "Category 6", "Category 7", "Category 8", "Category 9", "Category 10",
            "Category 11", "Category 12", "Category 13", "Category 14", "Category 15"
        };
        categorySelectBox.setItems(categories);
        table.add(categorySelectBox).padBottom(20).width(200);
        table.row();

        // --- Difficulty Selection ---
        Label difficultyLabel = new Label("Select Difficulty", skin);
        table.add(difficultyLabel).padBottom(10);
        table.row();

        // Create a SelectBox for difficulty options.
        SelectBox<String> difficultySelectBox = new SelectBox<>(skin);
        difficultySelectBox.setItems("Easy", "Medium", "Hard");
        table.add(difficultySelectBox).padBottom(20).width(200);
        table.row();

        // --- Play Button ---
        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedCategory = categorySelectBox.getSelected();
                String selectedDifficulty = difficultySelectBox.getSelected();
                // Transition to QuizScreen, passing the chosen settings.
                game.setScreen(new QuizScreen(game, selectedCategory, selectedDifficulty));
            }
        });
        table.add(playButton).pad(10);

        // --- Back Button ---
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Navigate back to the Main Menu or previous screen.
                game.setScreen(new MenuScreen(game));
            }
        });
        table.add(backButton).pad(10);
    }

    @Override
    public void show() {
        // This method is called when the screen becomes the current screen.
    }

    @Override
    public void render(float delta) {
        // Clear the screen.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw the stage.
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Optionally handle pause.
    }

    @Override
    public void resume() {
        // Optionally handle resume.
    }

    @Override
    public void hide() {
        // Called when this screen is no longer the current screen.
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
