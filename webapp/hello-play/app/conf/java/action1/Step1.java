package conf.java.action1;

import ax.bru.defs.Data;
import ax.bru.defs.Executable;
import ax.bru.java.Step;

/**
 * Created by alexbruckner on 26/01/2014
 */
@Step(name = "1-1")
public class Step1 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 1-1!!!");
        data.set("1", System.nanoTime());
    }
}
