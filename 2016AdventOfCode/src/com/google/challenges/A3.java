package com.google.challenges;

public class A3
{
    static class Node
    {
        int key;
        Node left, right;

        public Node(int item)
        {
            key = item;
            left = right = null;
        }
    }

    static class BinaryTree
    {
        // Root of Binary Tree
        Node root;

        BinaryTree()
        {
            root = null;
        }

        /*
         * Given a binary tree, print its nodes according to the "bottom-up"
         * postorder traversal.
         */
        void markPostorder(Node node)
        {
            if (node == null) return;

            // first recur on left subtree
            markPostorder(node.left);

            // then recur on right subtree
            markPostorder(node.right);

            // now deal with the node
            node.key = markCnt++;
            // System.out.print(node.key + " ");
        }

    }

    /*
     * Inputs: (int) h = 3 (int list) q = [7, 3, 5, 1] Output: (int list) [-1,
     * 7, 6, 3]
     * 
     * Inputs: (int) h = 5 (int list) q = [19, 14, 28] Output: (int list) [21,
     * 15, 29]
     */
    public static void main(String[] args) throws Exception
    {
        int[] q = { 19, 14, 28 };
        int[] p = answer(6, q);

        for (int jj = 0; jj < q.length; jj++)
        {
            System.out.println(q[jj] + " child of " + p[jj]);
        }
        System.out.println("bye bye");
    }

    static int markCnt;

    public static int[] answer(int h, int[] q)
    {
        tree = new BinaryTree();
        tree.root = buildTree(h - 1);

        printTree(tree.root, 0);
        markCnt = 0;
        tree.markPostorder(tree.root);
        printTree(tree.root, 0);

        int[] p = new int[q.length];

        for (int jj = 0; jj < q.length; jj++)
        {
            p[jj] = getParent(tree.root, q[jj]);
        }
        return p;
    }

    private static int getParent(Node root, int i)
    {
        int child;
        if (root.key == i || root.left == null) return -1;
        else if (root.left.key == i || root.right.key == i) return root.key;
        else if ((child = getParent(root.left, i)) != -1) return child;
        else if ((child = getParent(root.right, i)) != -1) return child;
        else
            return -1;
    }

    private static void printTree(Node root, int depth)
    {
        // TODO Auto-generated method stub
        if (root == null)
        {
            System.out.println();
            return;
        }
//        for (int jj = 0; jj < depth; jj++)
//            System.out.print(" ");
        System.out.format("%3d ", root.key);
        printTree(root.left, depth+1);
        for (int jj = 0; jj <= depth; jj++)
            System.out.print("    ");
        
        printTree(root.right, depth+1);
//        System.out.println();
        
    }

    private static BinaryTree tree;
    
    private static Node buildTree(int h)
    {
        Node node = new Node(0);
        if (h>0)
        {
            node.left = buildTree(h-1);
            node.right = buildTree(h-1);
        }
        return node;
    }

}