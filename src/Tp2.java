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
        for (ArrayList<String> element : sol.getParsedCmds()) {
            for(String ord : element){
                System.out.println(ord);
            }
            System.out.println("\n");
        }
    }
}


//bst for commands
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

class Medicament{
    private String name;
    private int amount;
    private String expDate;

    public Medicament(String name, int amount, String expDate) {
        this.name = name;
        this.amount = amount;
        this.expDate = expDate;
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
                    if(line.length() != 1) {
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

    //build a bst for commands
    public void treatCmds() {
        stock = new ArrayList<>();
        for (ArrayList<String> cmd : parsedCmds) {
            switch (cmd.get(0)) {
                case "APPROV :" :
                    for (int i = 1; i < cmd.size(); i++) {
                        String med = cmd.get(i);
                        String[] parts = med.split("\\s+");
                        Medicament medicament = new Medicament(parts[0], Integer.parseInt(parts[1]), parts[2]);
                        stock.add(medicament);
                    }
                    break;
                case "STOCK ;" :
                    break;
                case "PRESCRIPTION :" :
                    break;
                default:
                    //DATE
                    break;
            }
        }
    }

    public void updateStock(){}


    public ArrayList<ArrayList<String>> getParsedCmds() {
        return parsedCmds;
    }
}