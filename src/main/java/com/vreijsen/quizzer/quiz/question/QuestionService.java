package com.vreijsen.quizzer.quiz.question;

import com.vreijsen.quizzer.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuestionService {

    private final List<Question> questions = new ArrayList<>();

    /**
     * Loads questions from the resources directory in the following structure:
     * resources/
     *   questions/
     *     1/
     *       question.txt (single line)
     *       answers.txt (single line, comma separated; first answer is considered the correct answer)
     *       image.jpg|jpeg|png (optional)
     *     2/
     *       question.txt (single line)
     *       answers.txt (single line, comma separated; first answer is considered the correct answer)
     *       image.jpg|jpeg|png (optional)
     *     ...
     * @throws IOException
     */
    @PostConstruct
    public void load() throws IOException {
        loadingTxt("questions");
    }

    public void loadingTxt(String root) throws IOException {
        File questionsFolder = new File(
                getClass().getClassLoader().getResource(root).getPath()
        );

        File[] folders = questionsFolder.listFiles();

        assert folders != null;

        for (File folder : folders) {
            if (! folder.isDirectory()) continue;

            String question = getContents(folder, "question.txt");
            String answers = getContents(folder, "answers.txt");
            Pair<String, Boolean> image = getImageContents(folder);

            List<String> options = parseAnswers(answers);
            String answer = options.get(0);

            Collections.shuffle(options);

            Question q = Question.builder()
                    .question(question)
                    .answer(answer)
                    .options(options)
                    .image(image == null ? null : image.getFirst())
                    .blurred(image != null && image.getSecond())
                    .build();

            questions.add(q);
        }
    }

    public Optional<Question> getRandomQuestion() {
        if(questions.size() == 0) {
            return Optional.empty();
        }

        int random = new Random().nextInt(questions.size());

        Question question = questions.get(random);
        questions.removeIf(question::equals);

        return Optional.of(question);
    }

    private List<String> parseAnswers(String text) {
        String[] answers = text.split(",");

        return Arrays.stream(answers)
                .collect(Collectors.toList());
    }

    private Pair<String, Boolean> getImageContents(File folder) throws IOException {
        File[] files = folder.listFiles();

        assert files != null;

        for (File file : files) {
            if(file.getName().toLowerCase().endsWith(".png")
                    || file.getName().toLowerCase().endsWith(".jpg")
                    || file.getName().toLowerCase().endsWith(".jpeg")) {
                byte[] image = FileUtils.readFileToByteArray(file);
                String base64 = Base64.getEncoder().encodeToString(image);

                if(file.getName().contains("blur")) {
                    return Pair.of(base64, true);
                }

                return Pair.of(base64, false);
            }
        }

        // Valid if there is no image needed
        return null;
    }

    private String getContents(File folder, String fileName) throws FileNotFoundException {
        File[] files = folder.listFiles();

        assert files != null;

        for (File file : files) {
            if(file.getName().toLowerCase().contains(fileName.toLowerCase())) {
                return getContents(file);
            }
        }

        throw new IllegalArgumentException(
                String.format("Folder [%s] without the appropriate file names.", folder.getName())
        );
    }

    private String getContents(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        StringBuilder content = new StringBuilder();

        while (sc.hasNextLine()) {
            content.append(sc.nextLine());
        }

        return content.toString();
    }

}
