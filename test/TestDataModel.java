import com.example.image_sorter.DataModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestDataModel {
    @Test
    public void testNewModelStandard() {
        DataModel Data = new DataModel("test/TestData/Case1");
        assertEquals(2, Data.Ratings.size());
    }
    @Test
    public void testNewModelEmpty() {
        DataModel Data = new DataModel("test/TestData/Case2");
        assertEquals(0, Data.Ratings.size());
    }
    @Test
    public void testNewModelAddImage() {
        DataModel Data = new DataModel("test/TestData/Case3");
        assertEquals(2, Data.Ratings.size());
    }
    @Test
    public void testCountUnrated() {
        DataModel model = new DataModel("test/TestData/Case4");
        int count = model.countUnrated();
        assertEquals(4, count);
    }

    @Test
    public void testRateImageUpdatesCount() throws Exception {
        DataModel model = new DataModel("test/TestData/Case5");
        int initialUnratedCount = model.countUnrated(); // Count before rating
        String randomImage = model.randomUnrated(); // Get a random unrated image
        model.rateImage(randomImage, 5); // Rate the image
        int finalUnratedCount = model.countUnrated(); // Count after rating

        // Assert that the unrated count decreases by 1
        assertEquals(initialUnratedCount - 1, finalUnratedCount);
    }
}
