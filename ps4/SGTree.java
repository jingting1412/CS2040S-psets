import com.sun.source.tree.Tree;

/**
 * ScapeGoat Tree class
 *
 * This class contains some of the basic code for implementing a ScapeGoat tree.
 * This version does not include any of the functionality for choosing which node
 * to scapegoat.  It includes only code for inserting a node, and the code for rebuilding
 * a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     *
     * This class holds the data for a node in a binary tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;

        TreeNode(int k) {
            key = k;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree
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
     * Builds an array of nodes in the specified subtree
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */

    public TreeNode[] enumerateNodes(TreeNode node, Child child) { //returns an inorder traversal of subtree
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

    /**
     * Builds a tree from the list of nodes
     * Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    public TreeNode buildTreeHelper(TreeNode[] nodeList, int start, int end) {
        if(start < end) { return null; }
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
    * Rebuilds the specified subtree of a node
    * 
    * @param node the part of the subtree to rebuild
    * @param child specifies which child is the root of the subtree to rebuild
    */
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null) return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        // Finally, replace the specified child with the new subtree
        if (child == Child.LEFT) {
            node.left = newChild;
        } else if (child == Child.RIGHT) {
            node.right = newChild;
        }
    }

    /**
    * Inserts a key into the tree
    *
    * @param key the key to insert
    */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

         TreeNode node = root;

        while (true) {
            if (key <= node.key) {
                if (node.left == null) break;
                node = node.left;
            } else {
                if (node.right == null) break;
                node = node.right;
            }
        }

        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }
    }


    // Simple main function for debugging purposes
    public static void main(String[] args) {

    }
}
