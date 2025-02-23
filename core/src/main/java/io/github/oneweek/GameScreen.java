package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Camera camera;
    private Texture img;
    private Player player;

    private int speed = 200;
    private float acceleration = 50;
    private float deceleration = 50;

    private float playerOffsetX = -1400;

    private ParallaxLayer[] layers;

    public GameScreen(Main game) {
        this.game = game;
        this.batch = game.batch; // Use the batch from Main to avoid unnecessary creation
        this.camera = new OrthographicCamera(1920, 1080);
        this.player = new Player(-2000, -320);

        // Initialize Parallax Layers
        layers = new ParallaxLayer[10];
        layers[0] = new ParallaxLayer(new Texture("Background/0.png"), 0.1f, true, false);
        layers[1] = new ParallaxLayer(new Texture("Background/1.png"), 0.2f, true, false);
        layers[2] = new ParallaxLayer(new Texture("Background/2.png"), 0.3f, true, false);
        layers[3] = new ParallaxLayer(new Texture("Background/3.png"), 0.5f, true, false);
        layers[4] = new ParallaxLayer(new Texture("Background/4.png"), 0.8f, true, false);
        layers[5] = new ParallaxLayer(new Texture("Background/5.png"), 1.0f, true, false);
        layers[6] = new ParallaxLayer(new Texture("Background/6.png"), 1.2f, true, false);
        layers[7] = new ParallaxLayer(new Texture("Background/7.png"), 1.3f, true, false);
        layers[8] = new ParallaxLayer(new Texture("Background/8.png"), 1.5f, true, false);
        layers[9] = new ParallaxLayer(new Texture("Background/9.png"), 1.8f, true, false);

        for (ParallaxLayer layer : layers) {
            layer.setCamera(camera);
        }
    }

    @Override
    public void show() {
        // Called when this screen becomes the current screen
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.047f, 0.067f, 0.133f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        batch.begin();
        batch.draw(layers[0].texture, camera.position.x - camera.viewportWidth / 2, camera.viewportHeight / 2, camera.viewportWidth, camera.viewportHeight);

        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE) && speed < 300 && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            speed += acceleration;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && speed > 0) {
            speed -= deceleration;
        }

        camera.position.x += speed * deltaTime;
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        for (ParallaxLayer layer : layers) {
            layer.render(batch);
        }

        // Player stays fixed relative to the camera
        float playerOffsetY = -280;
        if(playerOffsetX < -800) playerOffsetX += 460 * deltaTime;

        player.setPosition(camera.position.x + playerOffsetX, camera.position.y + playerOffsetY);

        player.update(deltaTime);
        player.render(batch);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.setState(Player.PlayerState.IDLE);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.setState(Player.PlayerState.ATTACKING);
        } else {
            player.setState(Player.PlayerState.RUNNING);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Handle screen resizing
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
        // Dispose resources if necessary
    }
}
