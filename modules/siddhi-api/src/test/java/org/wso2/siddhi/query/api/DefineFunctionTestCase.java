package org.wso2.siddhi.query.api;

import org.junit.Test;
import org.wso2.siddhi.query.api.definition.FunctionDefinition;

public class DefineFunctionTestCase {

    @Test
    public void testCreatingFunctionDefinition() {
        FunctionDefinition functionDefinition = QueryFactory.createFunctionDefinition();
        functionDefinition.functionID("testFunction").language("Scala").body(
                "var concatenatedString = \"\"\n" +
                "for(i <- 0 until data.length) {\n" +
                "  concatenatedString += data(i).toString\n" +
                "}\n" +
                "concatenatedString");
    }
}