package io.xmeta.api;

import io.xmeta.api.data.MetaWorkspace;
import io.xmeta.api.query.MetaQueryResult;

import java.util.List;
import java.util.Map;

public interface MetaDatabaseApi {

    MetaQueryResult execute(String query);

    MetaQueryResult execute(String query, Map<String, Object> statementParameters);

    List<MetaWorkspace> metadata();

    default String getToken() throws Exception {return null;}
}
