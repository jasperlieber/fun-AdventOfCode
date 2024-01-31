package com.google.challenges;

public class Answer
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
        Node root;

        BinaryTree()
        {
            root = null;
        }

        void markPostorder(Node node)
        {
            if (node == null) return;
            markPostorder(node.left);
            markPostorder(node.right);
            node.key = markCnt++;
        }
    }

    static int markCnt;

    public static int[] answer(int h, int[] q)
    {
        tree = new BinaryTree();
        tree.root = buildTree(h - 1);

        markCnt = 0;
        tree.markPostorder(tree.root);

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
        else return -1;
    }

    private static BinaryTree tree;

    private static Node buildTree(int h)
    {
        Node node = new Node(0);
        if (h > 0)
        {
            node.left = buildTree(h - 1);
            node.right = buildTree(h - 1);
        }
        return node;
    }
}