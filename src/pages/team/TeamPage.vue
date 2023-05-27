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

      <van-button
        plain
        style="margin-right: 15%"
        type="primary"
        @click="toSearchTeam"
        >搜索队伍
      </van-button>
    </div>
    <!-- <slot> 队伍 </slot> -->
    <van-divider content-position="left">我创建的队伍</van-divider>
    <van-card
      v-if="teamCreated"
      v-for="team in teamCreated"
      :tag="getTeamStatus(team.status)"
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
        <van-button size="mini" type="danger" @click="deleteTeam(team)"
          >解散</van-button
        >
      </template>
    </van-card>
    <!-- 已创建队伍信息展示 -->
    <van-empty v-if="!teamCreated" description="创建队伍为空" />

    <van-divider content-position="right">我加入的队伍</van-divider>
    <van-card
      v-if="teamJoined"
      v-for="team in teamJoined"
      :tag="getTeamStatus(team.status)"
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
        <van-button size="mini" type="danger" @click="quitTeam(team)"
          >退出</van-button
        >
      </template>
    </van-card>

    <!-- 无用户信息展示 -->
    <van-empty v-if="!teamJoined" description="加入队伍为空" />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import { requestData } from "../../models/user";
import { getCurrentUser } from "../../service/user";
import myAxios from "../../plugins/myAxios";
import { showConfirmDialog } from "vant";
import { getTeamStatus } from "../../service/function/getTeamStatus";

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
      console.log("created = " + response.data);
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
      console.log("joined = " + response.data);
      return response.data;
    })
    // 抛异常
    .catch(function (error) {
      console.log(error);
    });

  // // 处理已创建队伍状态
  // if (teamList1) {
  //   teamList1.forEach((team: teamType) => {
  //     if (team.status == "0") team.status = "公开";
  //     else if (team.status == "1") team.status = "私有";
  //     else team.status = "加密";
  //   });
  // }
  // // 处理已加入队伍状态
  // if (teamList2) {
  //   teamList2.forEach((team: teamType) => {
  //     if (team.status == "0") team.status = "公开";
  //     else if (team.status == "1") team.status = "私有";
  //     else team.status = "加密";
  //   });
  // }

  // 拿到数据
  teamCreated.value = teamList1;
  teamJoined.value = teamList2;
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

// 退出队伍
const quitTeam = async (team: any) => {
  showConfirmDialog({
    title: "提示",
    message: "你确定要退出队伍吗?",
  })
    .then(async () => {
      // on confirm
      const quit = await myAxios
        .post("/team/quit", {
          id: team.id,
          userId: team.userId,
          joinNum: team.joinNum,
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

      if (quit.data) {
        console.log(`${quit.data}`);
      } else {
        console.log("退出队伍失败");
      }
      // 刷新页面
      location.reload;
    })
    .catch(() => {
      // on cancel
    });
};

// 解散队伍
const deleteTeam = async (team: any) => {
  showConfirmDialog({
    title: "提示",
    message: "你确定要解散队伍吗?",
  })
    .then(async () => {
      // on confirm
      const del = await myAxios
        .post("/team/delete", {
          id: team.id,
          userId: team.userId,
          status: team.status,
          password: team.password,
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

      if (del.data) {
        console.log(`${del.data}`);
      } else {
        console.log("解散队伍失败");
      }
      // 刷新页面
      location.reload;
    })
    .catch(() => {
      // on cancel
    });
};

const toSearchTeam = async () => {
  router.push({
    path: "/team/list",
  });
};
</script>
