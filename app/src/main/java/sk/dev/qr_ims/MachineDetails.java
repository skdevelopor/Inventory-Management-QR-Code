package sk.dev.qr_ims;


public class MachineDetails {
    private  String UId;
    private  String machineName;
    private String  machineInstallationDate;
    private String  QrImageUrl;
 MachineDetails(){

 }
    public MachineDetails(String UId, String machineName, String machineInstallationDate, String qrImageUrl) {
        this.UId = UId;
        this.machineName = machineName;
        this.machineInstallationDate = machineInstallationDate;
        QrImageUrl = qrImageUrl;
    }

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineInstallationDate() {
        return machineInstallationDate;
    }

    public void setMachineInstallationDate(String machineInstallationDate) {
        this.machineInstallationDate = machineInstallationDate;
    }

    public String getQrImageUrl() {
        return QrImageUrl;
    }

    public void setQrImageUrl(String qrImageUrl) {
        QrImageUrl = qrImageUrl;
    }
}
