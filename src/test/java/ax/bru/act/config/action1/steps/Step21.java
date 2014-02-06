package ax.bru.act.config.action1.steps;

import ax.bru.defs.Executable;
import ax.bru.java.Action;
import ax.bru.java.Step;
import ax.bru.java.SubAction;

/**
 * Created by alexbruckner on 26/01/2014
 */
@Action(name = "Action 3 (2-1)")
public class Step21 implements SubAction {

    @Step(order = 1, name = "3-1")
    public Executable step31() {
        return new Step31();
    }

    @Step(order = 2, name = "3-2")
    public Executable step32() {
        return new Step32();
    }

    @Step(order = 3, name = "3-3")
    public SubAction step33() {
        return new Step33();
    }

}
