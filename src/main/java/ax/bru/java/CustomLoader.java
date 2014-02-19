package ax.bru.java;

import ax.bru.defs.Executable;
import ax.bru.java.strict.Description;
import ax.bru.java.strict.Gets;
import ax.bru.java.strict.Sets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CustomLoader {

    public static Map<String, String> executables = new ConcurrentHashMap<>();
    public static Map<String, String> descriptions = new ConcurrentHashMap<>();

    private static volatile boolean STRICT;

    public static void setStrict(boolean strict) {
        CustomLoader.STRICT = strict;
    }

    public static List<ax.bru.defs.Action> loadConfigWith(ClassLoader additionalClassLoader) {
        return loadConfig(additionalClassLoader);
    }

    private static void clear() {
        executables.clear();
        descriptions.clear();
    }

    public static List<ax.bru.defs.Action> loadConfig(ClassLoader... additionalClassLoaders) {
        clear();
        List<ax.bru.defs.Action> actions = new ArrayList<>();
        try {
            for (Class<?> c : Classes.listAnnotatedClasses(Action.class, additionalClassLoaders)) {
                Action action = c.getAnnotation(Action.class);
                if (action != null && !Arrays.asList(c.getInterfaces()).contains(SubAction.class)) {
                    actions.add(build(c));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); //TODO logging
        }
        return actions;
    }

    private static ax.bru.defs.Action build(Class<?> c) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Action action = c.getAnnotation(Action.class);
        String actionName = action.name();
        String id = ax.bru.defs.Action.getId(actionName);
        checkStrict(c, id);
        List<StepInfo> steps = inspectStepAnnotatedMethods(c);
        ax.bru.defs.Action actionDefinition = ax.bru.defs.Action.create(actionName);
        addStepsToActionDefinition(actionDefinition, steps);
        return actionDefinition;
    }

    private static void checkStrict(Class<?> c, String id) {
        Description desc = c.getAnnotation(Description.class);
        boolean isAction = c.isAnnotationPresent(Action.class);
        if (desc == null && STRICT) {
            //TODO throw exception!
            System.err.println(String.format("The %s %s must have a Description annotation in STRICT mode", isAction ? "main action" : "executable", c.getName()));
        } else if (desc != null) {
            descriptions.put(id, desc.value());
        }

        // also want Gets and Sets annotations for executables //TODO clean up
        if (!isAction && STRICT) {
            if (!c.isAnnotationPresent(Gets.class)) {
                System.err.println(String.format("The executable %s must have a Gets annotation in STRICT mode", c.getName()));
            }
            if (!c.isAnnotationPresent(Sets.class)) {
                System.err.println(String.format("The executable %s must have a Sets annotation in STRICT mode", c.getName()));
            }
        }
    }

    private static void addStepsToActionDefinition(ax.bru.defs.Action actionDefinition, List<StepInfo> steps) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        for (StepInfo step : steps) {
            if (step.getType() == SubAction.class) {
                Action stepAction = step.getInstance().getClass().getAnnotation(Action.class);
                boolean parallel = false;
                String actionName = step.getName();
                if (stepAction != null) {
                    parallel = stepAction.parallel();
                    actionName = stepAction.name();
                }
                ax.bru.defs.Action actionDefinition2 = actionDefinition.addStep(step.getName()).setAction(actionName, parallel);
                addStepsToActionDefinition(actionDefinition2, inspectStepAnnotatedMethods(step.getInstance().getClass()));
            } else if (step.getType() == Executable.class) {
                Executable stepInstance = (Executable) step.getInstance();
                actionDefinition.addStep(step.getName()).setExecutable(stepInstance);
                String id = ax.bru.defs.Action.getId(actionDefinition.name()) + "/" + ax.bru.defs.Action.getId(step.getName());
                checkStrict(stepInstance.getClass(), id);
                executables.put(id, stepInstance.getClass().getName());
            }
        }
    }

    private static List<StepInfo> inspectStepAnnotatedMethods(Class<?> c) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        List<StepInfo> steps = new ArrayList<>();
        for (Method m : c.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Step.class)) {
                steps.add(new StepInfo(m.getAnnotation(Step.class).order(), m.getAnnotation(Step.class).name(), m.getReturnType(), m.invoke(c.newInstance())));
            }
        }

        Collections.sort(steps);

        return steps;
    }

    private static class StepInfo implements Comparable<StepInfo>{
        private int order;
        private String name;
        private Class<?> type;
        private Object instance;

        private StepInfo(int order, String name, Class<?> type, Object instance) {
            this.order = order;
            this.name = name;
            this.type = type;
            this.instance = instance;
        }

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            return type;
        }

        public Object getInstance() {
            return instance;
        }

        @Override
        public int compareTo(StepInfo o) {
            return order - o.order;
        }
    }

}
