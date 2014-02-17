package com.bru.test.ax.config.action1.steps;

import ax.bru.defs.Executable;
import ax.bru.java.Action;
import ax.bru.java.Step;
import ax.bru.java.SubAction;

/**
 * Created by alexbruckner on 26/01/2014
 */
@Action(name = "Action 4 (3-3)", parallel = true)
public class Step33 implements SubAction {

    @Step(order = 1, name = "4-1")
    public Executable step41() {
        return new Step41();
    }

    @Step(order = 2, name = "4-2")
    public Executable step42() {
        return new Step42();
    }
}
