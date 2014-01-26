package ax.bru.act.config;

import ax.bru.defs.Data;
import ax.bru.defs.Executable;
import ax.bru.java.Step;

/**
 * Created by alexbruckner on 26/01/2014
 */
@Step(name = "3-1")
public class Step31 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 3-1!!!");
        data.set("3", System.nanoTime());
    }
}
