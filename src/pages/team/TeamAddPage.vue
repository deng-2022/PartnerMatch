<template>
  <!-- <slot>新增队伍</slot> -->
  <div id="content">
    <van-form @submit="onSubmit">
      <van-cell-group inset>
        <van-field
          v-model="teamAdd.name"
          name="name"
          label="队伍名"
          placeholder="请输入队伍名"
          :rules="[{ message: '请输入队伍名' }]"
        />

        <van-field
          v-model="teamAdd.description"
          rows="4"
          name="description"
          label="队伍描述"
          type="textarea"
          placeholder="请输入队伍描述"
        />

        <van-field name="maxNum" label="最大人数">
          <template #input>
            <van-stepper v-model="teamAdd.maxNum" max="20" min="2" />
          </template>
        </van-field>

        <van-field
          v-model="teamAdd.expireTime"
          is-link
          readonly
          name="expireTime"
          label="过期时间"
          placeholder="点击选择时间"
          @click="showPicker = true"
        />

        <van-popup v-model:show="showPicker" position="bottom">
          <van-date-picker @confirm="onConfirm" @cancel="showPicker = false" />
        </van-popup>

        <van-field name="radio" label="队伍状态">
          <template #input>
            <van-radio-group v-model="teamAdd.status" direction="horizontal">
              <van-radio name="0">公开</van-radio>
              <van-radio name="1">私有</van-radio>
              <van-radio name="2">加密</van-radio>
            </van-radio-group>
          </template>
        </van-field>

        <van-field
          v-if="Number(teamAdd.status) === 2"
          v-model="teamAdd.password"
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
import { ref } from "vue";
import myAxios from "../../plugins/myAxios";
import { showSuccessToast, showFailToast } from "vant";
import { useRouter } from "vue-router";

// 日期展示
const showPicker = ref(false);
const onConfirm = ({ selectedValues }) => {
  teamAdd.value.expireTime = selectedValues.join("/");
  showPicker.value = false;
};

const initTeam = {
  name: "",
  description: "",
  maxNum: 2,
  expireTime: "",
  status: 0,
  password: "",
};

const teamAdd = ref({ ...initTeam });

// 提交表单
const onSubmit = async () => {
  // 发送请求 - 新增队伍
  const add = await myAxios
    .post("/team/add", teamAdd.value)
    // 响应s
    .then(function (response) {
      // 返回响应数据（用户列表）
      console.log("response.data = " + response.data);
      return response.data;
    })
    // 抛异常
    .catch(function (error) {
      console.log(error);
    });

  if (add.code == 0) {
    showSuccessToast(add.data);
  } else {
    showFailToast("新增队伍失败");
  }

  const router = useRouter();
  router.push("/team");
};
</script>
