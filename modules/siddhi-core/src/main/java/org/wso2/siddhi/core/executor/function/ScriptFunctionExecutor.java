package org.wso2.siddhi.core.executor.function;

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.function.EvalScript;
import org.wso2.siddhi.query.api.definition.Attribute;

public class ScriptFunctionExecutor extends FunctionExecutor {

    static final Logger log = Logger.getLogger(ScriptFunctionExecutor.class);

    private String functionId;
    Attribute.Type returnType;
    EvalScript evalScript;

    public ScriptFunctionExecutor(String name) {
        this.functionId = name;
    }

    @Override
    public void init(Attribute.Type[] attributeTypes, SiddhiContext siddhiContext) {
        returnType = siddhiContext.getEvalScript(functionId).getReturnType();
        evalScript = siddhiContext.getEvalScript(functionId);
    }

    @Override
    protected Object process(Object data) {
        if(data instanceof Object[]) {
            return evalScript.eval(functionId,(Object[])data);
        } else {
            return evalScript.eval(functionId, new Object[]{data});
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public Attribute.Type getReturnType() {
        return returnType;
    }
}
