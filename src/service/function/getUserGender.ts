// 转换stasus
export const getUserGender = (gender: string) => {
  if (gender === "0") return "女";
  else if (gender === "1") return "男";
  else return "人妖";
};
