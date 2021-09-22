package io.xmeta.generator.model;

/**
 *  列配置类型
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public enum UIConfigType {
    SingleLineText("SingleLineText"),
    MultiLineText("MultiLineText"),
    Email("Email"),
    WholeNumber("WholeNumber"),
    DateTime("DateTime"),
    DecimalNumber("DecimalNumber"),
    Lookup("Lookup"),
    MultiSelectOptionSet("MultiSelectOptionSet"),
    OptionSet("OptionSet"),
    Boolean("Boolean"),
    GeographicLocation("GeographicLocation"),
    Id("Id"),
    CreatedAt("CreatedAt"),
    UpdatedAt("UpdatedAt"),
    Roles("Roles"),
    Username("Username"),
    Password("Password");

    private final String graphqlName;

    private UIConfigType(String graphqlName) {
        this.graphqlName = graphqlName;
    }

    @Override
    public String toString() {
        return this.graphqlName;
    }
}
