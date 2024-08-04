public class ReverseList {
    public static void main(String[] args) {

    }

    /**
     * leetcode 第206题，反转链表，发现使用sout打印在这种情况下也很耗时
     * @return
     */
    public ListNode reverseList(ListNode head) {
        ListNode pre = null;
        while (head != null) {
            ListNode node = head.next;
            // 1. 刚开始思考的时候，想多了，觉得在记录pre前需要判断下是否为null，进行下最初的初始化
            /*if (pre ==null) {
                pre = head;
                head.next = null;
            } else {
                head.next = pre;
                pre = head;
            }*/
            // 2. 实际上回过头检查代码的时候发现，不需要做这个判断
            head.next = pre;
            pre = head;
            System.out.println("pre: " + pre.val);
            head = node;
        }
        return head;
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
