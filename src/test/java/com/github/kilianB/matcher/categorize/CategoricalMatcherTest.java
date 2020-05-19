package com.github.kilianB.matcher.categorize;

import com.jstarcraft.dip.lsh.AverageHash;
import com.jstarcraft.dip.lsh.HashingAlgorithm;

/**
 * @author Kilian
 *
 */
class CategoricalMatcherTest extends CategorizeBaseTest {

	@Override
	CategoricalMatcher getInstance() {
		CategoricalMatcher matcher = new CategoricalMatcher(.2);
		HashingAlgorithm hasher = new AverageHash(32);
		matcher.addHashingAlgorithm(hasher);
		return matcher;
	}

}
