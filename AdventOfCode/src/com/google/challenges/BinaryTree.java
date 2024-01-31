//package com.google.challenges;
//
//import com.goatwalker.aoc16.Day22.Node;
//
///*
//
// Inputs:
// (int) h = 3
// (int list) q = [7, 3, 5, 1]
// Output:
// (int list) [-1, 7, 6, 3]
//
// Inputs:
// (int) h = 5
// (int list) q = [19, 14, 28]
// Output:
// (int list) [21, 15, 29]
//
// */
//
////public class Answer2
////{
//
//// Java program for different tree traversals
//
///*
// * Class containing left and right child of current node and key value
// */
//class Node2
//{
//    int key;
//    Node left, right;
//
//    public Node(int item)
//    {
//        key = item;
//        left = right = null;
//    }
//}
//
//public class BinaryTree
//{
//    // Root of Binary Tree
//    Node root;
//
//    BinaryTree()
//    {
//        root = null;
//    }
//
//    /*
//     * Given a binary tree, print its nodes according to the "bottom-up"
//     * postorder traversal.
//     */
//    void printPostorder(Node node)
//    {
//        if (node == null) return;
//
//        // first recur on left subtree
//        printPostorder(node.left);
//
//        // then recur on right subtree
//        printPostorder(node.right);
//
//        // now deal with the node
//        System.out.print(node.key + " ");
//    }
//
//    // Driver method
//    public static void main(String[] args)
//    {
//        BinaryTree tree = new BinaryTree();
//        tree.root = new Node(1);
//        tree.root.left = new Node(2);
//        tree.root.right = new Node(3);
//        tree.root.left.left = new Node(4);
//        tree.root.left.right = new Node(5);
//
//        // System.out.println("Preorder traversal of binary tree is ");
//        // tree.printPreorder();
//        //
//        // System.out.println("\nInorder traversal of binary tree is ");
//        // tree.printInorder();
//
//        System.out.println("\nPostorder traversal of binary tree is ");
//        tree.printPostorder(tree.root);
//    }
//
//}
//
//// void printPostorder()
//// {
//// printPostorder(root);
//// }
//// }