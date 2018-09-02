package com.inspur.hebeiline.core;

import com.inspur.hebeiline.entity.ACodeBean;
import com.inspur.hebeiline.entity.LXMineEntity;
import com.inspur.hebeiline.entity.LXResEntity;
import com.inspur.hebeiline.entity.LXResNewEntity;
import com.inspur.hebeiline.entity.LXTestLoginBean;
import com.inspur.hebeiline.entity.LXWaitListEntity;
import com.inspur.hebeiline.entity.LineResultEntity;
import com.inspur.hebeiline.entity.TMLineMainEntity;
import com.inspur.hebeiline.entity.UserNamePwdBean;
import com.inspur.hebeiline.entity.post_bean.AcodePost;
import com.inspur.hebeiline.entity.post_bean.LXGetResPostBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;


/**
 * Created by Administrator on 2016/11/29.
 */
public interface MallRequest {

//    @FormUrlEncoded
    @POST(AllUrl.mLoginUrl)
    Observable<LXTestLoginBean> login(@Body UserNamePwdBean userName);

    @Headers({"urlname:login", "Content-Type:application/json"})
    @POST(AllUrl.mLoginUrl2)
    Observable<Map<String, String>> login2(
            @Body Map<String, Object> params
    );

    //查询周围资源
    //@FormUrlEncoded
    @POST(AllUrl.mGetOrderList)
    Observable<TMLineMainEntity> getMapResList(@HeaderMap Map<String, String> lang,@Body LXGetResPostBean lxGetResPostBean);
    //查询周围资源2
    //@FormUrlEncoded
    @GET
    Observable<List<LXResEntity>> getrouteInspectinfo(@HeaderMap Map<String, String> lang,@Url String mUrl);

    //第二次修改后
    @GET
    Observable<List<LXResEntity>> getrouteInspectinfoSS(@HeaderMap Map<String, String> lang,@Url String mUrl);



    //第二次修改后
    @GET
    Observable<List<LXResNewEntity>> getrouteInspectinfoNew(@HeaderMap Map<String, String> lang, @Url String mUrl);

    //待办
    @GET
    Observable<List<LXWaitListEntity>> getTaskToDo(@HeaderMap Map<String, String> lang, @Url String mUrl);
 //待办
    @GET
    Observable<LXWaitListEntity> getTask(@HeaderMap Map<String, String> lang, @Url String mUrl);

    //获取我的
    @GET
    Observable<LXMineEntity> getMineInfo(@HeaderMap Map<String, String> lang, @Url String mUrl);

    //获取lishi
    @GET
    Observable<List<LXWaitListEntity>> getTaskLine(@HeaderMap Map<String, String> lang, @Url String mUrl);

    //提交
//    @FormUrlEncoded
    @POST(AllUrl.updateTaskRecord)
    Observable<Object> updateTaskRecord(@HeaderMap Map<String, String> lang, @Body List<LineResultEntity> lineResultEntity);


    //高德地图的编码转换
//    @FormUrlEncodedAcodePost
    @POST(AllUrl.regiontransfer)
    Observable<ACodeBean> regiontransfer(@HeaderMap Map<String, String> lang, @Body AcodePost acodePost);


    //
//    @FormUrlEncoded
//    @POST(AllUrl.mGetAdressList)
//    Observable<AdressEntity> getAdressList(@Field("id") String id);

//    @FormUrlEncoded
//    @POST(AllUrl.mGetTellList)
//    Observable<TellEntity> getTellList(@Field("page") String page
//            , @Field("size") String size
//            , @Field("uid") String uid
//            , @Field("type") String type
//            , @Field("truename") String truename);
//
//    @FormUrlEncoded
//    @POST(AllUrl.addOrder)
//    Observable<SaveOrderEntity> saveOrder(@Field("uid") String uid
//            , @Field("shipuser_address_id") String shipuser_address_id
//            , @Field("getuser_address_id") String getuser_address_id
//            , @Field("shipuser_truename") String shipuser_truename
//            , @Field("shipuser_mobile") String shipuser_mobile
//            , @Field("shipuser_address") String shipuser_address
//            , @Field("getuser_truename") String getuser_truename
//            , @Field("getuser_mobile") String getuser_mobile
//            , @Field("getuser_address") String getuser_address
//            , @Field("order_price") String order_price
//            , @Field("order_weight") String order_weight
//            , @Field("express_no") String manual
//            , @Field("shipuser_img") String shipuser_img
//            , @Field("getuser_img") String getuser_img
//    );
//
//    @FormUrlEncoded
//    @POST(AllUrl.getShowImgList)
//    Observable<ShowImgEntity> getShowImgList(@Field("id") String id);
//
//
//
//    @FormUrlEncoded
//    @POST(AllUrl.rijie)
//    Observable<SaveOrderEntity> rijie(@Field("uid") String uid);
//
//    /**
//     * 修改打状态
//     * @param uid
//     * @return
//     */
//    @FormUrlEncoded
//    @POST(AllUrl.changePrintStatus)
//    Observable<SaveOrderEntity> changePrintStatus(@Field("uid") String uid, @Field("order_no") String order_no);
//
//
//
//    //添加通讯录
//    //      uid
//    //    type   （shipuser|getuser）  寄件人|收件人
//    //    truename  真实姓名
//    //    mobile   手机号
//    //    address_id   获取三级联动的地址
//    @FormUrlEncoded
//    @POST(AllUrl.addTellList)
//    Observable<SaveOrderEntity> addTell(@Field("uid") String id
//            , @Field("type") String type
//            , @Field("truename") String truename
//            , @Field("mobile") String mobile
//            , @Field("address_id") String address_id
//            , @Field("address") String address
//            , @Field("base64") String base64
//    );
//
//
//    //    修改通讯录
//    //id  通讯录ID
//    //    truename  真实姓名
//    //    mobile   手机号
//    //    address_id  三级联动地址id 一户给你发获取地址的接口
//    //    address   详细地址
//    @FormUrlEncoded
//    @POST(AllUrl.mChangeTell)
//    Observable<SaveOrderEntity> changeTell(@Field("id") String id
//            , @Field("truename") String truename
//            , @Field("mobile") String mobile
//            , @Field("address_id") String address_id
//            , @Field("address") String address
//    );
//
//
//
//    @FormUrlEncoded
//    @POST(AllUrl.removeTell)
//    Observable<SaveOrderEntity> removeTell(@Field("id") String id);


    /**
     * 上传一张图片
     *
     * @return
     */
    /*@Multipart
    @POST(AllUrl.uploadImageTrue)
    Call<StringBean> uploadImageTrue(@PartMap Map<String, RequestBody> params);
    @FormUrlEncoded
    @POST(AllUrl.uploadImage)
    Call<UpLoadResult> uploadImage(
            @Field("id") String id
            , @Field("photoString") String photoString);*/



    /*@Multipart
    @POST()
    Call<ResponseBody> uploadFiles(@Url String url, @PartMap() Map<String, RequestBody> maps);
    @Multipart
    @POST
    Call<ResponseBody> uploadFileWithPartMap(
            @Url() String url,
            @PartMap() Map<String, RequestBody> partMap,
            @Part("file") MultipartBody.Part file);
    @Multipart
    @POST(AllUrl.uploadImage)
    Call<String> updateImage(@Part MultipartBody.Part[] file, @QueryMap Map<String, String> maps);
    @Streaming //大文件时要加不然会OOM
    @FormUrlEncoded
    @POST
    Call<ResponseBody> downloadFile(@Url String fileUrl, @Field("jsonRequest") String jsonRequest);*/
    //站址的列表
//    @FormUrlEncoded
//    @POST(AllUrl.querySiteList)
//    Observable<GroupListMainEntity> querySiteList(@Field("start") String start, @Field("length") String length, @Field("resName") String resName, @Field("siteNo") String siteNo, @Field("oldComName") String oldComName, @Field("uid") String uid);
//
//    //直放站
//    @FormUrlEncoded
//    @POST(AllUrl.queryRepeaterList)
//    Observable<GroupListMainEntity> queryRepeaterList(@Field("start") String start, @Field("length") String length, @Field("resName") String resName, @Field("siteNo") String siteNo, @Field("oldComName") String oldComName, @Field("uid") String uid);
//
//    //室分
//    @FormUrlEncoded
//    @POST(AllUrl.queryRoomsubList)
//    Observable<GroupListMainEntity> queryRoomsubList(@Field("start") String start, @Field("length") String length, @Field("resName") String resName, @Field("siteNo") String siteNo, @Field("oldComName") String oldComName, @Field("uid") String uid);
//
//    //WLAN
//    @FormUrlEncoded
//    @POST(AllUrl.queryWlanhotspotList)
//    Observable<GroupListMainEntity> queryWlanhotspotList(@Field("start") String start, @Field("length") String length, @Field("resName") String resName, @Field("siteNo") String siteNo, @Field("oldComName") String oldComName, @Field("uid") String uid);
//
//    //家客
//    @FormUrlEncoded
//    @POST(AllUrl.queryCommunityList)
//    Observable<GroupListMainEntity> queryCommunityList(@Field("start") String start, @Field("length") String length, @Field("resName") String resName, @Field("siteNo") String siteNo, @Field("oldComName") String oldComName, @Field("uid") String uid);
//
//    //铁塔
//    @FormUrlEncoded
//    @POST(AllUrl.queryTowerList)
//    Observable<GroupListMainEntity> queryTowerList(@Field("start") String start, @Field("length") String length, @Field("resName") String resName, @Field("siteNo") String siteNo, @Field("oldComName") String oldComName, @Field("uid") String uid);
//
//    //集客
//    @FormUrlEncoded
//    @POST(AllUrl.queryTrsoProductList)
//    Observable<GroupListMainEntity> queryTrsoProductList(@Field("start") String start, @Field("length") String length, @Field("resName") String resName, @Field("siteNo") String siteNo, @Field("oldComName") String oldComName, @Field("uid") String uid, @Field("circuitCode") String circuitCode, @Field("resBusinessFlag") String resBusinessFlag);
//
//    //查询待确认列表====================================================================================
//    //站址的列表
//    @FormUrlEncoded
//    @POST(AllUrl.queryConfirmSiteList)
//    Observable<GroupListMainEntity> queryConfirmSiteList(@Field("start") String start, @Field("length") String length, @Field("resName") String resName, @Field("siteNo") String siteNo, @Field("oldComName") String oldComName, @Field("uid") String uid);
//
//    //直放站
//    @FormUrlEncoded
//    @POST(AllUrl.queryConfirmRepeaterList)
//    Observable<GroupListMainEntity> queryConfirmRepeaterList(@Field("start") String start, @Field("length") String length, @Field("resName") String resName, @Field("siteNo") String siteNo, @Field("oldComName") String oldComName, @Field("uid") String uid);
//
//    //室分
//    @FormUrlEncoded
//    @POST(AllUrl.queryConfirmRoomsubList)
//    Observable<GroupListMainEntity> queryConfirmRoomsubList(@Field("start") String start, @Field("length") String length, @Field("resName") String resName, @Field("siteNo") String siteNo, @Field("oldComName") String oldComName, @Field("uid") String uid);
//
//    //WLAN
//    @FormUrlEncoded
//    @POST(AllUrl.queryConfirmWlanhotspotList)
//    Observable<GroupListMainEntity> queryConfirmWlanhotspotList(@Field("start") String start, @Field("length") String length, @Field("resName") String resName, @Field("siteNo") String siteNo, @Field("oldComName") String oldComName, @Field("uid") String uid);
//
//    //家客
//    @FormUrlEncoded
//    @POST(AllUrl.queryConfirmCommunityList)
//    Observable<GroupListMainEntity> queryConfirmCommunityList(@Field("start") String start, @Field("length") String length, @Field("resName") String resName, @Field("siteNo") String siteNo, @Field("oldComName") String oldComName, @Field("uid") String uid);
//
//    //铁塔
//    @FormUrlEncoded
//    @POST(AllUrl.queryConfirmTowerList)
//    Observable<GroupListMainEntity> queryConfirmTowerList(@Field("start") String start, @Field("length") String length, @Field("resName") String resName, @Field("siteNo") String siteNo, @Field("oldComName") String oldComName, @Field("uid") String uid);
//
//    //集客
//    @FormUrlEncoded
//    @POST(AllUrl.queryConfirmTrsoProductList)
//    Observable<GroupListMainEntity> queryConfirmTrsoProductList(@Field("start") String start, @Field("length") String length, @Field("resName") String resName, @Field("siteNo") String siteNo, @Field("oldComName") String oldComName, @Field("uid") String uid, @Field("circuitCode") String circuitCode, @Field("resBusinessFlag") String resBusinessFlag);
//
//
//    //**********************************************
//    //显示基站详情
//    @FormUrlEncoded
//    @POST(AllUrl.queryBasicSiteInfo)
//    Observable<DeliveryInfoForLxEntity> queryBasicSiteInfo(@Field("resourceid") String resourceid, @Field("uid") String uid);
//
//    //显示直放站详情
//    @FormUrlEncoded
//    @POST(AllUrl.queryBasicRepeaterInfo)
//    Observable<DeliveryInfoForLxEntity> queryBasicRepeaterInfo(@Field("resourceid") String resourceid, @Field("uid") String uid);
//
//    //显示室分详情
//    @FormUrlEncoded
//    @POST(AllUrl.queryBasicRoomsubInfo)
//    Observable<DeliveryInfoForLxEntity> queryBasicRoomsubInfo(@Field("resourceid") String resourceid, @Field("uid") String uid);
//
//    //显示WLAN详情
//    @FormUrlEncoded
//    @POST(AllUrl.queryBasicWlanhotspotInfo)
//    Observable<DeliveryInfoForLxEntity> queryBasicWlanhotspotInfo(@Field("resourceid") String resourceid, @Field("uid") String uid);
//
//    //显示家客详情
//    @FormUrlEncoded
//    @POST(AllUrl.queryBasicCommunityInfo)
//    Observable<DeliveryInfoForLxEntity> queryBasicCommunityInfo(@Field("resourceid") String resourceid, @Field("uid") String uid);
//
//    //显示铁塔详情
//    @FormUrlEncoded
//    @POST(AllUrl.queryBasicTowerInfo)
//    Observable<DeliveryInfoForLxEntity> queryBasicTowerInfo(@Field("resourceid") String resourceid, @Field("uid") String uid);
//
//    //显示集客详情
//    @FormUrlEncoded
//    @POST(AllUrl.queryBasicTrsoProductInfo)
//    Observable<DeliveryInfoForLxEntity> queryBasicTrsoProductInfo(@Field("resourceid") String resourceid, @Field("uid") String uid);
//
////=================================================================================================================
//
//
//    //显示基站确认详情
//    @FormUrlEncoded
//    @POST(AllUrl.queryConfirmBasicSiteInfo)
//    Observable<DeliveryInfoForLxEntity> queryConfirmBasicSiteInfo(@Field("resourceid") String resourceid, @Field("query") String query, @Field("uid") String uid);
//
//
//    //显示直放站确认详情
//    @FormUrlEncoded
//    @POST(AllUrl.queryConfirmBasicRepeaterInfo)
//    Observable<DeliveryInfoForLxEntity> queryConfirmBasicRepeaterInfo(@Field("resourceid") String resourceid, @Field("query") String query, @Field("uid") String uid);
//
//    //显示室分确认详情
//    @FormUrlEncoded
//    @POST(AllUrl.queryConfirmBasicRoomsubInfo)
//    Observable<DeliveryInfoForLxEntity> queryConfirmBasicRoomsubInfo(@Field("resourceid") String resourceid, @Field("query") String query, @Field("uid") String uid);
//
//    //显示WLAN确认详情
//    @FormUrlEncoded
//    @POST(AllUrl.queryConfirmBasicWlanhotspotInfo)
//    Observable<DeliveryInfoForLxEntity> queryConfirmBasicWlanhotspotInfo(@Field("resourceid") String resourceid, @Field("query") String query, @Field("uid") String uid);
//
//    //显示家客确认详情
//    @FormUrlEncoded
//    @POST(AllUrl.queryConfirmBasicCommunityInfo)
//    Observable<DeliveryInfoForLxEntity> queryConfirmBasicCommunityInfo(@Field("resourceid") String resourceid, @Field("query") String query, @Field("uid") String uid);
//
//    //显示铁塔确认详情
//    @FormUrlEncoded
//    @POST(AllUrl.queryConfirmBasicTowerInfo)
//    Observable<DeliveryInfoForLxEntity> queryConfirmBasicTowerInfo(@Field("resourceid") String resourceid, @Field("query") String query, @Field("uid") String uid);
//
//    //显示集客确认详情
//    @FormUrlEncoded
//    @POST(AllUrl.queryConfirmBasicTrsoProductInfo)
//    Observable<DeliveryInfoForLxEntity> queryConfirmBasicTrsoProductInfo(@Field("resourceid") String resourceid, @Field("query") String query, @Field("uid") String uid);
//
//    //=================================================================================================================
//    //确认基站确认详情
//    @FormUrlEncoded
//    @POST(AllUrl.updateConfirmSiteDeliveryInfoNew)
//    Observable<SaveTaoBean> updateConfirmSiteDeliveryInfoNew(@Field("resourceDeliveryInfo") String resourceDeliveryInfo, @Field("confirmUserID") String confirmUserID, @Field("confirmUserName") String confirmUserName, @Field("uid") String uid);
//
//    //确认直放站确认详情
//    @FormUrlEncoded
//    @POST(AllUrl.updateConfirmRepeaterDeliveryInfoNew)
//    Observable<SaveTaoBean> updateConfirmRepeaterDeliveryInfoNew(@Field("resourceDeliveryInfo") String resourceDeliveryInfo, @Field("confirmUserID") String confirmUserID, @Field("confirmUserName") String confirmUserName, @Field("uid") String uid);
//
//    //确认室分确认详情
//    @FormUrlEncoded
//    @POST(AllUrl.updateConfirmRoomsubDeliveryInfoNew)
//    Observable<SaveTaoBean> updateConfirmRoomsubDeliveryInfoNew(@Field("resourceDeliveryInfo") String resourceDeliveryInfo, @Field("confirmUserID") String confirmUserID, @Field("confirmUserName") String confirmUserName, @Field("uid") String uid);
//
//    //确认WLAN确认详情
//    @FormUrlEncoded
//    @POST(AllUrl.updateConfirmWlanhotspotDeliveryInfoNew)
//    Observable<SaveTaoBean> updateConfirmWlanhotspotDeliveryInfoNew(@Field("resourceDeliveryInfo") String resourceDeliveryInfo, @Field("confirmUserID") String confirmUserID, @Field("confirmUserName") String confirmUserName, @Field("uid") String uid);
//
//    //确认家客确认详情
//    @FormUrlEncoded
//    @POST(AllUrl.updateConfirmCommunityDeliveryInfoNew)
//    Observable<SaveTaoBean> updateConfirmCommunityDeliveryInfoNew(@Field("resourceDeliveryInfo") String resourceDeliveryInfo, @Field("confirmUserID") String confirmUserID, @Field("confirmUserName") String confirmUserName, @Field("uid") String uid);
//
//    //确认铁塔确认详情
//    @FormUrlEncoded
//    @POST(AllUrl.updateConfirmTowerDeliveryInfoNew)
//    Observable<SaveTaoBean> updateConfirmTowerDeliveryInfoNew(@Field("resourceDeliveryInfo") String resourceDeliveryInfo, @Field("confirmUserID") String confirmUserID, @Field("confirmUserName") String confirmUserName, @Field("uid") String uid);
//
//    //确认集客确认详情
//    @FormUrlEncoded
//    @POST(AllUrl.updateConfirmTrsoproductDeliveryInfoNew)
//    Observable<SaveTaoBean> updateConfirmTrsoproductDeliveryInfoNew(@Field("resourceDeliveryInfo") String resourceDeliveryInfo, @Field("confirmUserID") String confirmUserID, @Field("confirmUserName") String confirmUserName, @Field("uid") String uid);
//
//    //**********************************************
//    //显示基站交割大类/站址的二级列表
//    @GET(AllUrl.querySiteDeliveryList)
//    Observable<GroupListSecondListEntity> querySiteDeliveryList();
//
//    //显示直放站交割大类/站址的二级列表
//    @GET(AllUrl.queryRepeaterDeliveryList)
//    Observable<GroupListSecondListEntity> queryRepeaterDeliveryList();
//
//
//    //显示室分交割大类/站址的二级列表
//    @GET(AllUrl.queryRoomsubDeliveryList)
//    Observable<GroupListSecondListEntity> queryRoomsubDeliveryList();
//
//    //显示WLAN交割大类/站址的二级列表
//    @GET(AllUrl.queryWlanhotspotDeliveryList)
//    Observable<GroupListSecondListEntity> queryWlanhotspotDeliveryList();
//
//    //显示家客交割大类/站址的二级列表
//    @GET(AllUrl.queryCommunityDeliveryList)
//    Observable<GroupListSecondListEntity> queryCommunityDeliveryList();
//
//    //显示铁塔交割大类/站址的二级列表
//    @GET(AllUrl.queryTowerDeliveryList)
//    Observable<GroupListSecondListEntity> queryTowerDeliveryList();
//
//    //显示集客交割大类/站址的二级列表
//    @GET(AllUrl.queryTrsoProductDeliveryList)
//    Observable<GroupListSecondListEntity> queryTrsoProductDeliveryList();
//
//    //**********************************************
//
//    //基站巡检项的接口
//    @FormUrlEncoded
//    @POST(AllUrl.querySiteSubDeliveryList)
//    //   Observable<DeliveryDetailEntity> querySiteSubDeliveryList(@Field("recourseType") String recourseType, @Field("parentId")String parentId,@Field("siteID") String siteID);
//    Observable<DeliveryDetailEntity> querySiteSubDeliveryList(@Field("recourseType") String recourseType, @Field("parentId") String parentId, @Field("resourceId") String resourceId, @Field("uid") String uid);
//
//    //直放站巡检项的接口
//    @FormUrlEncoded
//    @POST(AllUrl.queryRepeaterSubDeliveryList)
//    //  Observable<DeliveryDetailEntity> queryRepeaterSubDeliveryList(@Field("recourseType") String recourseType, @Field("parentId")String parentId,@Field("siteID") String siteID);
//    Observable<DeliveryDetailEntity> queryRepeaterSubDeliveryList(@Field("recourseType") String recourseType, @Field("parentId") String parentId, @Field("resourceId") String resourceId, @Field("uid") String uid);
//
//    //室分巡检项的接口
//    @FormUrlEncoded
//    @POST(AllUrl.queryRoomsubSubDeliveryList)
//    //  Observable<DeliveryDetailEntity> queryRoomsubSubDeliveryList(@Field("recourseType") String recourseType, @Field("parentId")String parentId,@Field("siteID") String siteID);
//    Observable<DeliveryDetailEntity> queryRoomsubSubDeliveryList(@Field("recourseType") String recourseType, @Field("parentId") String parentId, @Field("resourceId") String resourceId, @Field("uid") String uid);
//
//    //WLAN巡检项的接口
//    @FormUrlEncoded
//    @POST(AllUrl.queryWlanhotspotSubDeliveryList)
//    //  Observable<DeliveryDetailEntity> queryWlanhotspotSubDeliveryList(@Field("recourseType") String recourseType, @Field("parentId")String parentId,@Field("siteID") String siteID);
//    Observable<DeliveryDetailEntity> queryWlanhotspotSubDeliveryList(@Field("recourseType") String recourseType, @Field("parentId") String parentId, @Field("resourceId") String resourceId, @Field("uid") String uid);
//
//    //家客巡检项的接口
//    @FormUrlEncoded
//    @POST(AllUrl.queryCommunitySubDeliveryList)
//    //  Observable<DeliveryDetailEntity> queryCommunitySubDeliveryList(@Field("recourseType") String recourseType, @Field("parentId")String parentId,@Field("siteID") String siteID);
//    Observable<DeliveryDetailEntity> queryCommunitySubDeliveryList(@Field("recourseType") String recourseType, @Field("parentId") String parentId, @Field("resourceId") String resourceId, @Field("uid") String uid);
//
//    //铁塔巡检项的接口
//    @FormUrlEncoded
//    @POST(AllUrl.queryTowerSubDeliveryList)
//    //  Observable<DeliveryDetailEntity> queryTowerSubDeliveryList(@Field("recourseType") String recourseType, @Field("parentId")String parentId,@Field("siteID") String siteID);
//    Observable<DeliveryDetailEntity> queryTowerSubDeliveryList(@Field("recourseType") String recourseType, @Field("parentId") String parentId, @Field("resourceId") String resourceId, @Field("uid") String uid);
//
//    //集客巡检项的接口
//    @FormUrlEncoded
//    @POST(AllUrl.queryTrsoProductSubDeliveryList)
//    //  Observable<DeliveryDetailEntity> queryTrsoProductSubDeliveryList(@Field("recourseType") String recourseType, @Field("parentId")String parentId,@Field("siteID") String siteID);
//    Observable<DeliveryDetailEntity> queryTrsoProductSubDeliveryList(@Field("recourseType") String recourseType, @Field("parentId") String parentId, @Field("resourceId") String resourceId, @Field("uid") String uid);
//
//
//    //查询枚举接口
//    @FormUrlEncoded
//    @POST(AllUrl.queryDictionary)
//    Observable<DeliveryDictionaryidEntity> queryDictionary(@Field("dictionaryid") String dictionaryid);
//
//    //************************************************
//    //保存基站巡检项
//    @FormUrlEncoded
//    @POST(AllUrl.updateSiteDeliveryInfo)
//    Observable<DeliverySaveEntity> updateSiteDeliveryInfo(@Field("resourceDeliveryInfo") String siteDeliveryInfo, @Field("uid") String uid);
//
//    //保存直放站巡检项
//    @FormUrlEncoded
//    @POST(AllUrl.updateRepeaterDeliveryInfo)
//    Observable<DeliverySaveEntity> updateRepeaterDeliveryInfo(@Field("resourceDeliveryInfo") String siteDeliveryInfo, @Field("uid") String uid);
//
//    //保存室分巡检项
//    @FormUrlEncoded
//    @POST(AllUrl.updateRoomsubDeliveryInfo)
//    Observable<DeliverySaveEntity> updateRoomsubDeliveryInfo(@Field("resourceDeliveryInfo") String siteDeliveryInfo, @Field("uid") String uid);
//
//    //保存WLAN巡检项
//    @FormUrlEncoded
//    @POST(AllUrl.updateWlanhotspotDeliveryInfo)
//    Observable<DeliverySaveEntity> updateWlanhotspotDeliveryInfo(@Field("resourceDeliveryInfo") String siteDeliveryInfo, @Field("uid") String uid);
//
//    //保存家客巡检项
//    @FormUrlEncoded
//    @POST(AllUrl.updateCommunityDeliveryInfo)
//    Observable<DeliverySaveEntity> updateCommunityDeliveryInfo(@Field("resourceDeliveryInfo") String siteDeliveryInfo, @Field("uid") String uid);//保存基站巡检项
//
//    //保存铁塔巡检项
//    @FormUrlEncoded
//    @POST(AllUrl.updateTowerDeliveryInfo)
//    Observable<DeliverySaveEntity> updateTowerDeliveryInfo(@Field("resourceDeliveryInfo") String siteDeliveryInfo, @Field("uid") String uid);
//
//    //保存集客巡检项
//    @FormUrlEncoded
//    @POST(AllUrl.updateTrsoProductDeliveryInfo)
//    Observable<DeliverySaveEntity> updateTrsoProductDeliveryInfo(@Field("resourceDeliveryInfo") String siteDeliveryInfo, @Field("uid") String uid);
//
//    //上传图片
//    @FormUrlEncoded
//    @POST(AllUrl.upload)
//    Observable<SaveTaoBean> upload(@Field("resType") String resType
//            , @Field("resName") String resName,
//                                   @Field("deliveryCategory") String deliveryCategory,
//                                   @Field("deliveryItem") String deliveryItem,
//                                   @Field("ZhLabel") String ZhLabel,
//                                   @Field("file") String file
//            , @Field("resourceid") String resourceid
//            , @Field("uid") String uid);
//
//    //查看图片
//    @FormUrlEncoded
//    @POST(AllUrl.getPicList)
//    Observable<GroupPhotoListEntity> getPicList(@Field("resId") int resId, @Field("deliveryCategory") String deliveryCategory, @Field("deliveryItem") String deliveryItem, @Field("uid") String uid);
//
//
//    //传输线路 == 获取周边资源
//    @FormUrlEncoded
//    @POST(AllUrl.getResourcePointByLoacation)
//    Observable<List<TMLineMainEntity>> getResourcePointByLoacation(@Field("latitude") double latitude, @Field("longitude") double longitude, @Field("uid") String uid);
//
//
//    //删除图片
//    @FormUrlEncoded
//    @POST(AllUrl.del)
//    Observable<DeliverySaveEntity> del(@Field("intId") String resId, @Field("uid") String uid);
//
//
//    //保存路由
//    @FormUrlEncoded
//    @POST(AllUrl.saveTask)
//    Observable<SaveResultBean> saveTask(@Field("jsonRequest") String jsonRequest, @Field("uid") String uid);
//
//
//    //获取我的路由
//    @FormUrlEncoded
//    @POST(AllUrl.getMyRoute)
//    Observable<MyRouteBean> getMyRoute(@Field("jsonRequest") String jsonRequest, @Field("uid") String uid);
//
//    //删除我的路由
//    @FormUrlEncoded
//    @POST(AllUrl.deleteRoute)
//    Observable<SaveResultBean> deleteRoute(@Field("jsonRequest") String jsonRequest, @Field("uid") String uid);
//
//
//
//
//    //获取我的确定路由
//    @FormUrlEncoded
//    @POST(AllUrl.getMyToBeConfirm)
//    Observable<MyRouteBean> getMyToBeConfirm(@Field("jsonRequest") String jsonRequest, @Field("uid") String uid);
//
//    //确定路由
//    @FormUrlEncoded
//    @POST(AllUrl.updateRouteTask)
//    Observable<SaveResultBean> updateRouteTask(@Field("jsonRequest") String routeId, @Field("uid") String uid);
//
//
//    //保存隐患
//    @FormUrlEncoded
//    @POST(AllUrl.saveErrorInfo)
//    Observable<SaveBean> saveErrorInfo(@Field("jsonRequest") String jsonRequest, @Field("uid") String uid);
//
//    //保存隐患图片
//    @FormUrlEncoded
//    @POST(AllUrl.uploadPhotoAndInfo)
//    Observable<SaveBean> uploadPhotoAndInfo(@Field("jsonRequest") String jsonRequest, @Field("uid") String uid);
//
//    //获取路由id
//    @FormUrlEncoded
//    @POST(AllUrl.getRouteInfo)
//    Observable<SaveBean> getRouteInfo(@Field("uid") String uid);
//
//
//    //登录
//    //http://117.131.224.133:7004/hrmw/login?useraccount=dw_wanglili&password=123456
//    @FormUrlEncoded
//    @POST(AllUrl.login)
//    Observable<LoginEntity> login(@Field("useraccount") String useraccount, @Field("password") String password);
//
//    //基站
//    @FormUrlEncoded
//    @POST(AllUrl.updateSiteDeliveryInfoNew)
//    Observable<SaveTaoBean> updateSiteDeliveryInfoNew(@Field("resourceDeliveryInfo") String resourceDeliveryInfo, @Field("confirmUserID") String confirmUserID, @Field("confirmUserName") String confirmUserName, @Field("uid") String uid);
//
//    //直放站
//    @FormUrlEncoded
//    @POST(AllUrl.updaterepeaterDeliveryInfoNew)
//    Observable<SaveTaoBean> updaterepeaterDeliveryInfoNew(@Field("resourceDeliveryInfo") String resourceDeliveryInfo, @Field("confirmUserID") String confirmUserID, @Field("confirmUserName") String confirmUserName, @Field("uid") String uid);
//
//
//    //室分
//    @FormUrlEncoded
//    @POST(AllUrl.updateroomsubDeliveryInfoNew)
//    Observable<SaveTaoBean> updateroomsubDeliveryInfoNew(@Field("resourceDeliveryInfo") String resourceDeliveryInfo, @Field("confirmUserID") String confirmUserID, @Field("confirmUserName") String confirmUserName, @Field("uid") String uid);
//
//    //WLAN
//    @FormUrlEncoded
//    @POST(AllUrl.updatewlanhotspotDeliveryInfoNew)
//    Observable<SaveTaoBean> updatewlanhotspotDeliveryInfoNew(@Field("resourceDeliveryInfo") String resourceDeliveryInfo, @Field("confirmUserID") String confirmUserID, @Field("confirmUserName") String confirmUserName, @Field("uid") String uid);
//
//    //家客
//    @FormUrlEncoded
//    @POST(AllUrl.updatecommunityDeliveryInfoNew)
//    Observable<SaveTaoBean> updatecommunityDeliveryInfoNew(@Field("resourceDeliveryInfo") String resourceDeliveryInfo, @Field("confirmUserID") String confirmUserID, @Field("confirmUserName") String confirmUserName, @Field("uid") String uid);
//
//    //铁塔
//    @FormUrlEncoded
//    @POST(AllUrl.updatetowerDeliveryInfoNew)
//    Observable<SaveTaoBean> updatetowerDeliveryInfoNew(@Field("resourceDeliveryInfo") String resourceDeliveryInfo, @Field("confirmUserID") String confirmUserID, @Field("confirmUserName") String confirmUserName, @Field("uid") String uid);
//
//    //集客
//    @FormUrlEncoded
//    @POST(AllUrl.updatetrsoproductDeliveryInfoNew)
//    Observable<SaveTaoBean> updatetrsoproductDeliveryInfoNew(@Field("resourceDeliveryInfo") String resourceDeliveryInfo, @Field("confirmUserID") String confirmUserID, @Field("confirmUserName") String confirmUserName, @Field("uid") String uid);


}