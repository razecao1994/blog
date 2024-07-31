class Solution {
    /**
     * leetcode第238题，这道题比较难的就是限制使用除法，并且需要在0[n]复杂度内完成，看到一个老哥分享的解法，感觉思路很不错，记录一下
     * @param nums
     * @return
     */
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] ans = new int[n];
        Arrays.fill(ans, 1);
        int start = 1;
        int end = 1;
        for (int i = 0; i < n; i++) {
            ans[i] *= start;
            ans[n - i - 1] *= end;
            start *= nums[i];
            end *= nums[n - i - 1];
        }
        return ans;
    }
}