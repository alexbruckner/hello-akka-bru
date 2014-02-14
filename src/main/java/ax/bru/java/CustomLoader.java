package ax.bru.java;

import ax.bru.defs.Executable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CustomLoader {

    public static Map<String, String> executables = new ConcurrentHashMap<>();

    public static List<ax.bru.defs.Action> loadConfigWith(ClassLoader additionalClassLoader) {
        return loadConfig(additionalClassLoader);
    }

    public static List<ax.bru.defs.Action> loadConfig(ClassLoader... additionalClassLoaders) {
        executables.clear();
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
        List<StepInfo> steps = inspectStepAnnotatedMethods(c);
        ax.bru.defs.Action actionDefinition = ax.bru.defs.Action.create(actionName);
        addStepsToActionDefinition(actionDefinition, steps);
        return actionDefinition;
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
                executables.put(ax.bru.defs.Action.getId(actionDefinition.name()) + "/" + ax.bru.defs.Action.getId(step.getName()), stepInstance.getClass().getName());
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
