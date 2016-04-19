package us.hervalicio.ai.neural;

import java.util.*;

/**
 * Store valid characters is a map for later use in vectorization
 *
 * Created by herval on 10/31/15.
 */
public class CharacterMap {
    private final char[] validCharacters;
    private final Random rng = new Random(12345);
    private final Map<Character, Integer> charToIdxMap;
    
    /**
     * A minimal character set, with a-z, A-Z, 0-9 and common punctuation etc
     */
    public static CharacterMap getMinimalCharacterMap() {
        return new CharacterMap(getMinimalCharacterSet());
    }

    /**
     * As per getMinimalCharacterSet(), but with a few extra characters
     */
    public static CharacterMap getDefaultCharacterMap() {
        List<Character> validChars = new LinkedList<>();
        for (char c : getMinimalCharacterSet()) validChars.add(c);
        char[] additionalChars = {'@', '#', '$', '%', '^', '*', '{', '}', '[', ']', '/', '+', '_',
                '\\', '|', '<', '>'};
        for (char c : additionalChars) validChars.add(c);
        char[] out = new char[validChars.size()];
        int i = 0;
        for (Character c : validChars) out[i++] = c;

        return new CharacterMap(out);
    }

    private static char[] getMinimalCharacterSet() {
        List<Character> validChars = new LinkedList<>();
        for (char c = 'a'; c <= 'z'; c++) validChars.add(c);
        for (char c = 'A'; c <= 'Z'; c++) validChars.add(c);
        for (char c = '0'; c <= '9'; c++) validChars.add(c);
        char[] temp = {'!', '&', '(', ')', '?', '-', '\'', '"', ',', '.', ':', ';', ' ', '\n', '\t'};
        for (char c : temp) validChars.add(c);
        char[] out = new char[validChars.size()];
        int i = 0;
        for (Character c : validChars) out[i++] = c;
        return out;
    }

    public CharacterMap(char[] validCharacters) {
        this.validCharacters = validCharacters;
        charToIdxMap = new HashMap<>();
        for (int i = 0; i < validCharacters.length; i++) {
            charToIdxMap.put(validCharacters[i], i);
        }
    }

    public boolean contains(char c) {
        return charToIdxMap.containsKey(c);
    }

    public char charAt(int idx) {
        return this.validCharacters[idx];
    }

    public int indexOf(char c) {
        return charToIdxMap.get(c);
    }

    public char sampleChar() {
        return validCharacters[(int) (rng.nextDouble() * validCharacters.length)];
    }

    public int size() {
        return validCharacters.length;
    }
}
