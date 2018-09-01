package com.inspur.hebeiline.core;

/**
 * Created by lixu on 2017/12/7.
 */





public class AllUrl {
    //账号密码
    //dw_wanglili   密码123456
    //dw_zhouzhipeng   密码123456
    //qinshi6

   // public static String mainUrl = "http://10.216.42.197/geoservice/api/";
//    public static String mainUrl = "http://10.92.1.148:8080/";
    public static String mainUrl = "http://218.207.69.236:6105/";//正式


//    public static String mainUrl="http://218.207.69.236:6105/";
    //登录
    //mobile=18366158972&password=1111111
    public final static String mLoginUrl = "api/authenticate4debug";
    public final static String mLoginUrl2 = "/api/authenticate";
    //http://10.92.1.148:8080/routeinspection/api/v1/queryTask/execute
    public final static String mGetOrderList = "geoservice/api/v1/queryTask/execute";

    //查询周边资源通过uuid
    public final static String getrouteInspectinfo = "routeinspection/api/v1/getrouteInspectinfo";
    public final static String getrouteInspectinfo2 = "routeinspection/api/v1/getrouteInspectinfo2";

    //查询待办任务
    public final static String getTaskToDo = "routeinspection/api/v1/getTaskToDo";


    //查询个人
    public final static String getXJRecordByUser = "routeinspection/api/v1/getXJRecordByUser";

    //查询单个代办任务接口
    public final static String getTaskByUUID = "routeinspection/api/v1/getTaskByUUID";

    //查询历史
    public final static String getTaskLine = "routeinspection/api/v1/getTaskLine";

    //提交
    public final static String updateTaskRecord = "routeinspection/api/v1/updateTaskRecord";

    //高德地图的编码转换
    public final static String regiontransfer = "routeinspection/api/v1/gdregiontransfer";



    //    修改通讯录
    //    id  通讯录ID
    //    truename  真实姓名
    //    mobile   手机号
    //    address_id  三级联动地址id 一户给你发获取地址的接口
    //    address   详细地址
    public final static String mChangeTell = "savenotes";

    //获取通讯录
    //truename 模糊查询
    //    page
    //    size
    //    uid
    //    type（shipuser|getuser）  寄件人|收件人
    public final static String mGetTellList = "notes";


    //添加通讯录
    //      uid
    //    type   （shipuser|getuser）  寄件人|收件人
    //    truename  真实姓名
    //    mobile   手机号
    //    address_id   获取三级联动的地址
    //base64
    //address
    public final static String addTellList = "addnotes";

    //删除通讯录
    //      id
    public final static String removeTell = "delnotes";

    //轮播图  无参数
    public final static String getShowImgList = "apppic";


    //日结
    //只穿一个uid 就行
    public final static String rijie = "rijie";

    //下单
    /**
     * uid
     * shipuser_address_id   寄件人地址id
     * getuser_address_id    收件人地址id
     * shipuser_truename   发件人姓名
     * shipuser_mobile      发件人手机号
     * shipuser_address   发件人详细地址
     * getuser_truename  收件人姓名
     * getuser_mobile     收件人手机号
     * getuser_address    收件人详细地址
     * order_price   实付款
     * order_weight    重量
     * manual    手动录入单号
     * express_no    手动录入单号
     */
    public final static String addOrder = "save";

    /**
     * 修改打印状态
     * order_no
     * uid
     */
    public final static String changePrintStatus = "savedayin";

    //获取订单打印状态
    /**
     * uid
     * order_no 订单号
     */
    public final static String getPrintStatus = "dayin";


    //省市区
    //id : 上级id   0：省
    public final static String mGetAdressList = "district";


    //查询站址列表
    public final static String querySiteList = "site/querySiteList.ilf";
    public final static String queryConfirmSiteList = "site/queryConfirmSiteList.ilf";//查询带确认列表
    //查询直放站列表
    public final static String queryRepeaterList = "repeater/queryRepeaterList.ilf";
    public final static String queryConfirmRepeaterList = "repeater/queryConfirmRepeaterList.ilf";
    //查询室分列表
    public final static String queryRoomsubList = "roomsub/queryRoomsubList.ilf";
    public final static String queryConfirmRoomsubList = "roomsub/queryConfirmRoomsubList.ilf";
    //查询WLAN列表
    public final static String queryWlanhotspotList = "wlanhotspot/queryWlanhotspotList.ilf";
    public final static String queryConfirmWlanhotspotList = "wlanhotspot/queryConfirmWlanhotspotList.ilf";
    //查询家客列表
    public final static String queryCommunityList = "community/queryCommunityList.ilf";
    public final static String queryConfirmCommunityList = "community/queryConfirmCommunityList.ilf";
    //查询铁塔列表
    public final static String queryTowerList = "tower/queryTowerList.ilf";
    public final static String queryConfirmTowerList = "tower/queryConfirmTowerList.ilf";
    //查询集客列表
    public final static String queryTrsoProductList = "trsoproduct/queryTrsoProductList.ilf";
    public final static String queryConfirmTrsoProductList = "trsoproduct/queryConfirmTrsoProductList.ilf";









    //显示基站详情
    public final static String queryBasicSiteInfo = "site/queryBasicSiteInfo.ilf";
    public final static String updateSiteDeliveryInfoNew = "site/updateSiteDeliveryInfoNew.ilf";    //更新详情
    public final static String queryConfirmBasicSiteInfo = "site/queryConfirmBasicSiteInfo.ilf";    //确认详情
    public final static String updateConfirmSiteDeliveryInfoNew = "site/updateConfirmSiteDeliveryInfoNew.ilf";//确认

    //显示直放站详情
    public final static String queryBasicRepeaterInfo = "repeater/queryBasicRepeaterInfo.ilf";
    public final static String updaterepeaterDeliveryInfoNew = "repeater/updateRepeaterDeliveryInfoNew.ilf";
    public final static String queryConfirmBasicRepeaterInfo = "repeater/queryConfirmBasicRepeaterInfo.ilf";
    public final static String updateConfirmRepeaterDeliveryInfoNew = "repeater/updateConfirmRepeaterDeliveryInfoNew.ilf";//确认

    //显示室分详情
    public final static String queryBasicRoomsubInfo = "roomsub/queryBasicRoomsubInfo.ilf";
    public final static String updateroomsubDeliveryInfoNew = "roomsub/updateRoomsubDeliveryInfoNew.ilf";
    public final static String queryConfirmBasicRoomsubInfo = "roomsub/queryConfirmBasicRoomsubInfo.ilf";
    public final static String updateConfirmRoomsubDeliveryInfoNew = "roomsub/updateConfirmRoomsubDeliveryInfoNew.ilf";//确认

    //显示WLAN详情
    public final static String queryBasicWlanhotspotInfo = "wlanhotspot/queryBasicWlanhotspotInfo.ilf";
    public final static String updatewlanhotspotDeliveryInfoNew = "wlanhotspot/updateWlanhotspotDeliveryInfoNew.ilf";
    public final static String queryConfirmBasicWlanhotspotInfo = "wlanhotspot/queryConfirmBasicWlanhotspotInfo.ilf";
    public final static String updateConfirmWlanhotspotDeliveryInfoNew = "wlanhotspot/updateConfirmWlanhotspotDeliveryInfoNew.ilf";//确认


    //显示家客详情
    public final static String queryBasicCommunityInfo = "community/queryBasicCommunityInfo.ilf";
    public final static String updatecommunityDeliveryInfoNew = "community/updateCommunityDeliveryInfoNew.ilf";
    public final static String queryConfirmBasicCommunityInfo = "community/queryConfirmBasicCommunityInfo.ilf";
    public final static String updateConfirmCommunityDeliveryInfoNew = "community/updateConfirmCommunityDeliveryInfoNew.ilf";//确认


    //显示铁塔详情
    public final static String queryBasicTowerInfo = "tower/queryBasicTowerInfo.ilf";
    public final static String updatetowerDeliveryInfoNew = "tower/updateTowerDeliveryInfoNew.ilf";
    public final static String queryConfirmBasicTowerInfo = "tower/queryConfirmBasicTowerInfo.ilf";
    public final static String updateConfirmTowerDeliveryInfoNew = "tower/updateConfirmTowerDeliveryInfoNew.ilf";//确认


    //显示集客详情
    public final static String queryBasicTrsoProductInfo = "trsoproduct/queryBasicTrsoProductInfo.ilf";
    public final static String updatetrsoproductDeliveryInfoNew = "trsoproduct/updateTrsoProductDeliveryInfoNew.ilf";
    public final static String queryConfirmBasicTrsoProductInfo = "trsoproduct/queryConfirmBasicTrsoProductInfo.ilf";
    public final static String updateConfirmTrsoproductDeliveryInfoNew = "trsoproduct/updateConfirmTrsoProductDeliveryInfoNew.ilf";//确认



    //显示基站交割大类
    public final static String querySiteDeliveryList = "site/querySiteDeliveryList.ilf";
    //显示直放站交割大类
    public final static String queryRepeaterDeliveryList = "repeater/queryRepeaterDeliveryList.ilf";
    //显示室分交割大类
    public final static String queryRoomsubDeliveryList = "roomsub/queryRoomsubDeliveryList.ilf";
    //显示WLAN交割大类
    public final static String queryWlanhotspotDeliveryList = "wlanhotspot/queryWlanhotspotDeliveryList.ilf";
    //显示家客交割大类
    public final static String queryCommunityDeliveryList = "community/queryCommunityDeliveryList.ilf";
    //显示铁塔交割大类
    public final static String queryTowerDeliveryList = "tower/queryTowerDeliveryList.ilf";
    //显示集客交割大类
    public final static String queryTrsoProductDeliveryList = "trsoproduct/queryTrsoProductDeliveryList.ilf";


    //基站巡检项的接口
    public final static String querySiteSubDeliveryList = "site/querySiteSubDeliveryList.ilf";
    //直放站巡检项的接口
    public final static String queryRepeaterSubDeliveryList = "repeater/queryRepeaterSubDeliveryList.ilf";
    //室分巡检项的接口
    public final static String queryRoomsubSubDeliveryList = "roomsub/queryRoomsubSubDeliveryList.ilf";
    //WLAN巡检项的接口
    public final static String queryWlanhotspotSubDeliveryList = "wlanhotspot/queryWlanhotspotSubDeliveryList.ilf";
    //家客巡检项的接口
    public final static String queryCommunitySubDeliveryList = "community/queryCommunitySubDeliveryList.ilf";
    //铁塔巡检项的接口
    public final static String queryTowerSubDeliveryList = "tower/queryTowerSubDeliveryList.ilf";
    //集客巡检项的接口
    public final static String queryTrsoProductSubDeliveryList = "trsoproduct/queryTrsoProductSubDeliveryList.ilf";



    //查询枚举接口
    public final static String queryDictionary = "site/queryDictionary.ilf";
    //保存基站巡检项
    public final static String updateSiteDeliveryInfo = "site/updateSiteDeliveryInfo.ilf";
    //保存直放站巡检项
    public final static String updateRepeaterDeliveryInfo = "repeater/updateRepeaterDeliveryInfo.ilf";
    //保存室分巡检项
    public final static String updateRoomsubDeliveryInfo = "roomsub/updateRoomsubDeliveryInfo.ilf";
    //保存WLAN巡检项
    public final static String updateWlanhotspotDeliveryInfo = "wlanhotspot/updateWlanhotspotDeliveryInfo.ilf";
    //保存家客巡检项
    public final static String updateCommunityDeliveryInfo = "community/updateCommunityDeliveryInfo.ilf";
    //保存铁塔巡检项
    public final static String updateTowerDeliveryInfo = "tower/updateTowerDeliveryInfo.ilf";
    //保存集客巡检项
    public final static String updateTrsoProductDeliveryInfo = "trsoproduct/updateTrsoProductDeliveryInfo.ilf";
    //获取周边资源
    public final static String getResourcePointByLoacation = "transLine/getResourcePointByLoacation.ilf";

    //上传图片
    public final static String upload = "img/upload.ilf";

    //图片列表
    //http://117.131.224.133:7004/hrmw/img/getPicList.ilf?resId=2037080&deliveryCategory=ENODEB&deliveryItem=enodeb_type
    public final static String getPicList = "img/getPicList.ilf";

    //查看图片
    public final static String view = "img/view.ilf";

    //查看图片
    public final static String del = "img/del.ilf";

    //保存路由
    public final static String saveTask = "transLine/saveTask.ilf";
    //获取我的路由
    public final static String getMyRoute = "transLine/getMyRoute.ilf";
    //删除路由
    public final static String deleteRoute = "transLine/deleteRoute.ilf";
    //获取我的确认路由
    public final static String getMyToBeConfirm = "transLine/getMyToBeConfirm.ilf";
    //确认路由
    public final static String updateRouteTask = "transLine/updateRouteTask.ilf";
    //保存隐患
    public final static String saveErrorInfo = "transLine/saveErrorInfo.ilf";
    //线路上传图片
    public final static String uploadPhotoAndInfo = "transLine/uploadPhotoAndInfo.ilf";

    //http://117.131.224.133:7004/hrmw/transLine/getRouteInfo.ilf&UID=Choast
    public final static String getRouteInfo = "transLine/getRouteInfo.ilf";
    //登录
    //http://117.131.224.133:7004/hrmw/login?jsonRequest={%22passw ord%22:%22hh%22,%22username%22:%22hh%22}

}
