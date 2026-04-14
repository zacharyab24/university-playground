package com.zacharyab24.playground.war;

import com.zacharyab24.playground.ejb.HealthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/health")
public class HealthResource {

    @Inject
    private HealthService healthService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String health() {
        return healthService.getStatus();
    }
}