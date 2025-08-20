package braille;

import java.util.ArrayList;

/**
 * Contains methods to translate Braille to English and English to Braille using
 * a BST.
 * Reads encodings, adds characters, and traverses tree to find encodings.
 * 
 * @author Seth Kelley
 * @author Kal Pandit
 */
public class BrailleTranslator {

    private TreeNode treeRoot;

    /**
     * Default constructor, sets symbols to an empty ArrayList
     */
    public BrailleTranslator() {
        treeRoot = null;
    }

    /**
     * Reads encodings from an input file as follows:
     * - One line has the number of characters
     * - n lines with character (as char) and encoding (as string) space-separated
     * USE StdIn.readChar() to read character and StdIn.readLine() after reading
     * encoding
     * 
     * @param inputFile the input file name
     */
    public void createSymbolTree(String inputFile) {
        /* PROVIDED, DO NOT EDIT */
        StdIn.setFile(inputFile);
        int numberOfChars = Integer.parseInt(StdIn.readLine());
        for (int i = 0; i < numberOfChars; i++) {
            Symbol s = readSingleEncoding();
            addCharacter(s);
        }
    }

    /**
     * Reads one line from an input file and returns its corresponding
     * Symbol object
     * 
     * ONE line has a character and its encoding (space separated)
     * 
     * @return the symbol object
     */
    public Symbol readSingleEncoding() {
        // WRITE YOUR CODE HERE
            char character = StdIn.readChar();
            String encoding = StdIn.readString(); 
            StdIn.readLine();
            Symbol newSymbol = new Symbol (character, encoding);
        return newSymbol; // Replace this line, it is provided so your code compiles
    }

    /**
     * Adds a character into the BST rooted at treeRoot.
     * Traces encoding path (0 = left, 1 = right), starting with an empty root.
     * Last digit of encoding indicates position (left or right) of character within
     * parent.
     * 
     * @param newSymbol the new symbol object to add
     */
    public void addCharacter(Symbol newSymbol) {
        // WRITE YOUR CODE HERE
        String encoding = newSymbol.getEncoding();
        char lastChar = encoding.charAt(encoding.length() - 1);
        TreeNode leafNode = new TreeNode(newSymbol, null, null);

        if (treeRoot == null)  {
           TreeNode rootLeftChild = new TreeNode(new Symbol("L"), null, null);
           TreeNode rootRightChild = new TreeNode(new Symbol("R"), null, null);
           treeRoot = new TreeNode(new Symbol(""), rootLeftChild, rootRightChild);
        }

        TreeNode currentNode = treeRoot;

        for(int i = 0; i < encoding.length() - 1; i++){
            char currentChar = encoding.charAt(i);
            String partialEncoding = encoding.substring(0, i+1);

            if(currentChar == 'L'){
               if(currentNode.getLeft() == null){
                    currentNode.setLeft(new TreeNode(new Symbol(partialEncoding), null, null));
                }
                currentNode = currentNode.getLeft();
            }

            else if (currentChar == 'R'){
                if (currentNode.getRight() == null) {
                    currentNode.setRight(new TreeNode(new Symbol(partialEncoding), null, null));
                }
                currentNode = currentNode.getRight();
            }
        }
     
        if (lastChar == 'L') {
            currentNode.setLeft(leafNode);
        }

        else {
            currentNode.setRight(leafNode);
        }
    }

    /**
     * Given a sequence of characters, traverse the tree based on the characters
     * to find the TreeNode it leads to
     * 
     * @param encoding Sequence of braille (Ls and Rs)
     * @return Returns the TreeNode of where the characters lead to, or null if there is no path
     */
    public TreeNode getSymbolNode(String encoding) {
        // WRITE YOUR CODE HERE
        TreeNode currentNode = treeRoot;

        for(int i = 0; i < encoding.length(); i++){
            char character = encoding.charAt(i);

            if (character == 'L'){
                if (currentNode.getLeft() == null){
                    return null;
                }
                currentNode = currentNode.getLeft();
            }
            else if (character == 'R'){
                if (currentNode.getRight() == null){
                    return null;
                }
                currentNode = currentNode.getRight();
            }
        }
        return currentNode;
    }

    /**
     * Given a character to look for in the tree will return the encoding of the
     * character
     * 
     * @param character The character that is to be looked for in the tree
     * @return Returns the String encoding of the character
     */
    public String findBrailleEncoding(char character) {
        // WRITE YOUR CODE HERE
        String encoding = helperMethod(treeRoot, "", character);
        return encoding; // Replace this line, it is provided so your code compiles
    }
  

    private String helperMethod(TreeNode x, String key, char val){
        if (x == null){
            return null;
        }
        Symbol symbol = x.getSymbol();
        if (symbol.getCharacter() == val){
            return key;
        }

        String left = helperMethod(x.getLeft(), key + "L", val);
        if (left != null){
            return left;
        }

        else{
        String right = helperMethod(x.getRight(), key + "R", val);
        return right;
        }
    }

    /**
     * Given a prefix to a Braille encoding, return an ArrayList of all encodings that start with
     * that prefix
     * 
     * @param start the prefix to search for
     * @return all Symbol nodes which have encodings starting with the given prefix
     */
    public ArrayList<Symbol> encodingsStartWith(String start) {
        // WRITE YOUR CODE HERE
        ArrayList<Symbol> symbolsList = new ArrayList<>();
        TreeNode startNode = getSymbolNode(start);
        if (startNode == null){
            return null; 
        }
        
        preOrderHelper(startNode, symbolsList);

        return symbolsList; // Replace this line, it is provided so your code compiles
    }


    private void preOrderHelper(TreeNode x, ArrayList<Symbol> q){
        if (x == null){
            return;
        }
        
        if (x.getSymbol().hasCharacter() == true){
            q.add(x.getSymbol());
        }
        preOrderHelper(x.getLeft(), q);
        preOrderHelper(x.getRight(), q);
    }
    /**
     * Reads an input file and processes encodings six chars at a time.
     * Then, calls getSymbolNode on each six char chunk to get the
     * character.
     * 
     * Return the result of all translations, as a String.
     * @param input the input file
     * @return the translated output of the Braille input
     */
    public String translateBraille(String input) {
        // WRITE YOUR CODE HERE
        StdIn.setFile(input);
        String string = StdIn.readString();

        String result = "";
        int begin = 0;
        int end = 6;

        while (begin < string.length()){
        String partialEncoding = string.substring(begin, end);
        begin = end;
        end = end + 6;

        TreeNode symbolNode = getSymbolNode(partialEncoding);

        if (symbolNode != null){
            result = result + symbolNode.getSymbol().getCharacter();
            }
        }
        return result; // Replace this line, it is provided so your code compiles
    }


    /**
     * Given a character, delete it from the tree and delete any encodings not
     * attached to a character (ie. no children).
     * 
     * @param symbol the symbol to delete
     */
    public void deleteSymbol(char symbol) {
        // WRITE YOUR CODE HERE

        String encoding = findBrailleEncoding(symbol);
        TreeNode target = getSymbolNode(encoding);
        
    
        String parent = encoding.substring(0, encoding.length() - 1);
        TreeNode parentNode1 = getSymbolNode(parent);
    
        if (parentNode1 != null) {
            if (encoding.charAt(encoding.length() - 1) == 'R') {
                parentNode1.setRight(null);  
            } 

            else{
                parentNode1.setLeft(null);   
            }
        }
    
        String partial = parent;
    
        while (partial.length() > 0) {

            TreeNode currentNode = getSymbolNode(partial);

            if (currentNode != null) {
                if (currentNode.getLeft() == null && currentNode.getRight() == null) {

                    TreeNode parentNode2 = getSymbolNode(partial.substring(0, partial.length() - 1));

                    if (parentNode2 != null) {
                        if (partial.charAt(partial.length() - 1) == 'R') {

                            parentNode2.setRight(null);
                        }

                        else{
                            parentNode2.setLeft(null);   
                        }
                    }
                }
            }
            partial = partial.substring(0, partial.length() - 1);
        }
    }

    public TreeNode getTreeRoot() {
        return this.treeRoot;
    }

    public void setTreeRoot(TreeNode treeRoot) {
        this.treeRoot = treeRoot;
    }

    public void printTree() {
        printTree(treeRoot, "", false, true);
    }

    private void printTree(TreeNode n, String indent, boolean isRight, boolean isRoot) {
        StdOut.print(indent);

        // Print out either a right connection or a left connection
        if (!isRoot)
            StdOut.print(isRight ? "|+R- " : "--L- ");

        // If we're at the root, we don't want a 1 or 0
        else
            StdOut.print("+--- ");

        if (n == null) {
            StdOut.println("null");
            return;
        }
        // If we have an associated character print it too
        if (n.getSymbol() != null && n.getSymbol().hasCharacter()) {
            StdOut.print(n.getSymbol().getCharacter() + " -> ");
            StdOut.print(n.getSymbol().getEncoding());
        }
        else if (n.getSymbol() != null) {
            StdOut.print(n.getSymbol().getEncoding() + " ");
            if (n.getSymbol().getEncoding().equals("")) {
                StdOut.print("\"\" ");
            }
        }
        StdOut.println();

        // If no more children we're done
        if (n.getSymbol() != null && n.getLeft() == null && n.getRight() == null)
            return;

        // Add to the indent based on whether we're branching left or right
        indent += isRight ? "|    " : "     ";

        printTree(n.getRight(), indent, true, false);
        printTree(n.getLeft(), indent, false, false);
    }
}