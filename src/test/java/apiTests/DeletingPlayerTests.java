package apiTests;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dataProviders.DataProviders;
import dto.PlayerCreateResponseDto;
import dto.PlayerDeleteRequestDto;
import dto.PlayerGetAllResponseDto;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.PropertyLoader;
import util.TestConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class DeletingPlayerTests {

    int createdId;
    String createdLogin;
    String createdPassword;
    String createdScreenName;

    @Test(priority = 0, dataProvider = "CreatePlayerForDeletingPlayerTests", dataProviderClass = DataProviders.class)
    public void createPlayer(String editor, int age, int responseCode) {

        createdLogin = RandomStringUtils.randomAlphanumeric(7);
        createdPassword = RandomStringUtils.randomAlphanumeric(10);
        createdScreenName = RandomStringUtils.randomAlphanumeric(9);

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

            PlayerCreateResponseDto playerCreateResponseBody = response.as(PlayerCreateResponseDto.class);
            createdId = playerCreateResponseBody.getId();

            Assert.assertEquals(playerCreateResponseBody.getLogin(), createdLogin);

    }

    @Test(priority = 1, dependsOnMethods = "createPlayer", dataProvider = "DeletePlayerForDeletingPlayerTests", dataProviderClass = DataProviders.class)
    public void deletePlayer(String editor, int statusCode) {

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
        if (response.getStatusCode() == 204) {
            getAllPlayersAfterDeleting();
        }

    }

    public void getAllPlayersAfterDeleting() {
        System.out.println("Deleting");

        String response = given()
                .spec(RequestSpecifications.basicSpec)
                .get(PropertyLoader.getProperty("GET_ALL_PLAYERS_ENDPOINT"))
                .then()
                .statusCode(200)
                .log().all()
                .extract().response().body().asString();
        System.out.println("Deleting");
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
