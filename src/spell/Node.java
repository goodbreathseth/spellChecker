package spell;

public class Node implements ITrie.INode {

    int frequencyCount;
    boolean isEnd;
    public Node[] nodes;

    public Node() {
        frequencyCount = 0;
        nodes = new Node[26];
    }
    /**
     * Returns the frequency count for the word represented by the node
     *
     * @return The frequency count for the word represented by the node
     */
    @Override
    public int getValue() {
        return frequencyCount;
    }
}
