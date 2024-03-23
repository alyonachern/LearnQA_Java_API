import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckLengthTest {

    @ParameterizedTest
    @ValueSource(strings = {"Lorem ipsum dolor sit", "Short text"})
    public void checkLengthTest(String text) {
        assertTrue(text.length() > 15, "Length of text is less than 15 symbols");
    }
}
