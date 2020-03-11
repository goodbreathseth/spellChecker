package spell;

public class Trie implements ITrie {

    private Node root;
    int nodeCount;
    int wordCount;

    //Constructor
    public Trie() {
        root = new Node();
        nodeCount = 1;
        wordCount = 0;
    }
    /**
     * Adds the specified word to the trie (if necessary) and increments the word's frequency count
     *
     * @param word The word being added to the trie
     */


   public void add(String word) {
       Node n = root;
       for (int i = 0; i < word.length(); i++) {
           //Convert the letter to it's index in the array
           int indexInArray = charToAscii(word.charAt(i));

           //If it is the first letter, create a new node in array
           if(n.nodes[indexInArray] == null) {
               nodeCount++;
               Node temp = new Node();
               n.nodes[indexInArray] = temp;
               n = temp;
           }
           //If letter already exists, assign it to already existing node
           else
               n = n.nodes[indexInArray];

       } //End of for loop
       n.isEnd = true;
       wordCount++;
       n.frequencyCount++;
   }

   //Helper method to convert a char to its corresponding ASCII number
   public int charToAscii(char c) {
       int ascii = (int) Character.toLowerCase(c);
       ascii -= 97;
       return ascii;
   }

   //Helper method to convert an ASCII number to its corresponding char
   public char asciiToChar(int i) {
       i += 97;
       return (char)i;
   }

    /**
     * Searches the trie for the specified word
     *
     * @param word The word being searched for
     *
     * @return A reference to the trie node that represents the word,
     * 			or null if the word is not in the trie
     */
   public INode find (String word) {
       Node temp = root;
       int indexInArray;

       //Iterate through each char of the input string
       for (int i = 0; i < word.length(); i++) {
            //Find where the letter will land in the Node array
            indexInArray = charToAscii(word.charAt(i));

            //If it is in the array, change where the temp node points
            if (temp.nodes[indexInArray] != null)
                temp = temp.nodes[indexInArray];

            //If it isn't, then return null, meaning that it isn't the word
            else
                return null;
       }

       //If each node matches up, and the last node is "the end", then return it
       if (temp.isEnd)
           return temp;

       return null;
   }


    /**
     * Returns the number of nodes in the trie
     *
     * @return The number of nodes in the trie
     */
   public int getNodeCount() {
       return nodeCount;
   }

    @Override
    public String toString() {
        StringBuilder currentWord = new StringBuilder();
        StringBuilder output = new StringBuilder();
        toStringHelper(root, currentWord, output);
        return (output.toString());
    }

    private void toStringHelper(Node curNode, StringBuilder currentWord,
                                StringBuilder output) {
       //This is the call to return out of the recursive function
        if (curNode.getValue() > 0)
            output.append(currentWord.toString() + "\n");

        //Recursive call
        for (int i = 0; i < curNode.nodes.length; i++) {
            Node ourChild = curNode.nodes[i];

            if (ourChild != null) {
                currentWord.append(asciiToChar(i));
                toStringHelper(ourChild, currentWord, output);
                currentWord.deleteCharAt(currentWord.length()-1);
            }
        }

    }

    @Override
    public int hashCode() {
        //Create unique hash code value
        int hash = wordCount * nodeCount + 1;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
       if ( obj == null)
           return false;
       else if (obj == this)
           return true;
       else if (obj.getClass() != this.getClass())
           return false;

       Trie t = (Trie)obj;
       if (t.getNodeCount() == this.getNodeCount() && t.getWordCount() == this.getWordCount()) {
           if (recursiveHelper(t.root, this.root)) {
               return true;
           }
       }

       return false;
    }

    public boolean recursiveHelper(Node r1, Node r2) {
        if (r1.getValue() != r2.getValue())
            return false;

        //Recursive call
        for (int i = 0; i < r1.nodes.length; i++) {
            //Assign each element of the Node array to a new childOne Node
            Node childOne = r1.nodes[i];
            Node childTwo = r2.nodes[i]; //This is new

            //If the node exists, then assign it to a childTwo Node and see if it exists
            if (childOne != null || childTwo != null) {
                //Node childTwo = r2.nodes[i];
                if (childOne == null || childTwo == null) {
                    return false;
                }
            }
            if (childOne != null && childTwo != null)
                if (!recursiveHelper(childOne, childTwo))
                    return false;
        }

        return true;

    }


    /**
     * Returns the number of unique words in the trie
     *
     * @return The number of unique words in the trie
     */
    @Override
    public int getWordCount() {
        return wordCount;
    }
}

