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

// 2. 定义一些路由
// 每个路由都需要映射到一个组件。
// 我们后面再讨论嵌套路由。
const routes = [
  { path: "/", component: IndexPage }, // 主页
  { path: "/user", component: UserPage }, // 个人页
  { path: "/search", component: SearchPage }, // 搜索页
  { path: "/user/edit", component: UserEditPage }, // 用户信息修改页
  { path: "/user/list", component: UserListPage }, // 用户列表页
  { path: "/user/login", component: UserLoginPage }, // 用户登录页

  { path: "/team", component: TeamPage }, // 队伍页
  { path: "/team/edit", component: TeamEditPage }, // 队伍修改页
  { path: "/team/add", component: TeamAddPage }, // 队伍修改页
];

export default routes;
