package org.sounfury.cyber_hamster.data.network.request;

//入库请求体
public class InboundInput {
    private String isbn;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public InboundInput(String isbn, String storageLocation, String remark) {
        this.isbn = isbn;
        this.storageLocation = storageLocation;
        this.remark = remark;
    }

    private String storageLocation;
    private String remark;

}