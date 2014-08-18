package org.greencheek.dropwtutorial.resources;

import org.greencheek.dropwtutorial.service.GetJsonDoc;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/doc")
@Produces(MediaType.APPLICATION_JSON)
public class GetJsonDocResource {

    private final GetJsonDoc pooledClient;
    private final GetJsonDoc defaultClient;
    private final GetJsonDoc nettyClient;

    public GetJsonDocResource(GetJsonDoc pooledClient, GetJsonDoc nettyClient,GetJsonDoc defaultClient) {
        this.pooledClient = pooledClient;
        this.nettyClient = nettyClient;
        this.defaultClient = defaultClient;
    }

    @GET
    @Path("/pooled")
    @Produces("application/json" + "; charset=utf-8")
    public Response pooled(@Context HttpServletRequest request) {
        return Response.ok(pooledClient.get()).build();
    }



    @GET
    @Path("/netty")
    @Produces("application/json" + "; charset=utf-8")
    public Response netty(@Context HttpServletRequest request) {
        return Response.ok(nettyClient.get()).build();
    }


    @GET
    @Path("/default")
    @Produces("application/json" + "; charset=utf-8")
    public Response defaultClient(@Context HttpServletRequest request) {
        return Response.ok(defaultClient.get()).build();
    }
}
