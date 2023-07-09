import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentMap;

public class Tp2 {
    public static void main(String[] args) {
        solution sol = new solution();
        sol.launch(args);
    }
}

class Medicament {
    public String name;
    public int amount;
    public String expDate;

    public Medicament(String name, int amount, String expDate) {
        this.name = name;
        this.amount = amount;
        this.expDate = expDate;
    }

    // to get key nameTree
    public String getName() {
        return name;
    }

    // to get key expTree
    public int getDate() {
        // format :: 2017-10-27 -> 20171027
        String stripped = expDate.replaceAll("-", "");
        return Integer.parseInt(stripped);
    }

}

class NameTree {
    public Node root = null;

    public class Node {
        public String key;
        private Node left;
        private Node right;
        public ExpirationTree medStock;

        public Node(String key, ExpirationTree medStock) {
            this.key = key;
            left = null;
            right = null;
            this.medStock = medStock;
        }

    }

    public Node searchNode(Node root, String key) {
        if (root == null) {
            return null;
        }
        if (key.equals(root.key)) {
            return root;
        } else if (key.compareTo(root.key) < 0) {
            return searchNode(root.left, key);
        } else {
            return searchNode(root.right, key);
        }
    }

    public ArrayList<ExpirationTree> getExpTrees() {
        ArrayList<ExpirationTree> expTrees = new ArrayList<>();
        inOrder(root, expTrees);
        return expTrees;
    }

    private void inOrder(Node node, ArrayList<ExpirationTree> expTrees) {
        if (node != null) {
            inOrder(node.left, expTrees);
            expTrees.add(node.medStock);
            inOrder(node.right, expTrees);
        }
    }

    public Node insert(String key, ExpirationTree tree) {
        if (root == null) {
            root = new Node(key, tree);
            return root;
        } else {
            insertNode(root, key, tree);
        }
        return null;
    }

    public Node insertNode(Node root, String key, ExpirationTree tree) {
        if (key.compareTo(root.key) < 0) {
            if (root.left == null) {
                root.left = new Node(key, tree);
                return root.left;
            } else {
                return insertNode(root.left, key, tree);
            }
        } else if (key.compareTo(root.key) > 0) {
            if (root.right == null) {
                root.right = new Node(key, tree);
                return root.right;
            } else {
                return insertNode(root.right, key, tree);
            }
        }
        return null;
    }
}

class ExpirationTree {
    public Node root;
    public String name;

    public class Node {
        private int key;
        private Node left;
        private Node right;
        public int amount;

        public Node(int key) {
            this.key = key;
            left = null;
            right = null;
        }
    }

    public Node insert(int key, int amount) {
        if (root == null) {
            root = new Node(key);
            root.amount = amount;
            return root;
        } else {
            insertNode(root, key, amount);
        }
        return null;
    }

    public boolean removeAmount(int amount) {
        // TRAVERSE TT
        // doit enlever 30 meds

        return true;
    }

    public Node insertNode(Node root, int key, int amount) {
        if (root == null) {
            root = new Node(key);
            root.amount = amount;
            return root;
        }
        if (key < root.key) {
            root.left = insertNode(root.left, key, amount);
        } else if (key > root.key) {
            root.right = insertNode(root.right, key, amount);
        }
        return root;
    }

    public int getTrueAmount(Node root) {
        if (root == null) {
            return 0;
        }

        int leftSum = getTrueAmount(root.left);
        int rightSum = getTrueAmount(root.right);
        int nodeSum = root.amount;

        return leftSum + rightSum + nodeSum;
    }

    public String outputStock(Node root) {
        if (root == null) {
            return "";
        }

        String aws = this.name + "     " + root.amount + "    " +
                root.key;
        if (root.left != null) {
            aws = outputStock(root.left) + "\n" + aws;
        }
        if (root.right != null) {
            aws += "\n" + outputStock(root.left);
        }

        return aws;
    }

    public Node findNode(Node root, int key) {
        if (root == null) {
            return null;
        }
        if (key < root.key) {
            findNode(root.left, key);
        } else if (key > root.key) {
            findNode(root.right, key);
        } else {
            return root;
        }
        return null;
    }

    // public void expireMeds(String currDate, NameTree tree) {
    // TODO on enleve les nodes expiree, et on modifie le amount dans le name tree.
    // }
}

class solution {

    private String[] args;
    private ArrayList<ArrayList<String>> parsedCmds;

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
        NameTree nameTree = new NameTree();
        // TODO this wont work; initialize an expTree for each node of NameTree

        int currDate = 0;
        // String awns = "";

        for (ArrayList<String> cmd : parsedCmds) {

            switch (cmd.get(0)) {
                case "APPROV :":
                    for (int i = 1; i < cmd.size(); i++) {
                        String med = cmd.get(i);
                        String[] parts = med.split("\\s+");
                        Medicament medicament = new Medicament(parts[0], Integer.parseInt(parts[1]), parts[2]);
                        ExpirationTree expirationTree = new ExpirationTree();
                        // Si il trouve le meme medicament, il add les amounts
                        updateStock(nameTree, expirationTree, medicament);
                    }
                    break;
                case "STOCK ;":
                    // Enlever expired expiration tree
                    System.out.println("Stock 2020-10-30");
                    // traverse the nameTree and its nodes values
                    // awns += "STOCK: " + currDate + "\n";
                    ArrayList<ExpirationTree> expTrees = nameTree.getExpTrees();
                    for (ExpirationTree tree : expTrees) {
                        System.out.println(tree.outputStock(tree.root));
                    }
                    break;
                case "PRESCRIPTION :":
                    // TODO
                    // Enlever expired
                    // Prendre furthest from expired

                    NameTree.Node neededTree = nameTree.searchNode(nameTree.root, " "); // Prescription Name
                    boolean succes = true;
                    // boolean succes = neededTree.medStock.removeAmount(0); // Prescription amount
                    if (succes) {
                        // Did remove
                    } else {
                        // Fail
                        // wait
                    }

                    break;
                default:
                    // DATE
                    // expirationTree.expireMeds(currDate, nameTree);
                    break;
            }
        }
        // System.out.println(awns);

        // outputAwns(awns);
    }

    public void updateStock(NameTree nameTree, ExpirationTree expirationTree, Medicament med) {
        // only insert in nametree if not duplicate
        if (nameTree.searchNode(nameTree.root, med.getName()) == null) {

            nameTree.insert(med.getName(), expirationTree);
            expirationTree.name = med.getName();
        }
        // always insert in expirationTree
        expirationTree.insert(med.getDate(), med.amount);
    }

}