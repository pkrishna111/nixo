package com.nixo.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("api")
public class RestActivator extends Application {
    // empty - activates JAX-RS under /api
}
