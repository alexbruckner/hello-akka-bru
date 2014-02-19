package ax.bru.act.config.action1;

import ax.bru.act.config.action1.steps.Step1;
import ax.bru.act.config.action1.steps.Step2;
import ax.bru.act.config.action1.steps.Step3;
import ax.bru.act.config.action1.steps.Step4;
import ax.bru.defs.Executable;
import ax.bru.java.Action;
import ax.bru.java.Step;
import ax.bru.java.SubAction;
import ax.bru.java.strict.Description;


/**
 * Created by alexbruckner on 26/01/2014
 */
@Action(name = "Java Config Action 1") // is main as does not inherit from SubAction
@Description("This is the Java Config Action 1, \n" +
             "which is a test case example \n" +
             "for this framework and executed in sequence. \n" +
             "Steps 1,2 and 4 are executables, \n" +
             "whereas step 3 is itself a subaction.") // required in strict mode for main/top-level action
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