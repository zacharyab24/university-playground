package com.zacharyab24.playground.algorithms.tsp;

import com.zacharyab24.playground.os.BaseResourceTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TSPResourceTest extends BaseResourceTest {

    @Override
    protected Set<Class<?>> getResourceClasses() {
        return Set.of(TSPResource.class);
    }

    private static final String DYNAMIC_REQUEST = """
            {
              "algorithm": "dynamic",
              "coordinates": [
                {"x": 0, "y": 0},
                {"x": 3, "y": 4},
                {"x": 6, "y": 0}
              ]
            }
            """;

    private static final String HILLCLIMB_REQUEST = """
            {
              "algorithm": "hillclimb",
              "coordinates": [
                {"x": 0, "y": 0},
                {"x": 3, "y": 4},
                {"x": 6, "y": 0}
              ],
              "iterations": 1000,
              "maxAttempts": 10
            }
            """;

    @Test
    void postTsp_dynamic_returnsOkWithTour() {
        given()
            .contentType(ContentType.JSON)
            .body(DYNAMIC_REQUEST)
        .when()
            .post("/tsp")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("path.size()", is(4))
            .body("length", greaterThan(0f));
    }

    @Test
    void postTsp_hillclimb_returnsOkWithTour() {
        given()
            .contentType(ContentType.JSON)
            .body(HILLCLIMB_REQUEST)
        .when()
            .post("/tsp")
        .then()
            .statusCode(200)
            .body("path.size()", is(4))
            .body("length", greaterThan(0f));
    }

    @Test
    void postTsp_unknownAlgorithm_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"algorithm": "bogus", "coordinates": [{"x": 0, "y": 0}, {"x": 1, "y": 1}]}
            """)
        .when()
            .post("/tsp")
        .then()
            .statusCode(400);
    }

    @Test
    void postTsp_invalidJson_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("not json")
        .when()
            .post("/tsp")
        .then()
            .statusCode(400);
    }
}