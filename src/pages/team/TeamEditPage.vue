<template>
  <!-- <slot>新增队伍</slot> -->
  <div id="content">
    <!-- <slot>修改队伍</slot> -->
    <van-form @submit="onSubmit">
      <van-cell-group inset>
        <van-field
          v-model="teamUpdate.name"
          name="name"
          label="队伍名"
          placeholder="请输入队伍名"
          :rules="[{ message: '请输入队伍名' }]"
        />

        <van-field
          v-model="teamUpdate.description"
          rows="4"
          name="description"
          label="队伍描述"
          type="textarea"
          placeholder="请输入队伍描述"
        />

        <van-field name="radio" label="队伍状态">
          <template #input>
            <van-radio-group v-model="checked" direction="horizontal">
              <van-radio name="0">公开</van-radio>
              <van-radio name="1">私有</van-radio>
              <van-radio name="2">加密</van-radio>
            </van-radio-group>
          </template>
        </van-field>

        <van-field
          v-if="Number(checked) === 2"
          v-model="teamUpdate.password"
          type="password"
          name="password"
          label="密码"
          placeholder="请输入队伍密码"
          :rules="[{ required: true, message: '请填写密码' }]"
        />
      </van-cell-group>

      <div style="margin: 16px">
        <van-button round block type="primary" native-type="submit">
          提交
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import myAxios from "../../plugins/myAxios";
import { useRoute } from "vue-router";
import { useRouter } from "vue-router";
import { showSuccessToast, showFailToast } from "vant";

// 队伍状态
const checked = ref("");
// 修改队伍信息
const teamUpdate = ref({});

const route = useRoute();
const teamId = route.query.id;

// 钩子函数, 根据队伍id查询队伍信息
onMounted(async () => {
  // console.log("teamId = " + teamId);
  const teamData = await myAxios
    .get("/team/one", { params: { teamId } })
    // 响应
    .then(function (response) {
      // 返回响应数据（用户列表）
      console.log("response.data = " + response.data);
      return response.data;
    })
    // 抛异常
    .catch(function (error) {
      console.log(error);
    });

  if (teamData) {
    teamUpdate.value = teamData;
    console.log("stasus = " + typeof teamData.status);
    checked.value = `${teamData.status}`;

    if (teamData.status == 2) {
      console.log("psd = " + teamData.password);
    }
  }
});

// 提交表单
const onSubmit = async () => {
  // 发送请求 - 新增队伍
  const update = await myAxios
    .post("/team/update", teamUpdate.value)
    // 响应s
    .then(function (response) {
      // 返回响应数据（用户列表）
      console.log("response.data = " + response.data);
      return response;
    })
    // 抛异常
    .catch(function (error) {
      console.log(error);
    });

  if (update.code == 0) {
    showSuccessToast(update.data);
  } else {
    showFailToast("修改队伍失败");
  }

  const router = useRouter();
  router.push("/team");
};
</script>
