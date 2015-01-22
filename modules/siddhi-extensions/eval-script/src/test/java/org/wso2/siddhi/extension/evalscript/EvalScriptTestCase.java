package org.wso2.siddhi.extension.evalscript;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.config.SiddhiConfiguration;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.FunctionAlreadyExistException;
import org.wso2.siddhi.core.exception.SiddhiFunctionNotFoundException;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.extension.evalscript.exceptions.FunctionInitializationException;
import org.wso2.siddhi.extension.evalscript.exceptions.FunctionReturnTypeNotPresent;
import org.wso2.siddhi.query.api.QueryFactory;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.compiler.exception.SiddhiParserException;

import java.util.ArrayList;
import java.util.List;

public class EvalScriptTestCase {

    static final Logger log = Logger.getLogger(EvalScriptTestCase.class);

    boolean isReceived[] = new boolean[10];
    Object value[] = new Object[10];

    @Test
    public void testEvalScalaConcat() throws InterruptedException {

        log.info("TestEvalScalaConcat");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        InputHandler inputHandler = siddhiManager.defineStream(
                QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).
                        attribute("price1", Attribute.Type.FLOAT).attribute("price2", Attribute.Type.FLOAT)
                        .attribute("volume", Attribute.Type.LONG).attribute("quantity", Attribute.Type.INT));
        siddhiManager.defineFunction(
                "define function concat[Scala] return string {\n" +
                "  var concatenatedString = \"\"\n" +
                "  for(i <- 0 until data.length){\n" +
                "  concatenatedString += data(i).toString\n" +
                "  }\n" +
                "  concatenatedString\n" +
                "}");

        String queryReference = siddhiManager.addQuery("from cseEventStream" +
                " select symbol, concat(symbol,' ',price2) as price,quantity;");

        siddhiManager.addCallback(queryReference, new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                isReceived[0] = true;
                value[0] = inEvents[0].getData1();
            }
        });

        isReceived[0] = false;
        value[0] = null;

        inputHandler.send(new Object[]{"WSO2", 50f, 60f, 60l, 6});

        if(isReceived[0]) {
            Assert.assertEquals("WSO2 60.0", value[0]);
        } else {
            throw new RuntimeException("The event has not been received");
        }

        siddhiManager.shutdown();
    }

    @Test
    public void testEvalJavaScriptConcat() throws InterruptedException {

        log.info("testEvalJavaScriptConcat");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        InputHandler inputHandler = siddhiManager.defineStream(
                QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).
                        attribute("price1", Attribute.Type.FLOAT).attribute("price2", Attribute.Type.FLOAT)
                        .attribute("volume", Attribute.Type.LONG).attribute("quantity", Attribute.Type.INT));
        siddhiManager.defineFunction(
                "define function concat[JavaScript] return string {\n" +
                "  var str1 = data[0].toString();\n" +
                "  var str2 = data[1].toString();\n" +
                "  var str3 = data[2].toString();\n" +
                "  var res = str1.concat(str2,str3);\n" +
                "  return res;\n" +
                "}");

        String queryReference = siddhiManager.addQuery("from cseEventStream" +
                " select symbol, concat(symbol,' ',price2) as price,quantity;");

        siddhiManager.addCallback(queryReference, new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                isReceived[0] = true;
                value[0] = inEvents[0].getData1();
            }
        });

        isReceived[0] = false;
        value[0] = null;

        inputHandler.send(new Object[]{"WSO2", 50f, 60f, 60l, 6});

        if(isReceived[0]) {
            Assert.assertEquals("WSO2 60.0", value[0]);
        } else {
            throw new RuntimeException("The event has not been received");
        }

        siddhiManager.shutdown();
    }

    @Test
    public void testEvalRConcat() throws InterruptedException {

        log.info("TestEvalRConcat");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        InputHandler inputHandler = siddhiManager.defineStream(
                QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).
                        attribute("price1", Attribute.Type.FLOAT).attribute("price2", Attribute.Type.FLOAT)
                        .attribute("volume", Attribute.Type.LONG).attribute("quantity", Attribute.Type.INT));
        siddhiManager.defineFunction(
                "define function concat[R] return string {\n" +
                "return(paste(data, collapse=\"\"));  \n" +
                "}");

        String queryReference = siddhiManager.addQuery("from cseEventStream" +
                " select symbol, concat(symbol,' ',price2) as price,quantity;");

        siddhiManager.addCallback(queryReference, new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                isReceived[0] = true;
                value[0] = inEvents[0].getData1();
            }
        });

        isReceived[0] = false;
        value[0] = null;

        inputHandler.send(new Object[]{"WSO2", 50f, 60f, 60l, 6});

        if(isReceived[0]) {
            Assert.assertEquals("WSO2 60", value[0]);
        } else {
            throw new RuntimeException("The event has not been received");
        }

        siddhiManager.shutdown();
    }

    @Test(expected= FunctionInitializationException.class)
    public void testScalaCompilationFailure() throws InterruptedException {

        log.info("testScalaCompilationFailure");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        siddhiManager.defineFunction(
                "define function concat[Scala] return string {\n" +
                "  for(i <- 0 until data.length){\n" +
                "  concatenatedString += data(i).toString\n" +
                "  }\n" +
                "  concatenatedString\n" +
                "}");

        siddhiManager.shutdown();
    }

    @Test(expected=FunctionInitializationException.class)
    public void testJavaScriptCompilationFailure() throws InterruptedException {

        log.info("testJavaScriptCompilationFailure");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        siddhiManager.defineFunction((QueryFactory.createFunctionDefinition().functionID("concat").language("JavaScript").body(
                "var str1 = data[0\n" +
                "var str2 = data[1].toString()\n" +
                "var str3 = data[2].toString()\n" +
                "var res = str1.concat(str2,str3);\n" +
                "return res;")));

        siddhiManager.shutdown();
    }

    @Test(expected=FunctionAlreadyExistException.class)
    public void testDefineFunctionsWithSameFunctionID() throws InterruptedException {

        log.info("testDefineFunctionsWithSameFunctionID");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        siddhiManager.defineFunction((QueryFactory.createFunctionDefinition().functionID("concat").language("Scala").body(
                "var concatenatedString = \"\"\n" +
                        "for(i <- 0 until data.length){\n" +
                        "  concatenatedString += data(i).toString\n" +
                        "}\n"
                        +"concatenatedString").type(Attribute.Type.STRING)));

        siddhiManager.defineFunction((QueryFactory.createFunctionDefinition().functionID("concat").language("JavaScript").body(
                "var str1 = data[0].toString();\n" +
                        "var str2 = data[1].toString();\n" +
                        "var str3 = data[2].toString();\n" +
                        "var res = str1.concat(str2,str3);\n" +
                        "return res;").type(Attribute.Type.STRING)));

        siddhiManager.shutdown();
    }

    @Test
    public void testDefineManyFunctionsAndCallThemRandom() throws InterruptedException {

        log.info("testDefineManyFunctionsAndCallThemRandom");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        InputHandler inputHandler = siddhiManager.defineStream(
                QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).
                        attribute("price1", Attribute.Type.FLOAT).attribute("price2", Attribute.Type.FLOAT)
                        .attribute("volume", Attribute.Type.LONG).attribute("quantity", Attribute.Type.INT));

        siddhiManager.defineFunction(
                "define function concatS[Scala] return string {\n" +
                "  var concatenatedString = \"\"\n" +
                "  for(i <- 0 until data.length){\n" +
                "  concatenatedString += data(i).toString\n" +
                "  }\n" +
                "  concatenatedString\n" +
                "}");

        siddhiManager.defineFunction(
                "define function concatJ[JavaScript] return string {\n" +
                "   var str1 = data[0].toString();\n" +
                "   var str2 = data[1].toString();\n" +
                "   var str3 = data[2].toString();\n" +
                "   var res = str1.concat(str2,str3);\n" +
                "   return res;\n" +
                "}");

        siddhiManager.defineFunction(
                "define function toFloatS[Scala] return float {\n" +
                "   data(0).asInstanceOf[Long].toFloat\n" +
                "}");

        siddhiManager.defineFunction(
                "define function toStringJ[JavaScript] return string {\n" +
                "   return data[0].toString()\n" +
                "}");

        String queryReference1 = siddhiManager.addQuery("from cseEventStream" +
                " select symbol, toStringJ(price1) as price,quantity;");

        siddhiManager.addCallback(queryReference1, new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                //Assert.assertEquals("50.0", );
                isReceived[0] = true;
                value[0] = inEvents[0].getData1();
            }
        });

        String queryReference2 = siddhiManager.addQuery("from cseEventStream" +
                " select symbol, toFloatS(volume) as price,quantity;");

        siddhiManager.addCallback(queryReference2, new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                //Assert.assertEquals(60l, inEvents[0].getData1());
                isReceived[1] = true;
                value[1] = inEvents[0].getData1();
            }
        });

        String queryReference3 = siddhiManager.addQuery("from cseEventStream" +
                " select symbol, concatJ(symbol,' ',price1) as price,quantity;");

        siddhiManager.addCallback(queryReference3, new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                //Assert.assertEquals("WSO2 50.0", inEvents[0].getData1());
                isReceived[2] = true;
                value[2] = inEvents[0].getData1();
            }
        });

        String queryReference4 = siddhiManager.addQuery("from cseEventStream" +
                " select symbol, concatS(symbol,' ',price1) as price,quantity;");

        siddhiManager.addCallback(queryReference4, new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                //Assert.assertEquals("WSO2 50.0", inEvents[0].getData1());
                isReceived[3] = true;
                value[3] = inEvents[0].getData1();
            }
        });

        for(int i = 0; i < isReceived.length; i++ ) {
            isReceived[i] = false;
        }

        for(int i = 0; i < value.length; i++) {
            value[i] = null;
        }

        inputHandler.send(new Object[]{"WSO2", 50f, 60f, 60l, 6});

        if(isReceived[0]) {
            Assert.assertEquals("50.0", value[0]);
        } else {
            throw new RuntimeException("The event has not been received");
        }

        if(isReceived[1]) {
            Assert.assertEquals(60f, value[1]);
        } else {
            throw new RuntimeException("The event has not been received");
        }

        if(isReceived[2]) {
            Assert.assertEquals("WSO2 50.0", value[2]);
        } else {
            throw new RuntimeException("The event has not been received");
        }

        if(isReceived[3]) {
            Assert.assertEquals("WSO2 50.0", value[3]);
        } else {
            throw new RuntimeException("The event has not been received");
        }

        siddhiManager.shutdown();
    }

    @Test
    public void testReturnType() throws InterruptedException {

        log.info("testReturnType");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        InputHandler inputHandler = siddhiManager.defineStream(
                QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).
                        attribute("price1", Attribute.Type.STRING).attribute("price2", Attribute.Type.FLOAT)
                        .attribute("volume", Attribute.Type.LONG).attribute("quantity", Attribute.Type.INT));
        siddhiManager.defineFunction(
                "define function toFloat[Scala] return float {\n" +
                "   data(0).asInstanceOf[String].toFloat\n" +
                "}");

        String queryReference1 = siddhiManager.addQuery("from cseEventStream" +
                " select symbol, toFloat(price1) as price,quantity insert into outStream;");

        String queryReference2 = siddhiManager.addQuery("from outStream" +
                " select symbol, price/2 as halfPrice,quantity;");

        siddhiManager.addCallback(queryReference2, new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                //Assert.assertEquals(25.0, inEvents[0].getData1());
                isReceived[0] = true;
                value[0] = inEvents[0].getData1();
            }
        });

        isReceived[0] = false;
        value[0] = null;

        inputHandler.send(new Object[]{"WSO2", "50.0", 60f, 60l, 6});

        if(isReceived[0]) {
            Assert.assertEquals(25.0, value[0]);
        } else {
            throw new RuntimeException("The event has not been received");
        }

        siddhiManager.shutdown();
    }

    @Test(expected=FunctionReturnTypeNotPresent.class)
    public void testMissingReturnType() {
        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        siddhiManager.defineFunction((QueryFactory.createFunctionDefinition().functionID("concat").language("Scala").body(
                "var concatenatedString = \"\"\n" +
                        "for(i <- 0 until data.length){\n" +
                        "  concatenatedString += data(i).toString\n" +
                        "}\n"
                        +"concatenatedString")));

        siddhiManager.shutdown();
    }

    @Test(expected=SiddhiFunctionNotFoundException.class)
    public void testUseUndefinedFunction() throws InterruptedException {
        log.info("testUseUndefinedFunction");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        InputHandler inputHandler = siddhiManager.defineStream(
                QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).
                        attribute("price1", Attribute.Type.FLOAT).attribute("price2", Attribute.Type.FLOAT)
                        .attribute("volume", Attribute.Type.LONG).attribute("quantity", Attribute.Type.INT));

        String queryReference = siddhiManager.addQuery("from cseEventStream" +
                " select symbol, undefinedFunction(symbol,' ',price2) as price,quantity;");

        siddhiManager.addCallback(queryReference, new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });

        inputHandler.send(new Object[]{"WSO2", 50f, 60f, 60l, 6});

        siddhiManager.shutdown();
    }

    @Test(expected=SiddhiParserException.class)
    public void testMissingFunctionKeyWord() throws InterruptedException {
        log.info("testDefineManyFunctionsAndCallThemRandom");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        siddhiManager.defineFunction(
                "define concatS[Scala] return string {\n" +
                        "  var concatenatedString = \"\"\n" +
                        "  for(i <- 0 until data.length){\n" +
                        "  concatenatedString += data(i).toString\n" +
                        "  }\n" +
                        "  concatenatedString\n" +
                        "}");
        siddhiManager.shutdown();
    }

    @Test(expected=SiddhiParserException.class)
    public void testMissingDefineKeyWord() throws InterruptedException {
        log.info("testDefineManyFunctionsAndCallThemRandom");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        siddhiManager.defineFunction(
                "function concatS[Scala] return string {\n" +
                        "  var concatenatedString = \"\"\n" +
                        "  for(i <- 0 until data.length){\n" +
                        "  concatenatedString += data(i).toString\n" +
                        "  }\n" +
                        "  concatenatedString\n" +
                        "}");
        siddhiManager.shutdown();
    }

    @Test(expected=SiddhiParserException.class)
    public void testMissingFunctionName() throws InterruptedException {
        log.info("testDefineManyFunctionsAndCallThemRandom");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        siddhiManager.defineFunction(
                "define function [Scala] return string {\n" +
                        "  var concatenatedString = \"\"\n" +
                        "  for(i <- 0 until data.length){\n" +
                        "  concatenatedString += data(i).toString\n" +
                        "  }\n" +
                        "  concatenatedString\n" +
                        "}");
        siddhiManager.shutdown();
    }

    @Test(expected=SiddhiParserException.class)
    public void testMissingLanguage() throws InterruptedException {
        log.info("testDefineManyFunctionsAndCallThemRandom");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        siddhiManager.defineFunction(
                "define function concat[] return string {\n" +
                        "  var concatenatedString = \"\"\n" +
                        "  for(i <- 0 until data.length){\n" +
                        "  concatenatedString += data(i).toString\n" +
                        "  }\n" +
                        "  concatenatedString\n" +
                        "}");
        siddhiManager.shutdown();
    }

    @Test(expected=SiddhiParserException.class)
    public void testMissingBrackets() throws InterruptedException {
        log.info("testDefineManyFunctionsAndCallThemRandom");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        siddhiManager.defineFunction(
                "define function concat Scala return string {\n" +
                        "  var concatenatedString = \"\"\n" +
                        "  for(i <- 0 until data.length){\n" +
                        "  concatenatedString += data(i).toString\n" +
                        "  }\n" +
                        "  concatenatedString\n" +
                        "}");
        siddhiManager.shutdown();
    }

    @Test(expected=SiddhiParserException.class)
    public void testWrongBrackets() throws InterruptedException {
        log.info("testDefineManyFunctionsAndCallThemRandom");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        siddhiManager.defineFunction(
                "define function concat(Scala) return string {\n" +
                        "  var concatenatedString = \"\"\n" +
                        "  for(i <- 0 until data.length){\n" +
                        "  concatenatedString += data(i).toString\n" +
                        "  }\n" +
                        "  concatenatedString\n" +
                        "}");
        siddhiManager.shutdown();
    }

    @Test(expected=SiddhiParserException.class)
    public void testMissingReturnTypeWhileParsing() throws InterruptedException {
        log.info("testDefineManyFunctionsAndCallThemRandom");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        siddhiManager.defineFunction(
                "define function concat[Scala] {\n" +
                        "  var concatenatedString = \"\"\n" +
                        "  for(i <- 0 until data.length){\n" +
                        "  concatenatedString += data(i).toString\n" +
                        "  }\n" +
                        "  concatenatedString\n" +
                        "}");
        siddhiManager.shutdown();
    }

    @Test(expected=SiddhiParserException.class)
    public void testMissingLanguageBody() throws InterruptedException {
        log.info("testDefineManyFunctionsAndCallThemRandom");

        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalR.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        siddhiManager.defineFunction(
                "define function concat[Scala] { " +
                        "}");
        siddhiManager.shutdown();
    }
}
