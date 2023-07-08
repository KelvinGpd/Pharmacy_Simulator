import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentMap;

public class Tp2 {
    public static void main(String[] args) {
        solution sol = new solution();
        sol.launch(args);
        // for (ArrayList<String> element : sol.getParsedCmds()) {
        // for (String ord : element) {
        // System.out.println(ord);
        // }
        // System.out.println("\n");
        // }
    }
}

// bst for commands
class Commandes {
    private Node root;

    private class Node {
        private int key;
        private Node left;
        private Node right;

        public Node(int key) {
            this.key = key;
            left = null;
            right = null;
        }
    }

    public void insert(int key) {
        root = insertNode(root, key);
    }

    private Node insertNode(Node root, int key) {
        if (root == null) {
            root = new Node(key);
            return root;
        }

        if (key < root.key) {
            root.left = insertNode(root.left, key);
        } else if (key > root.key) {
            root.right = insertNode(root.right, key);
        }

        return root;
    }

    public boolean search(int key) {
        return searchNode(root, key);
    }

    private boolean searchNode(Node root, int key) {
        if (root == null) {
            return false;
        }

        if (key == root.key) {
            return true;
        } else if (key < root.key) {
            return searchNode(root.left, key);
        } else {
            return searchNode(root.right, key);
        }
    }

    public void inorderTraversal() {
        inorder(root);
    }

    private void inorder(Node root) {
        if (root != null) {
            inorder(root.left);
            System.out.print(root.key + " ");
            inorder(root.right);
        }
    }
}

class Medicament {
    public String name;
    public int amount;
    public String expDate;
    public int amountToBeUsed;

    public Medicament(String name, int amount, String expDate) {
        this.name = name;
        this.amount = amount;
        this.expDate = expDate;
        amountToBeUsed = 0;
    }
}

class NameTree {
    public Node root = null;

    private class Node {
        public Medicament key;
        private Node left;
        private Node right;

        public Node(Medicament key) {
            this.key = key;
            left = null;
            right = null;
        }
    }

    public Node insert(Medicament key) {
        if (root == null) {
            root = new Node(key);
            return root;
        } else {
            insertNode(root, key);
        }
        return null;
    }

    public Node insertNode(Node root, Medicament key) {
        if (root == null) {
            root = new Node(key);
            return root;
        }

        int compareVal = root.key.name.compareTo(key.name);

        if (compareVal < 0) {
            root.left = insertNode(root.left, key);
        } else if (compareVal > 0) {
            root.right = insertNode(root.right, key);
        } else {
            root.key.amount += key.amount;
        }

        return root;
    }
}

class ExpirationTree {
    public Node root;

    public class Node {
        private Medicament key;
        private Node left;
        private Node right;

        public Node(Medicament key) {
            this.key = key;
            left = null;
            right = null;
        }
    }

    public Node insert(Medicament key) {
        if (root == null) {
            root = new Node(key);
            return root;
        } else {
            insertNode(root, key);
        }
        return null;
    }

    public Node insertNode(Node root, Medicament key) {
        if (root == null) {
            root = new Node(key);
            return root;
        }

        // TODO: COMPARE DATES
        boolean compareVal = true;

        if (compareVal) {
            root.left = insertNode(root.left, key);
        } else {
            root.right = insertNode(root.right, key);
        }
        return root;
    }

    public String outputStock(Node root) {
        if (root == null) {
            return "";
        }

        String aws = root.key.name + "     " + root.key.amount + "    " + root.key.expDate;
        if (root.left != null) {
            aws = outputStock(root.left) + "\n" + aws;
        }
        if (root.right != null) {
            aws += "\n" + outputStock(root.left);
        }

        return aws;
    }

    public Node findNode(Node root, Medicament key) {
        if (root == null) {
            return null;
        }
        int compareVal = root.key.name.compareTo(key.name);

        if (compareVal < 0) {
            findNode(root.left, key);
        } else if (compareVal > 0) {
            findNode(root.right, key);
        } else {
            return root;
        }
        return null;
    }

    public void expireMeds(String currDate, NameTree tree) {
        // TODO on enleve les nodes expiree, et on modifie le amount dans le name tree.
    }
}

class solution {

    private String[] args;
    private ArrayList<ArrayList<String>> parsedCmds;
    private ArrayList<Medicament> stock;

    public void launch(String[] args) {
        this.args = args;
        parseFile();
        treatCmds();
    }

    public void parseFile() {
        parsedCmds = new ArrayList<>();
        String path = "tests/exemple1.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            ArrayList<String> app = new ArrayList();
            while ((line = br.readLine()) != null) {
                if (line.endsWith(";")) {
                    if (line.length() != 1) {
                        app.add(line);
                    }
                    parsedCmds.add(app);
                    app = new ArrayList<>();
                } else {
                    app.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    // build a bst for commands
    public void treatCmds() {
        stock = new ArrayList<>();
        NameTree nameTree = new NameTree();
        ExpirationTree expirationTree = new ExpirationTree();

        String currDate = "";
        String awns = "";

        for (ArrayList<String> cmd : parsedCmds) {
            expirationTree.expireMeds(currDate, nameTree);

            switch (cmd.get(0)) {
                case "APPROV :":
                    for (int i = 1; i < cmd.size(); i++) {
                        String med = cmd.get(i);
                        String[] parts = med.split("\\s+");
                        Medicament medicament = new Medicament(parts[0], Integer.parseInt(parts[1]), parts[2]);
                        // Si il trouve le meme medicament, il add les amounts
                        nameTree.insert(medicament);
                        expirationTree.insert(medicament);
                    }
                    break;
                case "STOCK ;":
                    awns += "STOCK: " + currDate + "\n";
                    awns += expirationTree.outputStock(expirationTree.root) + "\n";
                    break;
                case "PRESCRIPTION :":
                    break;
                default:
                    // DATE

                    break;
            }
        }
        System.out.println(awns);
        outputAwns(awns);
    }

    public void updateStock() {
    }

    public void outputAwns(String anws) {

    }

    public ArrayList<ArrayList<String>> getParsedCmds() {
        return parsedCmds;
    }
}