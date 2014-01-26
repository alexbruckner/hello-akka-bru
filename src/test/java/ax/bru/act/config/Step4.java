package ax.bru.act.config;

import ax.bru.defs.Data;
import ax.bru.defs.Executable;
import ax.bru.java.Step;

/**
 * Created by alexbruckner on 26/01/2014
 */
@Step(name = "1-4")
public class Step4 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 1-4!!!");
        data.set("8", System.nanoTime());
    }
}
