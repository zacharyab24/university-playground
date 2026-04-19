package com.zacharyab24.playground.os.concurrency;

import com.zacharyab24.playground.os.BaseResourceTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConcurrencyResourceTest extends BaseResourceTest {

    @Override
    protected Set<Class<?>> getResourceClasses() {
        return Set.of(ConcurrencyResource.class);
    }

    private static final String CONCURRENCY_REQUEST = """
            {
              "tasks": [
                {"name": "tasks1", "numProcesses": 2, "inputs": [5, 3]}
              ]
            }
            """;

    private static final String WORMHOLE_REQUEST = """
            {
              "earthHumans": 1,
              "proximaAliens": 1,
              "trips": 1
            }
            """;

    @Test
    void postSemaphore_returnsOkWithResults() {
        given()
            .contentType(ContentType.JSON)
            .body(CONCURRENCY_REQUEST)
        .when()
            .post("/multithreading/semaphore")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", greaterThan(0))
            .body("taskFile", everyItem(equalTo("tasks1")));
    }

    @Test
    void postMonitor_returnsOkWithResults() {
        given()
            .contentType(ContentType.JSON)
            .body(CONCURRENCY_REQUEST)
        .when()
            .post("/multithreading/monitor")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", greaterThan(0))
            .body("taskFile", everyItem(equalTo("tasks1")));
    }

    @Test
    void postWormhole_returnsOkWithCrossings() {
        given()
            .contentType(ContentType.JSON)
            .body(WORMHOLE_REQUEST)
        .when()
            .post("/multithreading/wormhole")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("totalCrossings", is(2))
            .body("crossings.size()", is(2));
    }

    @Test
    void postSemaphore_emptyTasks_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"tasks": []}
            """)
        .when()
            .post("/multithreading/semaphore")
        .then()
            .statusCode(400);
    }

    @Test
    void postMonitor_emptyTasks_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"tasks": []}
            """)
        .when()
            .post("/multithreading/monitor")
        .then()
            .statusCode(400);
    }

    @Test
    void postSemaphore_invalidJson_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("not json")
        .when()
            .post("/multithreading/semaphore")
        .then()
            .statusCode(400);
    }

    @Test
    void postWormhole_invalidJson_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("not json")
        .when()
            .post("/multithreading/wormhole")
        .then()
            .statusCode(400);
    }

    @Test
    void postSemaphore_wrongContentType_returns415() {
        given()
            .contentType(ContentType.TEXT)
            .body(CONCURRENCY_REQUEST)
        .when()
            .post("/multithreading/semaphore")
        .then()
            .statusCode(415);
    }
}