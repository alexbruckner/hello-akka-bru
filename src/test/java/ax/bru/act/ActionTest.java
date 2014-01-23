package ax.bru.act;

import ax.bru.defs.Action;
import org.junit.Test;

/**
 * Created by alexbruckner on 23/01/2014
 */
public class ActionTest {


    @Test
    public void test() {

        Action action = ExampleJavaAction.action;

        action.execute();


    }


}
