package com.polyglot.HNewsJava;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;



public class RssReader extends DefaultHandler {
	
    private ArrayList<HashMap<String, String>> rssItems = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> rssItem = new HashMap<String, String>();
    private StringBuilder chars;

    public static ArrayList<HashMap<String, String>> read(Reader ir) throws SAXException, IOException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            RssReader handler = new RssReader();
            InputSource input = new InputSource(ir);
            reader.setContentHandler(handler);
            reader.parse(input);
            
            return handler.rssItems;
        } catch (ParserConfigurationException e) {
            throw new SAXException();
        }
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        // reset the string buffer
        chars = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        chars.append(ch, start, length);
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName != null && qName.length() > 0) {
            String field = qName.toLowerCase();
            // Check if looking for article, and if article is complete
            if (field.equals("entry") || field.equals("item")) {
                rssItems.add(rssItem);
                rssItem = new HashMap<String, String>();
            }
            else {
                rssItem.put(field, chars.toString());
            }
        }
                    
    }

}
