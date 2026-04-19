package com.zacharyab24.playground.os.scheduler;

import com.zacharyab24.playground.os.BaseResourceTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SchedulerResourceTest extends BaseResourceTest {

    @Override
    protected Set<Class<?>> getResourceClasses() {
        return Set.of(SchedulerResource.class);
    }

    private static final String VALID_REQUEST = """
            {
              "dispatcher": 1,
              "processes": [
                {"processorID": "p1", "arrivalTime": 0, "serviceTime": 10, "priority": 0},
                {"processorID": "p2", "arrivalTime": 0, "serviceTime": 1, "priority": 2}
              ]
            }
            """;

    @Test
    void postScheduler_returnsOkWithFourAlgorithms() {
        given()
            .contentType(ContentType.JSON)
            .body(VALID_REQUEST)
        .when()
            .post("/scheduler")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", is(4))
            .body("processorID", hasItems("FCFS", "SPN", "PP", "PRR"))
            .body("turnAround", everyItem(notNullValue()))
            .body("waitingTime", everyItem(notNullValue()));
    }

    @Test
    void postScheduler_missingProcesses_returns500() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"dispatcher\": 1}")
        .when()
            .post("/scheduler")
        .then()
            .statusCode(500);
    }

    @Test
    void postScheduler_invalidJson_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("not json")
        .when()
            .post("/scheduler")
        .then()
            .statusCode(400);
    }

    @Test
    void postScheduler_wrongContentType_returns415() {
        given()
            .contentType(ContentType.TEXT)
            .body(VALID_REQUEST)
        .when()
            .post("/scheduler")
        .then()
            .statusCode(415);
    }
}