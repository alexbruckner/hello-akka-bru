package ax.bru.act;

import ax.bru.defs.Action;
import ax.bru.defs.Data;
import ax.bru.defs.Executable;

/**
 * Created by alexbruckner on 23/01/2014
 */
public class ExampleJavaAction {

    public static final Action action = create("Java Action 1");
    public static final Action configAction = create("Java Config Action!!!");

    private static Action create(String name) {

        Action action = Action.create(name);

        action.addStep("1-1").setExecutable(new Executable() {
            @Override
            public void execute(Data data) {
                System.out.println("exec 1-1");
                data.set("1", System.nanoTime());
            }
        });

        action.addStep("1-2").setExecutable(new Executable() {
            @Override
            public void execute(Data data) {
                System.out.println("exec 1-2");
                data.set("2", System.nanoTime());
            }
        });

        Action action2 = action.addStep("1-3").setAction("Action 2 (1-3)", true);

        // last step
        action.addStep("1-4").setExecutable(new Executable() {
            @Override
            public void execute(Data data) {
                data.set("8", System.nanoTime());
                System.out.println("exec 1-4");
                System.out.format("message id: %s", data.dataId());
                System.out.format("message history: %s", data.history());
            }
        });

        //Action 2 (1-3)
        Action action3 = action2.addStep("2-1").setAction("Action 3 (2-1)");
        action2.addStep("2-2").setExecutable(new Executable() {
            @Override
            public void execute(Data data) {
                System.out.println("exec 2-2");
                data.set("7", System.nanoTime());
            }
        });

        //Action 3 (2-1)
        action3.addStep("3-1").setExecutable(new Executable() {
            @Override
            public void execute(Data data) {
                System.out.println("exec 3-1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                data.set("3", System.nanoTime());
            }
        });

        action3.addStep("3-2").setExecutable(new Executable() {
            @Override
            public void execute(Data data) {
                System.out.println("exec 3-2");
                data.set("4", System.nanoTime());
            }
        });

        Action action4 = action3.addStep("3-3").setAction("Action 4 (3-3)", true);

        //Action 4 (3-3)
        action4.addStep("4-1").setExecutable(new Executable() {
            @Override
            public void execute(Data data) {
                System.out.println("exec 4-1");
                data.set("5", System.nanoTime());
            }
        });

        action4.addStep("4-2").setExecutable(new Executable() {
            @Override
            public void execute(Data data) {
                System.out.println("exec 4-2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                data.set("6", System.nanoTime());
            }
        });

        return action;

    }


}
