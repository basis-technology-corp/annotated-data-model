/*******************************************************************************
 * Copyright 2021 Basis Technology Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.basistech.rosette.dm.json.plain;

import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.Test;

import java.io.File;

public class EventsTest extends AdmAssert {
    @Test
    public void testDeserializeEvent() throws Exception {
        ObjectMapper mapper = objectMapper();
        ObjectReader reader = mapper.readerFor(AnnotatedText.class);
        // threw
        AnnotatedText adm = reader.readValue(new File("test-data/events.json"));
        assertNotNull(adm.getEvents());
        assertEquals(Event.class, adm.getEvents().getItemClass());
        assertNotNull(adm.getEvents().get(0).getEventType());
        assertEquals("flight_booking_schema.flight_booking", adm.getEvents().get(0).getEventType());
        assertEquals(3, adm.getEvents().get(0).getMentions().get(0).getRoles().size());
    }

    @Test
    public void testDeserializeNegatedEvent() throws Exception {
        ObjectMapper mapper = objectMapper();
        ObjectReader reader = mapper.readerFor(AnnotatedText.class);
        // threw
        AnnotatedText adm = reader.readValue(new File("test-data/negatedEvents.json"));
        assertNotNull(adm.getEvents());
        assertEquals(Event.class, adm.getEvents().getItemClass());
        assertNotNull(adm.getEvents().get(0).getEventType());
        assertEquals("flight_booking_schema.flight_booking", adm.getEvents().get(0).getEventType());
        assertEquals(3, adm.getEvents().get(0).getMentions().get(0).getRoles().size());
        assertEquals("Negative", adm.getEvents().get(0).getMentions().get(0).getPolarity());
        assertEquals(1, adm.getEvents().get(0).getMentions().get(0).getNegationCues().size());
        assertEquals("don't", adm.getEvents().get(0).getMentions().get(0).getNegationCues().get(0).getDataSpan());
    }

}
