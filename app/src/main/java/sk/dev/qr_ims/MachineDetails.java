package sk.dev.qr_ims;


public class MachineDetails {
    private String UId;
    private String machineName;
    private String machineInstallationDate;
    private String qrImageUrl;
    private String machineImageUrl;
    private String machineBatchNumber;



    public MachineDetails(String UId, String machineName, String machineInstallationDate, String qrImageUrl, String machineImageUrl, String machineBatchNumber) {
        this.UId = UId;
        this.machineName = machineName;
        this.machineInstallationDate = machineInstallationDate;
        this.qrImageUrl = qrImageUrl;
        this.machineImageUrl = machineImageUrl;
        this.machineBatchNumber = machineBatchNumber;
    }
    public MachineDetails() {
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
        return qrImageUrl;
    }

    public void setQrImageUrl(String qrImageUrl) {
        this.qrImageUrl = qrImageUrl;
    }

    public String getMachineImageUrl() {
        return machineImageUrl;
    }

    public void setMachineImageUrl(String machineImageUrl) {
        this.machineImageUrl = machineImageUrl;
    }

    public String getMachineBatchNumber() {
        return machineBatchNumber;
    }

    public void setMachineBatchNumber(String machineBatchNumber) {
        this.machineBatchNumber = machineBatchNumber;
    }
}