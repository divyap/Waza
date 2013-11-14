package com.inncrewin.waza.main;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.inncrewin.waza.attributes.ElementAttributes;
import com.inncrewin.waza.hibernate.CreditCardInfo;
import com.inncrewin.waza.session.SessionManager;
import com.inncrewin.waza.util.JAXBParser;

public class ConsumerDAO {

	private SessionManager sm = new SessionManager();
	
	public void saveCreditCardInfo(CreditCardInfo cc){
		sm.save(cc);
	}

}
