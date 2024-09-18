package words;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Words {

    public static List<String> getUniqueWordsFromSentence(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return new ArrayList<>();
        }

        // Convert sentence to lowercase, replace non-alphabetic characters with space, and split by space
        String[] words = sentence.toLowerCase().replaceAll("[^a-z\\s]", "").split("\\s+");

        // Use a stream to collect unique words in a list
        return Arrays.stream(words)
                .distinct()
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toList());
    }
}
