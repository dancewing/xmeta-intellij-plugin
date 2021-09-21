package io.xmeta.jetbrains.ui.helpers;

import com.intellij.ui.treeStructure.PatchedDefaultMutableTreeNode;
import io.xmeta.api.data.*;
import io.xmeta.api.data.MetaNode;
import io.xmeta.api.data.MetaEntity;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.ui.datasource.tree.TreeNodeModelApi;
import io.xmeta.jetbrains.ui.datasource.tree.model.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

public final class UiHelper {

    public static final int INLINE_TEXT_LENGTH = 80;
    public static final String MAP = "map";
    public static final String LIST = "list";
    public static final String TEXT = "text";
    public static final String PATH = "path";
    public static final String RELATIONSHIP = "relationship";
    public static final String NODE = "node";
    public static final String PROPERTIES = "properties";
    public static final String LABELS = "labels";
    public static final String ID = "id";

    private UiHelper() {
    }

    public static boolean canBeTree(Object object) {
        return object instanceof List
                || object instanceof Map
                || object instanceof MetaNode
                || object instanceof EntityRelationship
                || object instanceof EntityPath;
    }

    public static PatchedDefaultMutableTreeNode keyValueToTreeNode(String key, Object value, DataSource dataSourceApi, Object rootObject) {
        if (value instanceof List) {
            return listToTreeNode(key, (List) value, dataSourceApi, rootObject);
        }
        if (value instanceof Map) {
            return mapToTreeNode(key, (Map) value, dataSourceApi, rootObject);
        }
        if (value instanceof MetaNode) {
            return nodeToTreeNode(key, (MetaNode) value, dataSourceApi);
        }
        if (value instanceof EntityRelationship) {
            return relationshipToTreeNode(key, (EntityRelationship) value, dataSourceApi);
        }
        if (value instanceof EntityPath) {
            return pathToTreeNode(key, (EntityPath) value, dataSourceApi, rootObject);
        }
        return objectToTreeNode(key, value, dataSourceApi, rootObject);
    }

    public static PatchedDefaultMutableTreeNode nodeToTreeNode(String key, MetaNode node, DataSource dataSourceApi) {
        PatchedDefaultMutableTreeNode treeRoot = new PatchedDefaultMutableTreeNode(modelOf(node, key, NODE, dataSourceApi, node));
        addGraphEntityData(treeRoot, node, dataSourceApi);
        return treeRoot;
    }

    public static PatchedDefaultMutableTreeNode relationshipToTreeNode(String key, EntityRelationship relationship, DataSource dataSourceApi) {
        PatchedDefaultMutableTreeNode treeRoot = new PatchedDefaultMutableTreeNode(modelOf(relationship, key, RELATIONSHIP, dataSourceApi, relationship));

        addGraphEntityData(treeRoot, relationship, dataSourceApi);

        treeRoot.add(objectToTreeNode("startNodeId", relationship.getStartNodeId(), dataSourceApi, relationship));
        if (relationship.getStartNode() != null) {
            treeRoot.add(nodeToTreeNode("startNode", relationship.getStartNode(), dataSourceApi));
        }
        treeRoot.add(objectToTreeNode("endNodeId", relationship.getEndNodeId(), dataSourceApi, relationship));
        if (relationship.getEndNode() != null) {
            treeRoot.add(nodeToTreeNode("endNode", relationship.getEndNode(), dataSourceApi));
        }
        return treeRoot;
    }

    private static void addGraphEntityData(PatchedDefaultMutableTreeNode treeRoot, MetaEntity metaEntity, DataSource dataSourceApi) {
        treeRoot.add(objectToTreeNode(ID, metaEntity.getId(), dataSourceApi, metaEntity));
        if (metaEntity.isTypesSingle()) {
            treeRoot.add(objectToTreeNode(metaEntity.getTypesName(), metaEntity.getTypes().get(0), dataSourceApi, metaEntity));
        } else {
            treeRoot.add(listToTreeNode(metaEntity.getTypesName(), metaEntity.getTypes(), dataSourceApi, metaEntity));
        }
        treeRoot.add(mapToTreeNode(PROPERTIES, metaEntity.getProperties(), dataSourceApi, metaEntity));
    }

    private static PatchedDefaultMutableTreeNode pathToTreeNode(String key, EntityPath path, DataSource dataSourceApi, Object rootObject) {
        PatchedDefaultMutableTreeNode root = new PatchedDefaultMutableTreeNode(modelOf(null, key, PATH, dataSourceApi, rootObject));
        List<Object> components = path.getComponents();
        for (int i = 0; i < components.size(); i++) {
            root.add(keyValueToTreeNode(String.valueOf(i), components.get(i), dataSourceApi, rootObject));
        }
        return root;
    }

    private static PatchedDefaultMutableTreeNode objectToTreeNode(String key, Object value, DataSource dataSourceApi, Object rootObject) {
        if (value instanceof String) {
            String string = (String) value;
            if (string.length() <= INLINE_TEXT_LENGTH) {
                return new PatchedDefaultMutableTreeNode(modelOf(representUiString(string), key, null, dataSourceApi, rootObject));
            } else {
                PatchedDefaultMutableTreeNode parent = new PatchedDefaultMutableTreeNode(modelOf(null, key, TEXT, dataSourceApi, rootObject));
                parent.add(new PatchedDefaultMutableTreeNode(string));
                return parent;
            }
        }
        return new PatchedDefaultMutableTreeNode(modelOf(Objects.toString(value), key, null, dataSourceApi, rootObject));
    }

    private static PatchedDefaultMutableTreeNode listToTreeNode(String key, List list, DataSource dataSourceApi, Object rootObject) {
        PatchedDefaultMutableTreeNode node;
        if (LABELS.equals(key)) {
            node = new PatchedDefaultMutableTreeNode(new LabelsModel(dataSourceApi, rootObject));
        } else {
            node = new PatchedDefaultMutableTreeNode(modelOf(null, key, LIST, dataSourceApi, rootObject));
        }

        for (int i = 0; i < list.size(); i++) {
            node.add(keyValueToTreeNode(String.valueOf(i), list.get(i), dataSourceApi, rootObject));
        }
        return node;
    }

    private static PatchedDefaultMutableTreeNode mapToTreeNode(String key, Map map, DataSource dataSourceApi, Object rootObject) {
        PatchedDefaultMutableTreeNode node;
        if (PROPERTIES.equals(key)) {
            node = new PatchedDefaultMutableTreeNode(new PropertiesModel(dataSourceApi, rootObject));
        } else {
            node = new PatchedDefaultMutableTreeNode(modelOf(null, key, MAP, dataSourceApi, rootObject));
        }

        map.forEach((mapKey, mapValue) -> node.add(keyValueToTreeNode(mapKey.toString(), mapValue, dataSourceApi, rootObject)));
        return node;
    }

    private static TreeNodeModelApi modelOf(Object value, String key, String description, DataSource dataSourceApi, Object rootObject) {
        if (value instanceof MetaNode) {
            return new NodeModel((MetaNode) value, key, dataSourceApi);
        } else if (value instanceof EntityRelationship) {
            return new RelationshipModel((EntityRelationship) value, key, dataSourceApi);
        } else if (value instanceof List) {
            return new ListModel(key, dataSourceApi);
        } else if (value instanceof Map) {
            return new MapModel(key, dataSourceApi);
        } else {
            return new ObjectModel(value, key, description, dataSourceApi, rootObject);
        }
    }

    public static String representUiString(String value) {
        return format("\"%s\"", Objects.toString(value));
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> cast(Object o, Class<T> clazz) {
        if (clazz.isInstance(o)) {
            return Optional.of((T) o);
        } else {
            return Optional.empty();
        }
    }
}
