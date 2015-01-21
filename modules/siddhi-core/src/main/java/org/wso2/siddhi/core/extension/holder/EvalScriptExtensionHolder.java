package org.wso2.siddhi.core.extension.holder;

import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.function.EvalScript;

public class EvalScriptExtensionHolder extends AbstractExtensionHolder {

    private static EvalScriptExtensionHolder instance;

    protected EvalScriptExtensionHolder(SiddhiContext siddhiContext) {
        super(EvalScript.class, siddhiContext);
    }

    public static EvalScriptExtensionHolder getInstance(SiddhiContext siddhiContext) {
        if (instance == null) {
            instance = new EvalScriptExtensionHolder(siddhiContext);
        }
        return instance;
    }
}
