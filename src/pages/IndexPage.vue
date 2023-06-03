<template>
  <!-- 寄语 -->
  <van-notice-bar
    left-icon="volume-o"
    text="盛年不重来，一日难再晨。及时当勉励，岁月不待人。"
  />

  <!-- 首页轮播图 -->
  <!-- <van-swipe :autoplay="3000" lazy-render :width="480" :height="300">
    <van-swipe-item v-for="image in images" :key="image">
      <img :src="image" />
    </van-swipe-item>
  </van-swipe> -->

  <van-field name="switch" label="开启每周推荐">
    <template #input>
      <van-switch v-model="checked" @click="matchUsers" />
    </template>
  </van-field>

  <!-- 匹配用户表单 -->
  <div id="recommend" style="padding-bottom: 53px">
    <van-divider
      :style="{ color: '#1989fa', borderColor: '#1989fa', padding: '0 16px' }"
    >
      每周推荐用户
    </van-divider>

    <!-- 匹配用户信息页 -->
    <van-card
      v-if="matchUserList"
      v-for="user in matchUserList"
      :tag="getUserGender(user.gender)"
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
    <van-empty v-else description="获取用户信息失败" />
  </div>

  <!-- 用户表单 -->
  <div id="index" style="padding-bottom: 53px">
    <van-divider
      :style="{ color: '#1989fa', borderColor: '#1989fa', padding: '0 16px' }"
    >
      主题
    </van-divider>

    <!-- 用户信息页 -->
    <van-card
      v-if="userList"
      v-for="user in userList"
      :tag="getUserGender(user.gender)"
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
  <!-- 返回顶部 -->
  <van-back-top />
</template>

<script setup lang="ts">
import { ref } from "vue";
import { onMounted } from "vue";
import { userType } from "../models/user";
import myAxios from "../plugins/myAxios";
import { getUserGender } from "../service/function/getUserGender";

// 轮播图片
const images = [
  "https://gitee.com/deng-2022/pictures/raw/master/images/0daac29a25d2247724f0b88135a3c23c.jpg",
  "https://gitee.com/deng-2022/pictures/raw/master/images/typora-icon.png",
];
// 开启/关闭每周推荐
const checked = ref(false);
// 匹配用户列表
const matchUserList = ref([]);
// 用户列表
const userList = ref([]);
// 当前页码
const currentPage = ref(1);
// 每页显示数
let pageSize = 10;
// 总记录数
let total = 0;
// 每周推荐功能
const matchUsers = async () => {
  console.log("checked = " + checked.value);
  // 每周推荐是否开启
  if (checked.value) {
    // 发送请求, 获取用户数据列表
    const userListData = await myAxios
      .get("/user/match", {
        params: {
          num: 3,
        },
      })
      // 响应
      .then(function (response) {
        // 返回响应数据（用户列表）
        return response.data;
      })
      // 抛异常
      .catch(function (error) {
        console.log(error);
      });
    // 成功拿到用户数据列表(不为空)
    if (userListData) {
      // 遍历用户数据列表
      userListData.forEach((user: userType) => {
        if (user.tags) user.tags = JSON.parse(user.tags);
      });
      // 处理过后的用户列表
      matchUserList.value = userListData;
      console.log(currentPage.value);
      console.log(total);
    }
  } else {
    matchUserList.value = [];
  }
};

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
      return response.data.records;
    })
    // 抛异常
    .catch(function (error) {
      console.log(error);
    });
  // 成功拿到用户数据列表(不为空)
  if (userListData) {
    // 遍历用户数据列表
    userListData.forEach((user: userType) => {
      // JSON字符串序列化为列表
      if (user.tags) user.tags = JSON.parse(user.tags);
    });
    // 处理过后的用户列表
    userList.value = userListData;
  }
};

// 用户列表, 钩子函数
onMounted(() => {
  // 匹配用户
  matchUsers();
  // 主页
  getPage(currentPage.value);
});

// 改变页码
const change = () => {
  console.log(currentPage.value);
  console.log(userList.value.length);
  getPage(currentPage.value);
};
</script>
