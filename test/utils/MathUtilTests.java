package utils;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class MathUtilTests {

    private static final float MIN = 1.0f;
    private static final float MAX = 10.0f;

    public void shouldReturnMinIfSmallerThanMin() {
        assertEquals(MathUtil.clamp(0.3f, MIN, MAX), MIN);
    }

    public void shouldReturnMaxIfGreaterThanMax() {
        assertEquals(MathUtil.clamp(44f, MIN, MAX), MAX);
    }

    public void shouldReturnTheSameValueWhenValueBetweenMinAndMax() {
        assertEquals(MathUtil.clamp(5.5f, MIN, MAX), 5.5f);
    }
}
