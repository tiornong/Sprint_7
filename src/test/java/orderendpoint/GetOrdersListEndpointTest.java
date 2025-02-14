package orderendpoint;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import org.junit.Before;
import org.junit.Test;

import util.Constant;
import util.client.ScooterServiceClient;


import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class GetOrdersListEndpointTest {

    @Test
    @DisplayName("Проверяем получение и корректность структуры списка заказов")
    public void getCorrectOrdersListTest() {
        ScooterServiceClient client = new ScooterServiceClient();
        ValidatableResponse response = client.getOrders();

        response.assertThat().statusCode(200);
        response.assertThat().body(matchesJsonSchemaInClasspath("ordersList.json"));
    }
}
