package ax.bru.act.config.action1.steps;

import ax.bru.defs.Data;
import ax.bru.defs.Executable;
import ax.bru.java.strict.Description;
import ax.bru.java.strict.Gets;
import ax.bru.java.strict.Sets;

/**
 * Created by alexbruckner on 26/01/2014
 */
@Description("Step 1 blibbly blobbly blubberly....") // if strict: must be provided
@Sets({Step1.field}) // if strict: must be empty when set
@Gets({}) // if strict: must exist when got
public class Step1 implements Executable {

    public static final String field = "1";

    @Override
    public void execute(Data data) {
        System.out.println("Step 1-1!!!");
        data.set(Step1.field, System.nanoTime());
    }
}
