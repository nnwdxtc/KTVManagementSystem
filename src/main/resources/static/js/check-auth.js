/**
 * 前端简易权限拦截：
 * 1. 没登录 → 跳去 /login.html
 * 2. 如果页面 URL 带了 ?role=xxx 且与当前身份不符 → 403
 */
(function () {
    const loginUser = JSON.parse(sessionStorage.getItem('user')); // 登录时存进去的对象

    console.log(loginUser)

    if (!loginUser || !loginUser.user||!loginUser.user.account) {
        location.replace('/login.html');
        return;
    }

    const role       = loginUser.role;          // 当前登录身份
    const needRole   = new URLSearchParams(location.search).get('role'); // URL 里带的角色要求
    if (needRole && needRole !== role) {
        document.body.innerHTML = '<div style="text-align:center;margin-top:20%"><h1>403 身份不符</h1></div>';
        document.title = '403';
        throw new Error('Blocked'); // 停止后续脚本执行
    }
})();