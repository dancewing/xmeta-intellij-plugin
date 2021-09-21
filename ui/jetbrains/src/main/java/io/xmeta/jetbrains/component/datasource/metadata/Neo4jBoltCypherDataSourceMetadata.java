package io.xmeta.jetbrains.component.datasource.metadata;

import io.xmeta.api.data.MetaWorkspace;
import io.xmeta.api.query.MetaQueryResult;
import io.xmeta.api.query.MetaQueryResultColumn;
import io.xmeta.api.query.MetaQueryResultRow;

import java.util.*;

public class Neo4jBoltCypherDataSourceMetadata implements DataSourceMetadata {

    public static final String INDEXES = "indexes";
    public static final String CONSTRAINTS = "constraints";
    public static final String PROPERTY_KEYS = "propertyKeys";
    public static final String STORED_PROCEDURES = "procedures";
    public static final String USER_FUNCTIONS = "functions";
    private List<MetaWorkspace> metaWorkspaces;

    private Map<String, List<Map<String, String>>> dataReceiver = new HashMap<>();

    private List<Neo4jLabelMetadata> labels = new ArrayList<>();
    private List<Neo4jRelationshipTypeMetadata> relationshipTypes = new ArrayList<>();

    @Override
    public List<Map<String, String>> getMetadata(String metadataKey) {
        return dataReceiver.getOrDefault(metadataKey, new ArrayList<>());
    }

    @Override
    public boolean isMetadataExists(final String metadataKey) {
        return dataReceiver.containsKey(metadataKey);
    }

    public void addPropertyKeys(MetaQueryResult propertyKeysResult) {
        addDataSourceMetadata(PROPERTY_KEYS, propertyKeysResult);
    }

    public void addStoredProcedures(MetaQueryResult storedProceduresResult) {
        addDataSourceMetadata(STORED_PROCEDURES, storedProceduresResult);
    }

    public void addUserFunctions(MetaQueryResult userFunctionsResult) {
        addDataSourceMetadata(USER_FUNCTIONS, userFunctionsResult);
    }

    private void addDataSourceMetadata(String key, MetaQueryResult metaQueryResult) {
        List<Map<String, String>> dataSourceMetadata = new ArrayList<>();

        List<MetaQueryResultColumn> columns = metaQueryResult.getColumns();
        for (MetaQueryResultRow row : metaQueryResult.getRows()) {
            Map<String, String> data = new HashMap<>();

            for (MetaQueryResultColumn column : columns) {
                Object value = row.getValue(column);
                if (value != null) {
                    data.put(column.getName(), value.toString());
                }
            }

            dataSourceMetadata.add(data);
        }

        dataReceiver.put(key, dataSourceMetadata);
    }

    public void addDataSourceMetadata(String key, List<Map<String, String>> data) {
        dataReceiver.put(key, data);
    }

    public void addLabels(MetaQueryResult labelCountResult, List<String> labelNames) {
        MetaQueryResultColumn column = labelCountResult.getColumns().get(0);
        for (int i = 0; i < labelCountResult.getRows().size(); i++) {
            MetaQueryResultRow row = labelCountResult.getRows().get(i);
            labels.add(new Neo4jLabelMetadata(labelNames.get(i), (Long) row.getValue(column)));
        }
    }

    public void addLabel(Neo4jLabelMetadata labelMetadata) {
        labels.add(labelMetadata);
    }

    public List<Neo4jLabelMetadata> getLabels() {
        return labels;
    }

    public void addRelationshipTypes(MetaQueryResult relationshipTypeCountResult, List<String> relationshipTypeNames) {
        MetaQueryResultColumn column = relationshipTypeCountResult.getColumns().get(0);
        for (int i = 0; i < relationshipTypeCountResult.getRows().size(); i++) {
            MetaQueryResultRow row = relationshipTypeCountResult.getRows().get(i);
            relationshipTypes.add(new Neo4jRelationshipTypeMetadata(relationshipTypeNames.get(i), (Long) row.getValue(column)));
        }
    }

    public void addRelationshipType(Neo4jRelationshipTypeMetadata relationshipTypeMetadata) {
        relationshipTypes.add(relationshipTypeMetadata);
    }

    public void addPropertyKey(String key) {
        List<Map<String, String>> propertyKeys = getMetadata(PROPERTY_KEYS);
        propertyKeys.add(Collections.singletonMap("propertyKey", key));
        dataReceiver.put(PROPERTY_KEYS, propertyKeys);
    }

    public List<Neo4jRelationshipTypeMetadata> getRelationshipTypes() {
        return relationshipTypes;
    }

    public void addIndexes(MetaQueryResult indexesResult) {
        addDataSourceMetadata(INDEXES, indexesResult);
    }

    public void addConstraints(MetaQueryResult constraintsResult) {
        addDataSourceMetadata(CONSTRAINTS, constraintsResult);
    }

    public void setWorkspaces(List<MetaWorkspace> metaWorkspaces) {
        this.metaWorkspaces = metaWorkspaces;
    }

    @Override
    public List<MetaWorkspace> getWorkspaces() {
        return this.metaWorkspaces;
    }
}
