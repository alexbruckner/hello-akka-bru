package ax.bru.act.config.action1.steps;

import ax.bru.defs.Data;
import ax.bru.defs.Executable;
import ax.bru.java.Step;

/**
 * Created by alexbruckner on 26/01/2014
 */
public class Step32 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 3-2!!!");
        data.set("4", System.nanoTime());
    }
}
