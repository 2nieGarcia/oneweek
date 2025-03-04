package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class QuizScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private OrthographicCamera uiCamera;
    private BitmapFont font;
    private Array<Question> questions;
    private int currentQuestionIndex = 0;

    public QuizScreen(Main game) {
        this.game = game;
        this.batch = game.batch;

        // Set up a fixed UI camera for consistent UI rendering.
        uiCamera = new OrthographicCamera(1920, 1080);
        uiCamera.position.set(1920 / 2f, 1080 / 2f, 0);
        uiCamera.update();

        font = new BitmapFont();
        loadQuestions();
    }

    private void loadQuestions() {
        FileHandle file = Gdx.files.internal("quiz.json");
        String jsonString = file.readString();
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Assumes quiz.json is a JSON array of question objects.
            Question[] questionArray = mapper.readValue(jsonString, Question[].class);
            questions = new Array<>(questionArray);
        } catch (IOException e) {
            e.printStackTrace();
            questions = new Array<>();
        }
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a dark gray color.
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setProjectionMatrix(uiCamera.combined);

        if (questions.size > 0) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            // Draw the question text.
            font.draw(batch, currentQuestion.getQuestion(), 660, 800);

            // Draw each answer option.
            String[] answers = currentQuestion.getAnswers();
            for (int i = 0; i < answers.length; i++) {
                font.draw(batch, (i + 1) + ". " + answers[i], 660, 700 - (i * 50));
            }

            // Handle answer input via numeric keys.
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) checkAnswer(0);
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) checkAnswer(1);
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) checkAnswer(2);
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) checkAnswer(3);
        }

        batch.end();
    }

    private void checkAnswer(int selectedIndex) {
        if (questions.get(currentQuestionIndex).isCorrect(selectedIndex)) {
            System.out.println("Correct!");
        } else {
            System.out.println("Wrong!");
        }
        // Move to the next question, cycling back to the first if needed.
        currentQuestionIndex = (currentQuestionIndex + 1) % questions.size;
    }

    @Override public void show() { }
    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        font.dispose();
    }
}
