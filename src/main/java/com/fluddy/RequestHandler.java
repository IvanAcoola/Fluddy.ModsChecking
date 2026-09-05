package com.fluddy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.codec.digest.DigestUtils;
import org.fusesource.jansi.Ansi;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

class RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        InputStreamReader isr = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);

        int b;
        StringBuilder buf = new StringBuilder(512);
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }

        br.close();
        isr.close();

        JSONObject obj = new JSONObject(buf.toString());

        byte[] decodedBytes = Base64.getDecoder().decode(obj.getString("data"));
        String checksum = DigestUtils.md5Hex(decodedBytes);

        System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a("[~]").reset().a(" Got bytes -> ").a( decodedBytes.length));

        JarParser jparser = new JarParser(checksum, decodedBytes);
        jparser.analyzeJar();
        JSONObject result = jparser.getFinalResult();

        String response = result.toString();
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
