package io.github.oneweek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Choice {
    private Texture idleTexture, hoverTexture, pressedTexture;
    private float x, y;
    private float width, height;
    private boolean isHovered, isPressed;
    private boolean isRightAnswer;
    private float pressTimer = 0;
    private Camera camera;
    private String choiceText;
    private BitmapFont font;
    private AnswerListener listener;

    public Choice(float x, float y, boolean isRightAnswer, Camera camera, String choiceText, AnswerListener listener) {
        this.x = x;
        this.y = y;
        this.width = 500; // Button size
        this.height = 125; // Button size
        this.idleTexture = new Texture(Gdx.files.internal("skin/quiz/quizbutton/quizbutton.png"));
        this.hoverTexture = new Texture(Gdx.files.internal("skin/quiz/quizbutton/quizbuttonhovered.png"));
        this.pressedTexture = new Texture(Gdx.files.internal("skin/quiz/quizbutton/quizbuttonpressed.png"));
        this.isRightAnswer = isRightAnswer;
        this.isHovered = false;
        this.isPressed = false;
        this.camera = camera;
        this.choiceText = choiceText;
        this.listener = listener;

        // Load font
        font = new BitmapFont(Gdx.files.internal("fonts/menu.fnt"));
        font.getData().setScale(.8f, .8f);
        font.setUseIntegerPositions(false);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Add a setter to update the choice text and correctness when a new question loads
    public void setChoice(String newChoiceText, boolean isRightAnswer) {
        this.choiceText = newChoiceText;
        this.isRightAnswer = isRightAnswer;
    }


    public void update(float delta) {
        // Get mouse position in world coordinates
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos); // Convert to world space
        float mouseX = mousePos.x;
        float mouseY = mousePos.y;

        isHovered = (mouseX >= x && mouseX <= x + width) && (mouseY >= y && mouseY <= y + height);

        if (isHovered && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            isPressed = true;
            pressTimer = 0.15f;
            handleClick();
        }

        // Reset press state after a short duration
        if (pressTimer > 0) {
            pressTimer -= delta;
            if (pressTimer <= 0) {
                isPressed = false;
            }
        }
    }

    public void render(SpriteBatch batch) {
        Texture currentTexture = idleTexture;
        if (isPressed) {
            currentTexture = pressedTexture;
        } else if (isHovered) {
            currentTexture = hoverTexture;
        }

        batch.draw(currentTexture, x, y, width, height);

        GlyphLayout layout = new GlyphLayout(font, choiceText);

        float textX = x + (width - layout.width) / 2;
        float textY = y + (height + layout.height) / 2;

        font.draw(batch, choiceText, textX, textY);
    }

    private void handleClick() {
        // Notify the listener about the answer result
        if (listener != null) {
            listener.onAnswerSelected(isRightAnswer);
        }
    }

    public boolean isRightAnswer() {
        return isRightAnswer;
    }


    public void dispose() {
        idleTexture.dispose();
        hoverTexture.dispose();
        pressedTexture.dispose();
    }
}
