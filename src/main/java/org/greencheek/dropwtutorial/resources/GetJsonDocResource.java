package org.greencheek.dropwtutorial.resources;

import org.greencheek.dropwtutorial.service.GetJsonDoc;
import org.greencheek.dropwtutorial.usage.client.FailedToFireUsageEventException;
import org.greencheek.dropwtutorial.usage.client.UsageClient;
import org.greencheek.dropwtutorial.usage.client.domain.ArticleViewEvent;

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
    private final UsageClient client;

    public GetJsonDocResource(GetJsonDoc pooledClient, GetJsonDoc nettyClient,GetJsonDoc defaultClient,
                              UsageClient client) {
        this.pooledClient = pooledClient;
        this.nettyClient = nettyClient;
        this.defaultClient = defaultClient;
        this.client = client;
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

    @GET
    @Path("/usage")
    @Produces("application/json" + "; charset=utf-8")
    public Response usageClient(@Context HttpServletRequest request) {
        try {
            client.fireUsageEvent(new ArticleViewEvent("bob", "bob", "bob"));
            return Response.ok("good").build();
        } catch(FailedToFireUsageEventException e) {
            return Response.serverError().build();
        }
    }
}
