/**
 * request 网络请求工具
 * 更详细的 api 文档: https://github.com/umijs/umi-request
 */
import {extend} from 'umi-request';
import {message} from "antd";
import {history} from 'umi';
import {stringify} from "querystring";


/**
 * 配置request请求时的默认参数
 */
const request = extend({
  credentials: 'include', // 默认请求是否带上cookie
  // requestType: 'form',
});

/**
 * 所以请求拦截器
 */
request.interceptors.request.use((url, options): any => {
  console.log(`do request url = ${url}`)
  return {
    url,
    options: {
      ...options,
      headers: {},
    },
  };
});

/**
 * 所有响应拦截器
 */
request.interceptors.response.use(async (response, options): Promise<any> => {
  const res = await response.clone().json();
  // 1.返回成功状态
  if (res.code === 0 && res.data > 0) {
    console.log(res.message);
    return res.data;
  }

  if (res.code === 50001) {
    console.log("...")
    // 弹出异常信息
    message.error(res.message)
    // 弹出异常描述
    // message.error(res.description)
    // 跳转至登录页
    history.replace({
      pathname: 'user/login',
      search: stringify({
        redirect: location.pathname,
      }),
    });
    console.log("code: " + res.code)
  }

  return res.data;
});

export default request;
