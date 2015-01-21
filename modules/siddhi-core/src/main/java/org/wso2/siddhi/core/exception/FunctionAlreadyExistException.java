package org.wso2.siddhi.core.exception;

import org.wso2.siddhi.query.compiler.exception.SiddhiParserException;

public class FunctionAlreadyExistException extends SiddhiParserException{
    public FunctionAlreadyExistException(String message) {
        super(message);
    }
}
