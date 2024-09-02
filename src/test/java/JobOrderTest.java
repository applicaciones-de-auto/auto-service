
import java.math.BigDecimal;
import java.sql.SQLException;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.service.JobOrder;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Arsiela
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JobOrderTest {
    static JobOrder model;
    JSONObject json;
    boolean result;
    static GRider instance;
    public JobOrderTest(){}
    
    @BeforeClass
    public static void setUpClass() {   
        
        String path;
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Maven_Systems";
        }
        else{
            path = "/srv/GGC_Maven_Systems";
        }
        System.setProperty("sys.default.path.config", path);
        instance = new GRider("gRider");
        if (!instance.logUser("gRider", "M001000001")){
            System.err.println(instance.getMessage() + instance.getErrMsg());
            System.exit(1);
        }
        System.out.println("Connected");
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");
        
        
        JSONObject json;
        
        System.out.println("sBranch code = " + instance.getBranchCode());
        model = new JobOrder(instance,false, instance.getBranchCode());
    }
    
    @AfterClass
    public static void tearDownClass() {
        
    }
    
    
    /**
     * COMMENTED TESTING TO CLEAN AND BUILD PROPERLY
     * WHEN YOU WANT TO CHECK KINDLY UNCOMMENT THE TESTING CASES (@Test).
     * ARSIELA 
     */
//    
    @Test
    public void test01NewRecord() throws SQLException{
        System.out.println("--------------------------------------------------------------------");
        System.out.println("------------------------------NEW RECORD--------------------------------------");
        System.out.println("--------------------------------------------------------------------");
        
        json = model.newTransaction();
        if ("success".equals((String) json.get("result"))){

            json = model.getMasterModel().getMasterModel().setSerialID("M001VS240005");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setClientID("M00124000028");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setWorkCtgy("2");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setJobType("0");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setLaborTyp("4");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setKMReadng(0);
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setEmployID("A00118000001");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setRemarks("TEST");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setPaySrce("3");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setSourceNo("M001VSP24001");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setSourceCD("VSP");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setPromisedDte(instance.getServerDate());
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setInsurnce("");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setCompUnit("0");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setActvtyID("");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setLaborAmt(new BigDecimal("10000.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setPartsAmt(new BigDecimal("6000.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setTranAmt(new BigDecimal("16000.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setRcvryAmt(new BigDecimal("0.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setPartFee(new BigDecimal("0.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setPrinted("0");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
        } else {
            System.err.println("result = " + (String) json.get("result"));
            fail((String) json.get("message"));
        }
        
    }
    
    @Test
    public void test01NewRecordSave(){
        System.out.println("--------------------------------------------------------------------");
        System.out.println("------------------------------NEW RECORD SAVING--------------------------------------");
        System.out.println("--------------------------------------------------------------------");
        
        json = model.saveTransaction();
        System.err.println((String) json.get("message"));
        
        if (!"success".equals((String) json.get("result"))){
            System.err.println((String) json.get("message"));
            result = false;
        } else {
            System.out.println((String) json.get("message"));
            result = true;
        }
        
        assertTrue(result);
        //assertFalse(result);
    }
    
//    @Test
//    public void test02OpenRecord(){
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------RETRIEVAL--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.openTransaction("M001IQ240004");
//        
//        if (!"success".equals((String) json.get("result"))){
//            result = false;
//        } else {
//            System.out.println("--------------------------------------------------------------------");
//            System.out.println("VSP MASTER");
//            System.out.println("--------------------------------------------------------------------");
//            System.out.println("sBranchNm  :  " + model.getMaster("sBranchNm"));
//            
//            System.out.println("--------------------------------------------------------------------");
//            System.out.println("VSP FINANCE");
//            System.out.println("--------------------------------------------------------------------");
//            for(int lnCtr = 0;lnCtr <= model.getVSPFinanceList().;lnCtr++){
//                System.out.println("sTransNox  :  " +); 
//            }
//            
//            System.out.println("--------------------------------------------------------------------");
//            System.out.println("VSP LABOR");
//            System.out.println("--------------------------------------------------------------------");
//            for(int lnCtr = 0;lnCtr <= model.getVSPLaborList().size()-1; lnCtr++){
//                System.out.println("sTransNox  :  " +); 
//            }
//            
//            System.out.println("--------------------------------------------------------------------");
//            System.out.println("VSP PARTS");
//            System.out.println("--------------------------------------------------------------------");
//            for(int lnCtr = 0;lnCtr <= model.getVSPPartsList().size()-1; lnCtr++){
//                System.out.println("sTransNox  :  " +); 
//            }
//            
//            result = true;
//        }
//        assertTrue(result);
//        //assertFalse(result);
//    }
//    
//    @Test
//    public void test03UpdateRecord(){
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------UPDATE RECORD--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.updateTransaction();
//        System.err.println((String) json.get("message"));
//        if ("error".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            result = false;
//        } else {
//            result = true;
//        }
    
//        assertTrue(result);
//        //assertFalse(result);
//    }
//    
//    @Test
//    public void test03UpdateRecordSave(){
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------UPDATE RECORD SAVING--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.saveTransaction();
//        System.err.println((String) json.get("message"));
//        
//        if (!"success".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            result = false;
//        } else {
//            System.out.println((String) json.get("message"));
//            result = true;
//        }
//        assertTrue(result);
//        //assertFalse(result);
//    }
    
}
