package com.zacharyab24.playground.datastructures.inventory;

import com.zacharyab24.playground.os.BaseResourceTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InventoryResourceTest extends BaseResourceTest {

    @Override
    protected Set<Class<?>> getResourceClasses() {
        return Set.of(InventoryResource.class);
    }

    private static final String STATS_REQUEST = """
            {
              "dataStructure": "BSTREE",
              "parts": [
                {"code": "AAA", "quantity": 50},
                {"code": "BBB", "quantity": 5},
                {"code": "CCC", "quantity": 100}
              ],
              "lessThanThreshold": 10
            }
            """;

    private static final String BENCHMARK_REQUEST = """
            {
              "dataStructure": "BSTREE",
              "parts": [
                {"code": "AAA", "quantity": 50},
                {"code": "BBB", "quantity": 5}
              ],
              "iterations": 100
            }
            """;

    @Test
    void postStats_bstree_returnsOk() {
        given()
            .contentType(ContentType.JSON)
            .body(STATS_REQUEST)
        .when()
            .post("/inventory/stats")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("dataStructure", is("BSTREE"))
            .body("totalParts", is(3))
            .body("totalInventory", is(155))
            .body("partsLessThan", is(1))
            .body("contents", not(containsString("@")));
    }

    @Test
    void postStats_htable_returnsOk() {
        given()
            .contentType(ContentType.JSON)
            .body(STATS_REQUEST.replace("BSTREE", "HTABLE"))
        .when()
            .post("/inventory/stats")
        .then()
            .statusCode(200)
            .body("dataStructure", is("HTABLE"))
            .body("totalParts", is(3));
    }

    @Test
    void postBenchmark_returnsOk() {
        given()
            .contentType(ContentType.JSON)
            .body(BENCHMARK_REQUEST)
        .when()
            .post("/inventory/benchmark")
        .then()
            .statusCode(200)
            .body("dataStructure", is("BSTREE"))
            .body("iterations", greaterThan(0))
            .body("totalTimeMs", greaterThanOrEqualTo(0));
    }

    @Test
    void postStats_invalidJson_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("not json")
        .when()
            .post("/inventory/stats")
        .then()
            .statusCode(400);
    }

    @Test
    void postBenchmark_invalidJson_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("not json")
        .when()
            .post("/inventory/benchmark")
        .then()
            .statusCode(400);
    }

    @Test
    void postStats_wrongContentType_returns415() {
        given()
            .contentType(ContentType.TEXT)
            .body(STATS_REQUEST)
        .when()
            .post("/inventory/stats")
        .then()
            .statusCode(415);
    }
}
