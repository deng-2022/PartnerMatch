import {GithubOutlined} from '@ant-design/icons';
import {DefaultFooter} from '@ant-design/pro-components';

const Footer: React.FC = () => {
  const defaultMessage = '2023 yupi->memory出品';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'Bilibili',
          title: 'ownMsg',
          href: 'https://space.bilibili.com/1453170444?spm_id_from=333.1007.0.0',
          blankTarget: true,
        },
        {
          key: 'gitee',
          title: <GithubOutlined/>,
          href: 'https://gitee.com/deng-2022/projects',
          blankTarget: true,
        },
        {
          key: 'codeNode',
          title: '编程导航',
          href: 'https://www.code-nav.cn/',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
