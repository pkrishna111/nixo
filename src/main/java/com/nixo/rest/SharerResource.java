package com.nixo.rest;

import com.nixo.ejb.impl.SharerBeanLocal;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("sharer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SharerResource {

    @EJB
    private SharerBeanLocal sharer;

    public static class CreateNewsReq {
        public Integer sharerUserId;
        public String title;
        public String content;
        public String imageUrl;
    }

    @POST
    @Path("news")
    public void addNews(CreateNewsReq req) {
        sharer.addNews(req.sharerUserId, req.title, req.content, req.imageUrl);
    }
}
