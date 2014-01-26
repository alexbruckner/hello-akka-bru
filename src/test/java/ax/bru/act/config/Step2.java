package ax.bru.act.config;

import ax.bru.defs.Data;
import ax.bru.defs.Executable;
import ax.bru.java.Step;

/**
 * Created by alexbruckner on 26/01/2014
 */
@Step(name = "1-2")
public class Step2 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 1-2!!!");
        data.set("2", System.nanoTime());
    }
}
