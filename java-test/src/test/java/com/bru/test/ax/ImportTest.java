package com.bru.test.ax;

import ax.bru.act.ActionSystem;
import ax.bru.act.Result;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by alexbruckner on 23/01/2014
 */
public class ImportTest {

    @Test
    public void testConfigAction() {

        System.setProperty("ax.bru.config", "com.bru.test.ax.config");

        Result result = ActionSystem.performAndWait(5, "Java Config Action 1");

        Assert.assertNotNull(result);

        Map<Object, String> timeSorted = new TreeMap<>();

        for (Map.Entry<String, Object> entry : result) {
            timeSorted.put(entry.getValue(), entry.getKey());
        }

        Assert.assertEquals("[1, 2, 7, 3, 4, 5, 6, 8]", timeSorted.values().toString());

    }

}