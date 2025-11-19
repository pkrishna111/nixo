package com.nixo.rest;

import com.nixo.ejb.impl.AdminBeanLocal;
import com.nixo.entity.SharerRequests;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {

    @EJB
    private AdminBeanLocal admin;

    @GET
    @Path("requests/pending")
    public List<SharerRequests> getPending() {
        return admin.getPendingRequests();
    }

    @POST
    @Path("requests/{id}/approve")
    public void approve(@PathParam("id") int id) {
        admin.approveRequest(id);
    }

    @POST
    @Path("requests/{id}/reject")
    public void reject(@PathParam("id") int id) {
        admin.rejectRequest(id);
    }
}
