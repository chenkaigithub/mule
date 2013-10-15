/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.transport.jdbc.functional;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.transport.NullPayload;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JdbcMessagePropertiesCopyingTestCase extends AbstractJdbcFunctionalTestCase
{

    private static final String PROPERTY_KEY = "custom-key";
    private static final String PROPERTY_VALUE = "custom-value";

    @Override
    protected String getConfigResources()
    {
        return super.getConfigResources() + ", jdbc-message-properties-copying.xml";
    }

    @Test
    public void testMessagePropertiesCopying() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        
        MuleMessage message = new DefaultMuleMessage(TEST_MESSAGE, muleContext);
        // provide a valid type header so the JDBC query actually returns something
        message.setOutboundProperty("type", 1);
        message.setOutboundProperty(PROPERTY_KEY, PROPERTY_VALUE);
        
        MuleMessage result = client.send("vm://in", message);
        assertNotNull(result);
        assertNull(result.getExceptionPayload());
        assertFalse(result.getPayload() instanceof NullPayload);
        assertEquals(PROPERTY_VALUE, result.getInboundProperty(PROPERTY_KEY));
    }
}
