package dataProviders;

import cache.TestCache;
import cache.TestCacheKey;
import org.testng.annotations.DataProvider;

import java.util.Random;

public class DataProviders {
    @DataProvider(name = "CreatePlayer")
    public Object[][] getDataForCreatingNewPlayer() {
        return new Object[][]{{"supervisor", new Random().nextInt((60-17)+1)+17, 200},
                {"admin", new Random().nextInt((16-1)+1)+1, 403}};
    }

    @DataProvider(name = "CreatePlayerForCreatingPlayerTests")
    public Object[][] getDataForCreatingNewPlayerForCreatingPlayerTests() {
        return new Object[][]{{"supervisor", new Random().nextInt((60-17)+1)+17, 200},
                {"supervisor", new Random().nextInt((16-1)+1)+1, 403},
                {"supervisor", new Random().nextInt((100-60)+1)+60, 403},
                {"admin", new Random().nextInt((60-17)+1)+17, 200},
                {"admin", new Random().nextInt((16-1)+1)+1, 403},
                {"admin", new Random().nextInt((100-60)+1)+60, 403},
                {"user", new Random().nextInt((60-17)+1)+17, 200},
                {"user", new Random().nextInt((16-1)+1)+1, 401},
                {"user", new Random().nextInt((100-60)+1)+60, 401}};
    }

    @DataProvider(name = "CreatePlayerForDeletingPlayerTests")
    public Object[][] getDataForCreatingNewPlayerForDeletingPlayerTests() {
        return new Object[][]{{"supervisor", new Random().nextInt((60-17)+1)+17, 200}};
    }

    @DataProvider(name = "GetPlayerByPlayerId")
    public Object[][] getDataForGettingPlayer(){
        return new Object[][] {{TestCache.getIntegerFromCache(TestCacheKey.ID), 200}};
    }

    @DataProvider(name = "UpdatePlayer")
    public Object[][] getDataForUpdatingPlayer(){
        return new  Object[][] {{"supervisor", 200}, {"user", 403}};
    }

    @DataProvider(name = "DeletePlayer")
    public Object[][] getDataForDeletingPlayer(){
        return new  Object[][] {{TestCache.getIntegerFromCache(TestCacheKey.ID)+1, "supervisor", 204},
                {TestCache.getIntegerFromCache(TestCacheKey.ID), "supervisor", 200}};
    }

    @DataProvider(name = "DeletePlayerForCreatingPlayerTests")
    public Object[][] getDataForDeletingPlayerForCreatingPlayerTests(){
        return new  Object[][] {{"supervisor", 200}};
    }

    @DataProvider(name = "DeletePlayerForDeletingPlayerTests")
    public Object[][] getDataForDeletingPlayerForDeletingPlayerTests(){
        return new  Object[][] {{"user", 401}};
    }
}
