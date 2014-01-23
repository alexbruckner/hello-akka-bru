package com.bru.test.ax;

import org.junit.Test;

import ax.bru.defs.Action;
import scala.Function1;
import scala.runtime.BoxedUnit;

/**
 * Created by alexbruckner on 23/01/2014
 */
public class ImportTest {

    @Test
    public void testImport(){
        Action a = Action.apply("test");
        a.addStep("step 1").setExecutable()

        System.out.println(a);
    }

}
