// 1. 定义路由组件.
import IndexPage from "../pages/IndexPage.vue";
import UserPage from "../pages/UserPage.vue";
import SearchPage from "../pages/SearchPage.vue";
import UserEditPage from "../pages/UserEditPage.vue";
import UserListPage from "../pages/UserListPage.vue";
import UserLoginPage from "../pages/UserLoginPage.vue";
import TeamPage from "../pages/team/TeamPage.vue";
import TeamEditPage from "../pages/team/TeamEditPage.vue";
import TeamAddPage from "../pages/team/TeamAddPage.vue";
import TeamListPage from "../pages/team/TeamListPage.vue";
import FriendPage from "../pages/friend/FriendPage.vue";

// 2. 定义一些路由
// 每个路由都需要映射到一个组件
// 我们后面再讨论嵌套路由
const routes = [
  { path: "/", title: "主页", component: IndexPage }, // 主页
  { path: "/user", title: "个人信息页", component: UserPage }, // 个人页
  { path: "/search", title: "用户搜索页", component: SearchPage }, // 搜索页
  { path: "/user/edit", title: "用户编辑页", component: UserEditPage }, // 用户信息修改页
  { path: "/user/list", title: "用户列表页", component: UserListPage }, // 用户列表页
  { path: "/user/login", title: "登录页", component: UserLoginPage }, // 用户登录页

  { path: "/team", title: "队伍信息页", component: TeamPage }, // 队伍页
  { path: "/team/edit", title: "队伍编辑", component: TeamEditPage }, // 队伍修改页
  { path: "/team/add", title: "队伍新增页", component: TeamAddPage }, // 队伍修改页
  { path: "/team/list", title: "队伍列表页", component: TeamListPage }, // 队伍列表页

  { path: "/friend", title: "用户列表页", component: FriendPage }, // 队伍列表页
];

export default routes;
