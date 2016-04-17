package com.jm.pattern;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

import com.jm.JmPattern;
//import org.junit.Test;
import com.jm.utility.Debug;

public class PatternAnalyzerTest extends TestCase {
	public void test() throws IOException {
		String filename = "pattern.jm";
		InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
		
		PatternAnalyzer pa = new PatternAnalyzer();
		pa.analyzer(is);

		Debug.d(this, "=================================================");
		List<JmPattern> pattern = pa.pattern;
		for (JmPattern p : pattern) {
			Debug.d(this, p.getType() + "\t" + p.toString());
		}
	}
}
