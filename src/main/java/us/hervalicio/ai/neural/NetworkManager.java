package us.hervalicio.ai.neural;

import org.apache.commons.io.FileUtils;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.*;
import java.nio.file.Path;

/**
 * Created by herval on 10/30/15.
 */
public class NetworkManager {
    private static final String COEFICIENTS_FILE = "coefficients_network.bin";
    private static final String NETWORK_CONFIG_FILE = "conf_network.json";

    private final File topology;
    private final File coefficients;
    private Network network;
    private final CharacterMap characterMap;

    public static NetworkManager defaultConfig(Path storagePath) throws IOException {
        File dir = storagePath.toFile();
        dir.mkdirs();

        return new NetworkManager(
                new File(storagePath.toFile(), COEFICIENTS_FILE),
                new File(storagePath.toFile(), NETWORK_CONFIG_FILE),
                CharacterMap.getMinimalCharacterMap()
        );
    }

    public NetworkManager(File coefficients, File topology, CharacterMap characterMap) throws IOException {
        this.coefficients = coefficients;
        this.topology = topology;
        this.characterMap = characterMap;
    }

    public CharacterMap characterMap() {
        return characterMap;
    }

    public Network network() {
        if(network == null) {
            throw new IllegalStateException("Network not initialized - you need to seed or load before you can use it.");
        }
        return network;
    }

    public void seed() {
        this.network = Network.cleanNetwork(characterMap);
    }

    public void load() throws IOException {
        MultiLayerConfiguration confFromJson = MultiLayerConfiguration.fromJson(FileUtils.readFileToString(topology));
        DataInputStream dis = new DataInputStream(new FileInputStream(coefficients));
        INDArray newParams = Nd4j.read(dis);
        dis.close();

        MultiLayerNetwork model = new MultiLayerNetwork(confFromJson);
        model.init();
        model.setParameters(newParams);

        network = new Network(model);
    }

    public void save() throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(coefficients));
        Nd4j.write(network.model.params(), dos);
        dos.flush();
        dos.close();

        FileUtils.writeStringToFile(topology, network.model.getLayerWiseConfigurations().toJson());
    }
}
