package org.wso2.siddhi.core.exception;

import org.wso2.siddhi.query.compiler.exception.SiddhiParserException;

public class UnsupportedScriptLanguageException extends SiddhiParserException {
    public UnsupportedScriptLanguageException(String message) {
        super(message);
    }
}
