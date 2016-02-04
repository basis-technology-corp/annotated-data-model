/*
* Copyright 2014 Basis Technology Corp.
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
*/

package com.basistech.rosette.dm.json.plain;

import com.basistech.rosette.dm.jackson.AnnotatedDataModelModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Test inspired by ROS-149
 */
public class NonNullTest {

    public static class Bean {
        private String something;
        private String nothing;

        public String getSomething() {
            return something;
        }

        public void setSomething(String something) {
            this.something = something;
        }

        public String getNothing() {
            return nothing;
        }

        public void setNothing(String nothing) {
            this.nothing = nothing;
        }
    }

    @Test
    public void nonNull() throws Exception {
        Bean data = new Bean();
        data.setSomething("else");

        ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());
        String json = mapper.writeValueAsString(data);
        assertFalse(json.contains("nothing"));

    }
}
