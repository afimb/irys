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
package irys.siri.server.producer;


import irys.common.SiriException;
import irys.siri.server.data.ServiceBean;
import irys.siri.server.data.SubscriberBean;

import java.util.Calendar;
import java.util.List;
import irys.uk.org.siri.siri.AccessNotAllowedErrorStructure;
import irys.uk.org.siri.siri.ContextualisedRequestStructure;
import irys.uk.org.siri.siri.ErrorDescriptionStructure;
import irys.uk.org.siri.siri.GeneralMessageDeliveriesStructure;
import irys.uk.org.siri.siri.GeneralMessageDeliveryStructure;
import irys.uk.org.siri.siri.GeneralMessageSubscriptionStructure;
import irys.uk.org.siri.siri.InfoMessageCancellationStructure;
import irys.uk.org.siri.siri.InfoMessageStructure;
import irys.uk.org.siri.siri.MessageQualifierStructure;
import irys.uk.org.siri.siri.GeneralMessageRequestStructure;
import irys.uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;
import irys.uk.org.siri.siri.SubscriptionResponseBodyStructure;


/**
 * Implémentation du service CheckStatus
 */
public class GeneralMessage extends AbstractSiri implements GeneralMessageInterface
{
   
    @Override
    public GeneralMessageDeliveriesStructure getGeneralMessage(ContextualisedRequestStructure serviceRequestInfo, GeneralMessageRequestStructure request, Calendar responseTimestamp) throws SiriException {
        GeneralMessageDeliveriesStructure answer = GeneralMessageDeliveriesStructure.Factory.newInstance();

        GeneralMessageDeliveryStructure delivery = answer.addNewGeneralMessageDelivery();
        delivery.setResponseTimestamp(responseTimestamp);
        delivery.setVersion(request.getVersion());
        ServiceDeliveryErrorConditionStructure errorCondition = delivery.addNewErrorCondition();
        ErrorDescriptionStructure description = errorCondition.addNewDescription();
        description.setStringValue("Erreur simulée");

        // ServiceNotAvailableError
        AccessNotAllowedErrorStructure serviceNotAvailableError = errorCondition.addNewAccessNotAllowedError();
        serviceNotAvailableError.setErrorText("[INTERNAL_ERROR] : Service unavailable");

        return answer;
    }

    @Override
    public void addSubscription(Calendar responseTimestamp, SubscriptionResponseBodyStructure answer, ServiceBean service, GeneralMessageSubscriptionStructure[] generalMessageSubscriptions, SubscriberBean subscriber, MessageQualifierStructure requestMessageRef, String notificationAddress) throws SiriException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<InfoMessageStructure> getGeneralMessages(GeneralMessageRequestStructure request) throws SiriException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<InfoMessageCancellationStructure> getGeneralMessageCancellations(GeneralMessageRequestStructure request) throws SiriException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
