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
package org.brooth.jeta.apt.samples;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import org.brooth.jeta.apt.MetacodeContext;
import org.brooth.jeta.apt.ProcessorEnvironment;
import org.brooth.jeta.apt.processors.SimpleProcessor;
import org.brooth.jeta.samples.myprocessor.MyAnnotation;
import org.brooth.jeta.samples.myprocessor.MyMetacode;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec.Builder;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
public class MyProcessor extends SimpleProcessor {

    public MyProcessor() {
        super(MyAnnotation.class);
    }

    @Override
    public boolean process(ProcessorEnvironment env, Builder builder) {
        MetacodeContext context = env.metacodeContext();
        ClassName masterClassName = ClassName.get(context.masterElement());
        builder.addSuperinterface(ParameterizedTypeName.get(
                ClassName.get(MyMetacode.class), masterClassName));

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("applyHello")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(masterClassName, "master");

        for (Element element : env.elements()) {
            String fieldName = element.getSimpleName().toString();
            methodBuilder.addStatement("master.$L = \"Hello, Jeta\"", fieldName);
        }

        builder.addMethod(methodBuilder.build());
        return false;
    }
}
