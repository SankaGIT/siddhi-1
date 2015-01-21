package org.wso2.siddhi.core.util.parser;

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.exception.FunctionAlreadyExistException;
import org.wso2.siddhi.core.extension.holder.EvalScriptExtensionHolder;
import org.wso2.siddhi.core.function.EvalScript;
import org.wso2.siddhi.core.util.SiddhiClassLoader;
import org.wso2.siddhi.query.api.definition.FunctionDefinition;
import org.wso2.siddhi.query.api.extension.Extension;

public class FunctionParser {
    static final Logger log = Logger.getLogger(FunctionParser.class);

    public static void addFunction(SiddhiContext siddhiContext, final FunctionDefinition functionDefinition) {
        if(siddhiContext.isFunctionExist(functionDefinition.getFunctionID())) {
            throw new FunctionAlreadyExistException("The function "
                    + functionDefinition.getFunctionID() + " already been defined");
        }
        EvalScript evalScript = (EvalScript) SiddhiClassLoader.loadExtensionImplementation(
                    new Extension() {
                        @Override
                        public String getNamespace() {
                            return "evalscript";
                        }

                        @Override
                        public String getFunction() {
                            return functionDefinition.getLanguage().toLowerCase();
                        }
                    }, EvalScriptExtensionHolder.getInstance(siddhiContext));
        evalScript.init(functionDefinition.getFunctionID(), functionDefinition.getBody());
        evalScript.setReturnType(functionDefinition.getReturnType());
        siddhiContext.getScriptFunctionMap().put(functionDefinition.getFunctionID(),evalScript);
    }
}