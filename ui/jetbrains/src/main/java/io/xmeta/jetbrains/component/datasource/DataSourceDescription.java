package io.xmeta.jetbrains.component.datasource;

import icons.MetaIcons;

import javax.swing.*;

public interface DataSourceDescription {

    DataSourceType getType();

    String geTypeName();

    Icon getIcon();

    String getDefaultFileExtension();

    DataSourceDescription NEO4J_BOLT = new DataSourceDescription() {
        @Override
        public DataSourceType getType() {
            return DataSourceType.NEO4J_BOLT;
        }

        @Override
        public Icon getIcon() {
            return MetaIcons.Database.NEO4J;
        }

        @Override
        public String getDefaultFileExtension() {
            return "cypher";
        }

        @Override
        public String geTypeName() {
            return "Neo4j - Bolt";
        }
    };

    DataSourceDescription OPENCYPHER_GREMLIN = new DataSourceDescription() {
        @Override
        public DataSourceType getType() {
            return DataSourceType.OPENCYPHER_GREMLIN;
        }

        @Override
        public Icon getIcon() {
            return MetaIcons.Database.OPENCYPHER;
        }

        @Override
        public String getDefaultFileExtension() {
            return "cypher";
        }

        @Override
        public String geTypeName() {
            return "openCypher - Gremlin";
        }
    };
}
