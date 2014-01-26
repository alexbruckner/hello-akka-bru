package ax.bru.act.config;

import ax.bru.defs.Data;
import ax.bru.defs.Executable;
import ax.bru.java.Action;


/**
 * Created by alexbruckner on 26/01/2014
 */
@Action(name = "Java Config Action 1", main = true, steps = {Step1.class, Step2.class, Step3.class, Step4.class})
public class Action1 {}

class Step1 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 1-1!!!");
    }
}

class Step2 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 1-2!!!");
    }
}

@Action(name = "Action 2 (1-3)", steps = {Step21.class, Step22.class}, parallel = true)
class Step3  {}

@Action(name = "Action 3 (2-1)", steps = {Step31.class, Step32.class, Step33.class})
class Step21  {}

class Step22 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 2-2!!!");
    }
}

class Step31 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 3-1!!!");
    }
}

class Step32 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 3-2!!!");
    }
}

@Action(name = "Action 4 (3-3)", steps = {Step41.class, Step42.class}, parallel = true)
class Step33 {}

class Step41 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 4-1!!!");
    }
}

class Step42 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 4-2!!!");
    }
}

class Step4 implements Executable {
    @Override
    public void execute(Data data) {
        System.out.println("Step 1-4!!!");
    }
}
