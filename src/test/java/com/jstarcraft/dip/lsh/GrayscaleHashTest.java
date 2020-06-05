package com.jstarcraft.dip.lsh;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class GrayscaleHashTest {

    @Nested
    @DisplayName("Algorithm Id")
    class AlgorithmId {
        /**
         * The algorithms id shall stay consistent throughout different instances of the
         * jvm. While simple hashcodes do not guarantee this behavior hash codes created
         * from strings and integers are by contract consistent.
         */
        @Test
        @DisplayName("Consistent AlgorithmIds")
        public void consistency() {
            assertAll(() -> {
                assertEquals(471804845, new GrayscaleHash(14).algorithmId());
            }, () -> {
                assertEquals(471835597, new GrayscaleHash(25).algorithmId());
            });
        }

        @Test
        @DisplayName("Consistent AlgorithmIds v 2.0.0 collision")
        public void notVersionTwo() {
            assertAll(() -> {
                assertNotEquals(1308249156, new GrayscaleHash(14).algorithmId());
            }, () -> {
                assertNotEquals(1308249156, new GrayscaleHash(14).algorithmId());
            });
        }
    }

    // Base Hashing algorithm tests
    @Nested
    class AlgorithmBaseTests extends HashTestBase {

        @Override
        protected HashingAlgorithm getInstance(int bitResolution) {
            return new GrayscaleHash(bitResolution);
        }

        @Override
        protected double differenceBallonHqHash() {
            return 76;
        }

        @Override
        protected double normDifferenceBallonHqHash() {
            return 76 / 132d;
        }

    }

}
