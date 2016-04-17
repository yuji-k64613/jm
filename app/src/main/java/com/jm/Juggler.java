package com.jm;

import com.jm.opengl.JuggleDrawer;
import com.jm.utility.JmPatternException;
import com.jm.utility.Resource;

public class Juggler {
	private JuggleDrawer drawer;
	private Body body;
	private JugglingBalls balls;
	//private SiteswapWriter siteswap;

	private SiteSwap site;

	public Juggler(JuggleDrawer drawer) {
		this.drawer = drawer;
		body = new Body();
		balls = new JugglingBalls(body.getRightHand(), body.getLeftHand());
		drawer.setBalls(balls);
	}

	public SiteSwap getSiteSwap() {
		return site;
	}

	public void start(JmPattern jp) throws JmPatternException {
		if (!balls.initialize(jp)) {
			throw new JmPatternException();
		}
		site = jp.getSiteSwap();
		//siteswap = new SiteswapWriter(site);
		new SiteswapWriter(site);
	}

	public void updateJuggler(){
		balls.juggle(Resource.counter++);
		body.move();		
	}
	
	public void drawJuggler() {
		// move
//		balls.juggle(counter++);
//		body.move();

		// draw body
		if (JmPattern.ifShowBody()) {
			body.drawBody(drawer);
		}
//		if (JmPattern.ifShowSiteSwap()) {
//			siteswap.drawSiteswap(jCanvas);
//		}
		balls.drawBalls(drawer);
	}
}
