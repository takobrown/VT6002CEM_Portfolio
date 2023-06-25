package com.mobile.diary;

import java.util.List;

public class AddressBean {

    private List<DataModel> data;

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }



    public static class DataModel {
        private String label;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
