package com.inncrewin.waza.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inncrewin.waza.main.UserDAO;

public class JAXBParser {

	private static Log log = LogFactory.getLog(UserDAO.class);
	
	public Object unmarshalXmlString(String inputXml, Class clazz){
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);

			Unmarshaller unmarshaller = jc.createUnmarshaller();
			StreamSource xmlSource = new StreamSource(new StringReader(inputXml));
			JAXBElement ele = unmarshaller.unmarshal(xmlSource, clazz);
			return ele.getValue();
			
		} catch (JAXBException e) {
			log.error(e.getMessage());
			return CommonUtil.getErrorXMl(e.getMessage());
		}
	}
	
	public String marshal(Class clazz, Object obj){
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			StringWriter stringWriter = new StringWriter();
			context.createMarshaller().marshal(obj, stringWriter);
			return stringWriter.toString();
		} catch (JAXBException e) {
			log.error(e.getMessage());
			return CommonUtil.getErrorXMl(e.getMessage());
		}
	}
}
