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

      <van-field
        v-model="checkPassword"
        type="password"
        name="确认密码"
        label="确认密码"
        placeholder="确认密码"
        :rules="[{ required: true, message: '请再次填写密码' }]"
      />

      <van-field
        v-model="planetCode"
        name="星球编号"
        label="星球编号"
        placeholder=" 星球编号"
        :rules="[{ required: true, message: '请填写星球编号' }]"
      />
    </van-cell-group>

    <div style="margin: 16px">
      <van-button round block type="primary" native-type="submit">
        注册
      </van-button>
    </div>
  </van-form>
</template>

<script setup lang="ts">
import { ref } from "vue";
import myAxios from "../../plugins/myAxios";
import { useRouter } from "vue-router";
import { showSuccessToast, showFailToast } from "vant";
import { requestData } from "../../models/user";

const router = useRouter();
// 用户名
const userAccount = ref("");
// 密码
const userPassword = ref("");
// 确认密码
const checkPassword = ref("");
// 星球编号
const planetCode = ref("");

const onSubmit = async () => {
  const res: requestData = await myAxios.post("/user/register", {
    userAccount: userAccount.value,
    userPassword: userPassword.value,
    checkPassword: checkPassword.value,
    planetCode: planetCode.value,
  });

  if (res.code === 0 && res.data) {
    showSuccessToast("注册成功");
    router.replace("/user/login");
  } else {
    showFailToast("注册失败");
  }
};

// 跳转至验证码登录页
const toCodeLogin = () => {
  router.push("/code/login");
};
</script>
