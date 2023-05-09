// 用户信息
export type userType = {
  id?: number;
  userAccount?: string;
  username?: string;
  avatarUrl?: string;
  gender?: string;
  phone?: string;
  email?: string;
  createTime?: Date;
  userStatus?: string;
  userRole?: number;
  planetCode?: string;
  profile?: string;
  tags?: string;
};

// 队伍信息
export type teamType = {
  id?: number;
  userId?: number;
  name?: string;
  description?: string;
  maxNum?: number;
  joinNum?: number;
  status?: string;
  expireTime?: Date;
  createTime?: Date;
};

// 用户修改信息
export type editUserType = {
  editKey: string;
  editName: string;
  currentValue: string;
};

export type userLoginRequest = {
  userAccount: string;
  userPassword: string;
};

// 后端响应信息形式
export type requestData = {
  code?: number;
  data?: string;
  decription?: string;
  message?: string;
};
