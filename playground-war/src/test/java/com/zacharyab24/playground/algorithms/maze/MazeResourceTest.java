package com.zacharyab24.playground.algorithms.maze;

import com.zacharyab24.playground.os.BaseResourceTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MazeResourceTest extends BaseResourceTest {

    @Override
    protected Set<Class<?>> getResourceClasses() {
        return Set.of(MazeResource.class);
    }

    @Test
    void postGenerate_returnsOkWithMaze() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"rows": 5, "cols": 5}
            """)
        .when()
            .post("/maze/generate")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("rows", is(5))
            .body("cols", is(5))
            .body("connectivity.size()", is(25))
            .body("start", greaterThan(0))
            .body("finish", greaterThan(0));
    }

    @Test
    void postGenerate_tooSmall_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"rows": 1, "cols": 5}
            """)
        .when()
            .post("/maze/generate")
        .then()
            .statusCode(400);
    }

    @Test
    void postSolve_returnsOkWithBfsAndDfs() {
        // First generate a maze
        String maze = given()
            .contentType(ContentType.JSON)
            .body("""
                {"rows": 3, "cols": 3}
            """)
        .when()
            .post("/maze/generate")
        .then()
            .statusCode(200)
            .extract().body().asString();

        // Then solve it
        given()
            .contentType(ContentType.JSON)
            .body(maze)
        .when()
            .post("/maze/solve")
        .then()
            .statusCode(200)
            .body("bfs.solution.size()", greaterThan(0))
            .body("dfs.solution.size()", greaterThan(0))
            .body("bfs.stepCount", greaterThan(0))
            .body("dfs.stepCount", greaterThan(0));
    }

    @Test
    void postVerify_generatedMaze_isValid() {
        // Generate a maze
        String maze = given()
            .contentType(ContentType.JSON)
            .body("""
                {"rows": 4, "cols": 4}
            """)
        .when()
            .post("/maze/generate")
        .then()
            .statusCode(200)
            .extract().body().asString();

        // Verify it
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"maze": %s, "solutions": []}
            """.formatted(maze))
        .when()
            .post("/maze/verify")
        .then()
            .statusCode(200)
            .body("cellsWithAllWalls", is(0))
            .body("cellsWithNoWalls", is(0))
            .body("hasCycles", is(false))
            .body("allReachable", is(true));
    }

    @Test
    void postSolve_invalidJson_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("not json")
        .when()
            .post("/maze/solve")
        .then()
            .statusCode(400);
    }

    @Test
    void postGenerate_invalidJson_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("not json")
        .when()
            .post("/maze/generate")
        .then()
            .statusCode(400);
    }
}