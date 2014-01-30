package ax.bru.java;

import ax.bru.defs.Executable;

import java.util.ArrayList;
import java.util.List;

public class CustomLoader {

    public static List<ax.bru.defs.Action> loadConfig() {
        List<ax.bru.defs.Action> actions = new ArrayList<>();
        try {
            for (Class<?> c : Classes.listAnnotatedClasses(Action.class)) {
                Action action = c.getAnnotation(Action.class);
                if (action != null && action.main()) {
                    actions.add(build(c));
                }
            }
        } catch (Exception e){
            e.printStackTrace(); //TODO logging
        }
        return actions;
    }

    private static ax.bru.defs.Action build(Class<?> c) throws InstantiationException, IllegalAccessException {
        Action action = c.getAnnotation(Action.class);
        String actionName = action.name();
        Class<?>[] steps = action.steps();
        ax.bru.defs.Action actionDefinition = ax.bru.defs.Action.create(actionName);
        addStepsToActionDefinition(actionDefinition, steps);
        return actionDefinition;
    }

    private static void addStepsToActionDefinition(ax.bru.defs.Action actionDefinition, Class<?>[] steps) throws IllegalAccessException, InstantiationException {
        for (Class<?> step : steps) {
            Action action = step.getAnnotation(Action.class);
            if (action != null) {
                ax.bru.defs.Action actionDefinition2 = actionDefinition.addStep(action.name()).setAction(action.name(), action.parallel());
                addStepsToActionDefinition(actionDefinition2, action.steps());
            } else {
                Step stepAnnot = step.getAnnotation(Step.class);
                actionDefinition.addStep(stepAnnot.name()).setExecutable((Executable)step.newInstance());
            }
        }
    }

}
