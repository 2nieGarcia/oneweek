package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class QuizScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private BitmapFont font;
    private Array<Question> questions;
    private int currentQuestionIndex = 0;
    private Stage stage;
    private TextButton optionA, optionB, optionC, optionD;
    private Skin skin;

    public QuizScreen(Main game, String selectedCategory, String selectedDifficulty) {
        this.game = game;
        batch = game.getBatch();
        font = game.getFont();
        questions = new Array<>();
        skin = game.getSkin();
        stage = new Stage(new ScreenViewport());
        // Question Label
        Label questionLabel = new Label("What is the capital of France?", skin);

        // Answer Buttons
        optionA = new TextButton("A: Berlin", skin);
        optionB = new TextButton("B: Paris", skin);
        optionC = new TextButton("C: Madrid", skin);
        optionD = new TextButton("D: Rome", skin);

        // Add Click Listeners
        optionA.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                checkAnswer("Berlin");
            }
        });
        optionB.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                checkAnswer("Paris");
            }
        });
        optionC.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                checkAnswer("Madrid");
            }
        });
        optionD.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                checkAnswer("Rome");
            }
        });

        // Table Layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(questionLabel).padBottom(20).colspan(2);
        table.row();
        table.add(optionA).pad(20);
        table.add(optionB).pad(20);
        table.row();
        table.add(optionC).pad(20);
        table.add(optionD).pad(20);

        stage.addActor(table);


    }

    private void checkAnswer(String selectedAnswer) {
        if (selectedAnswer.equals("Paris")) {
            System.out.println("Correct!");

            Gdx.app.exit();
        } else {
            System.out.println("Wrong!");
        }
    }

    private void loadQuestions() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
