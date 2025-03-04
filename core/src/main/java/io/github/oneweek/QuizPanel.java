package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class QuizPanel {
    private TextureAtlas panelAtlas;
    private Animation<TextureRegion> breakAnimation;
    private Animation<TextureRegion> respawnAnimation;
    private TextureRegion idlePanel;
    private float stateTime;
    private boolean isBreaking;
    private boolean isRespawning;
    private float x, y;
    private float width, height;
    private float scale = 3.5f;
    private OrthographicCamera uiCamera;

    private boolean respawnAfterBreak;

    private String panelText;

    private BitmapFont font;

    private float aspectRatio;

    public QuizPanel(float x, float y, float initialWidth) {
        this.x = x;
        this.width = initialWidth;
        panelAtlas = new TextureAtlas(Gdx.files.internal("atlas/quiz-panel.atlas"));

        uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
        uiCamera.update();

        idlePanel = panelAtlas.findRegion("quizpanelIdle");

        aspectRatio = (float) idlePanel.getRegionWidth() / (float) idlePanel.getRegionHeight();

        this.height = width - (width / 4);

        Array<TextureRegion> breakFrames = new Array<>();
        for (int i = 1; i <= 13; i++) {
            breakFrames.add(panelAtlas.findRegion("quizpanel" + i));
        }
        breakAnimation = new Animation<>(0.1f, breakFrames, Animation.PlayMode.NORMAL);

        Array<TextureRegion> respawnFrames = new Array<>();
        for (int i = 1; i <= 6; i++) {
            respawnFrames.add(panelAtlas.findRegion("quizpanelplace" + i));
        }
        respawnAnimation = new Animation<>(0.1f, respawnFrames, Animation.PlayMode.NORMAL);

        stateTime = 0;
        isBreaking = false;
        isRespawning = false;
        respawnAfterBreak = true;

        panelText = "Welcome to the Quiz!";

        font = new BitmapFont(Gdx.files.internal("fonts/menu.fnt"));
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setWidth(float newWidth) {
        this.width = newWidth;
        this.height = width / aspectRatio;
    }

    public void setScale(float scale) {
        this.scale = scale;
        this.width *= scale;
        this.height *= scale;
    }

    public void update(float delta) {
        if (isBreaking) {
            stateTime += delta;
            if (breakAnimation.isAnimationFinished(stateTime)) {
                if (respawnAfterBreak) {
                    isBreaking = false;
                    isRespawning = true;
                    stateTime = 0;
                } else {
                    stateTime = 0;
                }
            }
        }

        if (isRespawning) {
            stateTime += delta;
            if (respawnAnimation.isAnimationFinished(stateTime)) {
                isRespawning = false;
                stateTime = 0;
            }
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame;

        if (isBreaking) {
            currentFrame = breakAnimation.getKeyFrame(stateTime);
        } else if (isRespawning) {
            currentFrame = respawnAnimation.getKeyFrame(stateTime);
        } else {
            currentFrame = idlePanel;
        }

        batch.draw(currentFrame, x, y, width * scale, height * scale);

        font.draw(batch, panelText, x + 10, y + height - 20);
    }

    public void breakPanel() {
        isBreaking = true;
        stateTime = 0;
    }

    public void setRespawnAfterBreak(boolean respawn) {
        this.respawnAfterBreak = respawn;
    }

    public void respawnPanel() {
        isBreaking = false;
        isRespawning = true;
        stateTime = 0;
    }

    public void changeText(String newText) {
        this.panelText = newText;
    }

    public boolean isAnimationFinished() {
        return !isBreaking && !isRespawning;
    }

    public void dispose() {
        panelAtlas.dispose();
        font.dispose();
    }
}
