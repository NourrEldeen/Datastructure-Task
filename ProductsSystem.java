import java.io.*;
import java.util.*;

// Item class with price added
class Item {
    int id; // unique identifier
    String name;
    String description;
    String category;
    double price;

    public Item(int id, String name, String description, String category, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
    }

    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Description: " + description +
                ", Category: " + category + ", Price: $" + price;
    }
}

// Node class for Linked List and BST
class Node {
    Item item;
    Node next;
    Node left, right;

    public Node(Item item) {
        this.item = item;
    }
}

public class ProductsSystem {
    static Node head = null; // linked list head
    static Node bstRoot = null; // BST for IDs
    static Node bstNameRoot = null; // BST for names
    static Stack<Item> undoStack = new Stack<>();
    static Queue<Item> priorityQueue = new LinkedList<>();

    // Add to Linked List and Name BST and Priority Queue
    public static void addToLinkedList(Item item) {
        Node newNode = new Node(item);
        newNode.next = head;
        head = newNode;

        // Insert into BSTs
        bstRoot = insertBST(bstRoot, item); // by ID
        bstNameRoot = insertBSTByName(bstNameRoot, item); // by name

        // Add to priority queue if urgent
        if (item.category.equalsIgnoreCase("urgent")) {
            priorityQueue.add(item);
        }
    }

    // Insert into BST by ID
    public static Node insertBST(Node root, Item item) {
        if (root == null) return new Node(item);
        if (item.id < root.item.id) root.left = insertBST(root.left, item);
        else root.right = insertBST(root.right, item);
        return root;
    }

    // Insert into BST by name
    public static Node insertBSTByName(Node root, Item item) {
        if (root == null) return new Node(item);
        if (item.name.compareToIgnoreCase(root.item.name) < 0) root.left = insertBSTByName(root.left, item);
        else root.right = insertBSTByName(root.right, item);
        return root;
    }

    // Search by ID (BST)
    public static void searchById(Node root, int id) {
        if (root == null) {
            System.out.println("Item not found.");
            return;
        }
        if (root.item.id == id) {
            System.out.println(root.item);
        } else if (id < root.item.id) {
            searchById(root.left, id);
        } else {
            searchById(root.right, id);
        }
    }

    // Search by Name (BST)
    public static void searchByName(Node root, String name) {
        if (root == null) {
            System.out.println("Item not found.");
            return;
        }
        if (name.equalsIgnoreCase(root.item.name)) {
            System.out.println(root.item);
        } else if (name.compareToIgnoreCase(root.item.name) < 0) {
            searchByName(root.left, name);
        } else {
            searchByName(root.right, name);
        }
    }

    // Search by Category (linked list)
    public static void searchByCategory(String category) {
        Node temp = head;
        boolean found = false;
        while (temp != null) {
            if (temp.item.category.equalsIgnoreCase(category)) {
                System.out.println(temp.item);
                found = true;
            }
            temp = temp.next;
        }
        if (!found) {
            System.out.println("No items found in category: " + category);
        }
    }

    // View all items (linked list)
    public static void viewAll() {
        Node temp = head;
        if (temp == null) {
            System.out.println("No items.");
            return;
        }
        while (temp != null) {
            System.out.println(temp.item);
            temp = temp.next;
        }
    }

    // Delete by ID (linked list)
    public static void deleteItem(int id) {
        Node temp = head, prev = null;
        while (temp != null && temp.item.id != id) {
            prev = temp;
            temp = temp.next;
        }
        if (temp != null) {
            if (prev != null) prev.next = temp.next;
            else head = temp.next;
            undoStack.push(temp.item);
            System.out.println("Item deleted and stored for undo.");
        } else {
            System.out.println("Item not found.");
        }
    }

    // Undo deletion
    public static void undoDelete() {
        if (!undoStack.isEmpty()) {
            Item item = undoStack.pop();
            addToLinkedList(item);
            System.out.println("Undo successful. Item restored.");
        } else {
            System.out.println("Nothing to undo.");
        }
    }

    // Save to file
    public static void saveToFile() {
        try (PrintWriter writer = new PrintWriter("items.txt")) {
            Node temp = head;
            while (temp != null) {
                Item item = temp.item;
                writer.println(item.id + "," + item.name + "," + item.description + "," + item.category + "," + item.price);
                temp = temp.next;
            }
            System.out.println("Items saved to file.");
        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    // Load from file
    public static void loadFromFile() {
        try (Scanner scanner = new Scanner(new File("items.txt"))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length == 5) {
                    Item item = new Item(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3], Double.parseDouble(parts[4]));
                    addToLinkedList(item);
                }
            }
            System.out.println("Items loaded from file.");
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    // Process urgent items
    public static void processUrgentItems() {
        if (priorityQueue.isEmpty()) {
            System.out.println("No urgent items.");
        } else {
            System.out.println("Urgent Items:");
            while (!priorityQueue.isEmpty()) {
                System.out.println(priorityQueue.poll());
            }
        }
    }

    // Main menu
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n--- Item Management System ---");
            System.out.println("1. Add Item");
            System.out.println("2. View Items");
            System.out.println("3. Delete Item");
            System.out.println("4. Undo Delete");
            System.out.println("5. Search by ID");
            System.out.println("6. Search by Name");
            System.out.println("7. Search by Category");
            System.out.println("8. Save to File");
            System.out.println("9. Load from File");
            System.out.println("10. View Urgent Items");
            System.out.println("11. Exit");
            System.out.print("Choose: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Description: ");
                    String desc = sc.nextLine();
                    System.out.print("Enter Category: ");
                    String cat = sc.nextLine();
                    System.out.print("Enter Price: ");
                    double price = sc.nextDouble(); sc.nextLine();
                    Item item = new Item(id, name, desc, cat, price);
                    addToLinkedList(item);
                    System.out.println("Item added.");
                    break;
                case 2:
                    viewAll();
                    break;
                case 3:
                    System.out.print("Enter ID to delete: ");
                    int delId = sc.nextInt();
                    deleteItem(delId);
                    break;
                case 4:
                    undoDelete();
                    break;
                case 5:
                    System.out.print("Enter ID to search: ");
                    int searchId = sc.nextInt();
                    searchById(bstRoot, searchId);
                    break;
                case 6:
                    System.out.print("Enter Name to search: ");
                    String searchName = sc.nextLine();
                    searchByName(bstNameRoot, searchName);
                    break;
                case 7:
                    System.out.print("Enter Category to search: ");
                    String searchCategory = sc.nextLine();
                    searchByCategory(searchCategory);
                    break;
                case 8:
                    saveToFile();
                    break;
                case 9:
                    loadFromFile();
                    break;
                case 10:
                    processUrgentItems();
                    break;
                case 11:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 11);
        sc.close();
    }
}
