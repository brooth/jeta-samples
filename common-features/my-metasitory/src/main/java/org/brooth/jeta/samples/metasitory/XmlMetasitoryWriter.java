/*
 * Copyright 2015 Oleg Khalidov
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.brooth.jeta.samples.metasitory;

import org.brooth.jeta.apt.MetacodeContext;
import org.brooth.jeta.apt.MetacodeUtils;
import org.brooth.jeta.apt.ProcessingContext;
import org.brooth.jeta.apt.ProcessingException;
import org.brooth.jeta.apt.metasitory.MetasitoryWriter;

import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
public class XmlMetasitoryWriter implements MetasitoryWriter {

    private Writer xml;

    @Override
    public void open(ProcessingContext processingContext) {
        try {
            FileObject fileObject = processingContext.processingEnv().getFiler().createResource(
                    StandardLocation.SOURCE_OUTPUT,
                    "",
                    "metasitory.xml");
            xml = fileObject.openWriter();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xml.append("<items>\n");

        } catch (IOException e) {
            throw new ProcessingException(e);
        }
    }

    @Override
    public void write(MetacodeContext metacodeContext) {
        try {
            String master = metacodeContext.masterElement().getQualifiedName().toString();
            xml.append("\t<item master=\"")
                    .append(master)
                    .append("\" metacode=\"")
                    .append(MetacodeUtils.toMetacodeName(master))
                    .append("\">\n");

            for (Class<? extends Annotation> annotation : metacodeContext.metacodeAnnotations()) {
                xml.append("\t\t<annotation>")
                        .append(annotation.getCanonicalName())
                        .append("</annotation>\n");
            }

            xml.append("\t</item>\n");

        } catch (IOException e) {
            throw new ProcessingException(e);
        }
    }

    @Override
    public void close() {
        try {
            xml.append("</items>");
            xml.close();

        } catch (IOException e) {
            throw new ProcessingException(e);
        }
    }
}
