<template>
  <!-- 筛选按钮 -->
  <van-button
    plain
    type="success"
    style="margin-left: 110px"
    @click="showSearchMenu"
    >点此筛选所需队伍</van-button
  >

  <!-- 分割线 -->
  <van-divider
    :style="{ color: '#1989fa', borderColor: '#1989fa', padding: '0 16px' }"
    >符合条件的队伍</van-divider
  >

  <!-- 队伍列表分页展示 -->
  <van-tabs v-model:active="active" swipeable>
    <!-- 公开队伍列表 -->
    <van-tab title="公开" style="padding-bottom: 57px">
      <van-card
        v-if="pubTeamList"
        v-for="team in pubTeamList"
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
          <van-button
            size="mini"
            type="success"
            @click="toJoinTeam(team, password)"
          >
            申请加入
          </van-button>
          <van-button size="mini" type="primary" @click="">
            详细信息
          </van-button>
        </template>
      </van-card>
      <!-- 无队伍信息展示 -->
      <van-empty v-if="!pubTeamList" description="公开队伍为空" />
    </van-tab>

    <!-- 加密队伍列表 -->
    <van-tab title="加密" style="padding-bottom: 57px">
      <van-card
        v-if="safeTeamList"
        v-for="team in safeTeamList"
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
          <van-button size="mini" type="success" @click="preJoinTeam(team)">
            申请加入
          </van-button>
          <van-button size="mini" type="primary" @click="">
            详细信息
          </van-button>
        </template>
      </van-card>
      <!-- 无队伍信息展示 -->
      <van-empty v-if="!safeTeamList" description="加密队伍为空" />
    </van-tab>
  </van-tabs>

  <!-- 密码对话框 -->
  <van-dialog
    v-model:show="show"
    title="提示"
    show-cancel-button
    @confirm="toJoinTeam(joinTeamParam, password)"
    @cancel="cancelJoin"
  >
    <van-field v-model="password" placeholder="请输入密码"> </van-field>
  </van-dialog>

  <!-- 搜索条件圆角弹窗（底部） -->
  <van-popup
    v-model:show="showBottom"
    round
    position="bottom"
    :style="{ height: '50%' }"
  >
    <!-- 搜索表单 -->
    <van-form @submit="onSearchTeam">
      <van-cell-group inset>
        <van-field
          v-model="searchItem.name"
          name="name"
          label="队伍名"
          placeholder="请输入队伍名"
          :rules="[{ message: '请输入队伍名' }]"
        />

        <van-field
          v-model="searchItem.description"
          rows="4"
          name="description"
          label="队伍描述"
          type="textarea"
          placeholder="请输入队伍描述"
        />

        <van-field
          v-model="searchItem.userId"
          name="userId"
          label="队长id"
          placeholder="请输入队长id"
          :rules="[{ message: '请输入队长id' }]"
        />

        <van-field name="maxNum" label="最少人数">
          <template #input>
            <van-stepper v-model="searchItem.maxNum" max="20" min="2" />
          </template>
        </van-field>
      </van-cell-group>

      <div style="margin: 16px">
        <van-button round block type="primary" native-type="submit">
          查询队伍
        </van-button>
      </div>
    </van-form>
  </van-popup>
</template>

<script lang="ts" setup>
import { showSuccessToast } from "vant";
import { onMounted, ref } from "vue";
import myAxios from "../../plugins/myAxios";
import { getTeamStatus } from "../../service/function/getTeamStatus";

// 展示底部搜索条件
const showBottom = ref(false);
// 加入队伍参数
const joinTeamParam = ref({});
// 队伍密码
const password = ref("");
// 密码对话框
const show = ref(false);
// 标签页-激活
const active = ref(0);
//
const initItem = {
  name: "",
  description: "",
  maxNum: "",
  userId: "",
  status: "",
};
// 搜索条件
const searchItem = ref({ ...initItem });
// 公开队伍列表
const pubTeamList = ref([]);
// 加密队伍列表
const safeTeamList = ref([]);
// 钩子函数 - 加载所有队伍信息
onMounted(async () => {
  console.log("active = " + active.value);
  // 公开队伍列表
  const teamListData1 = await myAxios
    .get("/team/list/page", {
      params: {
        name: searchItem.value.name,
        description: searchItem.value.description,
        maxNum: searchItem.value.maxNum,
        userId: searchItem.value.userId,
        status: 0,
      },
    })
    // 响应
    .then(function (response) {
      // 返回响应数据（用户列表）
      return response?.data.records;
    })
    // 抛异常
    .catch(function (error) {
      console.log(error);
    });

  // 加密队伍列表
  const teamListData2 = await myAxios
    .get("/team/list/page", {
      params: {
        name: searchItem.value.name,
        description: searchItem.value.description,
        maxNum: searchItem.value.maxNum,
        userId: searchItem.value.userId,
        status: 2,
      },
    })
    // 响应
    .then(function (response) {
      // 返回响应数据（用户列表）
      return response?.data.records;
    })
    // 抛异常
    .catch(function (error) {
      console.log(error);
    });

  // 拿取公开队伍列表
  pubTeamList.value = teamListData1;
  console.log(pubTeamList.value);
  // 拿取加密队伍列表
  safeTeamList.value = teamListData2;
  console.log(safeTeamList.value);
});

// 搜索队伍
const onSearchTeam = async () => {
  let teamListData;
  // 判断为公开队伍栏
  if (active.value === 0) {
    teamListData = await myAxios
      .get("/team/list/page", {
        params: {
          name: searchItem.value.name,
          description: searchItem.value.description,
          maxNum: searchItem.value.maxNum,
          userId: searchItem.value.userId,
          status: 0,
        },
      })
      // 响应
      .then(function (response) {
        // 返回响应数据（用户列表）
        return response?.data.records;
      })
      // 抛异常
      .catch(function (error) {
        console.log(error);
      });
    // 拿取公开队伍列表
    pubTeamList.value = teamListData;
    console.log(pubTeamList.value);
  }
  // 加密队伍栏
  else {
    teamListData = await myAxios
      .get("/team/list/page", {
        params: {
          name: searchItem.value.name,
          description: searchItem.value.description,
          maxNum: searchItem.value.maxNum,
          userId: searchItem.value.userId,
          status: 2,
        },
      })
      // 响应
      .then(function (response) {
        // 返回响应数据（用户列表）
        return response?.data.records;
      })
      // 抛异常
      .catch(function (error) {
        console.log(error);
      });
    // 拿取队伍列表
    safeTeamList.value = teamListData;
    console.log(safeTeamList.value);
  }

  // 关闭底部搜索条件弹窗
  showBottom.value = false;
  // 拿取队伍列表
  safeTeamList.value = teamListData;
  console.log(safeTeamList.value);
};

// 弹出对话框 封装队伍信息
const preJoinTeam = async (team: any) => {
  show.value = true;
  joinTeamParam.value = team;
};

// 取消 清空密码槽 关闭对话框
const cancelJoin = async () => {
  password.value = "";
  show.value = false;
};

// 确定 申请加入队伍
const toJoinTeam = async (team: any, password: any) => {
  // on confirm
  // 发送请求, 获取用户数据列表
  const joinTeam = await myAxios
    .post("/team/join", {
      id: team.id,
      userId: team.userId,
      maxNum: team.maxNum,
      joinNum: team.joinNum,
      status: team.status,
      password: password,
    })
    // 响应
    .then(function (response) {
      // 返回响应数据（用户列表）
      console.log("加入队伍: = " + response.data);
      return response.data;
    })
    // 抛异常
    .catch(function (error) {
      console.log(error);
    });
  // 成功加入队伍
  if (joinTeam) {
    showSuccessToast(`joinTeam`);
    console.log(joinTeam);
  }
};

// 弹出底部搜索条件弹窗
const showSearchMenu = () => {
  showBottom.value = true;
};
</script>
