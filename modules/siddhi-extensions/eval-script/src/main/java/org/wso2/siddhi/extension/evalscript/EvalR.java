package org.wso2.siddhi.extension.evalscript;

import org.apache.log4j.Logger;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
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

	REngine re;
	static Logger log = Logger.getLogger("RTransformProcessor");
	REXP env;
	private String functionName;
	private Attribute.Type returnType;

	@Override
	public void init(String name, String body) {
		this.functionName = name;
		try {
			// Get the JRIEngine or create one
			re = JRIEngine.createEngine();
			// Create a new R environment
			env = re.newEnvironment(null, true);

		} catch (REngineException e) {
			throw new FunctionInitializationException("Error while initializing the REngine", e);
		} catch (REXPMismatchException e) {
			throw new FunctionInitializationException("Error while initializing the REngine", e);
		}

		try {
			// Define the function in R environment env
			re.parseAndEval(name + " <- function(data) { " + body + " }", env, false);
		} catch (REngineException e) {
			throw new FunctionInitializationException("Error while declaring function in R", e);
		} catch (REXPMismatchException e) {
			throw new FunctionInitializationException("Error while declaring function in R", e);
		}

	}

	@Override
	public Object eval(String name, Object[] arg) {
		REXP out;
		StringBuilder data = new StringBuilder("data <- c(");
		for (int i = 0; i < arg.length; i++) {
			if (arg[i] instanceof String)
				data.append("\"").append(arg[i].toString()).append("\"");
			else
				data.append(arg[i].toString());
			if (i < arg.length - 1) {
				data.append(", ");
			}
		}
		data.append(");\n");
		data.append(name).append("(data)");
		try {
			out = re.parseAndEval(data.toString(), env, true);
			switch (returnType) {
			case BOOL:
				if (out.isLogical()) {
					return out.asInteger() == 1;
				}
				break;
			case INT:
				if (out.isInteger()) {
					return out.asInteger();
				}
				break;
			case LONG:
				if (out.isInteger()) {
					return (Long.valueOf(out.asInteger()));
				}
				break;
			case STRING:
				if (out.isString()) {
					return out.asString();
				}
				break;
			case FLOAT:
				if (out.isNumeric()) {
					return ((Double) out.asDouble()).floatValue();
				}
				break;
			case DOUBLE:
				if (out.isNumeric()) {
					return ((Double) out.asDouble());
				}
				break;
			default:
				throw new FunctionEvaluationException(
						"Specified return type not supported.");

			}
			throw new FunctionEvaluationException("Wrong return type detected.");
		} catch (REngineException e) {
			e.printStackTrace();
		} catch (REXPMismatchException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void setReturnType(Type returnType) {
		if (returnType == null) {
			throw new FunctionReturnTypeNotPresent(
					"Cannot find the return type of the function "
							+ functionName);
		}
		this.returnType = returnType;
	}

	@Override
	public Type getReturnType() {
		return returnType;
	}

}
