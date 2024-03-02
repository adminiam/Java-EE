package org.example;

import java.util.Arrays;
import java.util.Random;

public class ValueCalculator {

    public static void main(String[] args) throws InterruptedException {
        int[] mas = new int[1000000];

        ValueCalculator valueCalculator = new ValueCalculator();
        long timeOneThread = valueCalculator.consumedTime(mas, 1);
        long timeTwoThreads = valueCalculator.consumedTime(mas, 2);
        System.out.println("Time spent using 1 thread = " + timeOneThread + "\n" + "Time spent using 2 thread = " + timeTwoThreads);
        long timeMaxValue = valueCalculator.maxValue(valueCalculator.shuffling(mas), 2);//change number of threads if you need
        System.out.println("Time spent to find max value " + timeMaxValue);
    }

    void showMassive(int[] mas) {
        for (int element : mas) {
            System.out.println(element);
        }
    }

    void showHalfMassive(int[] mas) {
        for (int i = 0; i < mas.length / 2; i++) {
            System.out.println(mas[i]);
        }
    }

    long consumedTime(int[] mas, int numberOfThreads) throws InterruptedException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            mas[i] = 1;
        }
        int[] mas1 = new int[mas.length / 2];
        int[] mas2 = new int[mas.length / 2];
        System.arraycopy(mas, 0, mas1, 0, mas.length / 2);
        System.arraycopy(mas, mas.length / 2, mas2, 0, mas.length / 2);
        Thread threadFill = new Thread(() -> {
            for (int i = 0; i < mas1.length; i++) {
                mas1[i] = (int) (mas[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        threadFill.start();
        threadFill.join();
        Thread threadFill1 = new Thread(() -> {
            for (int i = 0; i < mas1.length; i++) {
                mas2[i] = (int) (mas[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        threadFill1.start();
        threadFill1.join();
        if (numberOfThreads == 1) {
            System.arraycopy(mas1, 0, mas, 0, mas.length / 2);
            System.arraycopy(mas2, 0, mas, mas.length / 2, mas.length / 2);
            Thread thread = new Thread(() -> showMassive(mas));
            thread.start();
            thread.join();
        } else {
            Thread thread1 = new Thread(() -> showHalfMassive(mas1));
            thread1.start();
            thread1.join();
            Thread thread2 = new Thread(() -> showHalfMassive(mas2));
            thread2.start();
            thread2.join();
        }
        long finish = System.currentTimeMillis();
        return (finish - start);
    }

    int[] shuffling(int[] mas) {
        for (int i = 0; i < 1000000; i++) {
            mas[i] = i;
        }
        Random rnd = new Random();
        for (int i = 0; i < mas.length; i++) {
            int randomIndex = rnd.nextInt(mas.length);
            int temp = mas[i];
            mas[i] = mas[randomIndex];
            mas[randomIndex] = temp;

        }
        return mas;
    }

    long maxValue(int[] mas, int numberOfThreads) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        int[] maxValues = new int[numberOfThreads];
        Thread[] threads = new Thread[numberOfThreads];
        int chunk = mas.length / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            int start = i * chunk;
            int end = (i == numberOfThreads - 1) ? mas.length : (i + 1) * chunk;
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                maxValues[threadIndex] = Arrays.stream(mas, start, end).max().orElse(Integer.MIN_VALUE);
            });
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        int globalMax = Arrays.stream(maxValues).max().orElse(Integer.MIN_VALUE);
        System.out.println("Max number = " + globalMax);
        long finishTime = System.currentTimeMillis();

        return finishTime - startTime;
    }

}
