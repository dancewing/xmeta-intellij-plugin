package io.xmeta.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.xmeta.api.MetaDatabaseApi;
import io.xmeta.api.data.MetaWorkspace;
import io.xmeta.api.query.MetaQueryResult;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.net.URI;
import java.util.*;
/**
 * Communicates with TinkerPop database by translating Cypher to Gremlin
 */
public class OpenCypherGremlinDatabase implements MetaDatabaseApi {

    private final OpenCypherGremlinConfiguration configuration;
    private final ObjectMapper objectMapper = new ObjectMapper();
    static {
        disableGremlinLog();
    }

    public OpenCypherGremlinDatabase(Map<String, String> configuration) {
        this(new OpenCypherGremlinConfiguration(configuration));
    }

    public OpenCypherGremlinDatabase(OpenCypherGremlinConfiguration configuration) {
        String host = configuration.getHost();
        Integer port = configuration.getPort();
        String username = configuration.getUser();
        String password = configuration.getPassword();
        String url = String.format("gremlin://%s:%s", host, port);
        URI uri = URI.create(url);

        this.configuration = configuration;

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    @Override
    public MetaQueryResult execute(String query) {
        return execute(query, Collections.emptyMap());
    }

    @Override
    public MetaQueryResult execute(String query, Map<String, Object> statementParameters) {
//        Client gremlinClient = cluster.connect();
//
//        try (CypherGremlinClient client = flavor.client(gremlinClient)) {
//            if (query.toUpperCase().startsWith("PROFILE")) {
//                return notSupported();
//            }
//
//            HashMap<String, Object> serializableMap = new HashMap<>(statementParameters);
//            long startTime = System.currentTimeMillis();
//            List<Map<String, Object>> result = client.submit(query, serializableMap).all();
//            long endTime = System.currentTimeMillis();
//
//            List<MetaQueryResultColumn> headers = getHeaders(result);
//
//            List<MetaQueryResultRow> rows = result.stream()
//                    .map(converter::toRecord)
//                    .collect(toList());
//
//            List<MetaNode> nodes = rows.stream().flatMap(e -> e.getNodes().stream()).distinct().collect(toList());
//            List<EntityRelationship> relationships = rows.stream().flatMap(e -> e.getRelationships().stream()).distinct().collect(toList());
//
//            return new OpenCypherGremlinQueryResult(endTime - startTime,
//                    headers,
//                    rows,
//                    nodes,
//                    relationships);
//        } catch (RuntimeException e) {
//            if (query.toUpperCase().startsWith("EXPLAIN")) {
//                return new OpenCypherGremlinQueryResult(0, emptyList(), emptyList(), emptyList(), emptyList());
//            } else {
//                String exceptionMessage = wrapExceptionInMeaningMessage(e);
//                throw new OpenCypherGremlinException(exceptionMessage, e);
//            }
//        }
        return null;
    }

//    private List<MetaQueryResultColumn> getHeaders(List<Map<String, Object>> result) {
//        if (result.isEmpty()) {
//            return emptyList();
//        } else {
//            return result.get(0).keySet().stream()
//                    .map(Neo4jBoltQueryResultColumn::new)
//                    .collect(toList());
//        }
//    }


    @SuppressWarnings("unchecked")
    private static void disableGremlinLog() {
        final String gremlinDriver = "org.apache.tinkerpop.gremlin.driver";

        Properties props = new Properties();
        props.setProperty("log4j.logger." + gremlinDriver, Level.OFF.toString());
        PropertyConfigurator.configure(props);

        Enumeration<Logger> loggers = LogManager.getCurrentLoggers();
        Collections.list(loggers)
                .stream()
                .filter(logger -> logger.getName().startsWith(gremlinDriver))
                .forEach(logger -> logger.setLevel(Level.OFF));
    }

    @Override
    public String getToken() throws Exception {
        String host = configuration.getHost();
        Integer port = configuration.getPort();
        String username = configuration.getUser();
        String password = configuration.getPassword();
        HttpPost httpPost = new HttpPost("http://" + host + ":"+port+"/api/login");
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        // 设置 Cookie
        httpPost.setHeader("Content-Type", "application/json");

        // 创建 HttpPost 参数
        Map<String, String> params = new HashMap<>();
        params.put("email", username);
        params.put("password", password);
        try {
            byte[] data = objectMapper.writeValueAsBytes(params);
            // 请求并获得响应结果
            httpPost.setEntity(new StringEntity(new String(data), "UTF-8"));
            httpResponse = httpClient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() >300 ) {
                throw new Exception("Server internal error");
            }
            HttpEntity httpEntity = httpResponse.getEntity();

            // 输出请求结果
            return EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } // 无论如何必须关闭连接
        finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<MetaWorkspace> metadata() {
        String token = "";
        try {
            token = getToken();
        } catch (Exception ex) {

        }
        String host = configuration.getHost();
        Integer port = configuration.getPort();
        // 创建 HttpClient 客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建 HttpGet 请求
        HttpGet httpGet = new HttpGet("http://"+host+":"+port+"/api/metadata");
        // 设置长连接
        httpGet.setHeader("Authorization", "Bearer " + token);
        // 设置代理（模拟浏览器版本）
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");

        CloseableHttpResponse httpResponse = null;

        try {
            // 请求并获得响应结果
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            // 输出请求结果
            List<MetaWorkspaceDomain> workspaceDomains = objectMapper.readValue(EntityUtils.toByteArray(httpEntity),
                    new TypeReference<List<MetaWorkspaceDomain>>() {
            });

            List<MetaWorkspace> metaWorkspaces = new ArrayList<>();
            workspaceDomains.stream().forEach(workspaceDomain -> metaWorkspaces.add(workspaceDomain));

            return metaWorkspaces;

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 无论如何必须关闭连接
        finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return Collections.emptyList();
    }
}
