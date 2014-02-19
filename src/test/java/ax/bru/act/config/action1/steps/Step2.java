package ax.bru.act.config.action1.steps;

import ax.bru.defs.Data;
import ax.bru.defs.Executable;
import ax.bru.java.strict.Description;
import ax.bru.java.strict.Gets;
import ax.bru.java.strict.Sets;

/**
 * Created by alexbruckner on 26/01/2014
 */
@Description("Step 2 prints the step 1 results and sets its own to \"2\"") // if strict: must be provided
@Sets({"2"}) // if strict: must be empty when set
@Gets({"1"}) // if strict: must exist when got
public class Step2 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 1-2!!!");
        data.set("2", System.nanoTime());
        System.out.println("getting field 1 set as: " + data.get("1"));
    }
}
