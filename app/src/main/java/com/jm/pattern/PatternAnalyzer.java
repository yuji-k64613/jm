package com.jm.pattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jm.JmPattern;

public class PatternAnalyzer {
	public List<JmPattern> pattern = new ArrayList<JmPattern>();
	public List<String> title = new ArrayList<String>();

	private final static double RATIO = 100.0;

	enum Status {
		NORMAL, CALL_STYLE, STYLE, CALL_FORMATION, FORMATION, NONE
	}

	public void analyzer(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));

		Status status = Status.NORMAL;
		int type = -1;
		int id = 1;
		JmPattern p;

		double height = 100;
		double dwell = 200;

		boolean isSkip = false;
		String parameter;
		String separator;
		double value;
		String motionName = "";
		Map<String, List<byte[]>> motionMap = new HashMap<String, List<byte[]>>();
		List<byte[]> motionBuffer = new ArrayList<byte[]>();
		byte[] m = new byte[4];

		try {
			LineAnalyzer la = new LineAnalyzer();
			String line;

			m[0] = (byte) 13;
			m[1] = (byte) 0;
			m[2] = (byte) 4;
			m[3] = (byte) 0;
			motionBuffer = new ArrayList<byte[]>();
			motionBuffer.add(m.clone());
			motionMap.put("Normal", motionBuffer);			
			motionBuffer = new ArrayList<byte[]>();
			
			while ((line = br.readLine()) != null) {
				LineAnalyzer.Type t = la.analyze(line);

				if (t == LineAnalyzer.Type.COMMENT) {
					continue;
				}

				switch (status) {
				case NORMAL:
				case FORMATION:
					switch (t) {
					case PARAMETER:
						parameter = la.getParameter();
						value = la.getValue1();
						if (parameter.equals("HR")) {
							if (value < 0.01){
								value = 0.01;
							}
							else if (value > 1.0){
								value = 1.0;
							}
							height = value;
						} else if (parameter.equals("DR")) {
							if (value < 0.1){
								value = 0.1;
							}
							else if (value > 0.9){
								value = 0.9;
							}
							dwell = value;
						}
						break;
					case SEPARATOR:
						separator = la.getSeparator();
						title.add(separator);
						isSkip = false;
						type++;
						status = Status.NORMAL;
						break;
					case STYLE:
						status = Status.CALL_STYLE;
						break;
					case FORMATION:
						if (la.getName().equals("1-Person")) {
							status = Status.NORMAL;
						} else {
							isSkip = true;
							status = Status.CALL_FORMATION;
						}
						break;
					case SITESWAP:
						if (isSkip) {
							break;
						}
						p = new JmPattern(id++, type,
								(la.getName() != null) ? la.getName() : la
										.getSiteswap(), la.getSiteswap(),
								(int) (height * RATIO), (int) (dwell * RATIO),
								toArray(motionBuffer));
						pattern.add(p);
						break;
					default:
						break;
					}
					break;
				case CALL_STYLE:
					motionName = la.getStyle();
					List<byte[]> motion = motionMap.get(motionName);
					if (motion != null){
						motionBuffer = motion;
					}
					
					switch (t) {
					case MOTION:
						m[0] = (byte) la.getValue1();
						m[1] = (byte) la.getValue2();
						m[2] = (byte) la.getValue3();
						m[3] = (byte) la.getValue4();

						motionBuffer = new ArrayList<byte[]>();
						motionBuffer.add(m.clone());
						motionMap.put(motionName, motionBuffer);
						status = Status.STYLE;
						break;
					case FORMATION:
						if (la.getName().equals("1-Person")) {
							status = Status.NORMAL;
						} else {
							isSkip = true;
							status = Status.CALL_FORMATION;
						}
						break;
					case SITESWAP:
						if (isSkip) {
							break;
						}
						p = new JmPattern(id++, type,
								(la.getName() != null) ? la.getName() : la
										.getSiteswap(), la.getSiteswap(),
								(int) (height * RATIO), (int) (dwell * RATIO),
								toArray(motionBuffer));
						pattern.add(p);
						break;
					default:
						status = Status.NORMAL;
						break;
					}
					break;
				case STYLE:
					switch (t) {
					case FORMATION:
						if (la.getName().equals("1-Person")) {
							status = Status.NORMAL;
						} else {
							isSkip = true;
							status = Status.CALL_FORMATION;
						}
						break;
					case MOTION:
						m[0] = (byte) la.getValue1();
						m[1] = (byte) la.getValue2();
						m[2] = (byte) la.getValue3();
						m[3] = (byte) la.getValue4();
						motionBuffer.add(m.clone());
						break;
					case SITESWAP:
						if (isSkip) {
							break;
						}
						p = new JmPattern(id++, type,
								(la.getName() != null) ? la.getName() : la
										.getSiteswap(), la.getSiteswap(),
								(int) (height * RATIO), (int) (dwell * RATIO),
								toArray(motionBuffer));
						pattern.add(p);
						break;
					default:
						status = Status.NORMAL;
						break;
					}
					break;
				case CALL_FORMATION:
					switch (t) {
					case FORMATION:
						if (la.getName().equals("1-Person")) {
							status = Status.NORMAL;
						} else {
							isSkip = true;
							status = Status.CALL_FORMATION;
						}
						break;
					case PMOTION:
						isSkip = true;
						status = Status.FORMATION;
						break;
					default:
						isSkip = true;
						status = Status.NORMAL;
						break;
					}
					break;
				default:
					break;
				}
				// System.out.println(status + " " + t + "\t" + line + "\t" + sSkip);
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	public List<JmPattern> getPattern() {
		return pattern;
	}

	public List<String> getTitle() {
		return title;
	}

	private byte[][] toArray(List<byte[]> l) {
		byte[][] b = new byte[l.size()][];
		for (int i = 0; i < l.size(); i++) {
			b[i] = l.get(i);
		}
		return b;
	}
}
