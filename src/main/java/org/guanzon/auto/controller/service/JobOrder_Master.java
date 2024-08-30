/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.controller.service;

import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.iface.GTransaction;
import org.guanzon.auto.service.model.Model_Job_Order_Master;
import org.json.simple.JSONObject;

/**
 *
 * @author Arsiela
 */
public class JobOrder_Master implements GTransaction{
    GRider poGRider;
    boolean pbWthParent;
    int pnEditMode;
    String psTranStatus;
    String psTransNox;
    Model_Job_Order_Master poModel;
    JSONObject poJSON;
    
    public String getTransNox(){
        return psTransNox;
    }
    
    public JobOrder_Master(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;
        pnEditMode = EditMode.UNKNOWN;
    }
    
    
    @Override
    public JSONObject newTransaction() {
        poJSON = new JSONObject();
        try{
            poModel = new Model_Job_Order_Master(poGRider);
            poModel.newRecord();
            
            poJSON.put("result", "success");
            poJSON.put("message", "initialized new record.");
            pnEditMode = EditMode.ADDNEW;
               
        }catch(NullPointerException e){
            
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }
        
        return poJSON;
    }

    @Override
    public JSONObject openTransaction(String fsValue) {
        pnEditMode = EditMode.READY;
        poJSON = new JSONObject();
        
        poJSON = poModel.openRecord(fsValue);
        return poJSON;
    }

    @Override
    public JSONObject updateTransaction() {
         poJSON = new JSONObject();
        
        if (pnEditMode != EditMode.READY && pnEditMode != EditMode.UPDATE){
            poJSON.put("result", "success");
            poJSON.put("message", "Edit mode has changed to update.");
        } else {
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
        }

        return poJSON;
    }

    @Override
    public JSONObject saveTransaction() {
        poJSON = new JSONObject();
        if (!pbWthParent) {
            poGRider.beginTrans();
        }

        //ValidatorInterface validator = ValidatorFactory.make(ValidatorFactory.TYPE.Account_Accreditation, poModel);
        //poModel.setModifiedDate(poGRider.getServerDate());

//        if (!validator.isEntryOkay()){
//            poJSON.put("result", "error");
//            poJSON.put("message", validator.getMessage());
//            return poJSON;
//
//        }
        poJSON = poModel.saveRecord();  
        if ("success".equals((String)poJSON.get("result"))) {
            if (!pbWthParent) {
                poGRider.commitTrans();
            }
        } else {
            if (!pbWthParent) {
                poGRider.rollbackTrans();
            }
        }

        return poJSON;
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
    public int getEditMode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTransactionStatus(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
