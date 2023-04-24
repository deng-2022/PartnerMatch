import myAxios from "../plugins/myAxios";

// 获取当前登录用户
export const getCurrentUser = async () => {
  return await myAxios.get("/user/currentUser");
};
