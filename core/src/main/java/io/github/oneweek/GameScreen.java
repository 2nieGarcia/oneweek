package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class GameScreen implements Screen {
    private Main game;
    Camera camera;
    int speed = 200;
    float accelaration = 50;
    float deceleration = 1;
    ParallaxLayer[] layers;

    public GameScreen(Main game) {
        this.game = game;

        // Viewport size the same as the background texture
        camera = new OrthographicCamera(1920, 1080);

        // Load parallax layers
        layers = new ParallaxLayer[10];
        for (int i = 0; i < 10; i++) {
            layers[i] = new ParallaxLayer(new Texture("Background/" + i + ".png"), (i + 1) * 0.1f, true, false);
            layers[i].setCamera(camera);
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.047f, 0.067f, 0.133f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Speed control
        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE) && speed < 300) {
            speed += accelaration;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && speed > 0) {
            speed -= deceleration * Gdx.graphics.getDeltaTime();
        }

        // Update camera
        camera.position.x += speed * Gdx.graphics.getDeltaTime();
        camera.update();

        // Draw layers
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        for (ParallaxLayer layer : layers) {
            layer.render(game.batch);
        }
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
