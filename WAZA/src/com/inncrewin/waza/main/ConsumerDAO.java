package com.inncrewin.waza.main;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.inncrewin.waza.attributes.ElementAttributes;
import com.inncrewin.waza.hibernate.CreditCardInfo;
import com.inncrewin.waza.session.SessionManager;
import com.inncrewin.waza.util.JAXBParser;

public class ConsumerDAO {

	private SessionManager sm = new SessionManager();

	public String saveCreditCardInfo(String ccXml) {
		Element ele = DocumentFactory.getInstance().createElement("CookedItem");
		String statusMsg = "";
		String status = "";

		CreditCardInfo ccInput = (CreditCardInfo) new JAXBParser()
				.unmarshalXmlString(ccXml, CreditCardInfo.class);

		if (ccInput != null) {
			CreditCardInfo cc = new CreditCardInfo(ccInput.getUserId(),
					ccInput.getCreditCardNo(), ccInput.getExpiryDate(),
					ccInput.getCvv(), ccInput.getType(), ccInput.getNeedAuth());
			sm.save(cc);
 
			status = ElementAttributes.SUCCESS;
			statusMsg = "Credit Card Info is saved successfully";
		} else {
			status = ElementAttributes.FAILURE;
			statusMsg = "Credit Card Info could not be saved ";
		}
		ele.addAttribute(ElementAttributes.STATUS_MESSAGE, statusMsg);
		ele.addAttribute(ElementAttributes.STATUS, status);

		return ele.asXML();

	}

}
