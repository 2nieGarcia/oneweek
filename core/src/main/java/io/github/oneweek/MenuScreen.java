package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen {
    private Camera camera;
    private Texture img;
    private Player player;
    private SpriteBatch batch;

    private int speed = 200;
    private float acceleration = 50;
    private float deceleration = 50;

    private ParallaxLayer[] layers;

    private Stage stage;
    private Skin skin;
    private Main game;

    public MenuScreen(Main game) {
        this.game = game;
        this.batch = game.batch; // Use the batch from Main to avoid unnecessary creation
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        this.camera = new OrthographicCamera(1920, 1080);

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


        // Load the skin for the buttons
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Create buttons
        TextButton playButton = new TextButton("Play", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Add listeners for the buttons
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game)); // Go to the game screen
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Add settings screen later or show a dialog
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit(); // Exit the game
            }
        });

        // Arrange buttons using a table layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(playButton).padBottom(20).row();
        table.add(settingsButton).padBottom(20).row();
        table.add(exitButton);

        stage.addActor(table);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.047f, 0.067f, 0.133f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        batch.begin();
//        batch.draw(layers[0].texture, camera.position.x - camera.viewportWidth / 2, camera.viewportHeight / 2, camera.viewportWidth, camera.viewportHeight);
//
//        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE) && speed < 300 && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            speed += acceleration;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && speed > 0) {
//            speed -= deceleration;
//        }
//
//        camera.position.x += speed * deltaTime;
//        camera.update();
        batch.setProjectionMatrix(camera.combined);

        for (ParallaxLayer layer : layers) {
            layer.render(batch);
        }
        batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        skin.dispose();
    }
}
