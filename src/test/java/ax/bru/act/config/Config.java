package ax.bru.act.config;

import ax.bru.act.ExampleJavaAction;
import ax.bru.defs.Action;
import ax.bru.java.ActionConfig;
import ax.bru.java.ActionDef;

/**
 * Created by alexbruckner on 24/01/2014
 */
@ActionConfig
public class Config {

    @ActionDef
    public Action exampleAction() {
        return ExampleJavaAction.configAction;
    }

}
