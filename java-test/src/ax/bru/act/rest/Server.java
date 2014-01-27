package ax.bru.act.rest;

import ax.bru.act.ActionSystem;
import ax.bru.act.Result;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexbruckner on 27/01/2014
 */
public class Server {

    private HttpServer server = null;

    //http://localhost:8080/{"action":"Java Config Action 1", "data": {"test":"string", "value": 1}}
    public Server(int port, String configPkg) {
        System.setProperty("ax.bru.config", configPkg);

        // create an instance of the lightweight HTTP server on port 9097
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // assign a handler to "/oauth" context
        server.createContext("/", new HttpHandler() {
            public void handle(HttpExchange t) throws IOException {
                // send some descriptive page as a response
                t.sendResponseHeaders(200, 0);

                String action = URLDecoder.decode(t.getRequestURI().toString(), "UTF8").substring(1);

                if (!"favicon.ico".equals(action)) {

                    String json = "?";

                    try {
                        JSONObject object = (JSONObject) new JSONParser().parse(action);
                        String parsedAction = object.get("action").toString();
                        Map data = (Map) object.get("data");

                        System.out.println("action: " + parsedAction);
                        System.out.println("map: " + data);

                        Result result = ActionSystem.performAndWait(5L, parsedAction, data);

                        Map<String, Object> resultMap = new HashMap<>();

                        for (Map.Entry<String, Object> e : result) {
                           resultMap.put(e.getKey(), e.getValue());
                        }

                        json = JSONObject.toJSONString(resultMap);

                    } catch (ParseException e) {
                        System.err.println("Cannot parse: " + action);
                    }
                    OutputStream os = t.getResponseBody();
                    os.write(json.getBytes());
                    os.close();
                }


            }
        });
        server.setExecutor(null); // creates a default executor
    }

    public Server start() {
        server.start();
        return this;
    }

    public void stop() {
        server.stop(1);
    }

}
