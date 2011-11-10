Server Interface for Real-time Information
(C) Copyright CEN SIRI 2007,2008

Changes to SIRI schema v1.3b         since v1.0 

Note that this is a work in progress version that may be updated further. Any Changes will be backwards compatible
ie if not used will not break existing function unless marked specifcally

============================     
2008 05 08  StopMonitoring service 
    (a) Correct type on  FeatureRef in Stop monitoring       
    (b) Add StopMonitoringMultipleRequest 
    (c) Add optional MonitoringRef to StopMonitoringDelivery so that can return id even if no data  
 
2008 03 27
    Fix up ifopt & ACSB version numbers to match ifopt 0.4 
    
    
2008-03-26 EstimatedTimetable Production Timetable Service Service 
     Add wrapper tag for Line + Direction to help binding to Axis
     Wraps multiple instances
     ** Note this will break strict Comaptibility with 1.0    
     
2008 03  12    
       Add comments for elements and arrtributes that lacked them
       Correct wdsl errors 
       strip out degree character
       BeyondHorizon type corrected  
       
2008 02  12    
      Add SIRI-SX revisions & Datex2 harmonisation features

2008 02 12 V1.3 draft
=====================================
2007 10  17 
      Add Situation Exchange & Facility Exchange services.
      Added a  siri_all-v1.2.xsd, ifopt_allStopPlace-v0.3.xsd, acsp_all.xsd packages to force explicit declaration of all elements in an imported namespace on the first reference. This overcomes a limitation of some XML tools that only pick up thos elements on the first import and ignotre all subsequent imports.  


2007 04 17 
    Name Space improvements
        revise to use explicit namespaces
        Change name space :  http://siri.org.uk/ ==? siri.org.uk/siri
        
    harmonise Facility Monitoring 
         Revise SpecialNeeds to use  acsb package
         Use Ifopt facility etc        
         Factor out Extensions to utilty file
         
2000 04 V1.2 
=======================================