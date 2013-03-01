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

import lombok.Getter;
import lombok.Setter;

import irys.common.SiriException;
import irys.common.SiriTool;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.xmlbeans.XmlObject;

import uk.org.siri.siri.AbstractServiceRequestStructure;


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
  
  
   
}
