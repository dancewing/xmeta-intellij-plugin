package io.xmeta.api.data;

import java.util.Map;

public interface EntityField extends IDNameData {

     String getPermanentId();

     String getDisplayName();

     String getDataType();

     Map<String, Object> getProperties();

     Boolean getRequired();

     Boolean getSearchable();

     String getDescription();

     Integer getPosition();

     Boolean getUnique();
}
