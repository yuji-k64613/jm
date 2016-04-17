package com.jm.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//#GA=9.8		;Gravity (0<f<=98) [meter/second^2]
//#DR=0.50	;Dwell ratio (0.10<=f<=0.90)
//#HR=0.20	;Height (0.01<=f<=1.00) [meter]
//#SP=1.0		;Speed ratio (0.1<=f<=2.0)
//#BC=000		;Background color  Red Green Blue (0<=R,G,B<=F)
//#BP=1		;Beep ON (n=0,1)
//#HD=1		;Hand ON (n=0,1)
//#PD=1		;Pattern ON (n=0,1)
//#MR=0		;Switch right and left (n=0,1)
public class LineAnalyzer {
	public enum Type {
		COMMENT,
		PARAMETER,
		SEPARATOR,
		STYLE,
		FORMATION,
		PERSON,
		PMOTION,
		MOTION,
		SITESWAP,
		SPACE,
		NONE
	};
	
	private Pattern commentPattern = Pattern.compile("^;");
	private Pattern parameterPattern = Pattern.compile("^#([A-Z][A-Z])\\s*=\\s*([0-9]*(\\.[0-9]*)?)");
	private Pattern separatorPattern = Pattern.compile("^/(.*[^\\s])\\s*$");
	private Pattern separatorSubPattern = Pattern.compile("^\\[\\s*(.*[^\\s])\\s*\\]\\s*$");
	private Pattern stylePattern = Pattern.compile("^%(.*[^\\s])\\s*$");
	private Pattern formationPattern = Pattern.compile("^!(.*[^\\s])\\s*$");
	private Pattern personPattern = Pattern.compile("^\\$");	
	private Pattern motionPattern = Pattern.compile("^\\{\\s*(-?[0-9]+)\\s*,\\s*(-?[0-9]+)\\s*\\}\\s*\\{\\s*(-?[0-9]+)\\s*,\\s*(-?[0-9]+)\\s*\\}");
	private Pattern pmotionPattern = Pattern.compile("^\\{\\s*(-?[0-9]+)\\s*,\\s*(-?[0-9]+)\\s*\\}");
	private Pattern siteswapPattern = Pattern.compile("^([0-9A-Za-z,(\\)\\[\\]]+)(\\s+(.*[^\\s])\\s*$)?");	
	private Pattern spacePattern = Pattern.compile("^\\s*$");	

	private Matcher matcher;
	private String parameter;
	private double value1;
	private double value2;
	private double value3;
	private double value4;
	private String separator;
	private String style;
	private String siteswap;
	private String name;
	
	public Type analyze(String line){
		Matcher m = null;
		matcher = null;
		
		m = commentPattern.matcher(line);
		if (m.find()){
			matcher = m;
			return Type.COMMENT;
		}
		m = parameterPattern.matcher(line);
		if (m.find()){
			matcher = m;
			parameter = m.group(1);
			value1 = Double.parseDouble(m.group(2));
			return Type.PARAMETER;
		}
		m = separatorPattern.matcher(line);
		if (m.find()){
			matcher = m;
			separator = m.group(1);
			m = separatorSubPattern.matcher(separator);
			if (m.find()){
				separator = m.group(1);				
			}
			return Type.SEPARATOR;
		}
		m = stylePattern.matcher(line);
		if (m.find()){
			matcher = m;
			style = m.group(1);
			return Type.STYLE;
		}
		m = formationPattern.matcher(line);
		if (m.find()){
			matcher = m;
			name = m.group(1);
			return Type.FORMATION;
		}
		m = personPattern.matcher(line);
		if (m.find()){
			matcher = m;
			return Type.PERSON;
		}
		m = motionPattern.matcher(line);
		if (m.find()){
			matcher = m;
			value1 = Double.parseDouble(m.group(1));
			value2 = Double.parseDouble(m.group(2));
			value3 = Double.parseDouble(m.group(3));
			value4 = Double.parseDouble(m.group(4));
			return Type.MOTION;
		}
		m = pmotionPattern.matcher(line);
		if (m.find()){
			matcher = m;
			return Type.PMOTION;
		}
		m = siteswapPattern.matcher(line);
		if (m.find()){
			matcher = m;
			siteswap = m.group(1);
			name = (m.groupCount() > 1)? m.group(3) : null;
			return Type.SITESWAP;
		}
		m = spacePattern.matcher(line);
		if (m.find()){
			matcher = m;
			return Type.SPACE;
		}
		return Type.NONE;
	}

	public Matcher getMatcher() {
		return matcher;
	}

	public String getParameter() {
		return parameter;
	}

	public double getValue1() {
		return value1;
	}

	public double getValue2() {
		return value2;
	}

	public double getValue3() {
		return value3;
	}

	public double getValue4() {
		return value4;
	}

	public String getSeparator() {
		return separator;
	}

	public String getStyle() {
		return style;
	}
	
	public String getSiteswap() {
		return siteswap;
	}

	public String getName() {
		return name;
	}
}
