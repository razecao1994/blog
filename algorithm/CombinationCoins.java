import java.util.Arrays;

public class CombinationCoins {
    public static void main(String[] args) {
        int[] coins = {8, 3, 5, 5, 10};
        int target = 13;
        int result = combinationCoins(coins, target);
        System.out.println(result);
    }

    /**
     * 0-1背包零钱兑换问题
     * 有coins数组对应的这些零钱金币，且零钱个数是有限的，需要计算将target兑换成零钱金币最少有多少枚，如果无法兑换出零钱，则返回-1
     * @param coins
     * @param target
     * @return
     */
    public static int combinationCoins(int[] coins, int target) {
        // 动态规划
        // dp[i]表示i最少可以兑换dp[i]枚金币
        int[] dp = new int[target + 1];
        // 如果所有金币都为1，target最多也是由target枚金币组成，最大值设置成target+1，方便计算
        Arrays.fill(dp, target + 1);
        dp[0] = 0;
        for (int coin : coins) {
            // 从大到小遍历，0-1背包，有限枚金币，遍历过多少枚coin，dp数组中的值即为由已经遍历过的coin算出的最小金币数
            for (int i = target; i >= coin; i--) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
            dp[coin] = 1;
        }

        return dp[target] == target + 1 ? -1 : dp[target];
    }
}
