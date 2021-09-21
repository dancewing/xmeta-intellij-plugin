package io.xmeta.api.data;

import java.util.List;
import java.util.Map;

public interface MetaEntity extends NoIdEntity, IDNameData {

    String getDisplayName();

    String getPluralDisplayName();

    Map<String, Object> getProperties();

    List<String> getTypes();

    String getTypesName();

    Integer getVersionNumber();

    List<EntityField> getFields();

}
