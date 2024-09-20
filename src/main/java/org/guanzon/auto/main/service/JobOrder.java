/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.main.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.appdriver.iface.GTransaction;
import org.guanzon.auto.controller.sales.VehicleSalesProposal_Labor;
import org.guanzon.auto.controller.sales.VehicleSalesProposal_Parts;
import org.guanzon.auto.controller.service.Intake_Technician;
import org.guanzon.auto.controller.service.JobOrder_Labor;
import org.guanzon.auto.controller.service.JobOrder_Master;
import org.guanzon.auto.controller.service.JobOrder_Parts;
import org.json.simple.JSONObject;
/**
 *
 * @author Arsiela
 */
public class JobOrder implements GTransaction{
    GRider poGRider;
    String psBranchCd;
    boolean pbWtParent;
    int pnEditMode;
    String psTransStat;
    String psMessagex;
    public JSONObject poJSON;  
    
    JobOrder_Master poController;
    JobOrder_Labor poJOLabor;
    JobOrder_Parts poJOParts;
    Intake_Technician poIntakeTechnician;
    
    VehicleSalesProposal_Labor poVSPLabor;
    VehicleSalesProposal_Parts poVSPParts;
    
    public JobOrder(GRider foAppDrver, boolean fbWtParent, String fsBranchCd){
        poController = new JobOrder_Master(foAppDrver,fbWtParent,fsBranchCd);
        poJOLabor = new JobOrder_Labor(foAppDrver);
        poJOParts = new JobOrder_Parts(foAppDrver);
        poIntakeTechnician = new Intake_Technician(foAppDrver);
        poVSPLabor = new VehicleSalesProposal_Labor(foAppDrver);
        poVSPParts = new VehicleSalesProposal_Parts(foAppDrver);
        
        poGRider = foAppDrver;
        pbWtParent = fbWtParent;
        psBranchCd = fsBranchCd.isEmpty() ? foAppDrver.getBranchCode() : fsBranchCd;
    }

    @Override
    public int getEditMode() {
        pnEditMode = poController.getEditMode();
        return pnEditMode;
    }
    
    @Override
    public JSONObject setMaster(int fnCol, Object foData) {
        return poController.setMaster(fnCol, foData);
    }

    @Override
    public JSONObject setMaster(String fsCol, Object foData) {
        return poController.setMaster(fsCol, foData);
    }

    public Object getMaster(int fnCol) {
        if(pnEditMode == EditMode.UNKNOWN)
            return null;
        else 
            return poController.getMaster(fnCol);
    }

    public Object getMaster(String fsCol) {
        return poController.getMaster(fsCol);
    }

    @Override
    public JSONObject newTransaction() {
        poJSON = new JSONObject();
        try{
            poJSON = poController.newTransaction();
            
            if("success".equals(poJSON.get("result"))){
                pnEditMode = poController.getEditMode();
            } else {
                pnEditMode = EditMode.UNKNOWN;
            }
               
        }catch(NullPointerException e){
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
            pnEditMode = EditMode.UNKNOWN;
        }
        return poJSON;
    }

    @Override
    public JSONObject openTransaction(String fsValue) {
        poJSON = new JSONObject();
        
        poJSON = poController.openTransaction(fsValue);
        if("success".equals(poJSON.get("result"))){
            pnEditMode = poController.getEditMode();
        } else {
            pnEditMode = EditMode.UNKNOWN;
        }
        
        poJSON = checkData(poJOLabor.openDetail(fsValue));
        if(!"success".equals((String) poJSON.get("result"))){
            pnEditMode = EditMode.UNKNOWN;
            return poJSON;
        }
        
        poJSON =  checkData(poJOParts.openDetail(fsValue));
        if(!"success".equals((String) poJSON.get("result"))){
            pnEditMode = EditMode.UNKNOWN;
            return poJSON;
        }
        
        poJSON =  checkData(poIntakeTechnician.openDetail(fsValue));
        if(!"success".equals((String) poJSON.get("result"))){
            pnEditMode = EditMode.UNKNOWN;
            return poJSON;
        }

        return poJSON;
    }

    @Override
    public JSONObject updateTransaction() {
        poJSON = new JSONObject();  
        poJSON = poController.updateTransaction();
        if("error".equals(poJSON.get("result"))){
            return poJSON;
        }
        pnEditMode = poController.getEditMode();
        return poJSON;
    }

    @Override
    public JSONObject saveTransaction() {
        poJSON = new JSONObject();  
        
        poJSON = computeAmount();
        if("error".equalsIgnoreCase((String)poJSON.get("result"))){
            return poJSON;
        }
        
        poJSON = validateEntry();
        if("error".equalsIgnoreCase((String)poJSON.get("result"))){
            return poJSON;
        }
        
        if (!pbWtParent) poGRider.beginTrans();
        
        poJSON =  poController.saveTransaction();
        if("error".equalsIgnoreCase((String) checkData(poJSON).get("result"))){
            if (!pbWtParent) poGRider.rollbackTrans();
            return checkData(poJSON);
        }
        
//        poJOLabor.setTargetBranchCd(poController.getMasterModel().getBranchCD());
        poJSON =  poJOLabor.saveDetail((String) poController.getMasterModel().getTransNo());
        if("error".equalsIgnoreCase((String)checkData(poJSON).get("result"))){
            if (!pbWtParent) poGRider.rollbackTrans();
            return checkData(poJSON);
        }
        
//        poJOParts.setTargetBranchCd(poController.getMasterModel().getBranchCD());
        poJSON =  poJOParts.saveDetail((String) poController.getMasterModel().getTransNo());
        if("error".equalsIgnoreCase((String)checkData(poJSON).get("result"))){
            if (!pbWtParent) poGRider.rollbackTrans();
            return checkData(poJSON);
        }
        
//        poIntakeTechnician.setTargetBranchCd(poController.getMasterModel().getBranchCD());
        poJSON =  poIntakeTechnician.saveDetail((String) poController.getMasterModel().getTransNo());
        if("error".equalsIgnoreCase((String)checkData(poJSON).get("result"))){
            if (!pbWtParent) poGRider.rollbackTrans();
            return checkData(poJSON);
        }
        
        if (!pbWtParent) poGRider.commitTrans();
        
        return poJSON;
    }
    
    private JSONObject checkData(JSONObject joValue){
        if(pnEditMode == EditMode.ADDNEW ||pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE){
            if(joValue.containsKey("continue")){
                if(true == (boolean)joValue.get("continue")){
                    joValue.put("result", "success");
                    joValue.put("message", "Record saved successfully.");
                }
            }
        }
        return joValue;
    }
    
    public JSONObject searchTransaction(String fsValue, boolean fbByCode) {
        poJSON = new JSONObject();  
        poJSON = poController.searchTransaction(fsValue, fbByCode);
        if(!"error".equals(poJSON.get("result"))){
            poJSON = openTransaction((String) poJSON.get("sTransNox"));
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
    public JSONObject cancelTransaction(String fsValue) {
        return poController.cancelTransaction(fsValue);
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
    public JobOrder_Master getMasterModel() {
        return poController;
    }
    @Override
    public void setTransactionStatus(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public JobOrder_Labor getJOLaborModel(){return poJOLabor;} 
    public ArrayList getJOLaborList(){return poJOLabor.getDetailList();}
    public Object addJOLabor(){ return poJOLabor.addDetail(poController.getMasterModel().getTransNo());}
    public Object removeJOLabor(int fnRow){ return poJOLabor.removeDetail(fnRow);}
    
    public JobOrder_Parts getJOPartsModel(){return poJOParts;} 
    public ArrayList getJOPartsList(){return poJOParts.getDetailList();}
    public Object addJOParts(){ return poJOParts.addDetail(poController.getMasterModel().getTransNo());}
    public Object removeJOParts(int fnRow){ return poJOParts.removeDetail(fnRow);}
    
    public Intake_Technician getJOTechModel(){return poIntakeTechnician;} 
    public ArrayList getJOTechList(){return poIntakeTechnician.getDetailList();}
    public Object addJOTech(){ return poIntakeTechnician.addDetail(poController.getMasterModel().getTransNo());}
    public Object removeJOTech(int fnRow){ return poIntakeTechnician.removeDetail(fnRow);}
    
    public JSONObject computeAmount(){
        JSONObject loJSON = new JSONObject();
        int lnCtr = 0;
        BigDecimal ldblLaborAmt = new BigDecimal("0.00");
        BigDecimal ldblPartsAmt = new BigDecimal("0.00");
        /*Compute Labor Total*/
        for (lnCtr = 0; lnCtr <= getJOLaborList().size()-1; lnCtr++){
            ldblLaborAmt = ldblLaborAmt.add(poJOLabor.getDetailModel(lnCtr).getUnitPrce()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        
        /*Compute Parts Total*/
        for (lnCtr = 0; lnCtr <= getJOPartsList().size()-1; lnCtr++){
            ldblPartsAmt = ldblPartsAmt.add(new BigDecimal(String.valueOf(poJOParts.getDetailModel(lnCtr).getQtyEstmt())).multiply(poJOParts.getDetailModel(lnCtr).getUnitPrce()));
        }
        
        poController.getMasterModel().setLaborAmt(ldblLaborAmt);
        poController.getMasterModel().setPartsAmt(ldblPartsAmt);
        poController.getMasterModel().setTranAmt(ldblLaborAmt.add(ldblPartsAmt));
        
        return loJSON;
    }
    
    private JSONObject validateEntry(){
        JSONObject loJSON = new JSONObject();
        
        //Validate JO labor required
        if(poJOLabor.getDetailList().size() - 1 < 0 && poJOParts.getDetailList().size() - 1 < 0){
            loJSON.put("result","error");
            loJSON.put("message", "Job Order Labor and Parts cannot be empty.");
        }
        return loJSON;
    }
    
    public JSONObject searchVSP(String fsValue, boolean fbByCode){
        JSONObject loJSON = new JSONObject();
        JSONObject loJSONDet = new JSONObject();
        loJSON = poController.searchVSP(fsValue,fbByCode);
        if(!"error".equals((String) loJSON.get("result"))){
            loJSONDet = poVSPLabor.openDetail((String) loJSON.get("sTransNox"));
            if( "error".equals((String) loJSONDet.get("result"))){
                return loJSONDet;
            }
            
            loJSONDet = poVSPParts.openDetail((String) loJSON.get("sTransNox"));
            if( "error".equals((String) loJSONDet.get("result"))){
                return loJSONDet;
            }
            
            poController.getMasterModel().setSourceCD("VSP");
            poController.getMasterModel().setSourceNo((String) loJSON.get("sTransNox"));
            poController.getMasterModel().setVSPNo((String) loJSON.get("sVSPNOxxx"));
            poController.getMasterModel().setSerialID((String) loJSON.get("sSerialID"));
            poController.getMasterModel().setClientID((String) loJSON.get("sClientID"));
            poController.getMasterModel().setOwnrNm((String) loJSON.get("sBuyCltNm"));
            poController.getMasterModel().setClientTp((String) loJSON.get("cClientTp"));
            poController.getMasterModel().setAddress((String) loJSON.get("sAddressx"));
            poController.getMasterModel().setCSNo((String) loJSON.get("sCSNoxxxx"));
            poController.getMasterModel().setPlateNo((String) loJSON.get("sPlateNox"));
            poController.getMasterModel().setFrameNo((String) loJSON.get("sFrameNox"));
            poController.getMasterModel().setEngineNo((String) loJSON.get("sEngineNo"));
            poController.getMasterModel().setVhclDesc((String) loJSON.get("sVhclFDsc"));
            poController.getMasterModel().setBranchCD((String) loJSON.get("sBranchCD"));
            poController.getMasterModel().setBranchNm((String) loJSON.get("sBranchNm"));
            poController.getMasterModel().setEmployID((String) loJSON.get("sEmployID"));
            poController.getMasterModel().setEmployNm((String) loJSON.get("sSENamexx"));
            
        } else {
            poController.getMasterModel().setSourceCD("");
            poController.getMasterModel().setSourceNo("");        
            poController.getMasterModel().setVSPNo("");           
            poController.getMasterModel().setSerialID("");        
            poController.getMasterModel().setClientID("");        
            poController.getMasterModel().setOwnrNm("");          
            poController.getMasterModel().setClientTp("");        
            poController.getMasterModel().setAddress("");         
            poController.getMasterModel().setCSNo("");            
            poController.getMasterModel().setPlateNo("");         
            poController.getMasterModel().setFrameNo("");         
            poController.getMasterModel().setEngineNo("");        
            poController.getMasterModel().setVhclDesc("");        
            poController.getMasterModel().setBranchCD("");        
            poController.getMasterModel().setBranchNm("");        
            poController.getMasterModel().setEmployID("");        
            poController.getMasterModel().setEmployNm("");
        }
        return loJSON;
    }
    
    public VehicleSalesProposal_Labor getVSPLaborModel(){return poVSPLabor;} 
    public ArrayList getVSPLaborList(){return poVSPLabor.getDetailList();}
    
    public VehicleSalesProposal_Parts getVSPPartsModel(){return poVSPParts;} 
    public ArrayList getVSPPartsList(){return poVSPParts.getDetailList();}
    
    
    public JSONObject searchEmployee(String fsValue, boolean fbIsTechnician){
        JSONObject loJSON = new JSONObject();
        loJSON = poController.searchEmployee(fsValue);
        if(!"error".equals((String) poJSON.get("result"))){
            if(!fbIsTechnician){
                poController.getMasterModel().setEmployID((String) poJSON.get("sClientID"));
                poController.getMasterModel().setEmployNm((String) poJSON.get("sCompnyNm"));
            }
        } else {
            if(!fbIsTechnician){
                poController.getMasterModel().setEmployID("");
                poController.getMasterModel().setEmployNm("");
            }
        }
        return loJSON;
    }
    
    /**
     * Check VSP Parts linked to JO
     * @param fsValue parts Stock ID
     * @param fnInputQty parts quantity to be input
     * @param fnJoRow JO Row.
    */
    public JSONObject checkVSPJOParts(String fsValue, int fnInputQty, int fnJoRow, boolean fbIsAdd){ //, boolean fbIsAdd
        JSONObject loJSON = new JSONObject();
        int lnVSPQty = 0;
        int lnTotalQty = 0;
        
        poVSPParts.openDetail(poController.getMasterModel().getSourceNo());
        for (int lnCtr = 0; lnCtr <= poVSPParts.getDetailList().size() - 1; lnCtr++){
            if((poVSPParts.getDetailModel(lnCtr).getStockID()).equals(fsValue)){
                lnVSPQty = poVSPParts.getDetailModel(lnCtr).getQuantity();
                break;
            }
        }
        System.out.println(fsValue + " VSP Total Quantity : " + lnVSPQty);
        
        String lsSQL = poJOParts.getDetailModel(fnJoRow).makeSelectSQL();
        lsSQL = MiscUtil.addCondition(lsSQL, " sTransNox <> " + SQLUtil.toSQL(poController.getMasterModel().getTransNo()) 
                                                + " AND sStockIDx = " + SQLUtil.toSQL(fsValue)) 
                                                + " AND sTransNox IN (SELECT diagnostic_master.sTransNox FROM diagnostic_master " 
                                                +                    " WHERE diagnostic_master.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED) 
                                                                    + " AND diagnostic_master.sSourceNo = " + SQLUtil.toSQL(poController.getMasterModel().getSourceNo()) + ")" ;
        System.out.println(lsSQL);
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        if (MiscUtil.RecordCount(loRS) > 0){
            try {
                while(loRS.next()){
                    lnTotalQty = lnTotalQty +  loRS.getInt("nQtyEstmt") ;
                }
                
                MiscUtil.close(loRS);  
                System.out.println(fsValue + " VSP Parts Linked to JO Total Quantity : " + lnTotalQty);
                
                lnTotalQty = lnTotalQty + fnInputQty;
            } catch (SQLException ex) {
                Logger.getLogger(JobOrder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(fbIsAdd){
            fnInputQty = lnVSPQty - lnTotalQty;
        }
        
//        if ((lnTotalQty == lnVSPQty)){
//            loJSON.put("result", "error");
//            loJSON.put("message", "VSP Parts quantity already settled.");
//            return loJSON;
//        }
        
        if ((lnTotalQty > lnVSPQty) || (fnInputQty > lnVSPQty)){
            loJSON.put("result", "error");
            loJSON.put("message", "Declared VSP Parts quantity must not be less than the quantity linked to JO Parts.");
            return loJSON;
        }

        if (fnInputQty <= 0){
            if(fbIsAdd){
                loJSON.put("result", "error");
                loJSON.put("message", "All remaining VSP Parts Quantity already linked to JO.");
                return loJSON;
            }else{
                loJSON.put("result", "error");
                loJSON.put("message", "Please input valid Parts Quantity.");
                return loJSON;
            }
        }
        
//        if(fbIsAdd){
//            poJOParts.getDetailModel(fnJoRow).setQtyEstmt(nVSPQty-nTotalQty);
//        } else {
            poJOParts.getDetailModel(fnJoRow).setQtyEstmt(fnInputQty);
//        }
          
        return loJSON;
    }
}
