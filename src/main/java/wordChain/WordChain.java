package wordChain;

import java.util.List;
import java.util.stream.Collectors;

class WordChain {
    private List<String> wordChain;

    public WordChain(List<String> wordChain) {
        this.wordChain = wordChain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final WordChain wordChain1 = (WordChain) o;

        return wordChain.equals(wordChain1.wordChain);
    }

    public List<String> getChain() {
        return wordChain;
    }

    @Override
    public int hashCode() {
        return wordChain.hashCode();
    }

    @Override
    public String toString() {
        return "WordChain(" + getChain().stream().collect(Collectors.joining(",")) + ')';
    }
}
