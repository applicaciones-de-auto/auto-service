/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.main.service;

import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.service.controller.Job_Order_Labor;
import org.guanzon.auto.service.controller.Job_Order_Master;
import org.guanzon.auto.service.controller.Job_Order_Parts;

/**
 *
 * @author Arsiela
 */
public class JobOrder {
    GRider poGRider;
    String psBranchCd;
    boolean pbWtParent;
    
    int pnEditMode;
    String psMessagex;
    String psClientType = "0";    
    
    Job_Order_Master poMaster;
    Job_Order_Labor poLabor;
    Job_Order_Parts poParts;
    
    public JobOrder(GRider foAppDrver, boolean fbWtParent, String fsBranchCd){
        poGRider = foAppDrver;
        pbWtParent = fbWtParent;
        psBranchCd = fsBranchCd.isEmpty() ? foAppDrver.getBranchCode() : fsBranchCd;
    }
}
