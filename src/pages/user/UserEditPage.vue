<template>
  <!-- <slot>用户编辑页</slot> -->
  <van-form @submit="onSubmit">
    <van-field
      v-model="editUser.currentValue"
      :name="editUser.editKey"
      :label="editUser.editName"
      :placeholder="`${editUser.editKey}`"
    />

    <div style="margin: 16px">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRoute } from "vue-router";
import { useRouter } from "vue-router";
import { requestData } from "../../models/user";
import myAxios from "../../plugins/myAxios";
import { getCurrentUser } from "../../service/user";
import { showSuccessToast } from "vant";

const route = useRoute();
const router = useRouter();

const editUser = ref({
  editKey: route.query.editKey,
  editName: route.query.editName,
  currentValue: route.query.currentValue,
});

const onSubmit = async (values: string) => {
  const currentUser = await getCurrentUser();
  //提交之前做校验
  if (currentUser.data) {
    // 发送用户修改请求
    const res: requestData = await myAxios.post("/user/update", {
      id: currentUser.data.id,
      [editUser.value.editKey as string]: editUser.value.currentValue,
    });

    // console.log(res);
    // console.log(res.data);

    if (res.code === 0 && res.data) {
      showSuccessToast("修改成功");
      router.replace("/user");
    }
  }
};
</script>
