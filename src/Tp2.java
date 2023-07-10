import javax.naming.Name;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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
        public ExpirationTree medStock = new ExpirationTree();

        public Node(String key) {
            this.key = key;
            left = null;
            right = null;
            medStock.name = key;
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

    public Node insert(String key) {
        if (root == null) {
            root = new Node(key);
            return root;
        } else {
            insertNode(root, key);
        }
        return null;
    }

    public Node insertNode(Node root, String key) {
        if (key.compareTo(root.key) < 0) {
            if (root.left == null) {
                root.left = new Node(key);
                return root.left;
            } else {
                return insertNode(root.left, key);
            }
        } else if (key.compareTo(root.key) > 0) {
            if (root.right == null) {
                root.right = new Node(key);
                return root.right;
            } else {
                return insertNode(root.right, key);
            }
        }
        return null;
    }

    public void removeExpired(Node root, int currentDate) {
        if (root == null) {
            return;
        } else {
            if (root.medStock != null) {
                root.medStock.root = root.medStock.removeExpiredNodes(root.medStock.root, currentDate);
            }
            removeExpired(root.right, currentDate);
            removeExpired(root.left, currentDate);
        }

    }

    public String outputOrders(Node root) {
        String str = "";
        if (root == null) {
            return "";
        } else {

            str += outputOrders(root.left);
            if (root.medStock.orders != 0) {
                str += root.key + " " + root.medStock.orders + "\n";
            }
            str += outputOrders(root.right);

        }

        return str;
    }

}

class ExpirationTree {
    public Node root;
    public String name;
    public int orders = 0;

    public class Node {
        public int key;
        private Node parent;
        private Node left;
        private Node right;
        public int amount;

        public Node(int key) {
            this.key = key;
            left = null;
            right = null;
        }
    }

    public Node removeExpiredNodes(Node node, int currentDate) {
        if (node == null) {
            return null;
        } else {
            if (node.key <= currentDate) {
                // Quand la cle est plus petite, le sous arbre droit qui est peut etre pas
                // expire prend le dessus
                node.right = removeExpiredNodes(node.right, currentDate);
                if(node.right != null) {
                    node.right.parent = node;
                }
                return node.right;
            } else {
                node.left = removeExpiredNodes(node.left, currentDate);
                if(node.left != null) {
                    node.left.parent = node;
                }
                return node;
            }
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


    public boolean removeAmount(Node minNode, int amount) {
        boolean sucess;

        if (minNode.amount >= amount) {
            minNode.amount -= amount;

            if (minNode.amount == 0) {
                root = removeNode(root, minNode.key);
            }
            sucess = true;
            return sucess;
        }

        Node nextNode = getNextNode(minNode);
        if (nextNode != null) {
            sucess = removeAmount(nextNode, amount);
            return sucess;
        }
        sucess = false;
        return sucess;
    }

    private Node getNextNode(Node node) {
        if(node.right != null) {
            node = node.right;
            while (node.left != null) {
                node = node.left;
            }
            return node;
        } else {
            Node parent = node.parent;
            while (parent != null && node == parent.right) {
                node = parent;
                parent = parent.parent;
            }
            return parent;
        }
    }

    private Node removeNode(Node root, int key) {
        if (root == null) {
            return null;
        } else if (key > root.key) {
            root.right = removeNode(root.right, key);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            Node minNode = findMinNode(root.right);
            root.key = minNode.key;
            root.amount = minNode.amount;

            root.right = removeNode(root.right, minNode.key);

        }
        return root;
    }

    // since already avl, minimum to the left++
    public Node findMinNode(Node node) {
        Node curr = node;
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr;
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
        solution sol = new solution();
        String aws = this.name + " " + root.amount + " " +
                sol.toStringDate(root.key);
        if (root.left != null) {
            aws = outputStock(root.left) + "\n" + aws;
        }
        if (root.right != null) {
            aws = outputStock(root.right) + "\n" + aws;
        }

        return aws;
    }
}

class solution {

    private String[] args;
    private ArrayList<ArrayList<String>> parsedCmds;
    private String commands = "";
    public String output = "";

    public void launch(String[] args) {
        this.args = args;
        parseFile(args);
        treatCmds();
        output(args);
    }

    public void output(String[] args) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
            writer.write(output);
            writer.close();
        } catch (Exception e) {

        }
    }

    public void parseFile(String[] args) {
        parsedCmds = new ArrayList<>();
        String path = args[0];
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            ArrayList<String> app = new ArrayList<>();
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

        int currDate = 0;
        String date = null;
        int count = 1;

        for (ArrayList<String> cmd : parsedCmds) {
            String caseStr = cmd.get(0);
            int spaceIndex = caseStr.indexOf(" ");
            spaceIndex = Math.max(spaceIndex, 0);
            String firstWord = caseStr.substring(0, spaceIndex).trim();

            if (firstWord.isEmpty()) {
                firstWord = caseStr;
            }

            switch (firstWord) {
                case "APPROV":
                    for (int i = 1; i < cmd.size(); i++) {
                        String med = cmd.get(i);
                        String[] parts = med.split("\\s+");
                        Medicament medicament = new Medicament(parts[0], Integer.parseInt(parts[1]), parts[2]);
                        updateStock(nameTree, medicament);
                    }
                    output += "APPROV OK\n";
                    break;
                case "STOCK":
                    nameTree.removeExpired(nameTree.root, currDate);
                    output += "STOCK " + toStringDate(currDate) + "\n";
                    ArrayList<ExpirationTree> expTrees = nameTree.getExpTrees();
                    for (ExpirationTree tree : expTrees) {
                        if (tree.outputStock(tree.root) != "") {
                            output += tree.outputStock(tree.root) + "\n";
                        }

                    }
                    output += "\n";
                    break;
                case "PRESCRIPTION":
                    output += "PRESCRIPTION " + count + "\n";
                    for (int i = 1; i < cmd.size(); i++) {
                        String prescription = cmd.get(i);
                        String[] parts = prescription.split("\\s+");
                        int amount = Integer.parseInt(parts[1]) * Integer.parseInt(parts[2]);
                        removeStock(parts[0], amount, nameTree, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    }
                    output += "\n";
                    count++;
                    break;
                default:
                    String newDate = cmd.get(0);
                    date = newDate.substring(newDate.indexOf(" ") + 1, 15);
                    if (validateDate(date)) {
                        date = date.replaceAll("-", "");
                        currDate = Integer.parseInt(date);
                        nameTree.removeExpired(nameTree.root, currDate);
                        if (commands != "") {
                            output += toStringDate(currDate) + " COMMANDES :" + "\n";
                            output += nameTree.outputOrders(nameTree.root);
                        } else {
                            output += toStringDate(currDate) + " OK\n";
                        }
                    } else {
                        System.out.println("Erreur, la date n'est pas valide !");
                    }
                    output += "\n";
                    break;
            }
        }
    }

    public String toStringDate(int dateInt) {
        String date = String.valueOf(dateInt);
        return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
    }

    public boolean validateDate(String date) {
        String[] dateParts = date.split("-");

        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        boolean isValidYear = year >= 0 && year <= 9999;
        boolean isValidMonth = month >= 1 && month <= 12;
        boolean isValidDay = day >= 1 && day <= 31;

        return isValidYear && isValidMonth && isValidDay;
    }

    public void updateStock(NameTree nameTree, Medicament med) {
        // only insert in nametree if not duplicate
        if (nameTree.searchNode(nameTree.root, med.getName()) == null) {
            nameTree.insert(med.getName());
        }
        NameTree.Node node = nameTree.searchNode(nameTree.root, med.getName());
        // always insert in expirationTree
        node.medStock.insert(med.getDate(), med.amount);
    }

    public void removeStock(String name, int amount, NameTree nameTree, int cycles, int perServe) {
        NameTree.Node neededTree = nameTree.searchNode(nameTree.root, name); // Prescription Name
        try {
            ExpirationTree exptree = neededTree.medStock; // if nullpointer exc aka not initialized
            int stock = exptree.getTrueAmount(exptree.root);
            if (stock >= amount) {
                exptree.removeAmount(exptree.findMinNode(exptree.root),amount);
                output += name + " " + cycles + " " + perServe + " OK\n";
            } else {
                output += name + " COMMANDE\n";
                exptree.orders += amount;
            }
        } catch (Exception NullPointerException) {
            output += name + " " + cycles + " " + perServe + " COMMANDE\n";
            commands += name + " " + amount + "\n";
            nameTree.insert(name);
            NameTree.Node newNode = nameTree.searchNode(nameTree.root, name);
            newNode.medStock = new ExpirationTree();
            newNode.medStock.orders += amount;
        }

    }

}