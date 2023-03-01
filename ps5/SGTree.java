/**
 * ScapeGoat Tree class
 * <p>
 * This class contains some basic code for implementing a ScapeGoat tree. This version does not include any of the
 * functionality for choosing which node to scapegoat. It includes only code for inserting a node, and the code for
 * rebuilding a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     * <p>
     * This class holds the data for a node in a binary tree.
     * <p>
     * Note: we have made things public here to facilitate problem set grading/testing. In general, making everything
     * public like this is a bad idea!
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;

        public int weight;

        TreeNode(int k) {
            key = k;
            weight = 1;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node, Child child) {
        int count = 0;
        switch (child) {
            case LEFT:
                if (node.left == null) { return 0; }
                count = 1 + countNodes(node.left, Child.LEFT) + countNodes(node.left, Child.RIGHT);
                break;
            case RIGHT:
                if (node.right == null) { return 0; }
                count = 1 + countNodes(node.right, Child.LEFT) + countNodes(node.right, Child.RIGHT);
                break;
        }

        return count;
    }

    /**
     * Builds an array of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */

    TreeNode[] enumerateNodes(TreeNode node, Child child) {
        if (node == null) { return null; }
        int totalCount = countNodes(node, child);
        TreeNode[] enumeratedTree = new TreeNode[totalCount];

        TreeNode targetNode = (child == Child.LEFT) ? node.left : node.right;

        int leftCount = countNodes(targetNode, Child.LEFT);
        TreeNode[] left = new TreeNode[leftCount];

        int rightCount = countNodes(targetNode, Child.RIGHT);
        TreeNode[] right = new TreeNode[rightCount];

        if (targetNode.left != null) {
            left = enumerateNodes(targetNode, Child.LEFT);
        }
        if (targetNode.right != null) {
            right = enumerateNodes(targetNode, Child.RIGHT);
        }

        for (int i = 0; i < totalCount; i++) {
            if (i < leftCount) {
                enumeratedTree[i] = left[i];
            } else if (i == leftCount) {
                enumeratedTree[i] = targetNode;
            } else {
                enumeratedTree[i] = right[i - leftCount - 1];
            }
        }

        return enumeratedTree;
    }

    public int helper(TreeNode[] arr, int start, TreeNode node) {
        if (node == null) {
            return start;
        } else {
            start = helper(arr, start, node.left);
            arr[start++] = node;
            start = helper(arr, start, node.right);
        }

        return start;
    }

    /**
     * Builds a tree from the list of nodes Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    public TreeNode buildTreeHelper(TreeNode[] nodeList, int start, int end) {
        if(start > end) { return null; }
        int mid = (start + end) / 2;
        TreeNode middle = new TreeNode(nodeList[mid].key);
        middle.left = buildTreeHelper(nodeList, start, mid - 1);
        middle.right = buildTreeHelper(nodeList, mid + 1, end);
        return middle;
    }

    public TreeNode buildTree(TreeNode[] nodeList) {
        if (nodeList.length == 0) {
            return null;
        } else if (nodeList.length == 1) {
            return new TreeNode(nodeList[0].key);
        } else {
            return buildTreeHelper(nodeList, 0, nodeList.length - 1);
        }
    }

    /**
     * Determines if a node is balanced. If the node is balanced, this should return true. Otherwise, it should return
     * false. A node is unbalanced if either of its children has weight greater than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {
        if (node == null) return true;
        double rootWeight = node.weight;
        boolean leftCheck = true;
        boolean rightCheck = true;
        if (node.left != null) {
            leftCheck = node.left.weight <= (2.0/3.0) * rootWeight;
        }
        if (node.right != null) {
            rightCheck = node.right.weight <= (2.0/3.0) * rootWeight;
        }

        return (leftCheck && rightCheck);
    }

    /**
     * Rebuilds the specified subtree of a node.
     *
     * @param node  the part of the subtree to rebuild
     * @param child specifies which child is the root of the subtree to rebuild
     */
    public void updateWeights(TreeNode node) {
        if (node == null) return;
        int leftWeight = 0;
        int rightWeight = 0;
        if (node.left != null) {
            updateWeights(node.left);
            leftWeight = node.left.weight;
        }

        if (node.right != null) {
            updateWeights(node.right);
            rightWeight = node.right.weight;
        }

        node.weight = leftWeight + rightWeight + 1;
    }
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null) return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        updateWeights(newChild);
        // Finally, replace the specified child with the new subtree
        if (child == Child.LEFT) {
            node.left = newChild;
        } else if (child == Child.RIGHT) {
            node.right = newChild;
        }
    }

    /**
     * Insert the specified key in the tree using a typical binary search tree insertion (notice that
     * this new node will be inserted as a leaf).
     * Identify the highest unbalanced node on the root-to-leaf path to the newly inserted node.
     * If there is no such unbalanced node, then we are done. If there is an unbalanced node, then
     * rebuild it.
     * To rebuild an unbalanced node u, we need to call the rebuild method on its parent,
     * and we will also need to know if the unbalanced node is the left child or the right child.
     */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }
        TreeNode node = root;
        while (true) {
            if (key <= node.key) {
                node.weight++;
                if (node.left == null) break;
                node = node.left;
            } else {
                node.weight++;
                if (node.right == null) break;
                node = node.right;
            }
        }
        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }

        balanceTree(key);
    }

    public void balanceTree(int key) {
        TreeNode curr = root;
        while (true) {
            if (key > curr.key) {
                if (curr.right == null) break;
                if (!checkBalance(curr.right)) {
                    rebuild(curr, Child.RIGHT);
                    break;
                }
                curr = curr.right;

            } else {
                if (curr.left == null) break;
                if (!checkBalance(curr.left)) {
                    rebuild(curr, Child.LEFT);
                    break;
                }
                curr = curr.left;
            }
        }
    }



    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        tree.insert(1);
        tree.insert(0);
        tree.insert(2);
        tree.insert(3);
        tree.insert(4);
        tree.insert(5);
        System.out.println(tree.root.weight);

    }
}
