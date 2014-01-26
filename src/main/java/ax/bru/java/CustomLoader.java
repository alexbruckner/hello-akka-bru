package ax.bru.java;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class CustomLoader {


    public static List<ax.bru.defs.Action> loadConfig(final String packageName) {
        List<ax.bru.defs.Action> actions = new ArrayList<>();
        try {
            for (Class<?> c : getClasses(packageName)) {
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

    private static ax.bru.defs.Action build(Class<?> c) {
        Action action = c.getAnnotation(Action.class);
        String actionName = action.name();
        Class<?>[] steps = action.steps();
        ax.bru.defs.Action actionDefinition = ax.bru.defs.Action.create(actionName);
        addStepsToActionDefinition(actionDefinition, steps);
        return actionDefinition;
    }

    private static void addStepsToActionDefinition(ax.bru.defs.Action actionDefinition, Class<?>[] steps) {
        System.out.println("!!!!!!!!!!!!!: " + actionDefinition + "---" + Arrays.toString(steps));
    }


    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static Class<?>[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }


}
