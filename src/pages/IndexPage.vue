<template>
  <div id="content" style="padding-bottom: 53px">
    <!-- 用户信息页 -->
    <van-card
      v-for="user in userList"
      :tag="`${user.gender}`"
      :title="`${user.userAccount} ${user.username} ${user.planetCode}`"
      :desc="user.profile"
      :thumb="`${user.avatarUrl}`"
    >
      <!-- 标签展示 -->
      <template #tags>
        <van-tag
          plain
          type="primary"
          v-for="tag in user.tags"
          style="margin-right: 3px; margin-top: 3px"
        >
          {{ tag }}
        </van-tag>
      </template>
      <template #footer>
        <van-button size="mini">联系俺</van-button>
      </template>
    </van-card>
    <!-- 无用户信息展示 -->
    <van-empty v-if="!userList" description="获取用户信息失败" />
    <!-- 分页插件 -->
    <van-pagination
      v-if="userList"
      v-model="currentPage"
      :total-items="total"
      :items-per-page="pageSize"
      force-ellipses
      @change="change"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { onMounted } from "vue";
import { userType } from "../models/user";
import myAxios from "../plugins/myAxios";

// 用户列表
const userList = ref([]);
// 当前页码
const currentPage = ref(1);
// 每页显示数
let pageSize = 10;
// 总记录数
let total: number = 0;

const getPage = async (currentPage: number) => {
  // 发送请求, 获取用户数据列表
  const userListData = await myAxios
    .get("/user/recommend", {
      params: {
        currentPage: currentPage,
        pageSize: pageSize,
      },
    })
    // 响应
    .then(function (response) {
      // 返回响应数据（用户列表）
      console.log(response.data);
      total = response.data.total;
      pageSize = response.data.size;
      return response.data?.records;
    })
    // 抛异常
    .catch(function (error) {
      console.log(error);
    });
  // 成功拿到用户数据列表(不为空)
  if (userListData) {
    // 遍历用户数据列表
    userListData.forEach((user: userType) => {
      // 将gender的 "1"/"0" 渲染成 "男"/"女"
      if (user.gender === "1") user.gender = "男";
      if (user.gender === "0") user.gender = "女";
      // JSON字符串序列化为列表
      if (user.tags) user.tags = JSON.parse(user.tags);
    });
    // 处理过后的用户列表
    userList.value = userListData;
  }
};

// 用户列表, 钩子函数
onMounted(() => {
  getPage(currentPage.value);
});

// 改变页码
const change = () => {
  console.log(currentPage.value);
  console.log(userList.value.length);
  getPage(currentPage.value);
};
</script>
