package ax.bru.act;

import ax.bru.act.cases.Message;
import ax.bru.defs.Action;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by alexbruckner on 23/01/2014
 */
public class ActionTest {


    @Test
    public void test() {

        Action action = ExampleAction.action();

//        action.execute();

    // now with extra action system!

        ActionSystem.addAction(action);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Message message = ActionSystem.performAndWait(5, action.name());

        Assert.assertNotNull(message);

    }


}
