import java.util.ArrayList;
import java.util.Arrays;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';

    private class TrieNode {
        char letter;
        TrieNode[] presentChars = new TrieNode[62];
        boolean endOfWord;
        TrieNode(char alphabet) {
            this.letter = alphabet;
            this.endOfWord = false;
        }

    }
    TrieNode root;
    public Trie() {
        root = new TrieNode('\0');
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        TrieNode node = root;
        if (s.length() == 0) {
            return;
        }
        for (int i = 0; i < s.length(); i++) {
            char letter = s.charAt(i);
            int childIndex = getIndex(letter);
            if (node.presentChars[childIndex] == null) {
                node.presentChars[childIndex] = new TrieNode(letter);
            }
            node = node.presentChars[childIndex];
        }
        node.endOfWord = true;
    }
    public int getIndex(char letter) {
        //children[0-9] --> '0' - '9'
        //children[10-35] --> 'A' - 'Z'
        //children[36-61] --> 'a' - 'z'
        int index;
        if (letter >= 48 && letter <= 57) {
            index = letter - 48;
        } else if (letter >= 65 && letter <= 90) {
            index = (letter - 65) + 10;
        } else {
            index = (letter - 97) + 36;
        }
        return index;
    }



    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        TrieNode node = this.root;
        for (int i = 0; i < s.length(); i++) {
            char letter = s.charAt(i);
            int index = getIndex(letter);
            if (node.presentChars[index] == null) {
                return false;
            }
            node = node.presentChars[index];
        }
        return node.endOfWord;
    }


    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        TrieNode curr = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == WILDCARD) {
                //insert all possible values into the original string at position i
                for (int j = 0; j < 62; j++) {
                    if (curr.presentChars[j] != null) {
                        StringBuilder changedString = new StringBuilder(s);
                        changedString.replace(i, i + 1, String.valueOf(curr.presentChars[j].letter));
                        prefixSearch(changedString.toString(), results, limit);
                    }
                }
                return;
            }
            int index = getIndex(c);
            if (index < 0 || index > 61 || curr.presentChars[index] == null) {
                return;
            } else {
                curr = curr.presentChars[index];
            }
        }

        if (curr.endOfWord && !results.contains(s) && results.size() < limit) {
            results.add(s);
        }

        for (int i = 0; i < 62; i++) {
            if (curr.presentChars[i] != null) {
                char newLetter = curr.presentChars[i].letter;
                String newPrefix = s + newLetter;
                if (curr.presentChars[i].endOfWord && results.size() < limit) {
                    results.add(newPrefix);
                    prefixSearch(newPrefix, results, limit);
                } else {
                    prefixSearch(newPrefix, results, limit);
                }
            }
        }
    }

    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");
        Trie alphabet = new Trie();
        //[a, abba, abbde, abcd, abcdef, abd, abed, dbec]
        alphabet.insert("abbde");
        alphabet.insert("abcd");
        alphabet.insert("abcdef");
        alphabet.insert("abed");
        alphabet.insert("a");
        alphabet.insert("abba");
        alphabet.insert("abd");
        alphabet.insert("abed");
        alphabet.insert("dbec");
        String[] alpha = alphabet.prefixSearch("", 10);
        System.out.println(Arrays.toString(alpha));

        String[] result1 = t.prefixSearch("", 10);
        String[] result2 = t.prefixSearch("pe.", 10);
        String[] result3 = t.prefixSearch("peck", 10);
        String[] result4 = t.prefixSearch("pe", 10);
        System.out.println(Arrays.toString(result1));
        System.out.println(Arrays.toString(result2));
        System.out.println(Arrays.toString(result3));
        System.out.println(Arrays.toString(result4));

        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
