package com.konka.upgrade.xml;

import java.util.ArrayList;
import java.util.List;

public class XmlUpgradeInfo {

    protected String Manufacture;
    protected String ModelID;
    protected String HW_version;
    protected String SW_server_version;
    protected String SW_stb_version;
    protected String Period;
    protected String Control;
    protected String ImgInfo;
    
    protected List<String> urlList;
    protected List<Scope> scopeList;
    
    public XmlUpgradeInfo() {
        urlList = new ArrayList<String>();
        scopeList = new ArrayList<XmlUpgradeInfo.Scope>();
    }
    
    public List<String> getUrlList() {
        return urlList;
    }
    
    public List<Scope> getScopeList() {
        return scopeList;
    }
    
    public String getManufacture() {
        return Manufacture;
    }
    
    public String getModelID() {
        return ModelID;
    }
    
    public String getHwVersion() {
        return HW_version;
    }
    
    public String getServerVersion() {
        return SW_server_version;
    }
    
    public String getSTBVersion() {
        return SW_stb_version;
    }
    
    public String getPeriod() {
        return Period;
    }
    
    public String getControl() {
        return Control;
    }
    
    public String getImgInfo() {
        return ImgInfo;
    }
    
    public class Scope {
        protected String startSN;
        protected String endSN;
        protected List<String> index;
        
        protected Scope() {
            index = new ArrayList<String>();
        }
        
        public String getStartSN() {
            return startSN;
        }
        
        public String getEndSN() {
            return endSN;
        }
        
        public List<String> getIndex() {
            return index;
        }
    }
}
