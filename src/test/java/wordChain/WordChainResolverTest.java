package wordChain;


import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordChainResolverTest {
    private WordChainResolver wordChainResolver = new WordChainResolver();

    @Test
    public void Should_ReturnNoResult_When_DictionaryIsEmpty() {
        // given
        Set<String> dictionary = new HashSet<>();

        // when
        List<WordChain> wordChains = wordChainResolver.getChains("cat", "dog", dictionary);

        // then
        Assertions.assertThat(wordChains).isEmpty();
    }

    @Test
    public void Should_ReturnNoResult_When_BeginAndEndWordsAreSame() {
        // given
        Set<String> dictionary = new HashSet<>();

        // when
        List<WordChain> wordChains = wordChainResolver.getChains("cat", "cat", dictionary);

        // then
        Assertions.assertThat(wordChains).isEmpty();
    }


    @Test(expected = IllegalArgumentException.class)
    public void Should_ThrowException_When_BeginWordInNull() {
        // given
        Set<String> dictionary = new HashSet<>();

        // when
        wordChainResolver.getChains(null, "dog", dictionary);
    }

    @Test(expected = IllegalArgumentException.class)
    public void Should_ThrowException_When_EndWordInNull() {
        // given
        Set<String> dictionary = new HashSet<>();

        // when
        wordChainResolver.getChains("cat", null, dictionary);
    }

    @Test
    public void Should_ReturnNoResult_When_DictionaryDoesNotContainBeginWord() {
        // given
        Set<String> dictionary = new HashSet<>();
        dictionary.add("cat");
        dictionary.add("cot");
        dictionary.add("cog");
        dictionary.add("dog");

        // when
        List<WordChain> wordChains = wordChainResolver.getChains("bat", "dog", dictionary);

        // then
        Assertions.assertThat(wordChains).isEmpty();
    }

    @Test
    public void Should_ReturnNoResult_When_DictionaryDoesNotContainEndWord() {
        // given
        Set<String> dictionary = new HashSet<>();
        dictionary.add("cat");
        dictionary.add("cot");
        dictionary.add("cog");
        dictionary.add("dog");

        // when
        List<WordChain> wordChains = wordChainResolver.getChains("cat", "bat", dictionary);

        // then
        Assertions.assertThat(wordChains).isEmpty();
    }

    @Test
    public void Should_ReturnNoResult_When_BeginWordHasDifferentLengthThanEndWord() {
        // given
        Set<String> dictionary = new HashSet<>();

        // when
        List<WordChain> wordChains = wordChainResolver.getChains("veryShortBeginName", "longName", dictionary);

        // then
        Assertions.assertThat(wordChains).isEmpty();
    }

    @Test
    public void Should_ReturnOneWordChain_When_DictionaryHasOneChain() {
        // given
        Set<String> dictionary = new HashSet<>();
        dictionary.add("cat");
        dictionary.add("cot");
        dictionary.add("cog");
        dictionary.add("dog");

        // when
        List<WordChain> wordChains = wordChainResolver.getChains("cat", "dog", dictionary);

        // then
        Assertions.assertThat(wordChains).hasSize(1).contains(new WordChain(Arrays.asList("cat", "cot", "cog", "dog")));
    }

    @Test
    public void Should_ReturnOneWordChain_When_ExistShortAndLongChain() {
        // given
        Set<String> dictionary = new HashSet<>();
        dictionary.add("cat");
        dictionary.add("cot");
        dictionary.add("cog");
        dictionary.add("dog");

        dictionary.add("cat");
        dictionary.add("caf");
        dictionary.add("cof");
        dictionary.add("dof");
        dictionary.add("dog");

        // when
        List<WordChain> wordChains = wordChainResolver.getChains("cat", "dog", dictionary);

        // then
        Assertions.assertThat(wordChains).hasSize(1).contains(new WordChain(Arrays.asList("cat", "cot", "cog", "dog")));
    }

    @Test
    public void Should_ReturnFourWordChain_When_ExistFourChainAndTwoChainsHaveCommonSubset() {
        // given
        Set<String> dictionary = new HashSet<>();
        dictionary.add("cat");
        dictionary.add("cay");
        dictionary.add("coy");
        dictionary.add("cot");
        dictionary.add("cog");
        dictionary.add("dog");

        dictionary.add("cat");
        dictionary.add("dat");
        dictionary.add("dag");
        dictionary.add("dog");

        dictionary.add("cat");
        dictionary.add("dat");
        dictionary.add("dot");
        dictionary.add("dog");

        dictionary.add("cat");
        dictionary.add("cot");
        dictionary.add("dot");
        dictionary.add("dog");

        // when
        List<WordChain> wordChains = wordChainResolver.getChains("cat", "dog", dictionary);

        // then
        Assertions.assertThat(wordChains).hasSize(4)
                .contains(new WordChain(Arrays.asList("cat", "cot", "cog", "dog")))
                .contains(new WordChain(Arrays.asList("cat", "cot", "dot", "dog")))
                .contains(new WordChain(Arrays.asList("cat", "dat", "dot", "dog")))
                .contains(new WordChain(Arrays.asList("cat", "dat", "dag", "dog")));
    }
}