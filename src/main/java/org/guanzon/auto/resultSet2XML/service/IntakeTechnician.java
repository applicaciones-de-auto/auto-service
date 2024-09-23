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
public class IntakeTechnician {
    
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
        
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_Intake_Technician.xml");
        
        String lsSQL =   "   SELECT "                   
                        + "    a.sTransNox "           
                        + "  , a.sTechIDxx "           
                        + "  , a.sDiagNoxx "           
                        + "  , a.sWorkCtgy "           
                        + "  , a.sLaborCde "           
                        + "  , a.cReprActx "           
                        + "  , a.cPermissn "           
                        + "  , a.cIsCancld "           
                        + "  , a.sEntryByx "           
                        + "  , a.dEntryDte "
                        + "  , b.cTchSkill " 
                        + "  , b.cBrpSkill "
                        + "  , c.sCompnyNm AS sTechName "  
                        + "  , d.sLaborDsc "          
                        + " FROM intake_technician a "
                        + "LEFT JOIN service_mechanic b ON b.sClientID = a.sTechIDxx " 
                        + "LEFT JOIN ggc_isysdbf.client_master c ON c.sClientID = b.sClientID " 
                        + "LEFT JOIN labor d ON d.sLaborCde = a.sLaborCde "
                        + " WHERE 0=1";
        
        ResultSet loRS = instance.executeQuery(lsSQL);
        try {
            if (MiscUtil.resultSet2XML(instance, loRS, System.getProperty("sys.default.path.metadata"), "intake_technician", "")){
                System.out.println("ResultSet exported.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
