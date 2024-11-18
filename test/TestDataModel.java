import com.example.image_sorter.DataModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Test suite for the DataModel class
public class TestDataModel {

    // Test the constructor with a directory containing predefined data
    // requires: Case1 contains valid data
    // modifies: none
    // effects: verifies that the DataModel initializes Ratings with 2 entries
    @Test
    public void testNewModelStandard() {
        DataModel Data = new DataModel("test/TestData/Case1");
        assertEquals(2, Data.Ratings.size());

        Data = new DataModel("test/TestData/Case4");
        assertEquals(4, Data.Ratings.size());

        Data = new DataModel("test/TestData/Case5");
        assertEquals(5, Data.Ratings.size());
    }

    // Test the constructor with an empty directory
    // requires: Case2 is an empty directory
    // modifies: none
    // effects: verifies that the DataModel initializes with an empty Ratings list
    @Test
    public void testNewModelEmpty() {
        DataModel Data = new DataModel("test/TestData/Case2");
        assertEquals(0, Data.Ratings.size());
    }

    // Test the constructor with a directory containing new images
    // requires: Case3 contains two images not in Ratings.csv
    // modifies: none
    // effects: verifies that the DataModel adds the images to the Ratings list
    @Test
    public void testNewModelAddImage() {
        DataModel Data = new DataModel("test/TestData/Case3");
        assertEquals(2, Data.Ratings.size());

        Data = new DataModel("test/TestData/Case4");
        assertEquals(4, Data.Ratings.size());

        Data = new DataModel("test/TestData/Case5");
        assertEquals(5, Data.Ratings.size());
    }

    // Test the count of unrated images
    // requires: Case4 contains images with a mix of rated and unrated entries
    // modifies: none
    // effects: verifies that the count of unrated images matches the expected value
    @Test
    public void testCountUnrated() {
        DataModel model = new DataModel("test/TestData/Case4");
        int count = model.countUnrated();
        assertEquals(4, count);

        model = new DataModel("test/TestData/Case1");
        count = model.countUnrated();
        assertEquals(2, count);

        model = new DataModel("test/TestData/Case5");
        count = model.countUnrated();
        assertEquals(5, count);

    }

    // Test that rating an image decreases the count of unrated images
    // requires: Case5 contains unrated images
    // modifies: DataModel's Ratings list
    // effects: verifies that rating a random unrated image reduces the unrated count by 1
    @Test
    public void testRateImageUpdatesCount() throws Exception {
        DataModel model = new DataModel("test/TestData/Case5");

        // Count of unrated images before any updates
        int initialUnratedCount = model.countUnrated();

        // Select a random unrated image and rate it
        String randomImage = model.randomUnrated();
        model.rateImage(randomImage, 5);

        // Count of unrated images after the update
        int finalUnratedCount = model.countUnrated();

        // Assert that the unrated count has decreased by 1
        assertEquals(initialUnratedCount - 1, finalUnratedCount);
    }
}
