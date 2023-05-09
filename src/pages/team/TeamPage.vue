<template>
  <div id="content" style="padding-bottom: 57px">
    <div>
      <van-button
        plain
        style="margin-right: 20%; margin-left: 15%"
        type="primary"
        @click="toTeamAdd"
        >创建队伍
      </van-button>

      <van-button plain style="margin-right: 15%" type="primary"
        >搜索队伍
      </van-button>
    </div>
    <!-- <slot> 队伍 </slot> -->
    <van-divider content-position="left">我创建的队伍</van-divider>
    <van-card
      v-for="team in teamCreated"
      :tag="team.status"
      :title="team.name"
      :desc="team.description"
      thumb="https://fastly.jsdelivr.net/npm/@vant/assets/ipad.jpeg"
    >
      <template #bottom>
        <div>队伍人数: {{ team.joinNum }}/ {{ team.maxNum }}</div>
        <div>创建时间: {{ team.createTime }}</div>
        <div>解散时间: {{ team.expireTime }}</div>
      </template>

      <template #footer>
        <van-button size="mini" type="primary" @click="toEditTeam(team.id)">
          修改
        </van-button>
        <van-button size="mini" type="danger">解散</van-button>
      </template>
    </van-card>

    <van-divider content-position="right">我加入的队伍</van-divider>
    <van-card
      v-for="team in teamJoined"
      :tag="team.status"
      :title="team.name"
      :desc="team.description"
      thumb="https://fastly.jsdelivr.net/npm/@vant/assets/ipad.jpeg"
    >
      <template #bottom>
        <div>队伍人数: {{ team.joinNum }}/ {{ team.maxNum }}</div>
        <div>创建时间: {{ team.createTime }}</div>
        <div>解散时间: {{ team.expireTime }}</div>
      </template>

      <template #footer>
        <van-button size="mini" type="success">进群聊天</van-button>
      </template>
    </van-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { onMounted } from "vue";
import { useRouter } from "vue-router";

import { requestData, teamType } from "../../models/user";
import { getCurrentUser } from "../../service/user";
import myAxios from "../../plugins/myAxios";

const loginUser = ref();
const teamJoined = ref([]);
const teamCreated = ref([]);

// 钩子函数 => 查询用户已加入的队伍 用户创建的队伍
onMounted(async () => {
  // 校验用户登录
  const user: requestData = await getCurrentUser();
  if (user.data) {
    console.log("获取用户信息成功");
    loginUser.value = user.data;
  } else {
    console.log("获取用户信息失败");
  }
  // 发送请求 - 用户已创建的队伍
  const teamList1 = await myAxios
    .get("/team/created", {
      params: {
        userId: loginUser.value.id,
      },
    })
    // 响应
    .then(function (response) {
      // 返回响应数据（用户列表）
      console.log(response.data);
      return response.data;
    })
    // 抛异常
    .catch(function (error) {
      console.log(error);
    });

  // 发送请求 - 用户已加入的队伍
  const teamList2 = await myAxios
    .get("/team/joined", {
      params: {
        userId: loginUser.value.id,
      },
    })
    // 响应
    .then(function (response) {
      // 返回响应数据（用户列表）
      console.log(response.data);
      return response.data;
    })
    // 抛异常
    .catch(function (error) {
      console.log(error);
    });

  // 处理队伍状态
  if (teamList1 && teamList2) {
    teamList1.forEach(
      (team: teamType) => {
        if (team.status == "0") team.status = "公开";
        else if (team.status == "1") team.status = "私有";
        else team.status = "加密";
      },
      teamList2.forEach((team: teamType) => {
        if (team.status == "0") team.status = "公开";
        else if (team.status == "1") team.status = "私有";
        else team.status = "加密";
      })
    );
  }
  // 拿到数据
  teamJoined.value = teamList1;
  teamCreated.value = teamList2;
  console.log("teamJoin => " + teamJoined.value);
});

const router = useRouter();

// 跳转到新增队伍页面
const toTeamAdd = () => {
  router.push("/team/add");
};

// 跳转到修改队伍页
const toEditTeam = (id: number) => {
  router.push({
    path: "/team/edit",
    query: {
      id,
    },
  });
};
</script>
