package us.hervalicio.ai.content;

import org.apache.commons.io.FileUtils;
import org.deeplearning4j.examples.rnn.CharacterIterator;
import us.hervalicio.ai.neural.CharacterMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by herval on 3/28/16.
 */
public class Loader {

    private final List<String> contents;
    private final CharacterMap characterMap;

    public Loader(List<File> files, CharacterMap charMap) throws IOException {
        contents = new ArrayList<>();
        characterMap = charMap;

        for (File f : files) {
            contents.addAll(FileUtils.readLines(f));
        }
    }

    public List<String> lines() {
        return contents;
    }

    public CharacterIterator iterator(int miniBatchSize,
                                      int exampleLength,
                                      int examplesPerEpoch) throws IOException {
        return new CharacterIterator(
                contents,
                miniBatchSize,
                exampleLength,
                examplesPerEpoch,
                characterMap,
                true
        );
    }
}
