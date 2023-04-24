<template>
  <!-- 搜索栏 -->
  <form action="/">
    <van-search
      v-model="searchText"
      show-action
      placeholder="请输入搜索关键词"
      @search="onSearch"
      @cancel="onCancel"
    />
  </form>
  <!-- 分割线 -->
  <van-divider content-position="left">已选标签</van-divider>
  <div v-if="activeIds.length === 0">请选择标签</div>
  <!-- 选中的标签  layout布局 -->
  <van-row gutter="20">
    <van-col v-for="tag in activeIds">
      <van-tag closeable size="medium" type="primary" @close="close">
        {{ tag }}
      </van-tag>
    </van-col>
  </van-row>

  <van-divider content-position="right">可选标签</van-divider>
  <!-- 标签列表 -->
  <van-tree-select
    v-model:active-id="activeIds"
    v-model:main-active-index="activeIndex"
    :items="tagList"
  />
  <!-- 搜索 -->
  <van-button
    type="primary"
    style="margin: 8px; padding: 20px"
    @click="doSearch()"
    >搜索</van-button
  >
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";

const searchText = ref("");

// 已选中的标签
const activeIds = ref([]);
const activeIndex = ref(0);

// 原始标签列表
const originTagList = [
  {
    text: "年级",
    children: [
      { text: "大一", id: "大一" },
      { text: "大二", id: "大二" },
      { text: "大三", id: "大三" },
      { text: "大四", id: "大四" },
      { text: "大五", id: "大五", disabled: true },
    ],
  },
  {
    text: "性别",
    children: [
      { text: "男", id: "男" },
      { text: "女", id: "女" },
    ],
  },
];

// 实际标签列表
let tagList = ref(originTagList);

/**
 * 搜索过滤
 * @param val
 */
const onSearch = (val: any) => {
  tagList.value = originTagList.map((parentTag) => {
    const tempChildren = [...parentTag.children];
    const tempParentTag = { ...parentTag };
    tempParentTag.children = tempChildren.filter((item) =>
      item.text.includes(searchText.value)
    );
    return tempParentTag;
  });
};

// 取消
const onCancel = () => {
  searchText.value = "";
  tagList.value = originTagList;
};

// 关闭标签
const close = (tag: any) => {
  activeIds.value = activeIds.value.filter((item) => {
    return item !== tag;
  });
};

// 根据标签搜索, 向后台发送请求
const router = useRouter();
const doSearch = () => {
  router.push({
    path: "/user/list",
    query: {
      tags: activeIds.value,
    },
  });
  console.log("标签列表" + " " + activeIds.value + typeof activeIds.value);
};
</script>
