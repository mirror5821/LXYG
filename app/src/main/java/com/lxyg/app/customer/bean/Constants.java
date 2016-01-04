package com.lxyg.app.customer.bean;

public interface Constants {
	//配送半径
	public final static double DISTRIBUTION_RADIUS = 1000;//Distribution radius

	//一些用到的常量定义
	public final static int MAP_REQUESTCODE = 1101;
	public final static int ADDRESS_ADD_REQUESTCODE = 1102;
	public final static int ADDRESS_SELECT_REQUESTCODE = 1103;
	public final static int ADDRESS_UPDATE = 1104;

	public final static int LOGIN_CODE1 = 2101;
	public final static int FORUM_ADD_CODE = 5001;

	public final static int PRODUCT_TYPE1 = 1;//正常商品
	public final static int PRODUCT_TYPE2 = 2;//非标准商品
	public final static String INTENT_ID = "INTENT_ID";
	public final static String TYPE_ID = "TYPE_ID";
	public final static String NOTIFY_TV = "NOTIFY_TV";

	public final static int PAY_TYPE1 = 1;//支付入口1 //购物车进入
	public final static int PAY_TYPE2 = 2;//支付入口2 //未支付订单进入
	public final static int PAY_TYPE3 = 3;//支付入口3 //直接购买进入


//	public final static String BASE_URL = "http://192.168.0.210/LXYG/";
	public final static String BASE_URL = "http://www.lexiangyungou.cn/LXYG/";

	//正式上线用这个
	public static final String WECHAT_APP_ID = "wx2d2b54b6349d8ef7";
	public static final String WECHAT_SECRET = "d4624c36b6795d1d99dcf0547af5443d";
	public static final String WECHAT_VALUE = "android_pub";

//	//测试上线用这个
//	public static final String WECHAT_APP_ID = "wx9e721ab3631bd8bb";
//	public static final String WECHAT_SECRET = "e1a4a71e1c2946b485df5d9da251b371";
//	public static final String WECHAT_VALUE = "android_in_test";

	//地址
	public final static String LAT = "LAT";
	public final static String LNG = "LNG";
	public final static String ADDRESS = "ADDRESS";
	public final static String CITY = "CITY";
	public final static String COUNTRY = "COUNTRY";
	public final static String DISTRICT = "DISTRICT";
	public final static String PROVINCE = "PROVINCE";
	public final static String NAME = "NAME";
	public final static String PHONE = "PHONE";
	public final static String ADDRESS_ID = "ADDRESS_ID";
	public final static String ADR = "ADR";

	public final static String USER_INFO = "USER_INFO";
	public final static String USER_INFO_PHONE = "USER_INFO_PHONE";
	public final static String USER_INFO_PASS = "USER_INFO_PASS";
	public final static String SHOP_INFO1 = "SHOP_INFO1";

	public final static String SECRET = "caca51";
	public final static String JSONG_KEY = "info";
	public final static String ORDER_DELETE = "delOrder";


	//请求都和地址
	public final static String HOST_HEADER = BASE_URL+"app/user/";
	public final static String HOST_IMG_HEADER = BASE_URL+"res/";

	public final static String ALIPAY_NOTIFY_URL = BASE_URL+"app/pay/alipayNotify";

	public final static String INDEX = "homePage1";//首页
	public final static String PRODUCT_LIST ="products";//产品列表
	public final static String PRODUCT_DETAILS ="showProductInfo";//产品详情
	public final static String SHOPPING_CAR_LIST ="SCProduct";//购物车列表
	public final static String ADDRESS_LIST ="addressList";//收货地址列表
	public final static String ADDRESS_ADD = "addAddress";//添加收货地址
	public final static String CURRENT_TIME = "currentTime";//添加收货地址
	public final static String ORDER_MAKE = "addOrderInfo";//下单
	public final static String ORDER_LIST = "userOrderList";//订单列表
	public final static String WECHAT_LOGIN = "weLogin";//微信登陆
	public final static String SCAN = "scanCode";
	public final static String FEED_BACK = "v2/feedBack";//意见反馈
	public final static String TRACK = "orderTail";//意见反馈
	public final static String USER_CASH_TOTAL = "loadCash";//用户电子现金余额
	public final static String ORDER_NUM = "orderStatusNum";//用户电子现金余额
	public final static String USER_CASH_LIST = "loadCashs";//用户电子现金余额

	public final static int ORDER_STATUS_ALL = 1;//订单列表状态
	public final static int ORDER_STATUS1 = 1;//订单列表状态
	public final static int ORDER_STATUS2 = 2;//订单列表状态
	public final static int ORDER_STATUS3 = 3;//订单列表状态
	public final static int ORDER_STATUS4 = 4;//订单列表状态
	public final static int ORDER_STATUS5 = 5;//订单列表状态

	public final static String UPDATA_ADDRESS = "updateAddress";//修改地址信息

	public final static String WX_PAY_NO = "wxPayNo";
	public final static String WX_PAY = "wxPay";

	public final static String GET_CODE = "messageCode";//发送验证码
	public final static String USER_LOGIN = "userLogin";//用户手机登录

	public final static String RECEIVER_GOODS = "receiveGoods";
	public final static String ORDER_REF = "refuseOrder";

	public final static String UPLOAD_IMG = "addImg";//上传图片
	public final static String ACTIVITY_AUTUMN = "activityProducts";//获取中秋活动产品列表
	public final static String ACTIVITY_AUTUMN_BUY = "addActivityOrder";//中秋活动提交订单
	public final static String ACTIVITY_AUTUMN_BUY_OK = "activityOrderPay";//中秋活动提交订单完成后调用

	public final static String INDEXT2 = "v2/homePage";//新的首页
	public final static String FB_PRODUCT_DETAILS = "v2/FBProduct";//非标商品详情
	public final static String SHOPPING_CAR_LIST2 ="v2/SCProduct";//购物车列表
	public final static String ORDER_MAKE2 = "v2/addOrderInfo";//下单
	public final static String FB_SHOP_LIST = "v2/findShopsByType";//非标准店铺列表
	public final static String FB_SHOP_PRODUCT_LIST = "v2/FBProducts";//非标准店铺列表
	public final static String RECOMMEND_PRODUCT_LIST = "v2/recommProducts";//非标准店铺列表
	public final static String CITY_LIST = "v2/cityList";//城市列表
	public final static String SEARCH_LIST = "v2/searchName?";//搜索列表
	public final static String PRODUCT_TYPE ="v2/types";//产品分类1
	public final static String PRODUCT_BRAND = "v2/searchBands";//产品分类二级
	public final static String FORUM_ADD = "v2/addForm";//论坛添加
	public final static String FORUM_LIST = "v2/listForm";//论坛帖子列表
	public final static String FORUM_ZAN_ADD = "v2/zan";//论坛点赞
	public final static String FORUM_DETAILS = "v2/form";//帖子详情
	public final static String FORUM_COMMENT = "v2/replay";//帖子评论
	public final static String GET_SHOP_COORDINATE = "v2/getShopLatLng";//获取店铺经纬度
	public final static String MY_FOURM = "v2/myForms";//我的帖子
	public final static String FOURM_DELETE = "v2/delForm";//删除帖子
	public final static String SHOP_LIST_BY_AREA = "v2/shopList";//根据地区id获取店铺列表  需要传经纬度
	public final static String SHOP_LIST_BY_SEARCH = "v2/searchShop";//搜索店铺
	public final static String SHOP_LIST_BY_LAT_LNG = "v2/getShopsByLatLng";//
	public final static String SIGN_HISTORY = "v2/signLog";
	public final static String SIGN = "v2/addSign";
	public final static String CATEGORY_ALL = "v2/categorys";
	public final static String ACTIVITY_PRODUCT_LIST = "activityPros";
	public final static String RED_BAG = "v2/hb";
	public final static String ADDRESS_DELETE = "delAddress";
	public final static String SHOP_AREA = "v2/shopScope";


	public final static String SHOP_SHUIGUO = "http://7xk59r.com2.z0.glb.qiniucdn.com/ic_shop_f.jpg";//水果店店铺封面
	public final static String SHOP_XIAJIUXIAOCAI = "http://7xk59r.com2.z0.glb.qiniucdn.com/ic_shop_d.jpg";//水果店店铺封面
	public final static String JION_US = "http://www.lexiangyungou.cn/LXYG/join_us/join_us.html";
}
