/*
 * Copyright 2015 Oleg Khalidov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.brooth.jeta.samples.command_handler;

import org.brooth.jeta.collector.TypeCollector;
import org.brooth.jeta.collector.TypeCollectorController;
import org.brooth.jeta.metasitory.MapMetasitory;
import org.brooth.jeta.metasitory.Metasitory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
@TypeCollector(Command.class)
public class CommandProcessor {
    private Map<String, CommandHandler> handlers = new HashMap<>();

    public CommandProcessor() {
        //parseHandlers();
        collectHandlers();
    }

    private void collectHandlers() {
        Metasitory metasitory = new MapMetasitory("");
        List<Class<?>> types = new TypeCollectorController(metasitory, getClass()).getTypes(Command.class);
        for (Class handlerClass : types) {
            try {
                Command command = (Command) handlerClass.getAnnotation(Command.class);
                handlers.put(command.value(), (CommandHandler) handlerClass.newInstance());

            } catch (Exception e) {
                throw new RuntimeException("Failed to collect handlers", e);
            }
        }
    }

    private void parseHandlers() {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse("handlers.xml");
            NodeList nodes = document.getDocumentElement().getElementsByTagName("handler");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                handlers.put(node.getAttributes().getNamedItem("command").getTextContent(),
                        (CommandHandler) Class.forName(node.getTextContent()).newInstance());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse handlers.xml!", e);
        }
    }

    public void loop() {
        System.out.println("Input command. Type 'exit' to finish.");

        Scanner input = new Scanner(System.in);
        while (true) {
            String command = input.next();
            CommandHandler handler = handlers.get(command);
            if (handler == null)
                System.out.println("Unknown command '" + command + "'. Try again");
            else
                handler.handle();
        }
    }

    public static void main(String[] args) {
        new CommandProcessor().loop();
    }
}
