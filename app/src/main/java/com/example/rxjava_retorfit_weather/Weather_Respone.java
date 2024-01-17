package com.example.rxjava_retorfit_weather;

import java.util.List;

public class Weather_Respone {
    private String success; //拿取成功
    private Result result; //結果的class內部的資料
    private Records records; //Records的內部的資料




    public class Result{
        private String resource_id;
        private List<Fields> fields;

        public class Fields{
            private String id;
            private String type;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

        }

        public String getResource_id() {
            return resource_id;
        }

        public void setResource_id(String resource_id) {
            this.resource_id = resource_id;
        }

        public List<Fields> getFields() {
            return fields;
        }

        public void setFields(List<Fields> fields) {
            this.fields = fields;
        }
    }

    public class Records{
        private String datasetDescription;
        private List<Locatino> location;

        public class Locatino{
            private String locationName;
            private List<WeatherElement> weatherElement;

            public class WeatherElement{
                private String elementName; //參數名稱這裡是 "MaxT" or "MinT"
                private List<MyTime> time;
                public class MyTime{
                    private Parameter parameter;
                    public class Parameter{
                        private String parameterName; //溫度值 、 天期狀況 ...等
                        private String parameterUnit; //溫度單位

                        public String getParameterName() {
                            return parameterName;
                        }

                        public void setParameterName(String parameterName) {
                            this.parameterName = parameterName;
                        }

                        public String getParameterUnit() {
                            return parameterUnit;
                        }

                        public void setParameterUnit(String parameterUnit) {
                            this.parameterUnit = parameterUnit;
                        }
                    }

                    public Parameter getParameter() {
                        return parameter;
                    }

                    public void setParameter(Parameter parameter) {
                        this.parameter = parameter;
                    }
                }

                public String getElementName() {
                    return elementName;
                }

                public void setElementName(String elementName) {
                    this.elementName = elementName;
                }

                public List<MyTime> getTime() {
                    return time;
                }

                public void setTime(List<MyTime> time) {
                    this.time = time;
                }
            }

            public List<WeatherElement> getWeatherElement() {
                return weatherElement;
            }

            public void setWeatherElement(List<WeatherElement> weatherElement) {
                this.weatherElement = weatherElement;
            }

            public String getLocationName() {
                return locationName;
            }

            public void setLocationName(String locationName) {
                this.locationName = locationName;
            }
        }

        public String getDatasetDescription() {
            return datasetDescription;
        }

        public void setDatasetDescription(String datasetDescription) {
            this.datasetDescription = datasetDescription;
        }

        public List<Locatino> getLocation() {
            return location;
        }

        public void setLocation(List<Locatino> location) {
            this.location = location;
        }
    }

    public Records getRecords() {
        return records;
    }

}
