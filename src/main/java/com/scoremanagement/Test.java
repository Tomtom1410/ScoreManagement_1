package com.scoremanagement;

import java.util.Scanner;
///*
//        *
//
//      *   *
//
//   *    *    *
//
//*    *     *    *
//*/

public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter n: ");
        int n = sc.nextInt();
        int height = 0;
        for (int i = 0; i < 2 * n - 1; i++) {
            if (i % 2 == 0) {
                int count = 0;
                for (int j = 0; j < n - 1 - height; j++) {
                    System.out.printf("%3s", " ");
                    count++;
                }
                height++;
                if (height == 1) {
                    System.out.printf("%3s", "*");
                    System.out.printf("%3s", " ");
                    count += 2;
                } else {
                    for (int j = 0; j < height; j++) {
                        System.out.printf("%3s", "*");
                        System.out.printf("%3s", " ");
                        count += 2;
                    }
                }
                for (int j = n + 1; j < 2 * n; j++) {
                    if (count >= 2 * n - 1) {
                        if (count > 2 * n - 1) {
                            System.out.print("\b\b\b");
                        }
                        break;
                    } else {
                        System.out.printf("%3s", " ");
                        count++;
                    }
                }
            }
            if (i % 2 != 0) {
                for (int j = 0; j < 2 * n - 1; j++) {
                    System.out.printf("%3s", " ");
                }
            }
            System.out.println();
        }

    }
}
