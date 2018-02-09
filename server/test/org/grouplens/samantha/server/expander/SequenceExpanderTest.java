/*
 * Copyright (c) [2016-2017] [University of Minnesota]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.grouplens.samantha.server.expander;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import org.grouplens.samantha.FakeApplication;
import org.grouplens.samantha.server.io.RequestContext;
import org.junit.Test;
import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class SequenceExpanderTest {

    @Test
    public void testExpand() {
        FakeApplication.instance();
        List<String> nameAttrs = Lists.newArrayList("item", "action");
        List<String> valueAttrs = Lists.newArrayList("item", "action");
        List<String> historyAttrs = Lists.newArrayList("hitem", "haction");
        SequenceExpander expander = new SequenceExpander(nameAttrs, valueAttrs, historyAttrs, "\\|");
        List<ObjectNode> entities = new ArrayList<>();
        ObjectNode entity1 = Json.newObject();
        entity1.put("user", "123");
        entity1.put("item", "10|2|10|7|4|5");
        entity1.put("action", "1|0|0|0|0|1");
        entities.add(entity1);
        ObjectNode entity2 = Json.newObject();
        entity2.put("user", "455");
        entity2.put("item", "5");
        entity2.put("action", "0");
        entities.add(entity2);
        List<ObjectNode> expanded = expander.expand(entities, new RequestContext(Json.newObject(), "test"));
        assertEquals(7, expanded.size());
        assertEquals(expanded.get(0).get("item").asText(), "10");
        assertEquals(expanded.get(0).get("hitem").asText(), "");
        assertEquals(expanded.get(0).get("action").asText(), "1");
        assertEquals(expanded.get(0).get("haction").asText(), "");
        assertEquals(expanded.get(0).get("user").asText(), "123");
        /*
        assertEquals(expanded.get(4).get("item").asText(), "4");
        assertEquals(expanded.get(4).get("hitem").asText(), "10|2|10|7");
        assertEquals(expanded.get(4).get("action").asText(), "0");
        assertEquals(expanded.get(4).get("haction").asText(), "1|0|0|0");
        assertEquals(expanded.get(4).get("user").asText(), "123");
        assertEquals(expanded.get(6).get("item").asText(), "5");
        assertEquals(expanded.get(6).get("hitem").asText(), "");
        assertEquals(expanded.get(6).get("action").asText(), "0");
        assertEquals(expanded.get(6).get("haction").asText(), "");
        assertEquals(expanded.get(6).get("user").asText(), "455");
        */
    }
}
