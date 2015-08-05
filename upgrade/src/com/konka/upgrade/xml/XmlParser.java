package com.konka.upgrade.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.konka.upgrade.xml.XmlUpgradeInfo.Scope;

public class XmlParser extends DefaultHandler{
    
    private StringBuilder builder;
    private XmlUpgradeInfo mUpdateInfo;
    private Scope mScope;
    private XmlParserListener listener;
    
//    private static final String kk_UpdateInfo = "kk_UpdateInfo";
    private static final String Manufacture = "Manufacture";
    private static final String ModelID = "Model";
    private static final String HW_version = "HW_version";
    private static final String SW_server_version = "SW_server_version";
    private static final String SW_stb_version = "SW_stb_version";
    private static final String Period = "Period";
    private static final String Control = "Control";
//    private static final String SN = "SN";
    private static final String scope = "scope";
    private static final String index = "index";
//    private static final String ImgURL = "ImgURL";
    private static final String URL = "URL";
    private static final String ImgInfo = "ImgInfo";

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        // TODO Auto-generated method stub
        super.characters(ch, start, length);
        builder.append(ch,start,length);
    }

    @Override
    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub
        super.startDocument();
        builder = new StringBuilder();
        mUpdateInfo = new XmlUpgradeInfo();
        if(listener != null) {
            listener.onStart();
        }
    }

    @Override
    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub
        super.endDocument();
        if(listener != null) {
            listener.onSuccess(mUpdateInfo);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // TODO Auto-generated method stub
        super.endElement(uri, localName, qName);
        if(qName.equals(Manufacture)) {
            mUpdateInfo.Manufacture = builder.toString();
        } else if(qName.equals(ModelID)) {
            mUpdateInfo.ModelID = builder.toString();
        } else if(qName.equals(HW_version)) {
            mUpdateInfo.HW_version = builder.toString();
        } else if(qName.equals(SW_server_version)) {
            mUpdateInfo.SW_server_version = builder.toString();
        } else if(qName.equals(SW_stb_version)) {
            mUpdateInfo.SW_stb_version = builder.toString();
        } else if(qName.equals(Period)) {
            mUpdateInfo.Period = builder.toString();
        } else if(qName.equals(Control)) {
            mUpdateInfo.Control = builder.toString();
        } else if(qName.equals(index)) {
            mScope.index.add(builder.toString());
        } else if(qName.equals(URL)) {
            mUpdateInfo.urlList.add(builder.toString());
        } else if(qName.equals(scope)) {
            mUpdateInfo.scopeList.add(mScope);
        } else if(qName.equals(ImgInfo)) {
            mUpdateInfo.ImgInfo = builder.toString();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        // TODO Auto-generated method stub
        if(qName.equals(scope)) {
            mScope = mUpdateInfo.new Scope();
            mScope.startSN = attributes.getValue("start");
            mScope.endSN = attributes.getValue("end");
        }
        builder.setLength(0);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        // TODO Auto-generated method stub
        super.error(e);
        if(listener != null) {
            listener.onError(e);
        }
    }
    
    public void setXmlParserListener(XmlParserListener listener) {
        this.listener = listener;
    }

}
