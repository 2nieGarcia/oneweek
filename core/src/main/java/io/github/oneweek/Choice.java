
package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Choice {
    private Texture idleTexture, hoverTexture, pressedTexture;
    private float x, y;
    private boolean isHovered, isPressed;

    public Choice(float x, float y) {
        this.x = x;
        this.y = y;
        this.idleTexture = new Texture(Gdx.files.internal("skin/quiz/quizbutton/quizbutton.png"));
        this.hoverTexture = new Texture(Gdx.files.internal("skin/quiz/quizbutton/quizbuttonhovered.png"));
        this.pressedTexture = new Texture(Gdx.files.internal("skin/quiz/quizbutton/quizbuttonpressed.png"));

        this.isHovered = false;
        this.isPressed = false;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update(float delta) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip Y-axis

        isHovered = mouseX >= x && mouseX <= x + idleTexture.getWidth()
            && mouseY >= y && mouseY <= y + idleTexture.getHeight();

        if (isHovered && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            isPressed = true;
            handleClick();
        } else {
            isPressed = false;
        }
    }

    public void render(SpriteBatch batch) {
        Texture currentTexture = isPressed ? pressedTexture : (isHovered ? hoverTexture : idleTexture);
        batch.draw(currentTexture, x, y);
    }

    private void handleClick() {
        // TODO: Send answer selection to your buddy's quiz logic
        System.out.println("Button clicked!");
    }

    public void dispose() {
        if (idleTexture != null) idleTexture.dispose();
        if (hoverTexture != null) hoverTexture.dispose();
        if (pressedTexture != null) pressedTexture.dispose();
    }
}
