package com.zacharyab24.playground.scheduler;

import com.zacharyab24.playground.scheduling.Scheduler;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/scheduler")
public class SchedulingResource {

    private static final Logger logger = Logger.getLogger(SchedulingResource.class.getName());

    // TODO: replace file input with structured data dto. we can make the frontend be able to upload a file and convert that to structured data later
    @GET
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response runScheduler(MultipartFormDataInput input) {
        try {
            InputPart filePart = input.getFormDataMap().get("file").getFirst();
            InputStream fileStream = filePart.getBody(InputStream.class, null);

            String result = Scheduler.runScheduler(fileStream);
            return Response.ok(result).build();
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, () -> "Unable to parse request: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "Unexpected error whilst running program: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
