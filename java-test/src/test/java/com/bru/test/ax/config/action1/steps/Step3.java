package com.bru.test.ax.config.action1.steps;

import ax.bru.defs.Executable;
import ax.bru.java.Action;
import ax.bru.java.Step;
import ax.bru.java.SubAction;

/**
 * Created by alexbruckner on 26/01/2014
 */
@Action(name = "Action 2 (1-3)", parallel = true)
public class Step3 implements SubAction {

    @Step(name = "2-1")
    public SubAction step21(){
        return new Step21();
    }

    @Step(name = "2-2")
    public Executable step22(){
        return new Step22();
    }
}
