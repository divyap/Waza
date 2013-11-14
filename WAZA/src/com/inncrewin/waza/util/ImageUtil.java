package com.inncrewin.waza.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImageUtil {

	private static Log log = LogFactory.getLog(ImageUtil.class);

	private static String DEFAULT_IMAGE_PATH = "catalina.base";

	private static String DEFAULT_FOLDER_NAME = "wazaimages";

	public String doSaveImage(byte[] imagedata, String fileName) {
		String filePath = null;

		try {
			String serverDataDir = System.getProperty(DEFAULT_IMAGE_PATH) + "/wtpwebapps/WAZA/" + DEFAULT_FOLDER_NAME + "/";

			File file = new File(serverDataDir + fileName);
			File parent = file.getParentFile();
			if(!parent.exists() && !parent.mkdirs()){
			    throw new IllegalStateException("Couldn't create dir: " + parent);
			}

			if (file!=null) {
				file.createNewFile();
				BufferedImage buf= ImageIO.read(new ByteArrayInputStream(imagedata));
				FileOutputStream outputStream = new FileOutputStream(file);
				ImageIO.write(buf, "png", outputStream);	
				outputStream.close();
			}

		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return DEFAULT_FOLDER_NAME + "/" + fileName;
	}

	public static void main(String args[]) {

		new ImageUtil().doSaveImage(null, null);
	}
}
