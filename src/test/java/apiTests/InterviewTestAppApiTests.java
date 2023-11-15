package apiTests;

import cache.TestCache;
import cache.TestCacheKey;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dataProviders.DataProviders;
import dto.*;

import io.restassured.response.Response;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import org.testng.asserts.SoftAssert;
import util.PropertyLoader;
import util.TestConstants;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;


public class InterviewTestAppApiTests {


    @Test(priority = 0, dataProvider = "CreatePlayer", dataProviderClass = DataProviders.class)
    public void createPlayer(String editor, int age, int responseCode) {

        String createdLogin = RandomStringUtils.randomAlphanumeric(7);
        String createdPassword = RandomStringUtils.randomAlphanumeric(10);
        String createdScreenName = RandomStringUtils.randomAlphanumeric(9);

        Response response = given()
                .spec(RequestSpecifications.basicSpec)
                .when()
                .pathParam("editor", editor)
                .queryParam("age", age)
                .queryParam("gender", TestConstants.CREATED_GENDER)
                .queryParam("login", createdLogin)
                .queryParam("password", createdPassword)
                .queryParam("role", TestConstants.ROLE)
                .queryParam("screenName", createdScreenName)
                .get(PropertyLoader.getProperty("CREATE_PLAYER_ENDPOINT") + "{editor}")
                .then()
                .statusCode(responseCode)
                .log().all()
                .extract().response();
        if (response.getStatusCode() == 200) {
            TestCache.putDataInCache(TestCacheKey.LOGIN, createdLogin);
            TestCache.putDataInCache(TestCacheKey.PASSWORD, createdPassword);
            TestCache.putDataInCache(TestCacheKey.SCREEN_NAME, createdScreenName);

            PlayerCreateResponseDto playerCreateResponseBody = response.as(PlayerCreateResponseDto.class);
            TestCache.putDataInCache(TestCacheKey.ID, playerCreateResponseBody.getId());

            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(playerCreateResponseBody.getLogin(), createdLogin);
            softAssert.assertEquals(playerCreateResponseBody.getPassword(), createdPassword);
            softAssert.assertEquals(playerCreateResponseBody.getScreenName(), createdScreenName);
            softAssert.assertEquals(playerCreateResponseBody.getAge(), age);
            softAssert.assertEquals(playerCreateResponseBody.getRole(), TestConstants.ROLE);
            softAssert.assertAll();

        }
    }

    @Test(priority = 1, dependsOnMethods = "createPlayer", dataProvider = "GetPlayerByPlayerId", dataProviderClass = DataProviders.class)
    public void getPlayerByPlayerId(int playerId, int responseCode) {

        String createdLogin = TestCache.getStringFromCache(TestCacheKey.LOGIN);
        String createdPassword = TestCache.getStringFromCache(TestCacheKey.PASSWORD);
        String createdScreenName = TestCache.getStringFromCache(TestCacheKey.SCREEN_NAME);

        PlayerGetByPlayerIdRequestDto playerGetByPlayerIdRequestBody = PlayerGetByPlayerIdRequestDto.builder()
                .playerId(playerId).build();

        Response response = given()
                .spec(RequestSpecifications.basicSpec)
                .body(playerGetByPlayerIdRequestBody)
                .when()
                .post(PropertyLoader.getProperty("GET_PLAYER_ENDPOINT"))
                .then()
                .statusCode(responseCode)
                .log().all()
                .extract().response();

        if (response.getStatusCode() == 200) {
            PlayerGetByPlayerIdResponseDto playerGetByPlayerIdResponseBody = response.as(PlayerGetByPlayerIdResponseDto.class);

            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(playerGetByPlayerIdResponseBody.getId(), playerId);
            softAssert.assertEquals(playerGetByPlayerIdResponseBody.getLogin(), createdLogin);
            softAssert.assertEquals(playerGetByPlayerIdResponseBody.getPassword(), createdPassword);
            softAssert.assertEquals(playerGetByPlayerIdResponseBody.getScreenName(), createdScreenName);
            softAssert.assertAll();
        }
    }


    @Test(priority = 2, dependsOnMethods = "getPlayerByPlayerId", dataProvider = "UpdatePlayer", dataProviderClass = DataProviders.class)
    public void updatePlayer(String editor, int responseCode) {

        String updatedLogin = RandomStringUtils.randomAlphanumeric(7);
        String updatedPassword = RandomStringUtils.randomAlphanumeric(10);
        String updatedScreenName = RandomStringUtils.randomAlphanumeric(9);
        int createdId = TestCache.getIntegerFromCache(TestCacheKey.ID);
        int updatedAge = new Random().nextInt((60 - 17) + 1) + 17;

        PlayerUpdateRequestDto playerUpdateRequestBody = PlayerUpdateRequestDto.builder()
                .age(updatedAge)
                .gender("female")
                .login(updatedLogin)
                .password(updatedPassword)
                .screenName(updatedScreenName)
                .build();

        Response response = given()
                .spec(RequestSpecifications.basicSpec)
                .body(playerUpdateRequestBody)
                .pathParam("editor", editor)
                .pathParam("id", createdId)
                .when()
                .patch(PropertyLoader.getProperty("UPDATE_PLAYER_ENDPOINT") + "{editor}/{id}")
                .then()
                .statusCode(responseCode)
                .log().all()
                .extract().response();

        if (response.getStatusCode() == 200) {
            TestCache.putDataInCache(TestCacheKey.LOGIN, updatedLogin);
            TestCache.putDataInCache(TestCacheKey.PASSWORD, updatedPassword);
            TestCache.putDataInCache(TestCacheKey.SCREEN_NAME, updatedScreenName);
            TestCache.putDataInCache(TestCacheKey.AGE, updatedAge);

            PlayerUpdateResponseDto playerUpdateResponseBody = response.as(PlayerUpdateResponseDto.class);

            SoftAssert soft = new SoftAssert();
            soft.assertEquals(updatedLogin, playerUpdateResponseBody.getLogin());
            soft.assertEquals(playerUpdateResponseBody.getScreenName(), updatedScreenName);
            soft.assertEquals(playerUpdateResponseBody.getId(), createdId);
            soft.assertEquals(playerUpdateResponseBody.getGender(), TestConstants.UPDATED_GENDER);
            soft.assertEquals(playerUpdateResponseBody.getRole(), TestConstants.ROLE);
            soft.assertAll();
        }
    }

    @Test(priority = 4, dependsOnMethods = "updatePlayer")
    public void getAllPlayers() {

        int createdId = TestCache.getIntegerFromCache(TestCacheKey.ID);
        String updatedScreenName = TestCache.getStringFromCache(TestCacheKey.SCREEN_NAME);

        String response = given()
                .spec(RequestSpecifications.basicSpec)
                .get(PropertyLoader.getProperty("GET_ALL_PLAYERS_ENDPOINT"))
                .then()
                .statusCode(200)
                .log().all()
                .extract().response().body().asString();

        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("players");

        PlayerGetAllResponseDto.Player[] playersArr = new Gson().fromJson(jsonArray, PlayerGetAllResponseDto.Player[].class);
        List<PlayerGetAllResponseDto.Player> playerGetAllResponseList = Arrays.asList(playersArr);

        Assert.assertNotEquals(playerGetAllResponseList.size(), 0);

        List<Integer> playersId = new ArrayList<>();
        for (PlayerGetAllResponseDto.Player player : playerGetAllResponseList) {
            playersId.add(player.id);
        }
        List<String> playersLogin = new ArrayList<>();
        for (PlayerGetAllResponseDto.Player player : playerGetAllResponseList) {
            playersLogin.add(player.screenName);
        }

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(playersId.contains(createdId));
        softAssert.assertTrue(playersLogin.contains(updatedScreenName));
        softAssert.assertAll();
    }

    @Test(priority = 5, dependsOnMethods = "getAllPlayers", dataProvider = "DeletePlayer", dataProviderClass = DataProviders.class)
    public void deletePlayer(int createdId, String editor, int statusCode) {

        PlayerDeleteRequestDto playerDeleteRequestBody = PlayerDeleteRequestDto.builder()
                .playerId(createdId).build();

        Response response = given()
                .spec(RequestSpecifications.basicSpec)
                .pathParam("editor", editor)
                .body(playerDeleteRequestBody)
                .delete(PropertyLoader.getProperty("DELETE_PLAYER_ENDPOINT") + "{editor}")
                .then()
                .statusCode(statusCode)
                .log().all()
                .extract().response();
        if (response.getStatusCode() == 204)
            getAllPlayersAfterDeleting();
    }


    public void getAllPlayersAfterDeleting() {

        Integer createdId = TestCache.getIntegerFromCache(TestCacheKey.ID);
        String response = given()
                .spec(RequestSpecifications.basicSpec)
                .get(PropertyLoader.getProperty("GET_ALL_PLAYERS_ENDPOINT"))
                .then()
                .statusCode(200)
                .log().all()
                .extract().response().body().asString();
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("players");

        PlayerGetAllResponseDto.Player[] playersArr = new Gson().fromJson(jsonArray, PlayerGetAllResponseDto.Player[].class);

        List<PlayerGetAllResponseDto.Player> playerGetAllResponseList = Arrays.asList(playersArr);

        Assert.assertNotEquals(playerGetAllResponseList.size(), 0);

        List<Integer> playersId = new ArrayList<>();
        for (PlayerGetAllResponseDto.Player player : playerGetAllResponseList) {
            playersId.add(player.id);
        }
        Assert.assertFalse(playersId.contains(createdId));
    }
}
