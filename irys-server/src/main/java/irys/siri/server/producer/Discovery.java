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

import java.util.Calendar;
import irys.uk.org.siri.siri.AccessNotAllowedErrorStructure;
import irys.uk.org.siri.siri.ErrorDescriptionStructure;
import irys.uk.org.siri.siri.LinesDeliveryStructure;
import irys.uk.org.siri.siri.LinesDiscoveryRequestStructure;
import irys.uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;
import irys.uk.org.siri.siri.StopPointsDeliveryStructure;
import irys.uk.org.siri.siri.StopPointsDiscoveryRequestStructure;

/**
 * Implémentation du service CheckStatus
 */
public class Discovery extends AbstractSiri implements DiscoveryInterface
{

    @Override
    public LinesDeliveryStructure getLinesDiscovery(LinesDiscoveryRequestStructure request, Calendar responseTimestamp) throws SiriException {
        LinesDeliveryStructure answer = LinesDeliveryStructure.Factory.newInstance();

        ServiceDeliveryErrorConditionStructure errorCondition = answer.addNewErrorCondition();
        ErrorDescriptionStructure description = errorCondition.addNewDescription();
        description.setStringValue("Erreur simulée");

        // ServiceNotAvailableError
        AccessNotAllowedErrorStructure serviceNotAvailableError = errorCondition.addNewAccessNotAllowedError();
        serviceNotAvailableError.setErrorText("[INTERNAL_ERROR] : Service unavailable");

        return answer;
    }

    @Override
    public StopPointsDeliveryStructure getStopPointsDiscovery(StopPointsDiscoveryRequestStructure request, Calendar responseTimestamp) throws SiriException {
        StopPointsDeliveryStructure answer = StopPointsDeliveryStructure.Factory.newInstance();

        ServiceDeliveryErrorConditionStructure errorCondition = answer.addNewErrorCondition();
        ErrorDescriptionStructure description = errorCondition.addNewDescription();
        description.setStringValue("Erreur simulée");

        // ServiceNotAvailableError
        AccessNotAllowedErrorStructure serviceNotAvailableError = errorCondition.addNewAccessNotAllowedError();
        serviceNotAvailableError.setErrorText("[INTERNAL_ERROR] : Service unavailable");

        return answer;
    }
   

}
