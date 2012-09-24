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
/**
 * 
 */
package irys.siri.client.data.subscription.client;

/**
 * DAO Bean common attributes
 * 
 * @author michel
 *
 */
public abstract class AbstractBean
{
  private long id;

  /**
   * @return the id
   */
  public final long getId()
  {
    return id;
  }

  /**
   * @param id the id to set
   */
  public final void setId(long id)
  {
    this.id = id;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof AbstractBean)
    {
      AbstractBean aobj = (AbstractBean) obj;
      if (aobj.getClass().equals(getClass()))
         return aobj.getId() == getId();
      return false;
    }
    return false;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return Long.valueOf(getId()).hashCode();
  }

  
  
}
