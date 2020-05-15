/**
 * @author Kilian
 *
 */
package com.github.kilianB;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RequireTest {

	// TODO for long tests actually use numbers > than int max

	@Nested
	class RequireExact {

		@Nested
		class Integer {

			@Test
			void testZero() {
				assertEquals(0,(int)Require.exact(0,0));
			}

			@Test
			void testMinimal() {
				assertEquals(java.lang.Integer.MIN_VALUE,(int)Require.exact(java.lang.Integer.MIN_VALUE,java.lang.Integer.MIN_VALUE));
			}

			@Test
			void testMaximal() {
				assertEquals(java.lang.Integer.MAX_VALUE,(int)Require.exact(java.lang.Integer.MAX_VALUE,java.lang.Integer.MAX_VALUE));
			}

			@Test
			void testInvalid() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.exact(2,1);
				});
			}
			
			@Test
			void testInvalidNegative() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.exact(-1,1);
				});
			}
		}
		
		@Nested
		class Double {

			@Test
			void testZero() {
				assertEquals(0d,(double)Require.exact(0d,0d));
			}

			@Test
			void testMinimal() {
				assertEquals(-java.lang.Double.MAX_VALUE,(double)Require.exact(-java.lang.Double.MAX_VALUE,-java.lang.Double.MAX_VALUE));
			}

			@Test
			void testMax() {
				assertEquals(java.lang.Double.MAX_VALUE,(double)Require.exact(java.lang.Double.MAX_VALUE,java.lang.Double.MAX_VALUE));
			}


			@Test
			void testInvalid() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.exact(2d,1d);
				});
			}
			
			@Test
			void testInvalidNegative() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.exact(-1d,1d);
				});
			}
		}

	}
	
	@Nested
	class RequirePositiveValue {

		@Nested
		class Integer {

			@Test
			void testZero() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(0);
				});
			}

			@Test
			void testMinimal() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(java.lang.Integer.MIN_VALUE);
				});
			}

			@Test
			void testMaximal() {
				assertEquals(java.lang.Integer.MAX_VALUE, (int) Require.positiveValue(java.lang.Integer.MAX_VALUE));
			}

			@Test
			void testValid() {
				assertEquals(1, (int) Require.positiveValue(1));
			}

			@Test
			void testInvalid() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(-1);
				});
			}
		}

		@Nested
		class Long {

			@Test
			void testZero() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(0L);
				});
			}

			@Test
			void testMinimal() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(java.lang.Long.MIN_VALUE);
				});
			}

			@Test
			void testMaximal() {
				assertEquals(java.lang.Long.MAX_VALUE, (long) Require.positiveValue(java.lang.Long.MAX_VALUE));
			}

			@Test
			void testValid() {
				assertEquals(1L, (long) Require.positiveValue(1L));
			}

			@Test
			void testInvalid() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(-1L);
				});
			}
		}

		@Nested
		class Double {

			@Test
			void testZero() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(0d);
				});
			}

			@Test
			void testMinimal() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(-java.lang.Double.MAX_VALUE);
				});
			}

			@Test
			void testMaximal() {
				assertEquals(java.lang.Double.MAX_VALUE, (double) Require.positiveValue(java.lang.Double.MAX_VALUE));
			}

			@Test
			void testValid() {
				assertEquals(1d, (double) Require.positiveValue(1d));
			}

			@Test
			void testInvalid() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(-1d);
				});
			}
		}

	}

	@Nested
	class RequireInRange {

		@Test
		void lowerBound() {
			int value = Require.inRange(0, 0, 1, "");
			assertEquals(value, 0);
		}

		@Test
		void upperBound() {
			int value = Require.inRange(1, 0, 1, "");
			assertEquals(value, 1);
		}

		void valid() {
			int value = Require.inRange(1, 0, 2, "");
			assertEquals(value, 2);
		}

		@Test
		void lowerBoundFail() {
			assertThrows(IllegalArgumentException.class, () -> {
				Require.inRange(-1, 0, 1, "");
			});
		}

		@Test
		void upperBoundFail() {
			assertThrows(IllegalArgumentException.class, () -> {
				Require.inRange(2, 0, 1, "");
			});
		}

		@Test
		void testPositiveInfinity() {
			assertEquals(5d, (double) Require.inRange(5d, 0d, Double.POSITIVE_INFINITY));
		}

		@Test
		void testNegativeInfinity() {
			assertEquals(5d, (double) Require.inRange(5d, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
		}

		@Test
		void testNaN() {
			assertThrows(IllegalArgumentException.class, () -> {
				Require.inRange(2, 0, Double.NaN, "");
			});
		}
	}

	@Nested
	class NonNull {

		@Test
		void empty() {
			Integer[] intArr = new Integer[] {};
			Require.nonNull(intArr, "");
		}

		@Test
		void valid() {
			Integer[] intArr = new Integer[] { new Integer(1), Integer.MAX_VALUE, Integer.MIN_VALUE };
			Require.nonNull(intArr, "");
		}

		@Test
		void invalid() {
			Integer[] intArr = new Integer[] { null, Integer.MAX_VALUE, Integer.MIN_VALUE };
			assertThrows(IllegalArgumentException.class, () -> {
				Require.nonNull(intArr, "");
			});
		}

		@Test
		void emptyList() {
			Require.nonNull(new ArrayList(), "");
		}

		@Test
		void validList() {
			Integer[] intArr = new Integer[] { new Integer(1), Integer.MAX_VALUE, Integer.MIN_VALUE };
			Require.nonNull(Arrays.asList(intArr), "");
		}

		@Test
		void invalidList() {
			Integer[] intArr = new Integer[] { null, Integer.MAX_VALUE, Integer.MIN_VALUE };
			assertThrows(IllegalArgumentException.class, () -> {
				Require.nonNull(Arrays.asList(intArr), "");
			});
		}

	}

	@Nested
	class RequireOddValue {

		@Nested
		class Integer {

			@Test
			void testZero() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.oddValue(0);
				});
			}

			@Test
			void testValid() {
				assertEquals(1, (int) Require.oddValue(1));
			}

			@Test
			void testValidNegative() {
				assertEquals(-1, (int) Require.oddValue(-1));
			}

			@Test
			void testInvalid() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.oddValue(2);
				});
			}

			@Test
			void testInvalidNegative() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.oddValue(-2);
				});
			}
		}

		@Nested
		class Long {

			@Test
			void testZero() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.oddValue(0l);
				});
			}

			@Test
			void testValid() {
				assertEquals(1l, (long) Require.oddValue(1l));
			}

			@Test
			void testValidNegative() {
				assertEquals(-1l, (long) Require.oddValue(-1l));
			}

			@Test
			void testInvalid() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.oddValue(2l);
				});
			}

			@Test
			void testInvalidNegative() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.oddValue(-2l);
				});
			}
		}

		@Nested
		class Double {

			@Test
			void testZero() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.oddValue(0d);
				});
			}

			@Test
			void testValid() {
				assertEquals(1d, (double) Require.oddValue(1d));
			}

			@Test
			void testValidNegative() {
				assertEquals(-1d, (double) Require.oddValue(-1d));
			}

			@Test
			void testInvalid() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.oddValue(2d);
				});
			}

			@Test
			void testInvalidNegative() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.oddValue(-2d);
				});
			}

			@Test
			void testPositiveInfinity() {
				assertEquals(java.lang.Double.POSITIVE_INFINITY, (double)Require.oddValue(java.lang.Double.POSITIVE_INFINITY));
			}

			@Test
			void testNegativeInfinity() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.oddValue(java.lang.Double.NEGATIVE_INFINITY);
				});
			}

			@Test
			void testNaN() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.oddValue(java.lang.Double.NaN);
				});
			}
		}

	}

	@Nested
	class RequireEvenValue {

		@Nested
		class Integer {

			@Test
			void testZero() {
				assertEquals(0, (int) Require.evenValue(0));
			}

			@Test
			void testValid() {
				assertEquals(2, (int) Require.evenValue(2));
			}

			@Test
			void testValidNegative() {
				assertEquals(-2, (int) Require.evenValue(-2));
			}

			@Test
			void testInvalid() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.evenValue(1);
				});
			}

			@Test
			void testInvalidNegative() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.evenValue(-1);
				});
			}
		}

		@Nested
		class Long {

			@Test
			void testZero() {
				assertEquals(0l, (long) Require.evenValue(0));
			}

			@Test
			void testValid() {
				assertEquals(2l, (long) Require.evenValue(2l));
			}

			@Test
			void testValidNegative() {
				assertEquals(-2l, (long) Require.evenValue(-2l));
			}

			@Test
			void testInvalid() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.evenValue(1l);
				});
			}

			@Test
			void testInvalidNegative() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.evenValue(-1l);
				});
			}
		}

		@Nested
		class Double {

			@Test
			void testZero() {
				assertEquals(0d, (double) Require.evenValue(0d));
			}

			@Test
			void testValid() {
				assertEquals(2d, (double) Require.evenValue(2d));
			}

			@Test
			void testValidNegative() {
				assertEquals(-2d, (double) Require.evenValue(-2d));
			}

			@Test
			void testInvalid() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.evenValue(1d);
				});
			}

			@Test
			void testInvalidNegative() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.evenValue(-1d);
				});
			}

			//TODO define
			@Test
			@Disabled
			void testPositiveInfinity() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.evenValue(java.lang.Double.POSITIVE_INFINITY);
				});
			}

			@Test
			@Disabled
			void testNegativeInfinity() {
				assertThrows(IllegalArgumentException.class, () -> {
					
				});
			}

			@Test
			void testNaN() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.evenValue(java.lang.Double.NaN);
				});
			}
		}

	}
}