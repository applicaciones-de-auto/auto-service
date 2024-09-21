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

/**
 *
 * @author Arsiela
 */
public class JobOrderLabor {
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
        
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_JobOrder_Labor.xml");
        
        
        String lsSQL =    " SELECT "                                        
                        + "   a.sTransNox "                                 
                        + " , a.nEntryNox "                                 
                        + " , a.sPayChrge "                                 
                        + " , a.sLaborCde "                                 
                        + " , a.sLbrPckCd "                                 
                        + " , a.nUnitPrce "                                 
                        + " , a.nFRTxxxxx "                                 
                        + " , a.sEntryByx "                                 
                        + " , a.dEntryDte "                                 
                        + " , b.sLaborDsc "                                 
                        + " FROM diagnostic_labor a "                       
                        + " LEFT JOIN labor b ON a.sLaborCde = b.sLaborCde "
                        + " WHERE 0=1";
        
        
        ResultSet loRS = instance.executeQuery(lsSQL);
        try {
            if (MiscUtil.resultSet2XML(instance, loRS, System.getProperty("sys.default.path.metadata"), "diagnostic_labor", "")){
                System.out.println("ResultSet exported.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
