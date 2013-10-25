package com.inncrewin.waza.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImageUtil {

	private static Log log = LogFactory.getLog(ImageUtil.class);

	private static String DEFAULT_IMAGE_PATH = "jboss.server.data.dir";

	private static String DEFAULT_FOLDER_NAME = "waza";

	public String doSaveImage(InputStream imageFile) {
		String filePath = null;

		try {
			String serverDataDir = System.getProperty(DEFAULT_IMAGE_PATH);

			File file = new File(serverDataDir + "/" + DEFAULT_FOLDER_NAME);

			if (file.exists()) {
				BufferedImage image = ImageIO.read(imageFile);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ImageIO.write(image, "jpeg", outputStream);
			}

		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return filePath;
	}

	public static void main(String args[]) {

		new ImageUtil().doSaveImage(null);
	}
}
