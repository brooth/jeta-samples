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

package org.brooth.jeta.samples.eventbus;

import org.brooth.jeta.eventbus.*;
import org.brooth.jeta.samples.MetaHelper;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
public class EventBusSample {

    public static class AlertMessage extends BaseMessage {
        private AlertMessage(int level, String msg) {
            super(level, msg);
        }
    }

    static class Never implements Filter {
        @Override
        public boolean accepts(Object master, String methodName, Message msg) {
            return false;
        }
    }

    @MetaFilter(emitExpression = "$m.MIN_ALARM_ID <= $e.getId()")
    interface MinAlarmIdFilter extends Filter { }

    /**
     * Subscriber
     */
    public static class PanicAtTheDisco {
        final int MIN_ALARM_ID = 3;

        private SubscriptionHandler handler;

        public PanicAtTheDisco() {
            handler = MetaHelper.registerSubscriber(this);
        }

        @Subscribe(ids = 1)
        protected void onInfo(AlertMessage alert) {
            System.out.println("Info: '" + alert.getTopic() + "'");
        }

        @Subscribe(filters = MinAlarmIdFilter.class)
        protected void onAlarm(AlertMessage alert) {
            System.out.println("Error: '" + alert.getTopic() + "'. I quit!");
            quit();
        }

        @Subscribe(filters = Never.class)
        protected void onApocalypse(AlertMessage alert) {
            throw new IllegalStateException("Why God? Why?");
        }

        private void quit() {
            handler.unregisterAll();
        }
    }

    public static void main(String[] args) {
        new PanicAtTheDisco();
        EventBus bus = MetaHelper.getEventBus();
        bus.publish(new AlertMessage(1, "Have a good day :)"));
        bus.publish(new AlertMessage(3, "The village is on fire!"));
        bus.publish(new AlertMessage(5, "Zombie!"));
    }
}
