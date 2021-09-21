package io.xmeta.api;

import io.xmeta.api.data.Workspace;
import io.xmeta.api.query.GraphQueryResult;

import java.util.List;
import java.util.Map;

public interface GraphDatabaseApi {

    GraphQueryResult execute(String query);

    GraphQueryResult execute(String query, Map<String, Object> statementParameters);

    List<Workspace> metadata();

    default String getToken() throws Exception {return null;}
}
