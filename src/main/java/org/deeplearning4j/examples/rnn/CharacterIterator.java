package org.deeplearning4j.examples.rnn;

import org.deeplearning4j.datasets.iterator.DataSetIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.factory.Nd4j;
import us.hervalicio.ai.neural.CharacterMap;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * A very simple DataSetIterator for use in the GravesLSTMCharModellingExample.
 * Given a text file and a few options, generate feature vectors and labels for training,
 * where we want to predict the next character in the sequence.<br>
 * This is done by randomly choosing a position in the text file to start the sequence and
 * (optionally) scanning backwards to a new line (to ensure we don't start half way through a word
 * for example).<br>
 * Feature vectors and labels are both one-hot vectors of same length
 *
 * @author Alex Black
 */
public class CharacterIterator implements DataSetIterator {
    private static final long serialVersionUID = -7287833919126626356L;
    private static final int MAX_SCAN_LENGTH = 200;
    private char[] fileCharacters;
    private int exampleLength;
    private int miniBatchSize;
    private int numExamplesToFetch;
    private int examplesSoFar = 0;
    private final boolean alwaysStartAtNewLine;
    private CharacterMap characterMap;
    private Random rng = new Random(12345);

    /**
     */
    public CharacterIterator(List<String> lines, int miniBatchSize, int exampleLength, int numExamplesToFetch, CharacterMap characterMap, boolean alwaysStartAtNewLine) throws IOException {
        if (numExamplesToFetch % miniBatchSize != 0) {
            throw new IllegalArgumentException("numExamplesToFetch must be a multiple of miniBatchSize");
        }
        if (miniBatchSize <= 0) {
            throw new IllegalArgumentException("Invalid miniBatchSize (must be >0)");
        }
        this.characterMap = characterMap;
        this.exampleLength = exampleLength;
        this.miniBatchSize = miniBatchSize;
        this.numExamplesToFetch = numExamplesToFetch;
        this.alwaysStartAtNewLine = alwaysStartAtNewLine;


        //Load file and convert contents to a char[]
        boolean newLineValid = this.characterMap.contains('\n');
        int maxSize = lines.size();    //add lines.size() to account for newline characters at end of each line
        for (String s : lines) maxSize += s.length();
        char[] characters = new char[maxSize];
        int currIdx = 0;
        for (String s : lines) {
            char[] thisLine = s.toCharArray();
            for (int i = 0; i < thisLine.length; i++) {
                if (!this.characterMap.contains(thisLine[i])) continue;
                characters[currIdx++] = thisLine[i];
            }
            if (newLineValid) {
                characters[currIdx++] = '\n';
            }
        }

        if (currIdx == characters.length) {
            fileCharacters = characters;
        } else {
            fileCharacters = Arrays.copyOfRange(characters, 0, currIdx);
        }
        if (exampleLength >= fileCharacters.length) {
            throw new IllegalArgumentException("exampleLength=" + exampleLength
                    + " cannot exceed number of valid characters in file (" + fileCharacters.length + ")");
        }

        int nRemoved = maxSize - fileCharacters.length;
        System.out.println("Loaded and converted file: " + fileCharacters.length + " valid characters of "
                + maxSize + " total characters (" + nRemoved + " removed)");
    }

    public char convertIndexToCharacter(int idx) {
        return this.characterMap.charAt(idx);
    }

    public int convertCharacterToIndex(char c) {
        return this.characterMap.indexOf(c);
    }

    public char getRandomCharacter() {
        return characterMap.sampleChar();
    }

    public boolean hasNext() {
        return examplesSoFar + miniBatchSize <= numExamplesToFetch;
    }

    public DataSet next() {
        return next(miniBatchSize);
    }

    public DataSet next(int num) {
        if (examplesSoFar + num > numExamplesToFetch) throw new NoSuchElementException();
        //Allocate space:
        INDArray input = Nd4j.zeros(new int[]{num, characterMap.size(), exampleLength});
        INDArray labels = Nd4j.zeros(new int[]{num, characterMap.size(), exampleLength});

        int maxStartIdx = fileCharacters.length - exampleLength;

        //Randomly select a subset of the file. No attempt is made to avoid overlapping subsets
        // of the file in the same minibatch
        for (int i = 0; i < num; i++) {
            int startIdx = (int) (rng.nextDouble() * maxStartIdx);
            int endIdx = startIdx + exampleLength;
            int scanLength = 0;
            if (alwaysStartAtNewLine) {
                while (startIdx >= 1 && fileCharacters[startIdx - 1] != '\n' && scanLength++ < MAX_SCAN_LENGTH) {
                    startIdx--;
                    endIdx--;
                }
            }

            int currCharIdx = characterMap.indexOf(fileCharacters[startIdx]);    //Current input
            int c = 0;
            for (int j = startIdx + 1; j <= endIdx; j++, c++) {
                int nextCharIdx = characterMap.indexOf(fileCharacters[j]);        //Next character to predict
                input.putScalar(new int[]{i, currCharIdx, c}, 1.0);
                labels.putScalar(new int[]{i, nextCharIdx, c}, 1.0);
                currCharIdx = nextCharIdx;
            }
        }

        examplesSoFar += num;
        return new DataSet(input, labels);
    }

    public int totalExamples() {
        return numExamplesToFetch;
    }

    public int inputColumns() {
        return characterMap.size();
    }

    public int totalOutcomes() {
        return characterMap.size();
    }

    public void reset() {
        examplesSoFar = 0;
    }

    public int batch() {
        return miniBatchSize;
    }

    public int cursor() {
        return examplesSoFar;
    }

    public int numExamples() {
        return numExamplesToFetch;
    }

    public void setPreProcessor(DataSetPreProcessor preProcessor) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}