package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Arrays;
import java.util.Collections;

public class GameScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Player player;
    private QuizPanel quizPanel;
    private Choice[] choices;

    private int speed = 200;
    private float acceleration = 50;
    private float deceleration = 50;

    private float playerOffsetX = -1400;
    private ParallaxLayer[] layers;

    private String[] quizChoiceTest = {"Ako", "sya", "Ewan ko tangina", "Pwet ni hudas na malaki"};

    public GameScreen(Main game) {
        this.game = game;
        this.batch = game.batch;
        this.camera = new OrthographicCamera(1920, 1080);
        this.player = new Player(-2000, -320);

        layers = new ParallaxLayer[10];
        for (int i = 0; i < 10; i++) {
            layers[i] = new ParallaxLayer(new Texture("Background/" + i + ".png"), 0.1f * (i + 1), true, false);
            layers[i].setCamera(camera);
        }

        quizPanel = new QuizPanel(0, 0, 300);

        choices = new Choice[4];
        for (int i = 0; i < 4; i++) {
            if (i == 0 ){ choices[i] = new Choice(0, 0, true, camera, quizChoiceTest[i]); continue; }

            choices[i] = new Choice(0, 0, false, camera, quizChoiceTest[i]);
        }

        // Shuffle the choices after they have been created
        Collections.shuffle(Arrays.asList(choices));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.047f, 0.067f, 0.133f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        batch.begin();

        for (ParallaxLayer layer : layers) {
            layer.render(batch);
        }

        if ( speed < 300 && player.isRunning()) {
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

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.setState(Player.PlayerState.IDLE);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            quizPanel.breakPanel();
            player.setState(Player.PlayerState.ATTACKING);
        } else if (Gdx.input.isKeyPressed(Input.Keys.H)) {
            player.setState(Player.PlayerState.DYING);
        } else {
        player.setState(Player.PlayerState.RUNNING);
        }


        batch.end();
    }

    @Override
    public void dispose() {
        for (ParallaxLayer layer : layers) {
            layer.dispose();
        }
        quizPanel.dispose();
        for (Choice choice : choices) {
            choice.dispose();
        }
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

