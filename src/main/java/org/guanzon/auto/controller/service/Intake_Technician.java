/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.controller.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.iface.GTranDet;
import org.guanzon.auto.model.clients.Model_Service_Mechanic;
import org.guanzon.auto.model.service.Model_Intake_Technician;
import org.guanzon.auto.validator.service.ValidatorFactory;
import org.guanzon.auto.validator.service.ValidatorInterface;
import org.json.simple.JSONObject;

/**
 *
 * @author Arsiela
 */
public class Intake_Technician implements GTranDet {
    final String XML = "Model_Intake_Technician.xml";
    GRider poGRider;
//    String psTargetBranchCd = ""; JO will not required to tra
    boolean pbWtParent;
    
    int pnEditMode;
    String psMessagex;
    public JSONObject poJSON;
    
    ArrayList<Model_Intake_Technician> paDetail;
    ArrayList<Model_Intake_Technician> paRemDetail;

    public Intake_Technician(GRider foAppDrver){
        poGRider = foAppDrver;
    }
    
    @Override
    public int getEditMode() {
        return pnEditMode;
    }
    
    @Override
    public int getItemCount() {
        if(paDetail == null){
           paDetail = new ArrayList<>();
        }
        return paDetail.size();
    }

    @Override
    public Model_Intake_Technician getDetailModel(int fnRow) {
        return paDetail.get(fnRow);
    }
    
    public JSONObject addDetail(String fsTransNo){
        if(paDetail == null){
           paDetail = new ArrayList<>();
        }
        
        poJSON = new JSONObject();
        if (paDetail.size()<=0){
            paDetail.add(new Model_Intake_Technician(poGRider));
            paDetail.get(0).newRecord();
            
            paDetail.get(0).setDiagNo(fsTransNo);
            poJSON.put("result", "success");
            poJSON.put("message", "Technician add record.");
        } else {
            paDetail.add(new Model_Intake_Technician(poGRider));
            paDetail.get(paDetail.size()-1).newRecord();

            paDetail.get(paDetail.size()-1).setDiagNo(fsTransNo);
            
            poJSON.put("result", "success");
            poJSON.put("message", "Technician add record.");
        }
        return poJSON;
    }
    
    public JSONObject openDetail(String fsValue){
        paDetail = new ArrayList<>();
        paRemDetail = new ArrayList<>();
        poJSON = new JSONObject();
        Model_Intake_Technician loEntity = new Model_Intake_Technician(poGRider);
        String lsSQL = loEntity.makeSelectSQL();  
        lsSQL = MiscUtil.addCondition(lsSQL, " sDiagNoxx = " + SQLUtil.toSQL(fsValue))
                                               + "  ORDER BY sTechIDxx ASC " ;
        System.out.println(lsSQL);
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        try {
            int lnctr = 0;
            if (MiscUtil.RecordCount(loRS) > 0) {
                while(loRS.next()){
                        paDetail.add(new Model_Intake_Technician(poGRider));
                        paDetail.get(paDetail.size() - 1).openRecord(loRS.getString("sTransNox"));
                        
                        pnEditMode = EditMode.UPDATE;
                        lnctr++;
                        poJSON.put("result", "success");
                        poJSON.put("message", "Record loaded successfully.");
                    } 
                
            }else{
//                paDetail = new ArrayList<>();
//                addDetail(fsValue);
                poJSON.put("result", "error");
                poJSON.put("continue", true);
                poJSON.put("message", "No record selected.");
            }
            MiscUtil.close(loRS);
        } catch (SQLException e) {
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }
        return poJSON;
    }
    
    public JSONObject saveDetail(String fsTransNo){
        JSONObject obj = new JSONObject();
        
        int lnCtr;
        if(paRemDetail != null){
            int lnRemSize = paRemDetail.size() -1;
            if(lnRemSize >= 0){
                for(lnCtr = 0; lnCtr <= lnRemSize; lnCtr++){
                    obj = paRemDetail.get(lnCtr).deleteRecord();
                    if("error".equals((String) obj.get("result"))){
                        return obj;
                    }
                }
            }
        }
        
        if(paDetail == null){
            obj.put("result", "error");
            obj.put("continue", true);
            return obj;
        }
        
        int lnSize = paDetail.size() -1;
        if(lnSize < 0){
            obj.put("result", "error");
            obj.put("continue", true);
            return obj;
        }
        
        for (lnCtr = 0; lnCtr <= lnSize; lnCtr++){
            //if(lnCtr>0){
                if(paDetail.get(lnCtr).getLaborCde().isEmpty()){
                    continue; //skip, instead of removing the actual detail
//                    paDetail.remove(lnCtr);
//                    lnCtr++;
//                    if(lnCtr > lnSize){
//                        break;
//                    } 
                }
            //}
            
            paDetail.get(lnCtr).setDiagNo(fsTransNo);
//            paDetail.get(lnCtr).setTargetBranchCd(psTargetBranchCd);
            
            ValidatorInterface validator = ValidatorFactory.make(ValidatorFactory.TYPE.Intake_Technician, paDetail.get(lnCtr));
            validator.setGRider(poGRider);
            if (!validator.isEntryOkay()){
                obj.put("result", "error");
                obj.put("message", validator.getMessage());
                return obj;
            }
            obj = paDetail.get(lnCtr).saveRecord();
        }    
        
        return obj;
    }
//    public void setTargetBranchCd(String fsBranchCd){
//        psTargetBranchCd = fsBranchCd;
//    }
    
    public Object removeDetail(int fnRow){
        JSONObject loJSON = new JSONObject();
        
        if(paDetail.get(fnRow).getEntryBy() != null){
            if(!paDetail.get(fnRow).getEntryBy().trim().isEmpty()){
                RemoveDetail(fnRow);
            }
        }
        
        paDetail.remove(fnRow);
        return loJSON;
    }
    
    private JSONObject RemoveDetail(Integer fnRow){
        
        if(paRemDetail == null){
           paRemDetail = new ArrayList<>();
        }
        
        poJSON = new JSONObject();
        if (paRemDetail.size()<=0){
            paRemDetail.add(new Model_Intake_Technician(poGRider));
            paRemDetail.get(0).openRecord(paDetail.get(fnRow).getTransNo());
            poJSON.put("result", "success");
            poJSON.put("message", "added to remove record.");
        } else {
            paRemDetail.add(new Model_Intake_Technician(poGRider));
            paRemDetail.get(paRemDetail.size()-1).openRecord(paDetail.get(fnRow).getTransNo());
            poJSON.put("result", "success");
            poJSON.put("message", "added to remove record.");
        }
        return poJSON;
    }
    
    public ArrayList<Model_Intake_Technician> getDetailList(){
        if(paDetail == null){
           paDetail = new ArrayList<>();
        }
        
        return paDetail;
    }
    
    public void sortTechnician(){
        // Sorting using Comparator without lambda
        Collections.sort(paDetail, new Comparator<Model_Intake_Technician>() {
            @Override
            public int compare(Model_Intake_Technician t1, Model_Intake_Technician t2) {
                return t1.getTechName().compareTo(t2.getTechName());
            }
        });
    }
    
    public void setDetailList(ArrayList<Model_Intake_Technician> foObj){this.paDetail = foObj;}
    
    public Object getDetail(int fnRow, int fnIndex){return paDetail.get(fnRow).getValue(fnIndex);}
    public Object getDetail(int fnRow, String fsIndex){return paDetail.get(fnRow).getValue(fsIndex);}
    
    @Override
    public JSONObject setDetail(int fnRow, int fnIndex, Object foValue){ 
        return paDetail.get(fnRow).setValue(fnIndex, foValue);
    }
    
    @Override
    public JSONObject setDetail(int fnRow, String fsIndex, Object foValue){ 
        return paDetail.get(fnRow).setValue(fsIndex, foValue);
    }

    @Override
    public JSONObject searchDetail(int i, String string, String string1, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject searchDetail(int i, int i1, String string, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject newTransaction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject openTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject updateTransaction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject saveTransaction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject deleteTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject closeTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject postTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject voidTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject cancelTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject searchWithCondition(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject searchTransaction(String string, String string1, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject searchMaster(String string, String string1, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject searchMaster(int i, String string, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getMasterModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject setMaster(int i, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject setMaster(String string, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTransactionStatus(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public JSONObject searchTechnician() {
        JSONObject loJSON = new JSONObject();
        Model_Service_Mechanic loEntity = new Model_Service_Mechanic(poGRider);
        String lsSQL = loEntity.getSQL();
        String lsHeader = "ID»Name";
        String lsColName = "sClientID»sCompnyNm"; 
        String lsCriteria = "a.sClientID»b.sCompnyNm";
        
        lsSQL = MiscUtil.addCondition(lsSQL,  " a.cRecdStat = '1' ");
                                            //+ " AND b.sCompnyNm LIKE " + SQLUtil.toSQL(fsValue + "%"));
        
        System.out.println("SEARCH TECHNICIAN: " + lsSQL);
        loJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                "",
                    lsHeader,
                    lsColName,
                    lsCriteria,
                1);

        if (loJSON != null) {
        } else {
            loJSON = new JSONObject();
            loJSON.put("result", "error");
            loJSON.put("message", "No record loaded.");
            return loJSON;
        }
        
        return loJSON;
    }
    
}
