import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {useRef} from 'react';
import {searchUsers} from "@/services/ant-design-pro/api";

// id?: number;
// userAccount?: string;
// username?: string;
// avatarUrl?: string;
// gender?: string;
// phone?: string;
// email?: string;
// userStatus?: string;
// userRole?: number;
const columns: ProColumns<API.BaseResponse<API.CurrentUser>>[] = [
  {
    title: '账号',
    dataIndex: 'userAccount',
    copyable: true,
  },
  {
    title: '昵称',
    dataIndex: 'username',
    copyable: true,
  },
  {
    title: '头像',
    dataIndex: 'avatarUrl',
    copyable: true,
    render: (_, record) => (
      <div>
        <img src={record.avatarUrl} width={100}/>
      </div>
    )
  },
  {
    title: '性别',
    dataIndex: 'gender',
    valueType: 'select',
    valueEnum: {
      0: {
        text: '女',
      },
      1: {
        text: '男',
      },
    },
  },
  {
    title: '电话',
    dataIndex: 'phone',
    copyable: true,
  },
  {
    title: '邮件',
    dataIndex: 'email',
    copyable: true,
  },
  {
    title: '星球编号',
    dataIndex: 'planetCode',
    copyable: true,
  },
  {
    title: '角色',
    dataIndex: 'userRole',
    valueType: 'select',
    valueEnum: {
      0: {
        text: '普通用户',
        status: 'Default',
      },
      1: {
        text: '管理员',
        status: 'Success',
      },
    },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    copyable: true,
  },
  {
    title: '状态',
    dataIndex: 'userStatus',
    valueType: 'select',
    valueEnum: {
      0: {
        text: '正常',
        status: 'Success',
      },
      1: {
        text: '异常',
        status: 'Error',
      },
    },
  },

];

// 查找所有用户
export default () => {
  const actionRef = useRef<ActionType>();
  return (
    <ProTable<API.BaseResponse<API.CurrentUser>>
      columns={columns}
      actionRef={actionRef}
      cardBordered
      request={async (params = {}, sort, filter) => {
        console.log(sort, filter);
        // 返回userList
        const baseResponse = await searchUsers();
        return {
          data: baseResponse,
        }
      }}
      editable={{
        type: 'multiple',
      }}
      columnsState={{
        persistenceKey: 'pro-table-singe-demos',
        persistenceType: 'localStorage',
        onChange(value) {
          console.log('value: ', value);
        },
      }}
      rowKey="id"
      search={{
        labelWidth: 'auto',
      }}
      options={{
        setting: {},
      }}
      form={{
        // 由于配置了 transform，提交的参与与定义的不同这里需要转化一下
        syncToUrl: (values, type) => {
          if (type === 'get') {
            return {
              ...values,
              created_at: [values.startTime, values.endTime],
            };
          }
          return values;
        },
      }}
      pagination={{
        pageSize: 5,
        onChange: (page) => console.log(page),
      }}
      dateFormatter="string"
      headerTitle="高级表格"
      toolBarRender={() => []}
    />
  );
};

