package io.xmeta.impl;

import java.util.HashMap;
import java.util.Map;

public class OpenCypherGremlinConfiguration {

    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String FLAVOR = "flavor";
    public static final String SERIALIZER = "serializer";
    public static final String SSL = "ssl";

    private final Map<String, String> configuration;

    public OpenCypherGremlinConfiguration(Map<String, String> configuration) {
        this.configuration = new HashMap<>(configuration);
    }

    public OpenCypherGremlinConfiguration() {
        this.configuration = new HashMap<>();
    }

    public String getHost() {
        return configuration.get(HOST);
    }

    public Integer getPort() {
        String port = configuration.getOrDefault(PORT, "8080");
        if ("".equals(port)) {
            return 0;
        } else {
            return Integer.valueOf(port);
        }
    }

    public String getUser() {
        return configuration.get(USER);
    }

    public String getPassword() {
        return configuration.get(PASSWORD);
    }

    public boolean getSSL() {
        return Boolean.parseBoolean(configuration.get(SSL));
    }

    public void setHost(String host) {
        configuration.put(HOST, host);
    }

    public void setPort(String port) {
        configuration.put(PORT, port);
    }

    public void setUser(String user) {
        configuration.put(USER, user);
    }

    public void setPassword(String password) {
        configuration.put(PASSWORD, password);
    }

    public void setSSL(boolean ssl) {
        configuration.put(SSL, String.valueOf(ssl));
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }
}
