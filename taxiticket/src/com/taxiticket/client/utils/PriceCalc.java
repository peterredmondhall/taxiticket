package com.taxiticket.client.utils;

public class PriceCalc
{

    private static double MWST = 0.19;
    private static double BASE = 300;
    private static double PRO_M = 0.14;

    public static int[] getPrice(double meters)
    {
        int[] result = new int[3];
        result[0] = (int) (BASE + (PRO_M * meters));
        result[1] = (int) (result[0] * MWST);
        result[2] = (int) (result[0] + result[1]);
        return result;
    }
}
