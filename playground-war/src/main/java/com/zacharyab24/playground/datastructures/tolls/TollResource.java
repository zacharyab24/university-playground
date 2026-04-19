package com.zacharyab24.playground.datastructures.tolls;

import com.zacharyab24.playground.datastructures.tolls.model.TollReportResponse;
import com.zacharyab24.playground.datastructures.tolls.model.TollReportRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.zacharyab24.playground.api.OpenAPIResource.DATA_STRUCTURES_TAG;

// TODO: Add @Operation and OAS annotations
@Path("/tolls")
public class TollResource {

    private static final Logger logger = Logger.getLogger(TollResource.class.getName());

    @POST
    @Path("/report")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateReport(TollReportRequest request) {
        try {
            logger.log(Level.INFO, "Generating toll report");
            TollReportResponse response = TollService.generateReport(request);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, () -> "Unable to parse request: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "Unexpected error: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }
}