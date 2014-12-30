package com.fasterxml.jackson.dataformat.xml.failing;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlTestBase;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public class UnwrappedListWithEmptyCData129Test extends XmlTestBase
{
    static class ListValues {
        @XmlElement(name = "value", required = true)
        @JacksonXmlElementWrapper(useWrapping=false)
        public List<String> value;
    }


    private final XmlMapper MAPPER = new XmlMapper();
    {
        // easier for eye:
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // for [#129]
    public void testListWithEmptyCData() throws Exception
    {
        String SECOND = " ";
        ListValues result = MAPPER.readValue("<root>\n"
                + "<value>A</value>\n"
//                + "<value><![CDATA["+SECOND+"]]></value>\n"
                + "<value> </value>\n"
                + "<value>C</value>\n"
                + "</root>", ListValues.class);

        List<String> values = result.value;

        assertEquals(3, values.size()); // expecting 3 values, getting only 1
        assertEquals("A", values.get(0));
        assertEquals(SECOND, values.get(1)); // expecting empty string in second position
        assertEquals("C", values.get(2));
    }    
}
