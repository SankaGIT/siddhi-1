package org.wso2.siddhi.extension.evalscript;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPExpressionVector;
import org.rosuda.REngine.REXPWrapper;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.JRI.JRIEngine;
import org.wso2.siddhi.core.function.EvalScript;
import org.wso2.siddhi.extension.evalscript.exceptions.FunctionEvaluationException;
import org.wso2.siddhi.extension.evalscript.exceptions.FunctionInitializationException;
import org.wso2.siddhi.extension.evalscript.exceptions.FunctionReturnTypeNotPresent;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.Attribute.Type;
import org.wso2.siddhi.query.api.extension.annotation.SiddhiExtension;

@SiddhiExtension(namespace = "evalscript", function = "r")
public class EvalR implements EvalScript {

	private REngine rEngine;
	private REXP env;
	private REXP functionCall;
	private String functionName;
	private Attribute.Type returnType;

	@Override
	public void init(String name, String body) {
		this.functionName = name;
		try {
			// Get the JRIEngine or create one
			rEngine = JRIEngine.createEngine();
			// Create a new R environment
			env = rEngine.newEnvironment(null, true);

		} catch (Exception e) {
			throw new FunctionInitializationException("Error while initializing the REngine", e);
		}

		try {
			// Define the function in R environment env
			rEngine.parseAndEval(name + " <- function(data) { " + body + " }",
					env, false);
			// Parse the function call in R
			functionCall = rEngine.parse(name + "(data)", false);
		} catch (Exception e) {
			throw new FunctionInitializationException("Compilation failure of the R function " + name, e);
		}
	}

	@Override
	public Object eval(String name, Object[] arg) {
		REXP[] data = new REXP[arg.length];
		for (int i = 0; i < arg.length; i++) {
			data[i] = REXPWrapper.wrap(arg[i]);
		}

		try {
			// Send the data to R and assign it to symbol 'data'
			rEngine.assign("data", new REXPExpressionVector(new RList(data)), env);
			// Execute the function call
			REXP result = rEngine.eval(functionCall, env, true);
			switch (returnType) {
				case BOOL:
					if (result.isLogical()) {
						return result.asInteger() == 1;
					}
					break;
				case INT:
					if (result.isInteger()) {
						return result.asInteger();
					}
					break;
				case LONG:
					if (result.isNumeric()) {
						return ((long) result.asDouble());
					}
					break;
				case FLOAT:
					if (result.isNumeric()) {
						return ((Double) result.asDouble()).floatValue();
					}
					break;
				case DOUBLE:
					if (result.isNumeric()) {
						return ((Double) result.asDouble());
					}
					break;
				case STRING:
					if (result.isString()) {
						return result.asString();
					}
					break;
				default:
					break;
			}
			throw new FunctionEvaluationException(
					"Wrong return type detected. Expected: " + returnType
					+ " found: " + result.asNativeJavaObject().getClass().getCanonicalName());

		} catch (Exception e) {
			throw new FunctionEvaluationException("Error evaluating R function " + functionName, e);
		}
	}

	@Override
	public void setReturnType(Type returnType) {
		if (returnType == null) {
			throw new FunctionReturnTypeNotPresent("Cannot find the return type of the function " + functionName);
		}
		this.returnType = returnType;
	}

	@Override
	public Type getReturnType() {
		return returnType;
	}
}