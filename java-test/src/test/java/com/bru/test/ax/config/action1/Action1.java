package com.bru.test.ax.config.action1;

import ax.bru.defs.Executable;
import ax.bru.java.Action;
import ax.bru.java.Step;
import ax.bru.java.SubAction;
import com.bru.test.ax.config.action1.steps.Step1;
import com.bru.test.ax.config.action1.steps.Step2;
import com.bru.test.ax.config.action1.steps.Step3;
import com.bru.test.ax.config.action1.steps.Step4;


/**
 * Created by alexbruckner on 26/01/2014
 */
@Action(name = "Java Config Action 1") // is main as does not inherit from SubAction
public class Action1 {

    @Step(order = 1, name = "1-1")
    public Executable step1(){
        return new Step1();
    }

    @Step(order = 2, name = "1-2")
    public Executable step2(){
        return new Step2();
    }

    @Step(order = 3, name = "1-3")
    public SubAction step3(){
        return new Step3();
    }

    @Step(order = 4, name = "1-4")
    public Executable step4(){
        return new Step4();
    }
}