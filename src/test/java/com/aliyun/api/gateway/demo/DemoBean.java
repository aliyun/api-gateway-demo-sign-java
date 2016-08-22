package com.aliyun.api.gateway.demo;

import java.util.List;

/**
 * Created by Dingyili on 2016/8/22.
 */
public class DemoBean {

    /**
     * image : {"dataType":50,"dataValue":"Base64编码的字符"}
     */

    private List<InputsBean> inputs;

    public List<InputsBean> getInputs() {
        return inputs;
    }

    public void setInputs(List<InputsBean> inputs) {
        this.inputs = inputs;
    }

    public static class InputsBean {
        /**
         * dataType : 50
         * dataValue : Base64编码的字符
         */

        private ImageBean image;

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public static class ImageBean {
            private int dataType;
            private String dataValue;

            public int getDataType() {
                return dataType;
            }

            public void setDataType(int dataType) {
                this.dataType = dataType;
            }

            public String getDataValue() {
                return dataValue;
            }

            public void setDataValue(String dataValue) {
                this.dataValue = dataValue;
            }
        }
    }
}
