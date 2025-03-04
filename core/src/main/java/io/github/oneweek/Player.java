package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Player {
    private Animation<TextureRegion> runAnimation, idleAnimation, attackAnimation, dieAnimation;
    private float stateTime;
    private Vector2 position;
    private PlayerState currentState;

    public enum PlayerState {
        RUNNING, IDLE, ATTACKING, DYING
    }

    public Player(float x, float y) {
        this.position = new Vector2(x, y);
        this.stateTime = 0;
        this.currentState = PlayerState.RUNNING; // Default state

        // Load animations from PNG files
        runAnimation = loadAnimation("Character/Run/", 16, 0.08f, Animation.PlayMode.LOOP);
        idleAnimation = loadAnimation("Character/Idle/", 10, 0.2f, Animation.PlayMode.LOOP);
        attackAnimation = loadAnimation("Character/Attack/", 9, 0.08f, Animation.PlayMode.NORMAL);
        dieAnimation = loadAnimation("Character/Hurt/", 4, 0.2f, Animation.PlayMode.NORMAL);
    }

    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount, float frameDuration, Animation.PlayMode mode) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i <= frameCount; i++) {
            Texture texture = new Texture(Gdx.files.internal(basePath + i + ".png"));
            frames.add(new TextureRegion(texture));
        }
        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(mode);
        return animation;
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;

        // If dying animation finished, stay on the last frame
        if (currentState == PlayerState.DYING && dieAnimation.isAnimationFinished(stateTime)) {
            stateTime = dieAnimation.getAnimationDuration();
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = getCurrentFrame();
        float scale = 5.0f; // Adjust scale factor (increase for bigger character)
        batch.draw(currentFrame, position.x, position.y,
            currentFrame.getRegionWidth() * scale,
            currentFrame.getRegionHeight() * scale);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    private TextureRegion getCurrentFrame() {
        switch (currentState) {
            case IDLE:
                return idleAnimation.getKeyFrame(stateTime);
            case ATTACKING:
                return attackAnimation.getKeyFrame(stateTime, false);
            case DYING:
                return dieAnimation.getKeyFrame(stateTime, false);
            case RUNNING:
            default:
                return runAnimation.getKeyFrame(stateTime);
        }
    }

    public void setState(PlayerState newState) {
        if (this.currentState != newState) {
            this.currentState = newState;
            stateTime = 0; // Reset animation time on state change
        }
    }

    public boolean isRunning() {
        return currentState == PlayerState.RUNNING;
    }
}
