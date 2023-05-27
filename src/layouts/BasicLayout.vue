<template>
  <!-- 导航栏 -->
  <van-nav-bar
    :title="title"
    fixed
    left-arrow
    @click-left="onClickLeft"
    @click-right="onClickRight"
  >
    <template #right>
      <van-icon name="search" size="18" />
    </template>
  </van-nav-bar>

  <div id="content">
    <router-view />
  </div>

  <!-- <slot> 这里是内容 </slot> -->

  <!-- 标签页 -->
  <van-tabbar route>
    <van-tabbar-item to="/" icon="home-o" name="index">主页</van-tabbar-item>
    <van-tabbar-item to="/team" icon="search" name="team">队伍</van-tabbar-item>
    <van-tabbar-item to="/user" icon="friends-o" name="user"
      >个人</van-tabbar-item
    >
  </van-tabbar>
</template>

<!-- 脚本 -->
<script setup lang="ts">
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import { ref } from "vue";
import routes from "../config/route";
import { getCurrentUser } from "../service/user";

const router = useRouter();
// 默认页面标题
const DEFAULT_TITLE = "伙伴匹配";
// 页面标题
const title = ref(DEFAULT_TITLE);

// 校验是否登录, 未登录则跳转至登录页;
onMounted(async () => {
  const res = await getCurrentUser();
  console.log(res);
  if (!res.data) {
    console.log("未登录!");
    router.replace("/user/login");
  }
});
//
router.beforeEach((to) => {
  // 拿取跳转页面路由的path
  const toPath = to.path;
  // 在自定义路由中逐个比对
  const route = routes.find((route) => {
    return toPath == route.path;
  });
  // 拿取匹配的页面路由的title
  title.value = route?.title ?? DEFAULT_TITLE;
});
// 左”/“按钮, 返回上个页面
const onClickLeft = () => {
  router.back();
};
// 跳转至搜索页
const onClickRight = () => {
  router.push("/search");
};
</script>

<style></style>
