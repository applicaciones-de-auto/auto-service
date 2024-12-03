/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.resultSet2XML.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.TransactionStatus;

/**
 *
 * @author Arsiela
 */
public class JobOrderMaster {
    public static void main (String [] args){
        String path;
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Maven_Systems";
        }
        else{
            path = "/srv/GGC_Maven_Systems";
        }
        System.setProperty("sys.default.path.config", path);
        
        GRider instance = new GRider("gRider");

        if (!instance.logUser("gRider", "M001000001")){
            System.err.println(instance.getErrMsg());
            System.exit(1);
        }

        System.out.println("Connected");
        
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_JobOrder_Master.xml");
        
        
        String lsSQL =    " SELECT "                                                                         
                        + "   a.sTransNox "                                                                  
                        + " , a.dTransact "                                                                  
                        + " , a.sDSNoxxxx "                                                                  
                        + " , a.sSerialID "                                                                  
                        + " , a.sClientID "                                                                  
                        + " , a.sWorkCtgy "                                                                  
                        + " , a.sJobTypex "                                                                  
                        + " , a.sLaborTyp "                                                                  
                        + " , a.nKMReadng "                                                                  
                        + " , a.sEmployID "                                                                  
                        + " , a.sRemarksx "                                                                  
                        + " , a.cPaySrcex "                                                                  
                        + " , a.sSourceNo "                                                                  
                        + " , a.sSourceCD "                                                                  
                        + " , a.dPromised "                                                                  
                        + " , a.sInsurnce "                                                                  
                        + " , a.cCompUnit "                                                                  
                        + " , a.sActvtyID "                                                                  
                        + " , a.nLaborAmt "                                                                  
                        + " , a.nPartsAmt "                                                                  
                        + " , a.nTranAmtx "                                                                  
                        + " , a.nRcvryAmt "                                                                  
                        + " , a.nPartFeex "                                                                  
                        + " , a.cPrintedx "                                                                  
                        + " , a.cTranStat "                                                                  
                        + " , a.sEntryByx "                                                                  
                        + " , a.dEntryDte "                                                                  
                        + " , a.sModified "                                                                  
                        + " , a.dModified "                                                     
                        + "  , CASE "          
                        + " 	WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_CLOSED)+" THEN 'COMPLETED' "                     
                        + " 	WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)+" THEN 'CANCELLED' "                  
                        + " 	WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_OPEN)+" THEN 'ACTIVE' "                    
                        + " 	WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_POSTED)+" THEN 'POSTED' "                                      
                        + " 	ELSE 'ACTIVE'  "                                                          
                        + "    END AS sTranStat "                                                                 
                        + " , b.sCompnyNm AS sOwnrNmxx "                                                     
                        + " , b.cClientTp "                                                                  
                        + " , IFNULL(CONCAT( IFNULL(CONCAT(d.sHouseNox,' ') , ''), "                         
                        + "   IFNULL(CONCAT(d.sAddressx,' ') , ''), "                                        
                        + "   IFNULL(CONCAT(e.sBrgyName,' '), ''),  "                                        
                        + "   IFNULL(CONCAT(f.sTownName, ', '),''), "                                        
                        + "   IFNULL(CONCAT(g.sProvName),'') )	, '') AS sAddressx "                         
                        + " , k.sCompnyNm AS sCoOwnrNm "                                                     
                        + " , h.sCSNoxxxx "                                                                  
                        + " , h.sFrameNox "                                                                  
                        + " , h.sEngineNo "                                                                
                        + " , h.cVhclNewx "                                                                  
                        + " , i.sPlateNox "                                                                  
                        + " , j.sDescript AS sVhclDesc"                                                                  
                        + " , l.sVSPNOxxx "                                                                  
                        + " , l.sBranchCD "                                                                  
                        + " , n.sBranchNm "                                                                  
                        + " , m.sCompnyNm AS sCoBuyrNm "                                                     
                        + " , o.sCompnyNm AS sEmployNm "                                                                     
                        + " , DATE(p.dModified) AS dComplete "                                                                           
                        + " , q.sCompnyNm AS sComplete "                                                    
                        + " FROM diagnostic_master a "                                                       
                        + " LEFT JOIN client_master b ON b.sClientID = a.sClientID "                         
                        + " LEFT JOIN client_address c ON c.sClientID = a.sClientID AND c.cPrimaryx = '1' "  
                        + " LEFT JOIN addresses d ON d.sAddrssID = c.sAddrssID "                             
                        + " LEFT JOIN barangay e ON e.sBrgyIDxx = d.sBrgyIDxx  "                             
                        + " LEFT JOIN towncity f ON f.sTownIDxx = d.sTownIDxx  "                             
                        + " LEFT JOIN province g ON g.sProvIDxx = f.sProvIDxx  "                             
                        + " LEFT JOIN vehicle_serial h ON h.sSerialID = a.sSerialID "                        
                        + " LEFT JOIN vehicle_serial_registration i ON i.sSerialID = a.sSerialID "           
                        + " LEFT JOIN vehicle_master j ON j.sVhclIDxx = h.sVhclIDxx "                        
                        + " LEFT JOIN client_master k ON k.sClientID = h.sCoCltIDx " /*co-owner*/            
                        + " LEFT JOIN vsp_master l ON l.sTransNox = a.sSourceNo "                            
                        + " LEFT JOIN client_master m ON m.sClientID = l.sCoCltIDx " /*co-buyer*/            
                        + " LEFT JOIN branch n ON n.sBranchCd = l.sBranchCD "                                
                        + " LEFT JOIN ggc_isysdbf.client_master o ON o.sClientID = a.sEmployID "   
                        + " LEFT JOIN transaction_status_history p ON p.sSourceNo = a.sTransNox AND p.cRefrStat = "+ SQLUtil.toSQL(TransactionStatus.STATE_CLOSED) + " AND p.cTranStat <> "+ SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
                        + " LEFT JOIN ggc_isysdbf.client_master q ON q.sClientID = p.sModified " 
                        + " WHERE 0=1";
        
        
        ResultSet loRS = instance.executeQuery(lsSQL);
        try {
            if (MiscUtil.resultSet2XML(instance, loRS, System.getProperty("sys.default.path.metadata"), "diagnostic_master", "")){
                System.out.println("ResultSet exported.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
