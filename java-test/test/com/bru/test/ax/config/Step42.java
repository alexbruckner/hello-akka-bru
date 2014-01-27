package com.bru.test.ax.config;

import ax.bru.defs.Data;
import ax.bru.defs.Executable;
import ax.bru.java.Step;

/**
 * Created by alexbruckner on 26/01/2014
 */
@Step(name = "4-2")
public class Step42 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 4-2!!!");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        data.set("6", System.nanoTime());
    }
}
