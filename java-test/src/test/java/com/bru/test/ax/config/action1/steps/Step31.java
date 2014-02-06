package com.bru.test.ax.config.action1.steps;

import ax.bru.defs.Data;
import ax.bru.defs.Executable;

/**
 * Created by alexbruckner on 26/01/2014
 */
public class Step31 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 3-1!!!");
        data.set("3", System.nanoTime());
    }
}
