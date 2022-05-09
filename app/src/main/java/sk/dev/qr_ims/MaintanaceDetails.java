package sk.dev.qr_ims;

public class MaintanaceDetails {
    private  String ServiceDescription;
    private  String TechName;
    private  String MaintenanceDate;
    private  String DueDate;

    public MaintanaceDetails(String serviceDescription, String techName, String maintenanceDate, String dueDate) {
        ServiceDescription = serviceDescription;
        TechName = techName;
        MaintenanceDate = maintenanceDate;
        DueDate = dueDate;
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
