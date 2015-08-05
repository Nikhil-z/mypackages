package com.konka.upgrade.service;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class UpgradeDesc implements Parcelable {
    
    private String manufacture;
    private String model;
    private String hwVersion;
    private long SW_server_version;
    private long SW_stb_version;
    private String Period;
    private String Control;
    private List<String> urls = new ArrayList<String>();
    private String UpgradeFilePath;
    private String ImgInfoUrl;
    private String ImgInfo;
    
    public UpgradeDesc() {}
    
    public UpgradeDesc(Parcel in) {
        readFromParcel(in);
    }
    
    /**
     *  get upgrade image description
     * @return imgInfo
     */
    public String getImgInfo() {
        return ImgInfo;
    }
    
    /**
     * get upgrade image version
     * @return SW_server_version
     */
    public long getUpgradeVersion() {
        return SW_server_version;
    }
    
    /**
     * get manufacture
     * @return manufacture
     */
    public String getManufacture() {
        return manufacture;
    }
    
    /**
     * get model
     * @return model
     */
    public String getModel() {
        return model;
    }
    
    /**
     * get hardware version
     * @return hwVersion
     */
    public String getHWVersion() {
        return hwVersion;
    }
    
    /**
     * set upgrade image info
     * @param info
     */
    public void setImgInfo(String info) {
        this.ImgInfo = info;
    }
    
    /**
     * set upgrade image version
     * @param version
     */
    public void setUpgradeVersion(long version) {
        this.SW_server_version = version;
    }
    
    /**
     * set upgrade manufacture
     * @param manufacture
     */
    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }
    
    /**
     * set upgrade model
     * @param model
     */
    public void setModel(String model) {
        this.model = model;
    }
    
    /**
     * set upgrade hardware version
     * @param hwVersion
     */
    public void setHWVersion(String hwVersion) {
        this.hwVersion = hwVersion;
    }
    
    /**
     * reset all data
     */
    protected void clear() {
        manufacture = "";
        model = "";
        hwVersion = "";
        SW_stb_version = 0;
        SW_server_version = 0;
        Period = "";
        Control = "";
        UpgradeFilePath = "";
        ImgInfoUrl = "";
        ImgInfo = "";
        urls.clear();
        ImgInfo = "";
    }
    
    protected void setStbVersion(long version) {
        SW_stb_version = version;
    }
    
    protected long getStbVersion() {
        return SW_stb_version;
    }
    
    protected void setPeriod(String period) {
        Period = period;
    }
    
    protected String getPeriod() {
        return Period;
    }
    
    protected void setControl(String control) {
        Control = control;
    }
    
    protected String getControl() {
        return Control;
    }
    
    protected void setImgUrl(List<String> urls) {
        this.urls = urls;
    }
    
    protected List<String> getImgUrl() {
        return urls;
    }
    
    protected void setUpgradeFilePath(String path) {
        UpgradeFilePath = path;
    }
    
    protected String getUpgradeFilePath() {
        return UpgradeFilePath;
    }
    
    protected void setImgInfoUrl(String url) {
        ImgInfoUrl = url;
    }
    
    protected String getImgInfoUrl() {
        return ImgInfoUrl;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        // TODO Auto-generated method stub
        out.writeString(manufacture);
        out.writeString(model);
        out.writeString(hwVersion);
        out.writeLong(SW_stb_version);
        out.writeString(ImgInfo);
    }
    
    private void readFromParcel(Parcel in) {
        manufacture = in.readString();
        model = in.readString();
        hwVersion = in.readString();
        SW_stb_version = in.readLong();
        ImgInfo = in.readString();
    }

    public static final Parcelable.Creator<UpgradeDesc> CREATOR = 
            new Parcelable.Creator<UpgradeDesc>() {
                
                @Override
                public UpgradeDesc[] newArray(int size) {
                    // TODO Auto-generated method stub
                    return new UpgradeDesc[size];
                }
                
                @Override
                public UpgradeDesc createFromParcel(Parcel source) {
                    // TODO Auto-generated method stub
                    return new UpgradeDesc(source);
                }
            };
}
