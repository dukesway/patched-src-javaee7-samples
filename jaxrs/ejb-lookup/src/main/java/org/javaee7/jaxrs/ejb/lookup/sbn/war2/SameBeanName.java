// Copyright [2019] [Payara Foundation and/or its affiliates]
package org.javaee7.jaxrs.ejb.lookup.sbn.war2;

import jakarta.ejb.Stateless;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Stateless bean with a custom name and default <code>@Local</code> interface.
 *
 * @author David Matejcek
 */
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
@Stateless(name = "DuplicitBean2")
@Path("same-bean-name-bean")
public class SameBeanName {

    @GET
    @Path("ok")
    public String ok() {
        return "OK2";
    }
}
