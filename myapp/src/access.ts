/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(initialState: { baseResponse?: API.BaseResponse<API.CurrentUser> } | undefined) {
  const { baseResponse } = initialState ?? {};
  // 权限校验
  return {
    // 校验管理员权限
    canAdmin: baseResponse && baseResponse.data.userRole === 1,
  };
}
