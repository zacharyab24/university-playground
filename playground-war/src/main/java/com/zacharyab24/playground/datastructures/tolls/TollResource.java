package com.zacharyab24.playground.datastructures.tolls;

import com.zacharyab24.playground.datastructures.tolls.model.TollReportResponse;
import com.zacharyab24.playground.datastructures.tolls.model.TollReportRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.zacharyab24.playground.api.OpenAPIResource.DATA_STRUCTURES_TAG;

@Path("/tolls")
public class TollResource {

    private static final Logger logger = Logger.getLogger(TollResource.class.getName());

    @POST
    @Path("/report")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Generate a daily toll report",
            description = """
                          Merges toll records from two booths using a linked list, removes duplicate vehicles
                          (by licence plate), and returns counts by vehicle type, total income, and the merged records.""",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Daily toll report.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = TollReportResponse.class),
                                    examples = @ExampleObject(
                                            name = "Merged report",
                                            value = """
                                                {
                                                  "totalVehicles": 3,
                                                  "totalIncome": 13.00,
                                                  "countByType": {"Car": 1, "Truck": 1, "Motorcycle": 1},
                                                  "mergedRecords": [
                                                    {"license": "XYZ789", "type": "Truck", "charge": 8.00},
                                                    {"license": "DEF456", "type": "Motorcycle", "charge": 1.50},
                                                    {"license": "ABC123", "type": "Car", "charge": 3.50}
                                                  ]
                                                }
                                                """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Request body was malformed or failed validation."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error."),
            },
            tags = DATA_STRUCTURES_TAG
    )
    public Response generateReport(
            @RequestBody(
                    description = "Toll records from two booths.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TollReportRequest.class),
                            examples = @ExampleObject(
                                    name = "Two booths",
                                    value = """
                                            {
                                              "booth1": [
                                                {"license": "ABC123", "type": "Car", "charge": 3.50},
                                                {"license": "XYZ789", "type": "Truck", "charge": 8.00}
                                              ],
                                              "booth2": [
                                                {"license": "DEF456", "type": "Motorcycle", "charge": 1.50},
                                                {"license": "ABC123", "type": "Car", "charge": 3.50}
                                              ]
                                            }
                                            """
                            )
                    )
            )
            TollReportRequest request) {
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