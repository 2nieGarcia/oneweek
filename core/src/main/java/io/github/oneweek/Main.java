package io.github.oneweek;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    public SpriteBatch batch;

    Camera camera;
	Texture img;
    Player player;

    int speed = 200;
    float accelaration = 50;
    float deceleration = 50;

    ParallaxLayer[] layers;

    @Override
    public void create() {
        batch = new SpriteBatch();
		// Viewport size the same as the background texture
		camera = new OrthographicCamera(1920, 1080);
        player = new Player(200, -320);

		// Art assets from
        // https://opengameart.org/content/parallax-2d-backgrounds
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

		// Could be part of the constructor but this is a bit more flexible (can create the parallax layers before
		// initialising the camera if needed)
		for (ParallaxLayer layer : layers) {
			layer.setCamera(camera);
		}
       this.setScreen(new MenuScreen(this)); // Start with the Menu Screen
    }

    @Override
    public void render () {
		Gdx.gl.glClearColor(0.047f, 0.067f, 0.133f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render(); // Delegate render to the active screen
        float deltaTime = Gdx.graphics.getDeltaTime();

		batch.begin();
		batch.draw(layers[0].texture, camera.position.x - camera.viewportWidth / 2,  camera.viewportHeight / 2, camera.viewportWidth, camera.viewportHeight);


        if(!Gdx.input.isKeyPressed(Input.Keys.SPACE) && speed < 300 && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            speed += accelaration;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && speed > 0) {
            // Gradually reduce speed when SPACE is not pressed
            speed -= deceleration;

        }

        camera.position.x += speed * Gdx.graphics.getDeltaTime();

		camera.update();

		batch.setProjectionMatrix(camera.combined);
		for (ParallaxLayer layer : layers) {
			layer.render(batch);
		}

        // Keep player fixed relative to camera
        float playerOffsetX = -800; // Adjust this for horizontal placement
        float playerOffsetY = -280; // Adjust this for vertical placement
        player.setPosition(camera.position.x + playerOffsetX, camera.position.y + playerOffsetY);

        // Update and render the player
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
	public void dispose () {
        batch.dispose();

	}
}
