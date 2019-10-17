package uk.ac.soton.ecs.jsh2.l2c;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openimaj.content.slideshow.PictureSlide;
import org.openimaj.content.slideshow.Slide;
import org.openimaj.content.slideshow.SlideshowApplication;

import jep.JepException;

public class App {
	private static BufferedImage background = null;
	static int SLIDE_WIDTH;
	static int SLIDE_HEIGHT;

	static {
		final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		final Dimension size = device.getDefaultConfiguration().getBounds().getSize();
		SLIDE_WIDTH = size.width;
		if (SLIDE_WIDTH > 1920)
			SLIDE_WIDTH = 1920;
		SLIDE_HEIGHT = SLIDE_WIDTH * 10 / 16;
	}

	/**
	 * Main method
	 *
	 * @param args
	 *            ignored
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, JepException {
		final List<Slide> slides = new ArrayList<Slide>();

		for (int i = 0; i < 49; i++) {
			slides.add(new PictureSlide(App.class.getResource(String.format("slides/slide_%04d.jpg", i))));
		}

		slides.set(7, new RetinaNetDemo("FaceTime"));

		new SlideshowApplication(slides, SLIDE_WIDTH, SLIDE_HEIGHT, getBackground());
	}

	/**
	 * @return the generic slide background
	 */
	public synchronized static BufferedImage getBackground() {
		if (background == null) {
			background = new BufferedImage(SLIDE_WIDTH, SLIDE_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
			final Graphics2D g = background.createGraphics();
			g.setColor(Color.WHITE);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.fillRect(0, 0, background.getWidth(), background.getHeight());
		}
		return background;
	}

	public static int getVideoWidth(int remainder) {
		final int avail = SLIDE_WIDTH - remainder;
		if (avail > 1280)
			return 1280;
		if (avail > 960)
			return 960;
		if (avail > 640)
			return 640;
		return 320;
	}

	public static int getVideoHeight(int remainder) {
		final int width = getVideoWidth(remainder);
		switch (width) {
		case 1280:
			return 720;
		case 960:
			return 540;
		case 640:
			return 480;
		}
		return 240;
	}
}
