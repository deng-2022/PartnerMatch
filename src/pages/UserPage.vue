<template>
  <!-- <slot> 个人 </slot> -->
  <!-- 用户信息 -->
  <div v-if="user">
    <van-cell
      title="账号"
      is-link
      to="/user/edit"
      :value="user.userAccount"
      @click="toEdit('userAccount', '账号', user.userAccount)"
    />
    <van-cell
      title="昵称"
      is-link
      to="/user/edit"
      :value="user.username"
      @click="toEdit('username', '昵称', user.username)"
    />
    <van-cell
      title="头像"
      is-link
      to="/user/edit"
      :value="user.avatarUrl"
      @click="toEdit('avatarUrl', '头像', user.avatarUrl)"
    >
      <img :src="url" alt="头像" style="height: 45px" />
    </van-cell>
    <van-cell
      title="性别"
      is-link
      to="/user/edit"
      :value="getUserGender(user.gender)"
      @click="toEdit('gender', '性别', user.gender)"
    />
    <van-cell
      title="电话"
      is-link
      to="/user/edit"
      :value="user.phone"
      @click="toEdit('phone', '电话', user.phone)"
    />
    <van-cell
      title="邮箱"
      is-link
      to="/user/edit"
      :value="user.email"
      @click="toEdit('email', '邮箱', user.email)"
    />
    <van-cell
      title="星球编号"
      is-link
      to="/user/edit"
      :value="user.planetCode"
      @click="toEdit('planetCode', '星球编号', user.planetCode)"
    />
    <van-cell
      title="注册时间"
      is-link
      to="/user/edit"
      :value="user.createTime"
      @click="toEdit('createTime', '注册时间', user.createTime)"
    />
    <!-- 退出登录 -->
    <van-button
      plain
      type="danger"
      size="large"
      style="margin-top: 50px"
      @click="logout"
      >退出登录</van-button
    >
  </div>
  <van-empty v-else description="获取用户信息失败" />
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { onMounted } from "vue";
import { showSuccessToast } from "vant";
import { getCurrentUser } from "../service/user";
import { ref } from "vue";
import { requestData } from "../models/user";
import { getUserGender } from "../service/function/getUserGender";
import myAxios from "../plugins/myAxios";

// 用户头像url
const url = ref("");
// 用户信息
const user = ref();
// 钩子函数
onMounted(async () => {
  // 发送获取当前登录用户请求
  const res: requestData = await getCurrentUser();
  // console.log(res);
  if (res.data) {
    showSuccessToast("获取用户信息成功");
    // 获取用户信息
    user.value = res.data;
    // 获取用户头像url
    url.value = res.data.avatarUrl;
  } else {
    showSuccessToast("获取用户信息失败");
  }
});

// 跳转用户编辑页
const router = useRouter();
const toEdit = (editKey: string, editName: string, currentValue: string) => {
  router.replace({
    path: "/user/edit",
    query: {
      editKey,
      editName,
      currentValue,
    },
  });
};

// 退出登录
const logout = async () => {
  const logout = await myAxios
    .post("/user/logout", {})
    // 响应
    .then(function (response) {
      // 返回响应数据（用户列表）
      return response?.data;
    })
    // 抛异常
    .catch(function (error) {
      console.log(error);
    });

  if (logout) {
    showSuccessToast(logout);
    console.log(logout);
    router.replace("/user/login");
  }
};
</script>
