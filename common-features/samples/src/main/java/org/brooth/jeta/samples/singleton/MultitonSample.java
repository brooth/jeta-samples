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

package org.brooth.jeta.samples.singleton;

import org.brooth.jeta.samples.MetaHelper;
import org.brooth.jeta.util.Multiton;
import org.brooth.jeta.util.MultitonMetacode;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
@Multiton
public class MultitonSample {

    static MultitonMetacode<MultitonSample> multiton = 
        MetaHelper.getMultiton(MultitonSample.class);

    public static MultitonSample getInstance(String key) {
        return multiton.getMultiton(key);
    }

    MultitonSample(Object key) {

    }

    public static void main(String[] args) {
        System.out.println(getInstance("one") == getInstance("one"));
        System.out.println(getInstance("one") != getInstance("two"));
        System.out.println(getInstance("two") == getInstance("two"));
    }
}
