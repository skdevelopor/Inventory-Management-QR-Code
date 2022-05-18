package sk.dev.qr_ims;

public class EngineersDetail {
    private String UId;
    private String engineersName;
    private String engineersPhoneNumber;
    private String engineersImageUrl;
    private String engineersDesignation;
    private String engineersEmailId;

    public EngineersDetail() {
    }

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }

    public String getEngineersName() {
        return engineersName;
    }

    public void setEngineersName(String engineersName) {
        this.engineersName = engineersName;
    }

    public String getEngineersPhoneNumber() {
        return engineersPhoneNumber;
    }

    public void setEngineersPhoneNumber(String engineersPhoneNumber) {
        this.engineersPhoneNumber = engineersPhoneNumber;
    }

    public String getEngineersImageUrl() {
        return engineersImageUrl;
    }

    public void setEngineersImageUrl(String engineersImageUrl) {
        this.engineersImageUrl = engineersImageUrl;
    }

    public String getEngineersDesignation() {
        return engineersDesignation;
    }

    public void setEngineersDesignation(String engineersDesignation) {
        this.engineersDesignation = engineersDesignation;
    }

    public String getEngineersEmailId() {
        return engineersEmailId;
    }

    public void setEngineersEmailId(String engineersEmailId) {
        this.engineersEmailId = engineersEmailId;
    }

    public EngineersDetail(String UId, String engineersName, String engineersPhoneNumber, String engineersImageUrl, String engineersDesignation, String engineersEmailId) {
        this.UId = UId;
        this.engineersName = engineersName;
        this.engineersPhoneNumber = engineersPhoneNumber;
        this.engineersImageUrl = engineersImageUrl;
        this.engineersDesignation = engineersDesignation;
        this.engineersEmailId = engineersEmailId;
    }
}
