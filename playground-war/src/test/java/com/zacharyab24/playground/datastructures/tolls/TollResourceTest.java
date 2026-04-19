package com.zacharyab24.playground.datastructures.tolls;

import com.zacharyab24.playground.os.BaseResourceTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TollResourceTest extends BaseResourceTest {

    @Override
    protected Set<Class<?>> getResourceClasses() {
        return Set.of(TollResource.class);
    }

    private static final String VALID_REQUEST = """
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
            """;

    @Test
    void postReport_returnsOkWithDeduplicatedResults() {
        given()
            .contentType(ContentType.JSON)
            .body(VALID_REQUEST)
        .when()
            .post("/tolls/report")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("totalVehicles", is(3))
            .body("totalIncome", is(13.0f))
            .body("mergedRecords.size()", is(3));
    }

    @Test
    void postReport_countsByTypePresent() {
        given()
            .contentType(ContentType.JSON)
            .body(VALID_REQUEST)
        .when()
            .post("/tolls/report")
        .then()
            .statusCode(200)
            .body("countByType.Car", is(1))
            .body("countByType.Truck", is(1))
            .body("countByType.Motorcycle", is(1));
    }

    @Test
    void postReport_invalidJson_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("not json")
        .when()
            .post("/tolls/report")
        .then()
            .statusCode(400);
    }

    @Test
    void postReport_wrongContentType_returns415() {
        given()
            .contentType(ContentType.TEXT)
            .body(VALID_REQUEST)
        .when()
            .post("/tolls/report")
        .then()
            .statusCode(415);
    }
}
