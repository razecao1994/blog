package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LevelOrderTraversal {
    /**
     * 对二叉树进行层序遍历，关键在于将每一层的node在遍历中添加到一个链表中，进行遍历
     * @param root
     * @return
     */
    public List<List<Integer>> levelOrder(Main.TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        Queue<Main.TreeNode> treeNodeQueue = new LinkedList<>();
        treeNodeQueue.offer(root);
        while (!treeNodeQueue.isEmpty()) {
            int size = treeNodeQueue.size();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Main.TreeNode node = treeNodeQueue.poll();
                list.add(node.val);
                if (node.left != null) treeNodeQueue.offer(node.left);
                if (node.right != null) treeNodeQueue.offer(node.right);
            }
            ans.add(list);
        }
        return ans;
    }

    public class TreeNode {
        int val;
        Main.TreeNode left;
        Main.TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, Main.TreeNode left, Main.TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
