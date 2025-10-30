package com.puppet.sample;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import spark.utils.IOUtils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestRoutes {

    private static int port;

    @BeforeClass
    public static void beforeClass() throws Exception {
        Spark.port(0); // dynamically assign free port
        App.main(null); // start your app
        Spark.awaitInitialization(); // wait for it to start
        port = Spark.port();
        System.out.println("✅ Spark started on port: " + port);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        Spark.stop();
        Thread.sleep(1000); // wait a bit before JVM exits
        System.out.println("🛑 Spark stopped.");
    }

    @Test
    public void testEnMsg() throws IOException {
        TestResponse res = request("GET", "/");
        assertEquals(200, res.status);
        assertTrue(res.body.contains("Hello!!! My version is 1.0 and I am built from Develop branch"));
    }

    private TestResponse request(String method, String path) throws IOException {
        try {
            URL url = new URL("http://localhost:" + port + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.connect();
            String body = IOUtils.toString(connection.getInputStream());
            return new TestResponse(connection.getResponseCode(), body);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Sending request failed: " + e.getMessage());
            return null;
        }
    }

    private static class TestResponse {
        public final String body;
        public final int status;

        public TestResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }
    }
}
