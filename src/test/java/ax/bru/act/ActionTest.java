package ax.bru.act;

import ax.bru.defs.Action;
import ax.bru.java.CustomLoader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by alexbruckner on 23/01/2014
 */
public class ActionTest {


    @Test
    public void test() {

        Action action = ExampleJavaAction.action;

        ActionSystem.addAction(action);

        Result result = ActionSystem.performAndWait(5, action.name());

        Assert.assertNotNull(result);

        Map<Object, String> timeSorted = new TreeMap<>();


        for (Map.Entry<String, Object> entry : result) {
            timeSorted.put(entry.getValue(), entry.getKey());
        }

        Assert.assertEquals("[1, 2, 7, 3, 4, 5, 6, 8]", timeSorted.values().toString());


    }

    @Test
    // run with -Dax.bru.config=ax.bru.act.config
    public void testConfigAction() {


        Map<String, Object> init = new HashMap<>();
        init.put("0", 0L);

        Result result = ActionSystem.performAndWait(5, "Java Config Action 1", init);

        Assert.assertNotNull(result);

        Map<Object, String> timeSorted = new TreeMap<>();


        for (Map.Entry<String, Object> entry : result) {
            timeSorted.put(entry.getValue(), entry.getKey());
        }

        Assert.assertEquals("[0, 1, 2, 7, 3, 4, 5, 6, 8]", timeSorted.values().toString());


    }


}
