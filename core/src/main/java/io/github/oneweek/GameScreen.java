package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.badlogic.gdx.files.FileHandle;

import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameScreen implements Screen, AnswerListener {
    private Main game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Player player;
    private QuizPanel quizPanel;
    private Choice[] choices;
    private List<Question> questions;
    private String difficulty; // Received from DifficultyScreen
    private boolean uiLocked = false;
    private Sound attackSound;
    private Sound hurtSound;

    private int speed = 200;
    private float acceleration = 50;
    private float deceleration = 50;

    private static final float ATTACK_ANIMATION_DURATION = 2.5f; // Adjust as needed
    private static final float DYING_ANIMATION_DURATION = 1f;  // Adjust as needed

    private float playerOffsetX = -1400;
    private ParallaxLayer[] layers;

    private String[] quizChoiceTest = {"Ako", "sya", "Ewan ko tangina", "Pwet ni hudas na malaki"};
    private PlayerHearts playerhearts;
    private int lives = 3; // Initial lives

    public GameScreen(Main game, String difficulty) {
        this.game = game;
        this.batch = game.batch;
        this.camera = new OrthographicCamera(1920, 1080);
        this.player = new Player(-2000, -320);
        this.playerhearts = new PlayerHearts(lives);
        this.difficulty = difficulty;

        attackSound = Gdx.audio.newSound(Gdx.files.internal("fx/attack.mp3"));
        hurtSound = Gdx.audio.newSound(Gdx.files.internal("fx/hurt.mp3"));

        layers = new ParallaxLayer[10];
        for (int i = 0; i < 10; i++) {
            layers[i] = new ParallaxLayer(new Texture("Background/" + i + ".png"), 0.1f * (i + 1), true, false);
            layers[i].setCamera(camera);
        }

        quizPanel = new QuizPanel(0, 0, 300);

        choices = new Choice[4];
        for (int i = 0; i < 4; i++) {
            // For example, mark the first answer as correct initially, then update via loadNextQuestion()
            boolean isCorrect = (i == 0);
            choices[i] = new Choice(0, 0, isCorrect, camera, quizChoiceTest[i], this);
        }
        Collections.shuffle(Arrays.asList(choices));

        loadQuestions(difficulty);
        loadNextQuestion();
    }

    // Method to load questions from the JSON file
    private void loadQuestions(String difficulty) {
        String filePath = "quiz/all-" + difficulty.toLowerCase() + ".json";
        FileHandle fileHandle = Gdx.files.internal(filePath);
        String jsonString = fileHandle.readString();
        ObjectMapper mapper = new ObjectMapper();
        questions = new ArrayList<>();
        try {
            JsonNode arrayNode = mapper.readTree(jsonString);
            for (JsonNode node : arrayNode) {
                questions.add(new Question(node.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load and display the next question
    private void loadNextQuestion() {
        if (questions == null || questions.isEmpty()) return;
        // For simplicity, pick a random question
        Random random = new Random();
        int index = random.nextInt(questions.size());
        Question question = questions.get(index);

        // Update QuizPanel with the question text
        quizPanel.changeText(question.getQuestion());

        // Update choices with answers from the question
        String[] answers = question.getAnswers();
        for (int i = 0; i < choices.length; i++) {
            // Use the new setter in Choice
            choices[i].setChoice(answers[i], question.isCorrect(i));
        }
    }

    @Override
    public void onAnswerSelected(boolean isCorrect) {
        if (uiLocked) return; // Prevent further input during feedback
        uiLocked = true;

        float delay;
        if (isCorrect) {
            System.out.println("Correct answer selected!");
            player.setState(Player.PlayerState.ATTACKING);
            attackSound.play(game.getAudioVolume());
            quizPanel.breakPanel();
            delay = ATTACK_ANIMATION_DURATION;
        } else {
            System.out.println("Wrong answer. Try again!");
            player.setState(Player.PlayerState.DYING);
            hurtSound.play(game.getAudioVolume());
            playerhearts.loseHeart();
            // Optionally, trigger a negative effect on quizPanel or similar
            delay = DYING_ANIMATION_DURATION;
        }

        // Schedule transition to next question after the delay
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                // Reset the player's state (e.g., back to running)
                player.setState(Player.PlayerState.RUNNING);
                // Load the next question
                loadNextQuestion();
                quizPanel.respawnPanel();
                // Unlock UI for new input
                uiLocked = false;
            }
        }, delay);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.047f, 0.067f, 0.133f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        // Update player hearts (this updates the heart animation and state)
        playerhearts.update(deltaTime);

        // Check if the player is out of lives and switch to GameOverScreen
        if (playerhearts.getLives() <= 0) {
            game.setScreen(new GameOverScreen(game, difficulty));
            return;  // Skip the rest of the render to allow the transition
        }

        batch.begin();

        for (ParallaxLayer layer : layers) {
            layer.render(batch);
        }

        if (speed < 300 && player.isRunning()) {
            speed += acceleration;
        }
        if (!player.isRunning() && speed > 0) {
            speed -= deceleration;
        }

        camera.position.x += speed * deltaTime;
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        float uiOffsetX = camera.position.x;
        float uiOffsetY = camera.position.y;

        quizPanel.setPosition(uiOffsetX - 150, uiOffsetY - 300);

        choices[0].setPosition(uiOffsetX - 600, uiOffsetY - 525);
        choices[1].setPosition(uiOffsetX - 600, uiOffsetY - 375);
        choices[2].setPosition(uiOffsetX + 200, uiOffsetY - 525);
        choices[3].setPosition(uiOffsetX + 200, uiOffsetY - 375);

        quizPanel.update(deltaTime);
        quizPanel.render(batch);

        for (Choice choice : choices) {
            choice.update(deltaTime);
            choice.render(batch);
        }

        float playerOffsetY = -280;
        if (playerOffsetX < -800) playerOffsetX += 460 * deltaTime;
        player.setPosition(camera.position.x + playerOffsetX, camera.position.y + playerOffsetY);

        player.update(deltaTime);
        player.render(batch);

        if (Gdx.input.isKeyJustPressed(Input.Keys.R) && quizPanel.isAnimationFinished()) {
            quizPanel.changeText("Miss ko na sya");
            quizPanel.respawnPanel();
        }

        if (!uiLocked) { // Only update from input when not locked
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                player.setState(Player.PlayerState.IDLE);
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                quizPanel.breakPanel();
                player.setState(Player.PlayerState.ATTACKING);
            } else if (Gdx.input.isKeyPressed(Input.Keys.H)) {
                playerhearts.loseHeart();
                player.setState(Player.PlayerState.DYING);
            } else {
                player.setState(Player.PlayerState.RUNNING);
            }
        }

        // Render the player's hearts at the top-left (adjust positioning as needed)
        playerhearts.render(batch, camera.position.x - camera.viewportWidth / 2 + 50, camera.position.y + camera.viewportHeight / 2 - 100);

        batch.end();
    }


    @Override
    public void dispose() {
        playerhearts.dispose();
        for (ParallaxLayer layer : layers) {
            layer.dispose();
        }
        quizPanel.dispose();
        for (Choice choice : choices) {
            choice.dispose();
        }
        attackSound.dispose();
        hurtSound.dispose();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void show() {}
}

