/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.siddhi.sample;

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.config.SiddhiConfiguration;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.FunctionAlreadyExistException;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.extension.evalscript.exceptions.FunctionInitializationException;
import org.wso2.siddhi.query.api.QueryFactory;
import org.wso2.siddhi.query.api.definition.Attribute;


import java.util.ArrayList;
import java.util.List;

/**
 * Sample demonstrating a simple filtering use-case
 */
public class SimpleFilterSample {

    public static void main(String[] args) throws InterruptedException {

        // Create Siddhi Manager
        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();


        List<Class> list = new ArrayList<Class>();
        list.add(org.wso2.siddhi.extension.evalscript.EvalJavaScript.class);
        list.add(org.wso2.siddhi.extension.evalscript.EvalScala.class);

        siddhiConfiguration.setSiddhiExtensions(list);
        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        InputHandler inputHandler = siddhiManager.defineStream(
                QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).
                        attribute("price1", Attribute.Type.FLOAT).attribute("price2", Attribute.Type.FLOAT)
                        .attribute("volume", Attribute.Type.LONG).attribute("quantity", Attribute.Type.INT));
        siddhiManager.defineFunction(
                "define stream concat[Scala] return string {\n" +
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
            }
        });

        inputHandler.send(new Object[]{"WSO2", 50f, 60f, 60l, 6});
        siddhiManager.shutdown();
    }
}
