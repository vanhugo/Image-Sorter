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
}
