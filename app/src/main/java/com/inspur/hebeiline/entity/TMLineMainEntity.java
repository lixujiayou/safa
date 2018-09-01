package com.inspur.hebeiline.entity;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lixu on 2018/3/28.
 */

public class TMLineMainEntity implements Serializable{




    private String coordtype;
    private List<ResultsBean> results;

    public String getCoordtype() {
        return coordtype;
    }

    public void setCoordtype(String coordtype) {
        this.coordtype = coordtype;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {


        private String layerId;
        private String layerName;
        private AttributesBean attributes;
        private String geometryType;
        private GeometryBean geometry;
        private GeoJsonBean geoJson;

        public String getLayerId() {
            return layerId;
        }

        public void setLayerId(String layerId) {
            this.layerId = layerId;
        }

        public String getLayerName() {
            return layerName;
        }

        public void setLayerName(String layerName) {
            this.layerName = layerName;
        }

        public AttributesBean getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesBean attributes) {
            this.attributes = attributes;
        }

        public String getGeometryType() {
            return geometryType;
        }

        public void setGeometryType(String geometryType) {
            this.geometryType = geometryType;
        }

        public GeometryBean getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryBean geometry) {
            this.geometry = geometry;
        }

        public GeoJsonBean getGeoJson() {
            return geoJson;
        }

        public void setGeoJson(GeoJsonBean geoJson) {
            this.geoJson = geoJson;
        }

        public static class AttributesBean {
            /**
             * OBJECTID : 16950262
             * NAME : 邯郸复兴区铁西-邯郸邯郸永不分梨
             * SHAPE.LEN : 0.0139269654695315
             * UUID : 568065937
             * LABEL : null
             * CITY_UUID : 41
             * COUNTY_UUID : 48095513
             * ADDRESS : null
             * TASK_UUID : null
             * PROJECT_UUID : 485258834
             * ROUTE_TYPE : 9202
             * START_UUID : 71509
             * END_UUID : 568064838
             * CURRENT_UUID : null
             * FIBER_NUM : 12
             * OPTI_TYPE : null
             * SYSTEM_LEVEL : 5
             * TIME_STAMP : 1420543583000
             * CREATOR : 陈丹
             * CREAT_TIME : 1420543583000
             * MODIFIER : null
             * MODIFY_TIME : null
             * STATEFLAG : 0
             * DML_STATUS : null
             * LC_STATUS : 3
             * ATTR01 : null
             * ATTR02 : null
             * ATTR03 : null
             * ATTR04 : null
             * ATTR05 : null
             * ATTR06 : null
             * ATTR07 : null
             * ATTR08 : null
             * ATTR09 : null
             * ATTR10 : null
             * ATTR11 : null
             * ATTR12 : null
             * ATTR13 : null
             * ATTR14 : null
             * ATTR15 : null
             * ATTR16 : null
             * ATTR17 : null
             * ATTR18 : 0
             * ATTR19 : null
             * ATTR20 : null
             * BUSINESS_UUID : null
             * PORE_NUM : null
             * C_LENGTH : 1509.1500000000001
             * M_LENGTH : 1509.1500000000001
             * ID : 568065937
             * X : 0.0
             * Y : 0.0
             */

            private String OBJECTID;
            private String NAME;
            @SerializedName("SHAPE.LEN")
            private String _$SHAPELEN275; // FIXME check this code
            private String UUID;
            private Object LABEL;
            private String CITY_UUID;
            private String COUNTY_UUID;
            private Object ADDRESS;
            private Object TASK_UUID;
            private String PROJECT_UUID;
            private String ROUTE_TYPE;
            private String START_UUID;
            private String END_UUID;
            private Object CURRENT_UUID;
            private String FIBER_NUM;
            private Object OPTI_TYPE;
            private String SYSTEM_LEVEL;
            private String TIME_STAMP;
            private String CREATOR;
            private String CREAT_TIME;
            private Object MODIFIER;
            private Object MODIFY_TIME;
            private String STATEFLAG;
            private Object DML_STATUS;
            private String LC_STATUS;
            private Object ATTR01;
            private Object ATTR02;
            private Object ATTR03;
            private Object ATTR04;
            private Object ATTR05;
            private Object ATTR06;
            private Object ATTR07;
            private Object ATTR08;
            private Object ATTR09;
            private Object ATTR10;
            private Object ATTR11;
            private Object ATTR12;
            private Object ATTR13;
            private Object ATTR14;
            private Object ATTR15;
            private Object ATTR16;
            private Object ATTR17;
            private String ATTR18;
            private Object ATTR19;
            private Object ATTR20;
            private Object BUSINESS_UUID;
            private Object PORE_NUM;
            private String C_LENGTH;
            private String M_LENGTH;
            private String ID;
            private String X;
            private String Y;

            public String getOBJECTID() {
                return OBJECTID;
            }

            public void setOBJECTID(String OBJECTID) {
                this.OBJECTID = OBJECTID;
            }

            public String getNAME() {
                return NAME;
            }

            public void setNAME(String NAME) {
                this.NAME = NAME;
            }

            public String get_$SHAPELEN275() {
                return _$SHAPELEN275;
            }

            public void set_$SHAPELEN275(String _$SHAPELEN275) {
                this._$SHAPELEN275 = _$SHAPELEN275;
            }

            public String getUUID() {
                return UUID;
            }

            public void setUUID(String UUID) {
                this.UUID = UUID;
            }

            public Object getLABEL() {
                return LABEL;
            }

            public void setLABEL(Object LABEL) {
                this.LABEL = LABEL;
            }

            public String getCITY_UUID() {
                return CITY_UUID;
            }

            public void setCITY_UUID(String CITY_UUID) {
                this.CITY_UUID = CITY_UUID;
            }

            public String getCOUNTY_UUID() {
                return COUNTY_UUID;
            }

            public void setCOUNTY_UUID(String COUNTY_UUID) {
                this.COUNTY_UUID = COUNTY_UUID;
            }

            public Object getADDRESS() {
                return ADDRESS;
            }

            public void setADDRESS(Object ADDRESS) {
                this.ADDRESS = ADDRESS;
            }

            public Object getTASK_UUID() {
                return TASK_UUID;
            }

            public void setTASK_UUID(Object TASK_UUID) {
                this.TASK_UUID = TASK_UUID;
            }

            public String getPROJECT_UUID() {
                return PROJECT_UUID;
            }

            public void setPROJECT_UUID(String PROJECT_UUID) {
                this.PROJECT_UUID = PROJECT_UUID;
            }

            public String getROUTE_TYPE() {
                return ROUTE_TYPE;
            }

            public void setROUTE_TYPE(String ROUTE_TYPE) {
                this.ROUTE_TYPE = ROUTE_TYPE;
            }

            public String getSTART_UUID() {
                return START_UUID;
            }

            public void setSTART_UUID(String START_UUID) {
                this.START_UUID = START_UUID;
            }

            public String getEND_UUID() {
                return END_UUID;
            }

            public void setEND_UUID(String END_UUID) {
                this.END_UUID = END_UUID;
            }

            public Object getCURRENT_UUID() {
                return CURRENT_UUID;
            }

            public void setCURRENT_UUID(Object CURRENT_UUID) {
                this.CURRENT_UUID = CURRENT_UUID;
            }

            public String getFIBER_NUM() {
                return FIBER_NUM;
            }

            public void setFIBER_NUM(String FIBER_NUM) {
                this.FIBER_NUM = FIBER_NUM;
            }

            public Object getOPTI_TYPE() {
                return OPTI_TYPE;
            }

            public void setOPTI_TYPE(Object OPTI_TYPE) {
                this.OPTI_TYPE = OPTI_TYPE;
            }

            public String getSYSTEM_LEVEL() {
                return SYSTEM_LEVEL;
            }

            public void setSYSTEM_LEVEL(String SYSTEM_LEVEL) {
                this.SYSTEM_LEVEL = SYSTEM_LEVEL;
            }

            public String getTIME_STAMP() {
                return TIME_STAMP;
            }

            public void setTIME_STAMP(String TIME_STAMP) {
                this.TIME_STAMP = TIME_STAMP;
            }

            public String getCREATOR() {
                return CREATOR;
            }

            public void setCREATOR(String CREATOR) {
                this.CREATOR = CREATOR;
            }

            public String getCREAT_TIME() {
                return CREAT_TIME;
            }

            public void setCREAT_TIME(String CREAT_TIME) {
                this.CREAT_TIME = CREAT_TIME;
            }

            public Object getMODIFIER() {
                return MODIFIER;
            }

            public void setMODIFIER(Object MODIFIER) {
                this.MODIFIER = MODIFIER;
            }

            public Object getMODIFY_TIME() {
                return MODIFY_TIME;
            }

            public void setMODIFY_TIME(Object MODIFY_TIME) {
                this.MODIFY_TIME = MODIFY_TIME;
            }

            public String getSTATEFLAG() {
                return STATEFLAG;
            }

            public void setSTATEFLAG(String STATEFLAG) {
                this.STATEFLAG = STATEFLAG;
            }

            public Object getDML_STATUS() {
                return DML_STATUS;
            }

            public void setDML_STATUS(Object DML_STATUS) {
                this.DML_STATUS = DML_STATUS;
            }

            public String getLC_STATUS() {
                return LC_STATUS;
            }

            public void setLC_STATUS(String LC_STATUS) {
                this.LC_STATUS = LC_STATUS;
            }

            public Object getATTR01() {
                return ATTR01;
            }

            public void setATTR01(Object ATTR01) {
                this.ATTR01 = ATTR01;
            }

            public Object getATTR02() {
                return ATTR02;
            }

            public void setATTR02(Object ATTR02) {
                this.ATTR02 = ATTR02;
            }

            public Object getATTR03() {
                return ATTR03;
            }

            public void setATTR03(Object ATTR03) {
                this.ATTR03 = ATTR03;
            }

            public Object getATTR04() {
                return ATTR04;
            }

            public void setATTR04(Object ATTR04) {
                this.ATTR04 = ATTR04;
            }

            public Object getATTR05() {
                return ATTR05;
            }

            public void setATTR05(Object ATTR05) {
                this.ATTR05 = ATTR05;
            }

            public Object getATTR06() {
                return ATTR06;
            }

            public void setATTR06(Object ATTR06) {
                this.ATTR06 = ATTR06;
            }

            public Object getATTR07() {
                return ATTR07;
            }

            public void setATTR07(Object ATTR07) {
                this.ATTR07 = ATTR07;
            }

            public Object getATTR08() {
                return ATTR08;
            }

            public void setATTR08(Object ATTR08) {
                this.ATTR08 = ATTR08;
            }

            public Object getATTR09() {
                return ATTR09;
            }

            public void setATTR09(Object ATTR09) {
                this.ATTR09 = ATTR09;
            }

            public Object getATTR10() {
                return ATTR10;
            }

            public void setATTR10(Object ATTR10) {
                this.ATTR10 = ATTR10;
            }

            public Object getATTR11() {
                return ATTR11;
            }

            public void setATTR11(Object ATTR11) {
                this.ATTR11 = ATTR11;
            }

            public Object getATTR12() {
                return ATTR12;
            }

            public void setATTR12(Object ATTR12) {
                this.ATTR12 = ATTR12;
            }

            public Object getATTR13() {
                return ATTR13;
            }

            public void setATTR13(Object ATTR13) {
                this.ATTR13 = ATTR13;
            }

            public Object getATTR14() {
                return ATTR14;
            }

            public void setATTR14(Object ATTR14) {
                this.ATTR14 = ATTR14;
            }

            public Object getATTR15() {
                return ATTR15;
            }

            public void setATTR15(Object ATTR15) {
                this.ATTR15 = ATTR15;
            }

            public Object getATTR16() {
                return ATTR16;
            }

            public void setATTR16(Object ATTR16) {
                this.ATTR16 = ATTR16;
            }

            public Object getATTR17() {
                return ATTR17;
            }

            public void setATTR17(Object ATTR17) {
                this.ATTR17 = ATTR17;
            }

            public String getATTR18() {
                return ATTR18;
            }

            public void setATTR18(String ATTR18) {
                this.ATTR18 = ATTR18;
            }

            public Object getATTR19() {
                return ATTR19;
            }

            public void setATTR19(Object ATTR19) {
                this.ATTR19 = ATTR19;
            }

            public Object getATTR20() {
                return ATTR20;
            }

            public void setATTR20(Object ATTR20) {
                this.ATTR20 = ATTR20;
            }

            public Object getBUSINESS_UUID() {
                return BUSINESS_UUID;
            }

            public void setBUSINESS_UUID(Object BUSINESS_UUID) {
                this.BUSINESS_UUID = BUSINESS_UUID;
            }

            public Object getPORE_NUM() {
                return PORE_NUM;
            }

            public void setPORE_NUM(Object PORE_NUM) {
                this.PORE_NUM = PORE_NUM;
            }

            public String getC_LENGTH() {
                return C_LENGTH;
            }

            public void setC_LENGTH(String C_LENGTH) {
                this.C_LENGTH = C_LENGTH;
            }

            public String getM_LENGTH() {
                return M_LENGTH;
            }

            public void setM_LENGTH(String M_LENGTH) {
                this.M_LENGTH = M_LENGTH;
            }

            public String getID() {
                return ID;
            }

            public void setID(String ID) {
                this.ID = ID;
            }

            public String getX() {
                return X;
            }

            public void setX(String X) {
                this.X = X;
            }

            public String getY() {
                return Y;
            }

            public void setY(String Y) {
                this.Y = Y;
            }
        }

        public static class GeometryBean {
            /**
             * x : 0
             * y : 0
             * paths : [[[114.45924557894247,36.615679859941885],[114.45980982369913,36.61570565765806],[114.45984773560947,36.61597973247768],[114.45998140949284,36.61681091884101],[114.46015103054195,36.618188340679744],[114.46029469470298,36.61922962625638],[114.46033061835584,36.61955973784733],[114.46044933298663,36.620356937588916],[114.46056109861871,36.62142030624509],[114.4605954949346,36.62173450225681],[114.46074031409341,36.62287698467169],[114.46085663045399,36.623729071986006],[114.46090411120865,36.62410037250027],[114.46107683422125,36.624743093974715],[114.46102737455189,36.625489906619315],[114.46128018804674,36.62673006803494],[114.46132053404843,36.626951397333904],[114.46143449930607,36.628020146629474],[114.46205315091156,36.628694218799886]]]
             */

            private int x;
            private int y;
            private List<List<List<Double>>> paths;

            public int getX() {
                return x;
            }

            public void setX(int x) {
                this.x = x;
            }

            public int getY() {
                return y;
            }

            public void setY(int y) {
                this.y = y;
            }

            public List<List<List<Double>>> getPaths() {
                return paths;
            }

            public void setPaths(List<List<List<Double>>> paths) {
                this.paths = paths;
            }
        }

        public static class GeoJsonBean {
            /**
             * type : LineString
             * coordinates : [[114.45924557894247,36.615679859941885],[114.45980982369913,36.61570565765806],[114.45984773560947,36.61597973247768],[114.45998140949284,36.61681091884101],[114.46015103054195,36.618188340679744],[114.46029469470298,36.61922962625638],[114.46033061835584,36.61955973784733],[114.46044933298663,36.620356937588916],[114.46056109861871,36.62142030624509],[114.4605954949346,36.62173450225681],[114.46074031409341,36.62287698467169],[114.46085663045399,36.623729071986006],[114.46090411120865,36.62410037250027],[114.46107683422125,36.624743093974715],[114.46102737455189,36.625489906619315],[114.46128018804674,36.62673006803494],[114.46132053404843,36.626951397333904],[114.46143449930607,36.628020146629474],[114.46205315091156,36.628694218799886]]
             * crs : {"type":"name","properties":{"name":"EPSG:4326"}}
             */

            private String type;
            private CrsBean crs;
            private List<List<Double>> coordinates;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public CrsBean getCrs() {
                return crs;
            }

            public void setCrs(CrsBean crs) {
                this.crs = crs;
            }

            public List<List<Double>> getCoordinates() {
                return coordinates;
            }

            public void setCoordinates(List<List<Double>> coordinates) {
                this.coordinates = coordinates;
            }

            public static class CrsBean {
                /**
                 * type : name
                 * properties : {"name":"EPSG:4326"}
                 */

                private String type;
                private PropertiesBean properties;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public PropertiesBean getProperties() {
                    return properties;
                }

                public void setProperties(PropertiesBean properties) {
                    this.properties = properties;
                }

                public static class PropertiesBean {
                    /**
                     * name : EPSG:4326
                     */

                    private String name;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }
            }
        }
    }
}
