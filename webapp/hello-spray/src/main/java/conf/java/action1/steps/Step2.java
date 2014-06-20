package conf.java.action1.steps;

import ax.bru.defs.Data;
import ax.bru.defs.Executable;

/**
 * Created by alexbruckner on 26/01/2014
 */
public class Step2 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 1-2!!!");
        data.set("2", System.nanoTime());
    }
}
