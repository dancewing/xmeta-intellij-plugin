package io.xmeta.api.data;

import java.util.List;

public interface MetaWorkspace extends IDNameData {
    List<MetaApp> getApps();
}
