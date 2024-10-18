package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.data.json.ItemConfig;
import com.jetug.chassis_core.common.data.json.ModelConfigBase;

public interface IConfigProvider<T extends ModelConfigBase> {
    T getConfig();
}
