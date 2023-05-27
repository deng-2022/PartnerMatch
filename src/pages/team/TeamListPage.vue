<template>
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

      <van-field name="radio" label="队伍状态">
        <template #input>
          <van-radio-group v-model="searchItem.status" direction="horizontal">
            <van-radio name="0">公开</van-radio>
            <van-radio name="1">私有</van-radio>
            <van-radio name="2">加密</van-radio>
          </van-radio-group>
        </template>
      </van-field>
    </van-cell-group>

    <div style="margin: 16px">
      <van-button round block type="primary" native-type="submit">
        查询队伍
      </van-button>
    </div>
  </van-form>

  <van-divider content-position="left">符合条件的队伍</van-divider>
  <van-card
    v-if="teamList"
    v-for="team in teamList"
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
      <van-button size="mini" type="primary" @click=""> 详细信息 </van-button>
    </template>
  </van-card>
  <!-- 无用户信息展示 -->
  <van-empty v-if="!teamList" description="加入队伍为空" />
</template>

<script lang="ts" setup>
import { ref } from "vue";
import myAxios from "../../plugins/myAxios";
import { getTeamStatus } from "../../service/function/getTeamStatus";

const initItem = {
  name: "",
  description: "",
  maxNum: "",
  userId: "",
  status: "",
};
// 搜索条件
const searchItem = ref({ ...initItem });
// 队伍列表
const teamList = ref([]);
// 表单项
const onSearchTeam = async () => {
  const res = await myAxios
    .get("/team/list/page", {
      params: {
        name: searchItem.value.name,
        description: searchItem.value.description,
        maxNum: searchItem.value.maxNum,
        userId: searchItem.value.userId,
        status: searchItem.value.status,
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
  teamList.value = res;
  console.log(teamList.value);
};
</script>
