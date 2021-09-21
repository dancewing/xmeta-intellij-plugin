package io.xmeta.api.query;

import java.util.List;
import java.util.Map;

public interface MetaQueryPlan {

    String getOperatorType();

    Map<String, Object> getArguments();

    List<String> getIdentifiers();

    List<? extends MetaQueryPlan> children();

}
