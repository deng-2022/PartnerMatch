<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
        v-model="userAccount"
        name="用户名"
        label="用户名"
        placeholder="用户名"
        :rules="[{ required: true, message: '请填写用户名' }]"
      />

      <van-field
        v-model="userPassword"
        type="password"
        name="密码"
        label="密码"
        placeholder="密码"
        :rules="[{ required: true, message: '请填写密码' }]"
      />
    </van-cell-group>

    <div style="margin: 16px">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>

    <a @click="toCodeLogin" style="color: rgb(17, 131, 207); margin-left: 67px"
      >新用户注册</a
    >

    <a @click="toCodeLogin" style="color: rgb(17, 131, 207); margin-left: 90px"
      >验证码登录</a
    >
  </van-form>
</template>

<script setup lang="ts">
import { ref } from "vue";
import myAxios from "../../plugins/myAxios";
import { useRouter } from "vue-router";
import { showSuccessToast, showFailToast } from "vant";
import { requestData } from "../../models/user";

const router = useRouter();
const userAccount = ref("");
const userPassword = ref("");

const onSubmit = async () => {
  const res: requestData = await myAxios.post("/user/login", {
    userAccount: userAccount.value,
    userPassword: userPassword.value,
  });

  // console.log(res, "用户登录");
  // console.log(res.code, "用户登录");
  // console.log(res.data, "用户登录");

  if (res.code === 0 && res.data) {
    showSuccessToast("登录成功");
    router.replace("/");
  } else {
    showFailToast("登录失败");
  }
};

// 跳转至验证码登录页
const toCodeLogin = () => {
  router.push("/code/login");
};
</script>
