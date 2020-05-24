import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WordProcessor {

    private Node wordTrie;  //Root Node of the Trie

    public class Node {

        protected char c;
        protected Node left, equal, right;
        protected boolean isEnd = false;

        public Node(char ca) {
            this.c = ca;
            this.isEnd = false;
            this.left = null;
            this.right = null;
            this.equal = null;
        }
    }
    
    public WordProcessor() {
        wordTrie = null;
    }
    
    //Method to add a string to the trie
    public void addWord(String s) {
        wordTrie = addWord(wordTrie, s, 0);
    }

    public Node addWord(Node node, String st, int ptr) {
        if (node == null || st == "") {
            node = new Node(st.toCharArray()[ptr]);
        }

        if (node.c == st.toCharArray()[ptr]) {
            if (ptr == st.length() - 1) {
                node.isEnd = true;
                return node;
            }
            node.equal = addWord(node.equal, st, ++ptr);
        }
        else if (st.toCharArray()[ptr] < node.c) {
            node.left = addWord(node.left, st, ptr);
        }
        else if (st.toCharArray()[ptr] > node.c) {
            node.right = addWord(node.right, st, ptr);
        }
        return node;
    }

    //Method to add an array of strings to the trie
    public Node addAllWords(Node node, String st, int ptr) {
        if (node == null || st == null) {
            node = new Node(st.toCharArray()[ptr]);
        }
        if (node.c == st.toCharArray()[ptr]) {
            if (ptr == st.length() - 1) {
                node.isEnd = true;
                return node;
            }
            node.equal = addAllWords(node.equal, st, ++ptr);
        } else if (st.toCharArray()[ptr] < node.c) {
            node.left = addAllWords(node.left, st, ptr);
        } else if (st.toCharArray()[ptr] > node.c) {
            node.right = addAllWords(node.right, st, ptr);
        }
        return node;
    }
   
    //Method to check of a string exists in the trie
    public boolean wordSearch(String s) {
        return wordSearch(wordTrie, s.toCharArray(), 0);
    }

    public boolean wordSearch(Node node, char[] st, int ptr) {
        if (node == null) {
            return false;
        }
        if ((int)st[ptr] < (int)node.c) {
            return wordSearch(node.left, st, ptr);
        }
        else if ((int)st[ptr] > (int)node.c) {
            return wordSearch(node.right, st, ptr);
        }
        else {
            if (node.isEnd && ptr == st.length - 1) {
                return true;
            }
            else if (ptr == st.length - 1) {
                return false;
            }
            else {
                return wordSearch(node.equal, st, ptr + 1);
            }
        }
    }

    //check if the trie if empty
    public boolean isEmpty()
    {
        return wordTrie == null;
    }

    //empty the trie
    public void clear()
    {
        wordTrie = null;
    }


    //Getter for wordTire
    public Node getWordTrie() { return wordTrie; }


    //search autocomplete options
    public List<String> autoCompleteOptions(String s) {
        List<String> options = new ArrayList<String>();

        boolean comp = true;

        int pos = 0;
        int store = 0;
        Node node = wordTrie;
        while (node != null) {
            int cmp = s.charAt(pos) - node.c;
            if (s.charAt(pos) != node.c) {
                if (cmp < 0) {
                    node = node.left;
                }
                else {
                    node = node.right;
                }
            }
            else {
                if (++pos == s.length()) {
                    if (node.isEnd) {
                        String str = s + node.c;
                        for (int i = 0; i < s.length(); i++) {
                            int str1 = s.charAt(i);
                            int str2 = str.charAt(i);
                            store++;
                            if (str1 != str2) {
                                comp = false;
                            }
                        }
                        if (store == s.length()) {
                            options = new ArrayList<String>();
                            return options;
                        }
                        if (comp) {
                            options.add(s + node.c);
                        }
                    }
                    findAllSuggestions(s, options, node.equal);
                    if (options.contains(s)) {
                        options = null;
                    }
                    return options;
                }
                node = node.equal;
            }
        }
        if (options.contains(s)) {
            options = null;
        }
        return options;
    }

    private void findAllSuggestions(String s, List<String> options, Node node) {
        if (node == null) {
            return;
        }
        if (node.isEnd) {
            options.add(s + node.c);
        }
        findAllSuggestions(s, options, node.left);
        findAllSuggestions(s + node.c, options, node.equal);
        findAllSuggestions(s, options, node.right);
    }

    public static void main (String[] args) {
        Scanner scan = new Scanner(System.in);
        WordProcessor tst = new WordProcessor();
        char ch;
        
        do
        {
            System.out.println("\nTernary Search Tree Operations\n");
            System.out.println("1. insert word");
            System.out.println("2. search word");
            System.out.println("3. delete word");
            System.out.println("4. check empty");
            System.out.println("5. make empty");

            int choice = scan.nextInt();
            switch (choice)
            {
                case 1 :
                    System.out.println("Enter word to insert");
                    tst.addWord( scan.next() );
                    break;
                case 2 :
                    System.out.println("Enter word to search");
                    System.out.println("Search result : "+ tst.wordSearch(scan.next()));
                    break;
                case 3 :
                    System.out.println("Enter word to delete");
                    //tst.delete( scan.next() );
                    break;
                case 4 :
                    System.out.println("Empty Status : "+ tst.isEmpty() );
                    break;
                case 5 :
                    System.out.println("Ternary Search Tree cleared");
                    tst.clear();
                    break;
                default :
                    System.out.println("Wrong Entry \n ");
                    break;
            }
            System.out.println("Result:: " + tst);

            System.out.println("\nDo you want to continue (Type y or n) \n");
            ch = scan.next().charAt(0);
        } while (ch == 'Y'|| ch == 'y');
    }
}
