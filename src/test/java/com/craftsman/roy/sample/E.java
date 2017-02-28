package com.craftsman.roy.sample;

import java.util.Random;
import java.util.concurrent.LinkedTransferQueue;

/**
 * 
 * @author luoyh
 * @date Feb 24, 2017
 */
public class E {
	private static boolean lessThanVersion(String[] ver, int dst) {
		int r = 0, i = 3;
		for(String v : ver) {
			r += Integer.parseInt(v) * Integer.parseInt(Integer.toBinaryString((1 << (-- i * 3))));
		}
		System.out.println(r);
		return r < dst;
	}
	
	public static void main(String[] args) {
		
		lessThanVersion(new String[]{"1", "2", "3"}, 300);
	}

}
