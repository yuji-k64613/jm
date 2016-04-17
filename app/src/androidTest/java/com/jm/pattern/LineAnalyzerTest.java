package com.jm.pattern;

import java.util.regex.Matcher;

import junit.framework.TestCase;

public class LineAnalyzerTest extends TestCase {
	public void testAnalyze_001() {
		LineAnalyzer a = new LineAnalyzer();
		LineAnalyzer.Type t;

		t = a.analyze(";abcdefg");

		assertEquals(LineAnalyzer.Type.COMMENT, t);
	}

	public void testAnalyze_011() {
		LineAnalyzer a = new LineAnalyzer();
		LineAnalyzer.Type t;

		t = a.analyze("#AB=1");

		assertEquals(LineAnalyzer.Type.PARAMETER, t);
		assertEquals(1.0, a.getValue1(), 0);
	}

	public void testAnalyze_012() {
		LineAnalyzer a = new LineAnalyzer();
		LineAnalyzer.Type t;

		t = a.analyze("#AB=1.23");

		assertEquals(LineAnalyzer.Type.PARAMETER, t);
		assertEquals(1.23, a.getValue1(), 0);
	}

	public void testAnalyze_021() {
		LineAnalyzer a = new LineAnalyzer();
		LineAnalyzer.Type t;

		t = a.analyze("/[ abcdefg ]");

		assertEquals(LineAnalyzer.Type.SEPARATOR, t);
		assertEquals("abcdefg", a.getSeparator());
	}

	public void testAnalyze_022() {
		LineAnalyzer a = new LineAnalyzer();
		LineAnalyzer.Type t;

		t = a.analyze("/abcdefg");

		assertEquals(LineAnalyzer.Type.SEPARATOR, t);
		assertEquals("abcdefg", a.getSeparator());
	}

	public void testAnalyze_031() {
		LineAnalyzer a = new LineAnalyzer();
		LineAnalyzer.Type t;

		t = a.analyze("%abcdefg   ");

		assertEquals(LineAnalyzer.Type.STYLE, t);
		assertEquals("abcdefg", a.getStyle());
	}

	public void testAnalyze_041() {
		LineAnalyzer a = new LineAnalyzer();
		LineAnalyzer.Type t;

		t = a.analyze("!abcdefg");

		assertEquals(LineAnalyzer.Type.FORMATION, t);
	}

	public void testAnalyze_051() {
		LineAnalyzer a = new LineAnalyzer();
		LineAnalyzer.Type t;

		t = a.analyze("$abcdefg");

		assertEquals(LineAnalyzer.Type.PERSON, t);
	}

	public void testAnalyze_061() {
		LineAnalyzer a = new LineAnalyzer();
		LineAnalyzer.Type t;

		t = a.analyze("{123,45}{ 6 , 7 }");

		assertEquals(LineAnalyzer.Type.MOTION, t);

		// Matcher m = a.getMatcher();
		// assertEquals("123", m.group(1));
		// assertEquals("45", m.group(2));
		// assertEquals("6", m.group(3));
		// assertEquals("7", m.group(4));
		assertEquals(123.0, a.getValue1(), 0);
		assertEquals(45.0, a.getValue2(), 0);
		assertEquals(6.0, a.getValue3(), 0);
		assertEquals(7.0, a.getValue4(), 0);
	}

	public void testAnalyze_062() {
		LineAnalyzer a = new LineAnalyzer();
		LineAnalyzer.Type t;

		t = a.analyze("{-123,-45}{ -6 , -7 }");

		assertEquals(LineAnalyzer.Type.MOTION, t);

		Matcher m = a.getMatcher();
		assertEquals("-123", m.group(1));
		assertEquals("-45", m.group(2));
		assertEquals("-6", m.group(3));
		assertEquals("-7", m.group(4));
	}
}
