package com.zacharyab24.playground.os.paging;

import com.zacharyab24.playground.os.BaseResourceTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PagingResourceTest extends BaseResourceTest {

    @Override
    protected Set<Class<?>> getResourceClasses() {
        return Set.of(PagingResource.class);
    }

    private static final String VALID_REQUEST = """
            {
              "totalFrames": 30,
              "quantum": 3,
              "processes": [
                {"pid": 1, "name": "Process1", "pages": [1, 2, 3, 1, 4]},
                {"pid": 2, "name": "Process2", "pages": [2, 3, 4, 2, 1]}
              ]
            }
            """;

    @Test
    void postPaging_returnsOkWithTwoPolicies() {
        given()
            .contentType(ContentType.JSON)
            .body(VALID_REQUEST)
        .when()
            .post("/paging")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("results.size()", is(2))
            .body("results.algorithm", hasItems("Fixed-Local", "Variable-Global"));
    }

    @Test
    void postPaging_eachPolicyHasProcessResults() {
        given()
            .contentType(ContentType.JSON)
            .body(VALID_REQUEST)
        .when()
            .post("/paging")
        .then()
            .statusCode(200)
            .body("results[0].processes.size()", is(2))
            .body("results[1].processes.size()", is(2));
    }

    @Test
    void postPaging_processResultsHaveExpectedFields() {
        given()
            .contentType(ContentType.JSON)
            .body(VALID_REQUEST)
        .when()
            .post("/paging")
        .then()
            .statusCode(200)
            .body("results[0].processes[0].pid", is(1))
            .body("results[0].processes[0].processName", is("Process1"))
            .body("results[0].processes[0].finishTime", greaterThan(0))
            .body("results[0].processes[0].faults", greaterThan(0));
    }

    @Test
    void postPaging_invalidJson_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("not json")
        .when()
            .post("/paging")
        .then()
            .statusCode(400);
    }

    @Test
    void postPaging_missingProcesses_returns500() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"totalFrames": 30, "quantum": 3}
            """)
        .when()
            .post("/paging")
        .then()
            .statusCode(500);
    }

    @Test
    void postPaging_wrongContentType_returns415() {
        given()
            .contentType(ContentType.TEXT)
            .body(VALID_REQUEST)
        .when()
            .post("/paging")
        .then()
            .statusCode(415);
    }
}