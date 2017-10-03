package wordChain;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

class WordChainApp {

    private final static Logger logger = Logger.getLogger(WordChainApp.class);
    private static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        BasicConfigurator.configure(); // configure basic configuration for log4j

        boolean sameLength = false;
        String beginWord = "";
        String endWord = "";

        // get words
        while (!sameLength) {

            logger.info("Please enter the begin word: ");
            beginWord = in.next();

            logger.info("Please enter the end word: ");
            endWord = in.next();

            if (beginWord.length() == endWord.length()) sameLength = true;
            else logger.error("Words have different length!");
        }

        // resolve word chain's problem
        WordChainResolver wordChainResolver = new WordChainResolver();
        Set<String> dictionary = getDictionary();
        List<WordChain> wordChains = wordChainResolver.getChains(beginWord, endWord, dictionary);

        // print outcome
        IntStream.range(0, wordChains.size()).forEach(index -> {
            logger.info("List " + index);
            logger.info(wordChains.get(index).toString());
        });
    }

    private static Set<String> getDictionary() {
        Set<String> dictionary = new HashSet<>();
        String fileName = "wordChain/wordlist.txt";
        try (BufferedReader bufferReader = new BufferedReader(new FileReader(fileName))) {

            String line;
            while (((line = bufferReader.readLine()) != null)) {
                dictionary.add(line.toLowerCase());
            }

        } catch (Exception e) {
            logger.error("Error during reading file line by line: " + e.getMessage());
        }
        return dictionary;
    }
}
