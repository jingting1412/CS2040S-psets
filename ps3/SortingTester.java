import java.util.Arrays;
import java.util.Random;

public class SortingTester {
    public static boolean checkSort(ISort sorter, int size) {
        KeyValuePair[] testArray = new KeyValuePair[size];
        Random randomTests = new Random();
        for (int i = 0; i < size; i++) {
            KeyValuePair newPair = new KeyValuePair(randomTests.nextInt(100), randomTests.nextInt(100));
            testArray[i] = newPair;
        }
        sorter.sort(testArray);

        for (int i = 0; i < size - 1; i++) {
            if (testArray[i].getKey() > testArray[i + 1].getKey()) {
                System.out.println("bad");
                return false;
            }
        }
        return true;
    }

    public static boolean isStable(ISort sorter, int size) {
        KeyValuePair[] testArray = new KeyValuePair[size];
        Random randomTests = new Random();
        for (int i = 0; i < size; i++) {
            KeyValuePair newPair = new KeyValuePair(randomTests.nextInt(3), i);
            testArray[i] = newPair;
        }
        System.out.println("Before sorting: " + Arrays.toString(testArray));
        sorter.sort(testArray);
        System.out.println("After sorting: " + Arrays.toString(testArray));

        for (int i = 0; i < size - 1; i++) {
            if (testArray[i].getKey() == testArray[i + 1].getKey() &&
                testArray[i].getValue() > testArray[i + 1].getValue()) {
                System.out.println("unstable");
                return false;
            }
        }
        return true;
    }

    public static void bestAndWorst(ISort sorter, int size) {
        KeyValuePair[] decreasing = new KeyValuePair[size]; //worst case -- decreasing order
        KeyValuePair[] increasing = new KeyValuePair[size];
        Random tests = new Random();

        for (int i = 0; i < size; i++) {
            KeyValuePair element = new KeyValuePair(size - i, i);
            decreasing[i] = element;
        }

        for (int i = 0; i < size; i++) {
            KeyValuePair newPair = new KeyValuePair(i, i);
            increasing[i] = newPair;
        }

        int decreasingTime = sorter.sort(decreasing);
        int increasingTime = sorter.sort(increasing);

        System.out.println("Time to sort decreasing array: " + decreasingTime +
                           "\nTime to sort increasing array: " + increasingTime);

    }

    public static int timings(ISort sorter, int size) {
        KeyValuePair[] newArr = new KeyValuePair[size];
        Random randomTests = new Random();
        for (int i = 0; i < size; i++) {
            KeyValuePair newPair = new KeyValuePair(randomTests.nextInt(100), randomTests.nextInt(100));
            newArr[i] = newPair;
        }
        return sorter.sort(newArr);
    }

    public static boolean checkDuplicates(ISort sorter, int size) {
        KeyValuePair[] newArr = new KeyValuePair[size];
        Random randomTests = new Random();
        for (int i = 0; i < size; i++) {
            if (randomTests.nextInt(2) == 0) {
                newArr[i] = new KeyValuePair(5, i);
            } else {
                newArr[i] = new KeyValuePair(randomTests.nextInt(1000), i);
            }
        }
        sorter.sort(newArr);
        for (int i = 0; i < size - 1; i++) {
            if (newArr[i].getKey() > newArr[i + 1].getKey()) {
                System.out.println("WEIRD!!!");
                System.out.println(newArr[i].toString() + newArr[i + 1].toString());
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        /**
         * 1. Test for differences in best case & worst case (alr sorted vs random)
         * 2. Test for stability
         * 3. Test with duplicates to find the evil one
         */

        ISort sortArr1 = new SorterE();
        isStable(sortArr1, 10);



    }
}
