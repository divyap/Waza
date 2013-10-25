package com.inncrewin.waza.util;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inncrewin.waza.hibernate.Cook;
import com.inncrewin.waza.main.UserDAO;

public class JAXBParser {

	private static Log log = LogFactory.getLog(UserDAO.class);
	
	public Object unmarshalXmlString(String inputXml, Class clazz){
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);

			Unmarshaller unmarshaller = jc.createUnmarshaller();
			StreamSource xmlSource = new StreamSource(new StringReader(inputXml));
			JAXBElement<Cook> cookEle = unmarshaller.unmarshal(xmlSource, clazz);
			return cookEle.getValue();
			
		} catch (JAXBException e) {
			log.error(e.getMessage());
			return null;
		}
	}
}
