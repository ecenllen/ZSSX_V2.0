package com.gta.zssx.pub.common;


/**
 * 保存常用的字符串、键值对等信息
 *
 * @author bin.wang1
 */
public class Constant {

    public static final int LOAD_DATA_SIZE = 20;
    //获取数据成功的消息
    public static final int MSG_GETDATA_SUCCESS = 1001;
    //操作成功的消息
    public static final int MSG_OPERATE_SUCCESS = 1002;
    public static final int MSG_DELETE_SCHEDULE_SUCCESS = 10021;
    public static final int MSG_UPDATE_SCHEDULE_SUCCESS = 10022;


    public static final int MSG_FAIL = -1001;
    public static final int MSG_SHOW_PROGRESS = 1000;

    public static final int MSG_TASK_AGREE = 10000;//（传ID出来）

    public static final String ACCOUNT_NAME = "user.name";

    public static final String ACCOUNT_PWD = "user.pwd";

    public static final String SERVER_ADDR = "server.addr";//登录地址

    public static final String SUB_SERVER_ADDR = "sub.server.addr";//子系统地址

    // K-V 键值对
    public static final String PROP_KEY_PRIVATE_TOKEN = "private_token";
    public static final String PROP_KEY_UID = "user.uid";
    public static final String PROP_KEY_TIME_SPAN = "time.span";
    public static final String PROP_KEY_PORTRAIT = "user.portrait";// 头像的文件名
    public static final String PROP_KEY_SERVER_PORTRAIT = "user.serverportrait";//服务器传递过来的头像。
    public static final String PROP_KEY_USERNAME = "user.username";
    public static final String PROP_KEY_FULLNAME = "user.fullname";
    public static final String PROP_KEY_DEPARTMENT = "user.department";
    public static final String PROP_KEY_TELEPHONE = "user.telephone";
    public static final String PROP_KEY_EMAIL = "user.useremail";

    public static final String PROP_KEY_SWITCHER_NOTICE = "switcher.notice";
    public static final String PROP_KEY_SWITCHER_RING = "switcher.ring";
    public static final String PROP_KEY_SWITCHER_SHAKE = "switcher.shake";
    public static final String PROP_KEY_BPMHOST = "user.bpmhost";
    // 广播 字符串
    // 默认定时时间 10分钟
    public static final int DEFAULT_TIME_SPAN = 1;


    /*用于存储通知最新时间的键值*/
    public static final String TasksId = "TasksId";
    public static final String NoticeId = "NoticeId";
    public static final String MailId = "MailId";
    public static final String MeetingId = "MeetingId";
    public static final String RecordId = "RecordId";
    public static final String ScheduleId = "ScheduleId";
    /** 宿舍维度/班级维度*/
    public static final int DimensionType_Dormitory = 0;  //宿舍
    public static final int DimensionType_Class = 1; //班级
    /** 单项、多项、有选项、无选项*/
    public static final int ScoringTemplateType_Single = 1;
    public static final int ScoringTemplateType_Multiple = 2;
    public static final int ScoringTemplateType_HasOption = 3;
    public static final int ScoringTemplateType_NoOption = 4;
    /** 宿舍模块  详情新增、查看、编辑*/
    public static final int ACTION_TYPE_NEW = 0;  //新增
    public static final int ACTION_TYPE_MODIFY = 1;  //编辑
    public static final int ACTION_TYPE_JUST_CHECK = 2;  //查看
    /** 增分、扣分*/
    public static final int Addition = 1;  //记分
    public static final int Subtraction = 0;  //扣分
    /** 宿舍列表状态 0.未送审,  1.不通过 2.送审中  3.已通过  4.已发布*/
    public static final int TYPE_NOT_SUBMIT = 0;
    public static final int TYPE_NOT_PASS = 1;
    public static final int TYPE_ON_SUBMIT = 2;
    public static final int TYPE_HAVE_BEEN_PASS = 3;
    public static final int TYPE_HAVE_BEEN_PUBLIC = 4;
    /** 最外面的宿舍列表操作 */
    public static final int DORMITORY_ACTION_DETAIL = 0;  //查看详情
    public static final int DORMITORY_ACTION_DELETE = 1;  //删除一条记录
    public static final int DORMITORY_ACTION_SUBMIT = 2;  //提交一条记录
    /**
     * 上一宿舍、下一宿舍、保存、返回
     */
    public static final int DORMITORY_ACTION_BACK_DORMITORY_LIST = 0;
    public static final int DORMITORY_ACTION_NEXT_DORMITORY = 1;
    public static final int DORMITORY_ACTION_PREVIOUS_DORMITORY = 2;
    public static final int DORMITORY_ACTION_SAVE_DORMITORY = 3;
    /****
     * 邮件部分
     ***************************************************/

	/* 单个邮件附件的大小限制 */
    public static int singleAttachSize = 6 * 1024 * 1024;
    /* 所有附件大小限制 */
    public static int allAttachSize = singleAttachSize * 2;
    /* 附件下载路径 */
    public static final String downLoadPath = "/zssx/";
    /* 进入邮件编辑界面的参数 */
    public static String SendStatue = "SendStatue"; // 进入编辑界面的参数
    public static int Send_NewMail = 0; // 0为新建邮件
    public static int Send_FastRepaly = 1; // 1为快速回复
    public static int Send_SendMail = 2; // 2为发件箱进入的时候
    public static int Send_draftMail = 3; // 3为草稿箱进入的时候
    public static final String UNREAD_COUNT = "UnreadCount";  //未读邮件记录
    public static final String RE_LAOD = "IsReLoad"; //是否返回要重新加载

    public static final int AttACH_LOAD_SUCCESS = 25;
    /***********************************************************/

    public static int MeetNumber = 0;    // 包括会议通知未读数和会议纪要未读数的总和
    public static String taskNumber = "";
    public static int Record = 0;
    public static boolean IS_INNER = true;

    /**
     * 数据加载类型
     */
    public static final int REFRESH = 0;
    public static final int LOAD_MORE = 1;
    /**
     * 搜索类型 会议：1001  待办事项：1002  任务：1003
     */
    public static final String KEY_SEARCH_TYPE = "searchType";
    public static final int SEARCH_TYPE_MEETING = 1001;
    public static final int SEARCH_TYPE_BACKLOG = 1002;
    public static final int SEARCH_TYPE_TASK = 1003;
    public static final int SEARCH_TYPE_OFFICIAL = 1004;
    /**
     * 待办/已办事项类型  待办 ：1004  已办： 1005
     */
    public static final String KEY_BACKLOG_TYPE = "backlogType";
    public static final int BACKLOG_TYPE_UNDO = 1;
    public static final int BACKLOG_TYPE_FINISH = 2;
    /**
     * 公文公告 公文：1011 公告：1012
     */
    public static final String KEY_OFFICIAL_NOTICE_TYPE = "officialNoticeType";
    public static final int OFFICIAL_NOTICE_TYPE_OFFICIAL = 1011;  //公文
    public static final int OFFICIAL_NOTICE_TYPE_NOTICE = 1012;   //公告

    public static final int TYPE_ARRANGE = 101;

    public static final String DATE = "date";
    public static final String MID = "mId";
    public static final String MNAME = "mName";

    public static final int LEAD = 1;

    public static final int DETAIL_TYPE_APPLY = 201;//发起申请
    public static final int DETAIL_TYPE_MYAPPLY = 202;//我的申请
    public static final int DETAIL_TYPE_FINISHED = 203;//已办事项
    public static final int DETAIL_TYPE_APPROVAL = 204;//待办事项
    public static final int DETAIL_TYPE_MEETING = 205;//会议详情
    public static final int DETAIL_TYPE_NOTICE = 206;//学校公告
    public static final int DETAIL_TYPE_DOC = 207;//学校公文

    public static final int DOWNLOAD_CONNECT = 3000;
    public static final int DOWNLOAD_START = 3001;
    public static final int DOWNLOAD_PROGRESS = 3002;
    public static final int DOWNLOAD_SUCCESS = 3003;
    public static final int DOWNLOAD_ERROR = 3004;
    
    /** 登录失败/登录超时标识 */
    public static final String LOGIN_FAIL_FLAG = "/SignIn/Index";
}
