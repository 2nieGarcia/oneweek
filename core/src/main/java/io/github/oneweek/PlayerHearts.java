package io.github.oneweek;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayerHearts {
    private Texture[] heartFrames;
    private int lives;
    private int maxLives;
    private float animationTime;
    private boolean animating;
    private int animatingIndex;

    private static final float HEART_SCALE = 1.5f;
    private static final float HEART_WIDTH = 32 * HEART_SCALE;
    private static final float HEART_HEIGHT = 32 * HEART_SCALE;
    private static final float HEART_SPACING = 50 * HEART_SCALE;
    private static final float FRAME_DURATION = 0.2f;

    public PlayerHearts(int maxLives) {
        this.maxLives = maxLives;
        this.lives = maxLives;
        this.heartFrames = new Texture[7];

        for (int i = 0; i < 7; i++) {
            heartFrames[i] = new Texture("skin/raw/hearts/" + (i + 1) + ".png");
        }
    }

    public void render(SpriteBatch batch, float x, float y) {
        for (int i = 0; i < maxLives; i++) {
            if (i < lives) {
                batch.draw(heartFrames[0], x + (i * HEART_SPACING), y, HEART_WIDTH, HEART_HEIGHT);
            } else if (i == animatingIndex && animating) {
                batch.draw(heartFrames[getCurrentFrame()], x + (i * HEART_SPACING), y, HEART_WIDTH, HEART_HEIGHT);
            } else {
                batch.draw(heartFrames[6], x + (i * HEART_SPACING), y, HEART_WIDTH, HEART_HEIGHT);
            }
        }
    }

    public void update(float delta) {
        if (animating) {
            animationTime += delta;
            if (getCurrentFrame() >= 6) {
                animating = false;
                lives--;
            }
        }
    }

    private int getCurrentFrame() {
        if (!animating) return 0;
        return Math.min((int) (animationTime / FRAME_DURATION), 6);
    }

    public void loseHeart() {
        if (lives > 0 && !animating) {
            animating = true;
            animationTime = 0;
            animatingIndex = Math.max(lives - 1, 0);
        }
    }

    public int getLives() {
        return lives;
    }

    public void dispose() {
        for (Texture t : heartFrames) {
            t.dispose();
        }
    }
}
