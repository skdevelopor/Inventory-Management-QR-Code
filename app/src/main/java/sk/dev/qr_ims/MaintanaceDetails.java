package sk.dev.qr_ims;

public class MaintanaceDetails {
    private  String ServiceDescription;
    private  String TechName;
    private  String MaintenanceDate;
    private  String DueDate;
    private  String techEmailId;
    private  String techPosition;
    private  String techPhoneNumber;

    public MaintanaceDetails(String serviceDescription, String techName, String maintenanceDate, String dueDate, String techEmailId, String techPosition, String techPhoneNumber, String techProfilePic, String techUID) {
        ServiceDescription = serviceDescription;
        TechName = techName;
        MaintenanceDate = maintenanceDate;
        DueDate = dueDate;
        this.techEmailId = techEmailId;
        this.techPosition = techPosition;
        this.techPhoneNumber = techPhoneNumber;
        this.techProfilePic = techProfilePic;
        this.techUID = techUID;
    }

    private  String techProfilePic;
    private  String techUID;

    public String getTechUID() {
        return techUID;
    }

    public void setTechUID(String techUID) {
        this.techUID = techUID;
    }




    public String getTechEmailId() {
        return techEmailId;
    }

    public void setTechEmailId(String techEmailId) {
        this.techEmailId = techEmailId;
    }

    public String getTechPosition() {
        return techPosition;
    }

    public void setTechPosition(String techPosition) {
        this.techPosition = techPosition;
    }

    public String getTechPhoneNumber() {
        return techPhoneNumber;
    }

    public void setTechPhoneNumber(String techPhoneNumber) {
        this.techPhoneNumber = techPhoneNumber;
    }

    public String getTechProfilePic() {
        return techProfilePic;
    }

    public void setTechProfilePic(String techProfilePic) {
        this.techProfilePic = techProfilePic;
    }

    public MaintanaceDetails() {

    }

    public String getServiceDescription() {
        return ServiceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        ServiceDescription = serviceDescription;
    }

    public String getTechName() {
        return TechName;
    }

    public void setTechName(String techName) {
        TechName = techName;
    }

    public String getMaintenanceDate() {
        return MaintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        MaintenanceDate = maintenanceDate;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }
}
