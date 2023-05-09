<template>
  <!-- 导航栏 -->
  <van-nav-bar
    title="标题"
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
import { getCurrentUser } from "../service/user";

const router = useRouter();

// 校验是否登录, 未登录则跳转至登录页;
onMounted(async () => {
  const res = await getCurrentUser();
  console.log(res);
  if (!res.data) {
    console.log("未登录!");
    router.replace("/user/login");
  }
});

const onClickLeft = () => {
  router.replace("/");
};
const onClickRight = () => {
  router.push("/search");
};
</script>

<style></style>
