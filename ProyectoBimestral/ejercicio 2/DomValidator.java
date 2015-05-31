/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package domvalidator;

/**
 *
 * @author Edison
 */
// package com.sc.ch02.schema;

import static java.lang.System.out;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Validates an XML document according to a schema.
 */
public class DomValidator {

    private static final String SCHEMA_LANG_PROP = 
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String XML_SCHEMA = 
        "http://www.w3.org/2001/XMLSchema";
    
    private static final String SCHEMA_SOURCE_PROP = 
        "http://java.sun.com/xml/jaxp/properties/schemaSource";
    
    //run the example
    public static void main(String[] args) {

        // String schema = args[0]; //"ejercicio2_personas.xsd";
        String xmlDoc = args[0]; //"ejercicio2_personas.xml";
        String[] schemas = new String[args.length - 1];
        for(int i = 1; i < args.length; i++)
            schemas[i-1] = args[i];
        
        boolean valid = validate(schemas, xmlDoc);
        
        out.print("Valid? " + valid);
    }
    
    //do the work
    private static boolean validate(String[] schema, String xmlDoc) {
        DocumentBuilder builder = createDocumentBuilder(schema);
        
        ValidationHandler handler = new ValidationHandler();
        builder.setErrorHandler(handler);
        
        try {
            builder.parse(xmlDoc);
                        
        } catch (SAXException se) {
            out.println("Validation Error: " + se.getMessage());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        return handler.isValid();
    }

    /**
     * Convenience method sets up the validating factory and 
     * creates the builder.
     */
    private static DocumentBuilder createDocumentBuilder(
            String[] schemas) {
        
        DocumentBuilderFactory factory = 
            DocumentBuilderFactory.newInstance();
        
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        factory.setAttribute(SCHEMA_LANG_PROP, XML_SCHEMA);
        factory.setAttribute(SCHEMA_SOURCE_PROP, schemas);
        
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
        
        return builder;
    }
}

/**
 * This class gets notified by the parser in the event of a 
 * problem. 
 */
class ValidationHandler extends DefaultHandler {
    private boolean valid = true;
    private SAXException se;
    
    /**
     * The default implementation does nothing. 
     */
    @Override
    public void error(SAXParseException se) throws SAXException {
        this.se = se;
        valid = false;
        throw se;
    }
    
    /**
     * The default implementation does nothing. 
     */
    @Override
    public void fatalError(SAXParseException se) 
        throws SAXException {
        
        this.se = se;
        valid = false;
        throw se;
    }
    
    public boolean isValid() {
        return valid;
    }
}
