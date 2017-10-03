package wordChain;

import java.util.*;
import java.util.stream.Collectors;

public final class WordChainResolver {

    public List<WordChain> getChains(String beginWord, String endWord, Set<String> dictionary) {
        if (beginWord == null || endWord == null || dictionary == null) {
            throw new IllegalArgumentException();
        }

        if (!dictionary.contains(beginWord) || !dictionary.contains(endWord) || beginWord.length() != endWord.length()) {
            return new ArrayList<WordChain>();
        }

        Path path = new Path();
        List<WordChain> result = new ArrayList<WordChain>();

        Graph graph = buildGraphBasedOnDictionary(beginWord, endWord, dictionary);
        searchWordChains(graph, new Node(beginWord), new Node(endWord), path, result);
        return result;
    }

    private Graph buildGraphBasedOnDictionary(String beginWord, String endWord, Set<String> dict) {
        Graph graph = initGraph(dict, beginWord); // returned graph will contain only words with same length as beginWord'a length
        Queue<Node> queue = new LinkedList<>();
        queue.offer(new Node(beginWord));

        boolean notFoundedChain = true;

        while (!queue.isEmpty() && notFoundedChain) {
            int count = queue.size();
            for (int i = 0; i < count; i++) {
                Node currentNode = queue.poll();
                for (Node neighbour : getNeighbours(currentNode.getName(), dict)) { // initialize neighbours with next level of nodes
                    graph.connect(currentNode, neighbour);
                    if (graph.isNotVisited(neighbour)) {
                        int distance = graph.getDistanceForNode(currentNode) + 1;
                        graph.setDistanceForNode(neighbour, distance);
                        if (endWord.equals(neighbour.getName())) // check if BFS's reached the target word
                            notFoundedChain = false; // the shortest path is founded
                        else
                            queue.offer(neighbour);
                    }
                }
            }
        }
        return graph;
    }

    private Graph initGraph(Set<String> dictionary, String beginWord) {
        Graph graph = new Graph();
        dictionary.stream()
                .filter(word -> word.length() == beginWord.length())
                .forEach(graph::addNode);
        graph.setDistanceForNode(new Node(beginWord), 0);
        return graph;
    }

    private List<Node> getNeighbours(String word, Set<String> dict) {
        List<String> neighbours = new ArrayList<>(); // list of next level nodes
        char chars[] = word.toCharArray(); // convert the string to a character array
        for (char ch = 'a'; ch <= 'z'; ch++) {
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] != ch) {
                    char old_ch = chars[i];
                    chars[i] = ch;
                    if (dict.contains(String.valueOf(chars))) // Check if the word is in the dictionary
                        neighbours.add(String.valueOf(chars)); // Add the new word to the list
                    chars[i] = old_ch; // reset the char
                }
            }
        }
        return neighbours.stream()
                .map(neighbourWord -> new Node(neighbourWord))
                .collect(Collectors.toList());
    }

    private void searchWordChains(Graph graph, Node currentNode, Node targetNode, Path path, List<WordChain> result) {
        path.add(currentNode);
        if (currentNode.equals(targetNode)) {
            WordChain wordChain = new WordChain(path.copy());
            result.add(wordChain);
        } else {
            for (Node neighbourNode : graph.getNeighbours(currentNode)) {
                searchWordChains(graph, neighbourNode, targetNode, path, result);
            }
        }
        path.removeLastNode();
    }

    // ******************************************** //
    //  INNER CLASSES USED BY WORD_CHAIN_RESOLVER   //
    // ******************************************** //


    private class Path {
        private List<Node> nodes = new LinkedList<Node>();

        void removeLastNode() {
            nodes.remove(nodes.size() - 1);
        }

        void add(Node node) {
            nodes.add(node);
        }

        List<String> copy() {
            return nodes.stream().map(Node::getName).collect(Collectors.toList());
        }
    }

    private class Graph {
        private Map<Node, List<Node>> neighbourNodes = new HashMap<>(); // neighbours for every node
        private Map<Node, Integer> distance = new HashMap<>(); // distance of every node from start node

        List<Node> getNeighbours(Node node) {
            return neighbourNodes.getOrDefault(node, new ArrayList<>()).stream()
                    .filter(neighbour -> distance.get(neighbour) == distance.get(node) + 1)
                    .collect(Collectors.toList());
        }

        void addNode(String word) {
            neighbourNodes.put(new Node(word), new ArrayList<>());
        }

        void setDistanceForNode(Node node, int distanceValue) {
            distance.put(node, distanceValue);
        }

        int getDistanceForNode(Node node) {
            return distance.getOrDefault(node, Integer.MIN_VALUE);
        }

        void connect(Node node, Node neighbour) {
            neighbourNodes.get(node).add(neighbour);
        }

        boolean isNotVisited(Node node) {
            return !distance.containsKey(node);
        }
    }

    private class Node {
        private String name;

        Node(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final Node node = (Node) o;

            return name.equals(node.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}