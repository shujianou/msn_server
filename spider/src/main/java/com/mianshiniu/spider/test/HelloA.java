package com.mianshiniu.spider.test;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Vim 2019/2/20 12:48
 *
 * @author Vim
 */
class HelloA {
    public HelloA() {
        System.out.println("HelloA");
    }

    {
        System.out.println("I'm A class");
    }

    static {
        System.out.println("static A");
    }

}

class HelloB extends HelloA {

    public static void main(String[] args) {

        String a = "uube07beitc";
        String b = "be";
        System.out.println(a.replace(b, new StringBuilder(b).reverse()));
    }
}
