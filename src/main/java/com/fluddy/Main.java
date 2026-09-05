package com.fluddy;

import com.sun.net.httpserver.HttpServer;
import org.apache.commons.codec.digest.DigestUtils;
import org.fusesource.jansi.Ansi;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {
    private static boolean _DEBUG = true;

    public static void main(String[] args) throws IOException {

        if (!_DEBUG) {
            InetAddress localHost = InetAddress.getLoopbackAddress();
            InetSocketAddress sockAddr = new InetSocketAddress(localHost, 1337);
            HttpServer server = HttpServer.create(sockAddr, 0);
            server.createContext("/checkout", new RequestHandler());
            server.setExecutor(null);
            server.start();
        }

        if (_DEBUG) {
            Path path = Paths.get("files/bypass.jar");
            if (!Files.exists(path)) {
                System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a("[~]").reset().a(" File does not exists "));
                return;
            }
            System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a("[~]").reset().a(" Started analyze for -> ").a(path));

            byte[] decodedBytes = Files.readAllBytes(path);
            String checksum = DigestUtils.md5Hex(decodedBytes);

            JarParser jparser = new JarParser(checksum, decodedBytes);
            jparser.analyzeJar();
            JSONObject result = jparser.getFinalResult();
            System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a("[~]").reset().a(" Output result -> ").a(result));
        }
    }
}