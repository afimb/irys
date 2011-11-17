/**
 * 
 */
package net.dryade.siri.server.producer;

import net.dryade.siri.server.common.SiriException;
import uk.org.siri.siri.AbstractServiceDeliveryStructure;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;

/**
 * @author michel
 *
 */
public abstract class AbstractSiri
{
   /**
    * add an errocode without setting status false
    * 
    * @param delivery
    * @param errorCode
    * @param errorMsg
    */
   protected void addParameterIgnoredWarning(AbstractServiceDeliveryStructure delivery,  String errorMsg)
   {
      if (delivery.isSetErrorCondition())
      {
         ServiceDeliveryErrorConditionStructure errorCondition = delivery.getErrorCondition();
         OtherErrorStructure error = errorCondition.getOtherError();
         String text = error.getErrorText();
         if (text.contains(SiriException.Code.PARAMETER_IGNORED.toString()))
         {
            text += ","+errorMsg;
            error.setErrorText(text);
            errorCondition.setOtherError(error);
            delivery.setErrorCondition(errorCondition);
         }
      }
      else
      {
         ServiceDeliveryErrorConditionStructure errorCondition = delivery.addNewErrorCondition();
         OtherErrorStructure error = errorCondition.addNewOtherError();
         error.setErrorText("["+SiriException.Code.PARAMETER_IGNORED+"] : "+errorMsg);
      }
   }



}
