package irys.siri.server.ws;
///**
// *   Siri Product - Produit SIRI
// *  
// *   a set of tools for easy application building with 
// *   respect of the France Siri Local Agreement
// *
// *   un ensemble d'outils facilitant la realisation d'applications
// *   respectant le profil France de la norme SIRI
// * 
// *   Copyright DRYADE 2009-2010
// */
//package irys.siri.server.ws;
//
//import java.util.Calendar;
//
//import irys.siri.server.common.SiriException;
//
//import uk.org.siri.wsdl.DeleteSubscriptionDocument;
//import uk.org.siri.wsdl.SubscribeDocument.Subscribe;
//import uk.org.siri.siri.RequestStructure;
//import uk.org.siri.siri.SubscriptionResponseBodyStructure;
//import uk.org.siri.siri.TerminateSubscriptionResponseStructure;
//
///**
// * 
// */
//public interface SubscriptionInterface
//{
//
//  /**
//   * @param callingService
//   * @param serviceRequestInfo
//   * @param requestDelete
//   * @param responseTimestamp
//   * @return
//   * @throws SiriException
//   */
//  public TerminateSubscriptionResponseStructure deleteSubscription(SiriServices callingService,
//                                                                   RequestStructure serviceRequestInfo,
//                                                                   DeleteSubscription requestDelete,
//                                                                   Calendar responseTimestamp) 
//  throws SiriException;
//
//  /**
//   * @param callingService
//   * @param serviceRequestInfo
//   * @param request
//   * @param responseTimestamp
//   * @return
//   * @throws SiriException
//   */
//  SubscriptionResponseBodyStructure subscribe(SiriServices callingService,
//                                              RequestStructure serviceRequestInfo,
//                                              Subscribe request, 
//                                              Calendar responseTimestamp) 
//  throws SiriException;
//
//}
