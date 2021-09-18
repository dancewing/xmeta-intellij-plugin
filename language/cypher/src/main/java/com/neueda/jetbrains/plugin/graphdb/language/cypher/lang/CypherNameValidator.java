package com.neueda.jetbrains.plugin.graphdb.language.cypher.lang;

import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.openapi.project.Project;
import com.neueda.jetbrains.plugin.graphdb.language.cypher.completion.metadata.atoms.CypherKeywords;
import org.jetbrains.annotations.NotNull;

public class CypherNameValidator implements NamesValidator {

    @Override
    public boolean isKeyword(@NotNull String name, Project project) {
        return CypherKeywords.KEYWORDS.contains(name.toLowerCase());
    }

    @Override
    public boolean isIdentifier(@NotNull String name, Project project) {
        return !isKeyword(name, project) && name.matches(CypherRegexp.SYMBOLIC_NAME_REGEXP);
    }
}
