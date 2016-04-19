package us.hervalicio.ai;

import com.google.common.base.Joiner;
import us.hervalicio.ai.content.Loader;
import us.hervalicio.ai.neural.Extractor;
import us.hervalicio.ai.neural.NetworkManager;
import us.hervalicio.ai.neural.Trainer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by herval on 3/28/16.
 */
public class BotTeacher {

    private static final int ITERATIONS = 100;
    private static final int BATCH_SIZE = 10;
    private static final int EXAMPLE_LENGTH = 200;
    private static final int EXAMPLES_PER_ITERATION = 1600;

    private final Trainer trainer;
    private final Extractor extractor;
    private final NetworkManager network;

    public BotTeacher(Path networkPath, List<File> files) throws IOException {
        network = NetworkManager.defaultConfig(networkPath);
        try {
            network.load();
            System.out.println("Loaded network from disk");
        } catch (IOException e) {
            System.out.println("Generating network from scratch");
            network.seed();
        }

        Loader contentLoader = new Loader(files, network.characterMap());

        trainer = new Trainer(network, contentLoader.iterator(BATCH_SIZE, EXAMPLE_LENGTH, EXAMPLES_PER_ITERATION));
        extractor = new Extractor(network);
    }

    private void run() throws IOException {
        for (int i = 0; i < ITERATIONS; i++) {
            System.out.println("Training epoch " + i);
            trainer.fit();

            System.out.println("Sampling: ");
            System.out.println(
                    Joiner.on("\n").join(extractor.sample(EXAMPLE_LENGTH, 1))
            );
            network.save();
        }

        System.out.println("All done!");
    }

    public static void main(String[] args) throws IOException {
        Config c = new Config();

        new BotTeacher(
                c.networkPath,
                c.contentFiles
        ).run();
    }
}
