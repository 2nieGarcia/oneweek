package io.github.oneweek;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Question {
    private String question;
    private String[] answers;
    private int correctIndex;

    public Question(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(jsonString);

        // Retrieve the question and the correct answer
        this.question = json.get("question").asText();
        String correctAnswer = json.get("correct_answer").asText();

        // Retrieve the array of incorrect answers
        JsonNode incorrectAnswersArray = json.get("incorrect_answers");

        // Combine the correct answer with the incorrect answers
        List<String> allAnswers = new ArrayList<>();
        allAnswers.add(correctAnswer);
        for (JsonNode node : incorrectAnswersArray) {
            allAnswers.add(node.asText());
        }

        // Shuffle the answers so the correct answer is in a random position
        Collections.shuffle(allAnswers);
        this.answers = allAnswers.toArray(new String[0]);
        this.correctIndex = allAnswers.indexOf(correctAnswer);
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public boolean isCorrect(int index) {
        return index == correctIndex;
    }
}
