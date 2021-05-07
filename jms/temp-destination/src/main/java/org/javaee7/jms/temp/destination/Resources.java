package org.javaee7.jms.temp.destination;

import jakarta.jms.JMSDestinationDefinition;

/**
 * Application scoped JMS resources for the samples.
 * 
 * @author Patrik Dudits
 */
@JMSDestinationDefinition(
	name = Resources.REQUEST_QUEUE, 
	interfaceName = "jakarta.jms.Queue", 
	destinationName = "requestQueue", 
	description = "Queue for service requests")
public class Resources {
	public static final String REQUEST_QUEUE = "java:global/jms/requestQueue";
}
