class FindDuplicate {
    public static void main(String[] args) {
        int[] nums = {1, 3, 4, 2, 2};
        int result = findDuplicate(nums);
        System.out.println(result);
    }

    /**
     * 对应leetcode 287. 寻找重复数
     * 此题的关键就是将数组想象成链表，因为这个数组长度为n+1，而其中的元素范围又恰好是1->n
     * 数组的下标可以作为指针，同时数组的值也可以作为指针，从而将问题转换为求链表中的环的入口
     *
     * @param nums
     * @return
     */
    public static int findDuplicate(int[] nums) {
        int slow = 0, fast = 0;
        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while (slow != fast);
        fast = 0;
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }
        return slow;
    }
}