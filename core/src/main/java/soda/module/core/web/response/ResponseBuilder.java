package soda.module.core.web.response;

/**
 * @Describe
 * @Author orang
 * @Create 2019/2/13 15:40
 **/
public class ResponseBuilder {
    //禁止通过new来构造函数
    private ResponseBuilder() {}
    /**
     * 执行成功，但是没有返回结果
     * @return
     */
    public static ResponseModel<String> success() {
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setCode(20000);
//        responseModel.setData("成功的返回结果");
        responseModel.setMessage("成功返回一个无数据的结果");
        return responseModel;
    }

    /**
     * 带有泛型的返回结果的类
     * @param t
     * @param <T>
     * @return
     */
    public static <T> ResponseModel<T> success(T t) {
        ResponseModel<T> responseModel = new ResponseModel<>();
        responseModel.setCode(20000);
        responseModel.setData(t);
        responseModel.setMessage("返回结果成功");
        return responseModel;
    }

    /**
     * 返回一个未知的错误
     *
     * @return
     */
    public static ResponseModel<String> failure() {
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setCode(40000);
        responseModel.setMessage("未知的错误，请联系管理员！");
        return responseModel;
    }

    /**
     * 返回一个具有错误信息的结果，通常用在统一异常管理里面
     * @param body
     * @return
     */
    public static ResponseModel<String>  failure(String body) {
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setCode(40001);
        responseModel.setMessage(body);
        return responseModel;
    }

    /**
     * 返回一个未授权的错误
     * @param body
     * @return
     */
    public static ResponseModel<String> forbidden(String body){
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setCode(40003);
        responseModel.setMessage(body);
        return responseModel;
    }

    public static ResponseModel<String> warning(){
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setCode(60001);
        responseModel.setMessage("业务逻辑错误，警告提示");
        return responseModel;
    }
    public static ResponseModel<String> warning(String body){
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setCode(60001);
        responseModel.setMessage(body);
        return responseModel;
    }
}
