//package io.github.oneweek;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.Camera;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.badlogic.gdx.utils.viewport.ScreenViewport;
//
//public class MenuScreen implements Screen {
//    private Camera camera;
//    private SpriteBatch batch;
//    private boolean isPlaying = false;
//    private ParallaxLayer[] layers;
//    private Stage stage;
//    private Main game;
//
//    public MenuScreen(Main game) {
//        this.game = game;
//        this.batch = game.batch;
//        stage = new Stage(new ScreenViewport());
//        Gdx.input.setInputProcessor(stage);
//
//        this.camera = new OrthographicCamera(1920, 1080);
//
//        // Initialize Parallax Layers
//        layers = new ParallaxLayer[10];
//        for (int i = 0; i < 10; i++) {
//            layers[i] = new ParallaxLayer(new Texture("Background/" + i + ".png"), 0.1f * (i + 1), true, false);
//            layers[i].setCamera(camera);
//        }
//
//        // Create a simple default button style
//        BitmapFont title = new BitmapFont(Gdx.files.internal("fonts/title.fnt")); // Replace with your font file path
//        BitmapFont menu = new BitmapFont(Gdx.files.internal("fonts/menu.fnt")); // Replace with your font file path
//
//        TextButtonStyle buttonStyle = new TextButtonStyle();
//        buttonStyle.font = menu;
//
//        // Create title label
//        Label.LabelStyle labelStyle = new Label.LabelStyle(title, com.badlogic.gdx.graphics.Color.WHITE);
//        Label titleLabel = new Label("Ronin's Trial", labelStyle);
//
//        // Create buttons
//        TextButton playButton = new TextButton("Play", buttonStyle);
//        TextButton settingsButton = new TextButton("Settings", buttonStyle);
//        TextButton exitButton = new TextButton("Exit", buttonStyle);
//
//        // Add listeners
//        playButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
//                isPlaying = true;
//                game.setScreen(new GameScreen(game));
//            }
//        });
//        settingsButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
//                if (!isPlaying) {
//                    game.setScreen(new SettingsScreen(game));
//                }
//            }
//        });
//        exitButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
//                if (!isPlaying) {
//                    Gdx.app.exit();
//                }
//            }
//        });
//
//        // Arrange buttons using a table layout
//        Table table = new Table();
//        table.setFillParent(true);
//        table.center();
//        table.add(titleLabel).padBottom(100).row();
//        table.add(playButton).padBottom(10).row();
//        table.add(settingsButton).padBottom(10).row();
//        table.add(exitButton);
//
//        stage.addActor(table);
//    }
//
//    @Override
//    public void show() {}
//
//    @Override
//    public void render(float delta) {
//        Gdx.gl.glClearColor(0.047f, 0.067f, 0.133f, 1f);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        batch.begin();
//        batch.setProjectionMatrix(camera.combined);
//        for (ParallaxLayer layer : layers) {
//            layer.render(batch);
//        }
//        batch.end();
//
//        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
//        stage.draw();
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        stage.getViewport().update(width, height, true);
//    }
//
//    @Override
//    public void pause() {}
//
//    @Override
//    public void resume() {}
//
//    @Override
//    public void hide() {}
//
//    @Override
//    public void dispose() {
//        stage.dispose();
//    }
//}
package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MenuScreen implements Screen {
    private Camera camera;
    private SpriteBatch batch;
    private boolean isPlaying = false;
    private ParallaxLayer[] layers;
    private Stage stage;
    private Main game;

    public MenuScreen(Main game) {
        this.game = game;
        this.batch = game.batch;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        this.camera = new OrthographicCamera(1920, 1080);

        // Initialize Parallax Layers
        layers = new ParallaxLayer[10];
        for (int i = 0; i < 10; i++) {
            layers[i] = new ParallaxLayer(new Texture("Background/" + i + ".png"), 0.1f * (i + 1), true, false);
            layers[i].setCamera(camera);
        }

        // Load Fonts
        BitmapFont title = new BitmapFont(Gdx.files.internal("fonts/title.fnt"));
        BitmapFont menu = new BitmapFont(Gdx.files.internal("fonts/menu.fnt"));

        // Create button style with background
        Texture buttonTexture = new Texture("skin/menu-button.png");
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));

        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = menu;
        buttonStyle.up = buttonDrawable;
        buttonStyle.down = buttonDrawable.tint(com.badlogic.gdx.graphics.Color.DARK_GRAY);

        // Create title label
        Label.LabelStyle labelStyle = new Label.LabelStyle(title, com.badlogic.gdx.graphics.Color.WHITE);
        Label titleLabel = new Label("Ronin's Trial", labelStyle);

        // Create buttons
        TextButton playButton = new TextButton("Play", buttonStyle);
        TextButton settingsButton = new TextButton("Settings", buttonStyle);
        TextButton exitButton = new TextButton("Exit", buttonStyle);

        // Add listeners
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                isPlaying = true;
                game.setScreen(new GameScreen(game));
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                if (!isPlaying) {
                    game.setScreen(new SettingsScreen(game));
                }
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                if (!isPlaying) {
                    Gdx.app.exit();
                }
            }
        });

        // Arrange buttons using a table layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(titleLabel).padBottom(70).row();
        table.add(playButton).padBottom(20).row();
        table.add(settingsButton).padBottom(20).row();
        table.add(exitButton);

        buttonStyle.up.setMinWidth(250);
        buttonStyle.up.setMinHeight(70);
        stage.addActor(table);

    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.047f, 0.067f, 0.133f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
