class Solution {
    public int[] sortArray(int[] nums) {
        quickSort(nums, 0, nums.length - 1);
        return nums;
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    private int randomizedPartition(int[] nums, int left, int right) {
        int pivot = new Random().nextInt(right - left + 1) + left;
        swap(nums, left, pivot);
        return partition(nums, left, right);
    }

    private int partition(int[] nums, int left, int right) {
        int i = left;
        int j = right;
        while (i < j) {
            while (i < j && nums[j] >= nums[left]) {
                j--;
            }
            while (i < j && nums[i] <= nums[left]) {
                i++;
            }
            swap(nums, i, j);
        }
        swap(nums, left, i);
        return i;
    }

    private void quickSort(int[] nums, int i, int j) {
        if (i >= j) {
            return;
        }
        int index = randomizedPartition(nums, i, j);
        quickSort(nums, i, index - 1);
        quickSort(nums, index + 1, j);
    }
}