// This is a generated file. Not intended for manual editing.
package com.neueda.jetbrains.plugin.graphdb.language.cypher.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface CypherCypherOption extends PsiElement {

  @NotNull
  List<CypherConfigurationOption> getConfigurationOptionList();

  @Nullable
  CypherVersionNumber getVersionNumber();

  @NotNull
  PsiElement getKCypher();

}
