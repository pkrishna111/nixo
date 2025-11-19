package com.nixo.rest;

import com.nixo.ejb.impl.ReceiverBeanLocal;
import com.nixo.entity.News;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("receiver")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReceiverResource {

    @EJB
    private ReceiverBeanLocal receiver;

    @GET
    @Path("news")
    public List<News> listNews() {
        return receiver.getAllNews();
    }

    @GET
    @Path("news/search")
    public List<News> searchNews(@QueryParam("q") String q) {
        return receiver.searchNews(q == null ? "" : q);
    }

    @POST
    @Path("news/{newsId}/like/{userId}")
    public boolean toggleLike(@PathParam("userId") int userId, @PathParam("newsId") int newsId) {
        return receiver.toggleLike(userId, newsId);
    }
}
