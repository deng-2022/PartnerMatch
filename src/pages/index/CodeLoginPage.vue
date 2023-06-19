<template>
  <div class="content">
    <!-- 寄语 -->
    <van-notice-bar
      v-if="rightCode"
      left-icon="volume-o"
      :text="`验证码: ${rightCode}`"
    />

    <van-cell-group inset>
      <van-field
        v-model="phoneNumber"
        name="手机号"
        label="手机号"
        placeholder="请填写手机号"
        :rules="[{ required: true, message: '请填写手机号' }]"
      />

      <van-field
        v-model="code"
        name="验证码"
        placeholder="请填写验证码"
        :rules="[{ required: true, message: '请填写验证码' }]"
      />

      <van-button
        plain
        type="primary"
        @click="getCode"
        style="margin-left: 222px"
        >发送验证码</van-button
      >
    </van-cell-group>

    <div style="margin: 16px">
      <van-button
        round
        block
        type="primary"
        native-type="submit"
        @click="codeLogin"
      >
        登录
      </van-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import myAxios from "../../plugins/myAxios";
import { useRouter } from "vue-router";
import { showSuccessToast, showFailToast } from "vant";
import { requestData } from "../../models/user";

const router = useRouter();
// 电话号码
const phoneNumber = ref("");
// 填写的验证码
const code = ref("");
// 收到的验证码
const rightCode = ref("");

// 直接获取验证码
const getCode = async () => {
  const res: requestData = await myAxios.get("/user/getCode", {
    params: {
      phoneNumber: phoneNumber.value,
    },
  });

  if (res.code === 0 && res.data) {
    //获取验证码
    rightCode.value = res.data;
  } else {
    showFailToast("获取验证码失败");
  }
};

// 发送短信验证码

// 验证码登录
const codeLogin = async () => {
  const res: requestData = await myAxios.get("/user/codeLogin", {
    params: {
      phoneNumber: phoneNumber.value,
      code: code.value,
      rightCode: rightCode.value,
    },
  });

  // 登录成功，跳转至主页
  if (res.code === 0 && res.data) {
    showSuccessToast("登录成功");
    router.replace("/");
  } else {
    showFailToast("登录失败");
  }
};
</script>
