package algo_questions;

import java.util.Arrays;

public class Solutions {
    /**
     * this call is a static class that contains
     * solutions to four algorithmic problems
     */
    Solutions(){}

    public static int alotStudyTime(int[] tasks, int[] timeSlots){
        /**
         * this function solve the slotStudyTime problem
         * more on the functionality and run time can be found in the
         * README file
         * @param tasks a list of tasks to complete
         * @param timeSlots a list of time slots that we can
         *                  allocate the tasks to
         * @returns the max num of tasks that can be placed at
         *          the given time slots
         */
        Arrays.sort(tasks);
        Arrays.sort(timeSlots);
        int i =tasks.length-1;
        int j =timeSlots.length-1;
        int count =0;
        while (i>= 0 && j>=0){
            if(tasks[i]<=timeSlots[j]){
                j--;
                count++;
            }
            i--;
        }
        if(i == 0)
            return i;
        return count;
    }


    public static int bucketWalk(int n){
        /**
         * this function solve the bucketWalk problem
         * more on the functionality and run time can be found in the
         * README file
         * @param n the size of the well in liters
         * @returns all the ways to fill the well with buckets of 1 and 2
         */
        if(n==0){
            return 1;
        }
        int oddBuckets = 1;
        int evenBuckets = 2;
        int leftBuckets = n-3;
        while (leftBuckets>=0){
            oddBuckets = evenBuckets+oddBuckets;
            evenBuckets = oddBuckets+evenBuckets;
            leftBuckets -=2;
        }
        if(n%2==0){
            return evenBuckets;
        }
        return oddBuckets;
    }

    public static int minLeap(int[] leapNum){
        /**
         * this function solve the minLeap problem
         * more on the functionality and run time can be found in the
         * README file
         * @param leapNum a list of tasks to complete
         * @returns the min num of leaps to get to the
         *          end of the array
         */
        int curLeap = 0;
        int nextLeap = 0;
        int jmpCount = 0;
        while (curLeap<leapNum.length-1){
            int mostEfficientLeap = 0;
            for (int i = curLeap+1; i < leapNum[curLeap]+curLeap+1; i++) {
                if(i>=leapNum.length-1){
                    return ++jmpCount;
                }
                if(mostEfficientLeap<leapNum[i]+i){
                    mostEfficientLeap = leapNum[i]+i;
                    nextLeap = i;
                }
            }
            curLeap = nextLeap;
            ++jmpCount;
        }
        return jmpCount;
    }

    public static int numTrees(int n){
        /**
         * this function solve the numTrees problem
         * more on the functionality and run time can be found in the
         * README file
         * @param n num of nodes
         * @returns all the binary tress that can
         *          be made with n nods
         */
        int[] numTrees = new int[n+1];
        numTrees[0] = 1;
        numTrees[1] = 1;
        for (int i = 2; i <= n; i++) {
            int curNumTrees = 0;
            int k =i-1;
            for (int j = 0; j <= k; j++) {
                curNumTrees += numTrees[k-j] * numTrees[j];
            }
            numTrees[i] = curNumTrees;
        }
        return numTrees[n];


    }
}
