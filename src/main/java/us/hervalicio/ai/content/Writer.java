package us.hervalicio.ai.content;

import org.nd4j.linalg.factory.Nd4j;
import us.hervalicio.ai.Config;
import us.hervalicio.ai.neural.Extractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.nd4j.linalg.api.rng.Random;
import us.hervalicio.ai.neural.NetworkManager;

/**
 * Created by herval on 10/31/15.
 */
public class Writer {
    private final Extractor inspiredBrain;
    private final Random rnd = Nd4j.getRandom();


    public static Writer build(Config config) throws IOException {
        NetworkManager manager = NetworkManager.defaultConfig(config.networkPath);
        manager.load();

        return new Writer(
                new Extractor(manager)
        );
    }

    public Writer(Extractor contents) throws IOException {
        inspiredBrain = contents;
    }

    public Song writeASong() {
        String[] phrases = inspiredBrain.sample(100 + rnd.nextInt(200), 1 + rnd.nextInt(4));

        // pick up 1 - 8 verses
        int verses = rnd.nextInt(8);
        List<String> finalLyrics = new ArrayList<>();
        int pickedVerses = 0;
        for (String phrase : phrases) {
            finalLyrics.add(phrase);
            if (phrase.isEmpty()) {
                pickedVerses++;
                if (pickedVerses == verses) {
                    break;
                }
            }
        }

        String lyrics = String.join("\n", finalLyrics).trim();

        return new Song(
                makeUpTitle(),
                lyrics
        );
    }



}
