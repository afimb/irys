/**
 *   Siri Product - Produit SIRI
 *  
 *   a set of tools for easy application building with 
 *   respect of the France Siri Local Agreement
 *
 *   un ensemble d'outils facilitant la realisation d'applications
 *   respectant le profil France de la norme SIRI
 * 
 *   Copyright DRYADE 2009-2010
 */
package irys.siri.client.cmd;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

import irys.common.SiriException;
import irys.common.SiriTool;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;

import irys.uk.org.siri.siri.AbstractServiceRequestStructure;


public abstract class AbstractCommand
{
  public static String serverId = "default" ;  
  
  private @Getter @Setter SiriTool siriTool;
  
  private @Getter @Setter String outDirectory = ".";
  
  public AbstractCommand()
  {
  }

  
  public void save(XmlObject obj, String filename)
  {
    try
    {
      File d = new File(outDirectory);
      if (!d.exists())
      {
    	  d.mkdirs();
      }
      File f = new File(d,filename);
      PrintWriter w = new PrintWriter(new FileWriterWithEncoding(f, "UTF-8"));
      w.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      w.println(obj);
      w.close();
    }
    catch (Exception e)
    {
      System.out.println("erreur impression dans "+filename+ " : "+e.getMessage());
    }
  }    

  public abstract void call(String[] args);

  public abstract AbstractServiceRequestStructure getRequest(String[] args) throws SiriException;
  
  /**
   * check an object xml validity, add error details in log
   *
   * @param object object to check
   * @return validity result
   */
  public boolean checkXmlSchema(XmlObject object) {
      if (object == null) {
    	  System.err.println("validate null object");
          return false;
      }
      ArrayList<XmlValidationError> validationErrors = new ArrayList<XmlValidationError>();
      XmlOptions validationOptions = new XmlOptions();
      validationOptions.setErrorListener(validationErrors);

      boolean validation = object.validate(validationOptions);
      if (!validation) {
          // TODO patch pour contourner un pb sur le ANY du generalMessage
          // actuellement, le contenu du GeneralMessage ne peut donc pas etre valide par cette methode
          ArrayList<XmlValidationError> validationWrongErrors = new ArrayList<XmlValidationError>();
          for (XmlValidationError error : validationErrors) {
              if (error.toString().contains("IDFGeneralMessageStructure")) {
                  validationWrongErrors.add(error);
              } else if (error.toString().contains("IDFGeneralMessageRequestFilterStructure")) {
                  validationWrongErrors.add(error);
              }

          }
          validationErrors.removeAll(validationWrongErrors);
          if (validationErrors.size() == 0) {
              validation = true;
          } else {
              StringBuffer errorTxt = new StringBuffer(">> Invalid object " + object.getClass().getName());
              for (XmlValidationError error : validationErrors) {
                  errorTxt.append("\n >> [");
                  errorTxt.append(XmlValidationError.severityAsString(error.getSeverity()));
                  errorTxt.append("] ");
                  errorTxt.append(error.getMessage());
                  errorTxt.append(" (");
                  errorTxt.append(error.getErrorCode());
                  errorTxt.append(")");
                  errorTxt.append("\n >> ");
                  errorTxt.append(error.toString());
              }
              System.err.println(errorTxt);
              System.err.println("Invalid content = \n" + object.toString());
          }
      }
      return validation;
  }

   
}
