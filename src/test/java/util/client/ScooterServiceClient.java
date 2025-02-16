package util.client;


import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;

import io.restassured.response.ValidatableResponse;

import util.model.Courier;
import util.model.Credentials;
import util.model.Order;
import static util.Constant.*;

import static io.restassured.RestAssured.given;


public class ScooterServiceClient {

    @Step("Клиент – создание курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .body(courier)
                .post(COURIER_CREATE_TEST_URL)
                .then();
    }

    @Step("Клиент – логин курьера")
    public ValidatableResponse login(Credentials credentials) {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .body(credentials)
                .post(COURIER_LOGIN_TEST_URL)
                .then();
    }

    @Step("Клиент – получение списка заказов")
    public ValidatableResponse getOrders() {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .queryParam("limit", "2")
                .get(ORDERS_LIST_GET_TEST_URL)
                .then();
    }

    @Step("Клиент – удаление курьера")
    public ValidatableResponse deleteCourier(String id) {
        return given()
                .filter(new AllureRestAssured())
                .delete(COURIER_DELETE_TEST_URL + id)
                .then();
    }

    @Step("Клиент – создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .filter(new AllureRestAssured())
                .body(order)
                .post(ORDER_CREATE_TEST_URL)
                .then();
    }

    @Step("Клиент - удаление заказа")
    public ValidatableResponse cancelOrder(String id) {
        return given()
                .filter(new AllureRestAssured())
                .queryParam("track", id)
                .put(ORDER_CANCEL_TEST_URL)
                .then();
    }

}
