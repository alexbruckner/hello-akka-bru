package ax.bru.act.config;

import ax.bru.defs.Data;
import ax.bru.defs.Executable;
import ax.bru.java.Action;
import ax.bru.java.Step;


/**
 * Created by alexbruckner on 26/01/2014
 */
@Action(name = "Java Config Action 1", main = true, steps = {Step1.class, Step2.class, Step3.class, Step4.class})
public class Action1 {}

