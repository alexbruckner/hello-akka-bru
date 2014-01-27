package com.bru.test.ax;

import ax.bru.act.ActionSystem;
import ax.bru.act.Result;
import ax.bru.act.rest.Server;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by alexbruckner on 23/01/2014
 */
public class ImportTest {

    private static Server server;

    @BeforeClass
    public static void init() {
        server = new Server(8080, "com.bru.test.ax.config").start();
    }

    @AfterClass
    public static void cleanup() {
        server.stop();
    }

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

    @Test
    public void testServer() throws ParseException {
        String result = curl("http://localhost:8080/%7B%22action%22:%22Java%20Config%20Action%201%22,%20%22data%22:%20%7B%22test%22:%22string%22,%20%22value%22:%201%7D%7D");
        JSONObject object = (JSONObject) new JSONParser().parse(result);
        Assert.assertNotNull(object);
        String string = (String) object.get("test");
        Assert.assertEquals("string", string);
    }

    private String curl(String urlString)  {
        StringWriter sw = new StringWriter();
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            for (String line; (line = reader.readLine()) != null;) {
                sw.write(line);
            }
        } catch (Exception e) {}
        finally {
            if (reader != null) try { reader.close(); } catch (IOException ignore) {}
        }
        sw.flush();
        return sw.toString();
    }



}
