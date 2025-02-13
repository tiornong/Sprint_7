package util.client;


import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;

import io.restassured.response.ValidatableResponse;

import util.model.Courier;
import util.model.Credentials;
import util.model.Order;


import static io.restassured.RestAssured.given;


public class ScooterServiceClient {

    private final String baseURI;

    public ScooterServiceClient(String baseURI) {
        this.baseURI = baseURI;
    }

    @Step("Клиент – создание курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then();
    }

    @Step("Клиент – логин курьера")
    public ValidatableResponse login(Credentials credentials) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(credentials)
                .post("/api/v1/courier/login")
                .then();
    }

    @Step("Клиент – получение списка заказов")
    public ValidatableResponse getOrders() {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .queryParam("limit", "2")
                .get("/api/v1/orders")
                .then();
    }

    @Step("Клиент – удаление курьера")
    public ValidatableResponse deleteCourier(String id) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(baseURI)
                .delete(String.format("/api/v1/courier/%s", id))
                .then();
    }

    @Step("Клиент – создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(baseURI)
                .body(order)
                .post("/api/v1/orders")
                .then();
    }

}
