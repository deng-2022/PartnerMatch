<template>
  <div id="content" style="padding-bottom: 57px">
    <van-card
      v-for="user in userList"
      :tag="user.gender"
      :title="`${user.userAccount} ${user.username} ${user.planetCode}`"
      :desc="user.profile"
      thumb="https://fastly.jsdelivr.net/npm/@vant/assets/ipad.jpeg"
    >
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
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { onMounted } from "vue";
import { useRoute } from "vue-router";
import myAxios from "../../plugins/myAxios";
import qs from "qs";
import { userType } from "../../models/user";

const route = useRoute();
const { tags } = route.query;
const userList = ref([]);

// 用户列表, 钩子函数
onMounted(async () => {
  // 发送请求, 获取用户数据列表
  const userListData = await myAxios
    .get("/user/search/tags", {
      // 携带 标签列表 参数
      params: {
        tagNameList: tags,
      },
      // 引入 qs, 在axios请求下正确地携带数组参数发送至后端
      paramsSerializer: {
        serialize: (params: userType) =>
          qs.stringify(params, { indices: false }),
      },
    })
    // 响应
    .then(function (response) {
      console.log(response);
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
      // 将gender的 "1"/"0" 渲染成 "男"/"女"
      if (user.gender === "1") user.gender = "男";
      if (user.gender === "0") user.gender = "女";
      // JSON字符串序列化为列表
      if (user.tags) user.tags = JSON.parse(user.tags);
    });
    // 处理过后的用户列表
    userList.value = userListData;
  }
});

// 测试数据
// const users = [
//   {
//     id: 1,
//     userAccount: "memory",
//     username: "邓哈哈",
//     avatarUrl: "",
//     gender: "男",
//     phone: "18887786754",
//     email: "3348407547@qq.com",
//     planetCode: "17625",
//     createTime: new Date(),
//     profile: "这个用户很懒，什么也没写~",
//     tags: ["java", "emo", "努力中"],
//   },
//   {
//     id: 1,
//     userAccount: "memory",
//     username: "邓哈哈",
//     avatarUrl: "",
//     gender: "男",
//     phone: "18887786754",
//     email: "3348407547@qq.com",
//     planetCode: "17625",
//     createTime: new Date(),
//     profile: "这个用户很懒，什么也没写~",
//     tags: ["java", "emo", "努力中"],
//   },
// ];
</script>
