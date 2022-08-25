package top.hllcloud.platform.supports.websocket.consts;

/**
 * Websocket Session存储常量
 *
 * @author hllshiro
 * @date 2022/4/4 15:21
 */
public interface WebSocketConstant {

    /**
     * token
     */
    String TOKEN_NAME = "__TOKEN";

    /**
     * sysLoginUser
     */
    String SYS_LOGIN_USER = "__SYS_LOGIN_USER";

    /**
     * endpoint
     */
    String ENDPOINT = "__ENDPOINT";

    /**
     * 请求提前结束标识
     */
    String REQUEST_SESSION_FINISH = "__REQUEST_SESSION_FINISH";

    /**
     * 当前正在存储的文件信息
     */
    String SAVE_FILE_INFO = "__SAVE_FILE_INFO";

    /**
     * 当前文件分片编号
     */
    String SAVE_FILE_PART_NUM = "__SAVE_FILE_PART_NUM";

    /**
     * 资源唯一标识
     */
    String RES_CODE = "__RES_CODE";

    /**
     * 控制器索引
     */
    String CONTROLLER_INDEX = "__CONTROLLER_INDEX";

    /**
     * 席位类型
     */
    String SEAT_TYPE = "__SEAT_TYPE";

    /**
     * 席位id
     */
    String SEAT_ID = "__SEAT_ID";

    /**
     * chat域Id(subjectId)
     */
    String DOMAIN_ID = "__DOMAIN_ID";

    /**
     * chat域名称(subjectName)
     */
    String DOMAIN_NAME = "__DOMAIN_NAME";
}
