package io.xmeta.jetbrains.ui.datasource.tree;

public enum Neo4jTreeNodeType implements NodeType {
    ROOT,
    DATASOURCE,
    INDEXES,
    INDEX,
    CONSTRAINTS,
    CONSTRAINT,
    LABELS,
    LABEL,
    RELATIONSHIPS,
    RELATIONSHIP,
    PROPERTY_KEYS,
    PROPERTY_KEY,
    STORED_PROCEDURES,
    STORED_PROCEDURE,
    USER_FUNCTIONS,
    WORKSPACE,
    APP,
    ENTITY,
    USER_FUNCTION;

    Neo4jTreeNodeType() {
    }

}
