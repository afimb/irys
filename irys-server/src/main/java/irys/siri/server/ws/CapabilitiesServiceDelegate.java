package irys.siri.server.ws;
///**
// * 
// */
//package net.dryade.siri.server.ws;
//
//import java.util.Calendar;
//
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.GetCapabilitiesDocument;
//import uk.org.siri.wsdl.GetCapabilitiesError;
//import uk.org.siri.wsdl.GetCapabilitiesResponseDocument;
//import uk.org.siri.wsdl.GetCapabilitiesResponseDocument.GetCapabilitiesResponse;
//import uk.org.siri.siri.CapabilitiesRequestStructure;
//import uk.org.siri.siri.CapabilitiesResponseStructure;
//
///**
// * @author michel
// *
// */
//public class CapabilitiesServiceDelegate extends AbstractSiriServiceDelegate implements ApplicationContextAware{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = Logger.getLogger(CapabilitiesServiceDelegate.class);
//	private CapabilitiesInterface capabilitiesInterface;
//        private String capabilitiesService;
//
//	public void init()
//	{
//            if (context.containsBean(capabilitiesService)) {
//                this.capabilitiesInterface = (CapabilitiesInterface) context.getBean(capabilitiesService);
//            }
//	}
//
//	public GetCapabilitiesResponseDocument getCapabilities(GetCapabilitiesDocument requestDoc)
//	throws GetCapabilitiesError
//	{
//		logger.debug("Appel GetCapabilities");
//		long debut = System.currentTimeMillis();
//		try
//		{
//			// habillage de la reponse
//			GetCapabilitiesResponseDocument responseDoc = GetCapabilitiesResponseDocument.Factory.newInstance();
//			GetCapabilitiesResponse response = responseDoc.addNewGetCapabilitiesResponse();
//
//			CapabilitiesResponseStructure answer = null;
//			response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//
//			// validation XSD de la requete
//			boolean validate = true;
//			if (requestValidation)
//			{
//				validate = siriTool.checkXmlSchema(requestDoc,logger);
//			}
//			else
//			{
//				validate = (requestDoc.getGetCapabilities() != null);
//				if (validate)
//				{
//					// controle moins restrictif limite aux elements necessaires a la requete
//					CapabilitiesRequestStructure request = requestDoc.getGetCapabilities().getRequest();
//					boolean requestOk = siriTool.checkXmlSchema(request,logger);
//					validate = requestOk ;
//				}
//
//			}
//			Calendar responseTimestamp = Calendar.getInstance();
//			if (!validate)
//			{
//				// pas de message d'erreur dans ce service
//				answer = response.addNewAnswer();
//				answer.setResponseTimestamp(responseTimestamp );
//			}
//			else
//			{
//				if (this.capabilitiesService != null)
//				{
//					logger.debug("appel au service capabilitiesService");
//					CapabilitiesRequestStructure request = requestDoc.getGetCapabilities().getRequest();
//					answer = this.capabilitiesService.getCapabilities(request,responseTimestamp);
//					answer.setResponseTimestamp(responseTimestamp);
//					response.setAnswer(answer);
//				}
//				else
//				{
//					answer = response.addNewAnswer();
//					answer.setResponseTimestamp(responseTimestamp );
//					// pas de message d'erreur NOT_YET_IMPLEMENTED possible
//				}
//			}
//			return responseDoc;
//		}
//		catch (Exception e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new GetCapabilitiesError(e.getMessage());
//		}
//		catch (Error e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new GetCapabilitiesError(e.getMessage());
//		}
//		finally
//		{
//			long fin = System.currentTimeMillis();
//			logger.debug("fin GetCapabilities : duree = "+siriTool.getTimeAsString(fin - debut));
//		}
//	}
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.producer.delegation.AbstractSiriServiceDelegate#getLogger()
//	 */
//	@Override
//	protected Logger getLogger()
//	{
//		return logger;
//	}
//
//	/**
//	 * @param capabilitiesService the capabilitiesService to set
//	 */
//	public void setCapabilitiesService(String capabilitiesService) {
//		this.capabilitiesService = capabilitiesService;
//	}
//
//}
