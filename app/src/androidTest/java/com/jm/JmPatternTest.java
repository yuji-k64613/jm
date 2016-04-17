package com.jm;

import junit.framework.TestCase;

public class JmPatternTest extends TestCase {
	public void testJmPatternIntIntStringStringIntIntByteArrayArray() {
		JmPattern jp = new JmPattern(1, 2, "name", "siteswap", 3, 4, null);
		assertEquals("name", jp.toString());
	}
}
