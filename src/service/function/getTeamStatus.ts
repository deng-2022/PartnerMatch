// 转换stasus
export const getTeamStatus = (status: number) => {
  if (status === 0) return "公开";
  else if (status === 1) return "私有";
  else return "加密";
};
