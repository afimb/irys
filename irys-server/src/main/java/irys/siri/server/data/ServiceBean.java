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
package irys.siri.server.data;

/**
 * @author Michel Etienne
 * @version 1.0
 * @created 22-avr.-2009 10:47:56
 */
public class ServiceBean extends AbstractBean
{


   private String name;

   public ServiceBean()
   {
      super();
   }

   public String getName()
   {
      return this.name;
   }

   public void setName(String name)
   {
      this.name = name;
   }


}