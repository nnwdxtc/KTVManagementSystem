// 全局 axios 默认前缀 + 拦截器
axios.defaults.baseURL = '/api';

axios.interceptors.response.use(
    res => {
        if (res.data.code !== 0 && res.data.message === '未登录') {
            sessionStorage.clear();
            location.replace('/login.html');
        }
        return res;
    },
    err => Promise.reject(err)
);

// 公共工具
window.fmtDate = d => new Date(d).toLocaleString('zh-CN');