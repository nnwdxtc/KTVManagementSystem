/**
 * 顶部导航条组件
 * 插入位置：页面里任何 <div id="nav-placeholder"></div> 会自动被替换成导航栏
 */
(function () {
    const user = JSON.parse(sessionStorage.getItem('user') || '{}');
    const name = user.detail.name || '';
    const role = user.role || '';

    const navHtml = `
<nav class="navbar navbar-expand-sm navbar-dark bg-dark">
  <div class="container-fluid">
    <a class="navbar-brand" href="#">KTV</a>
    <div>
      <ul class="navbar-nav">
        <li class="nav-item">
          <span class="navbar-text text-white me-3">
            欢迎，${name}（${role}）
          </span>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="/login.html">退出</a>
        </li>
      </ul>
    </div>
  </div>
</nav>`;

    // 自动替换占位符
    const ph = document.getElementById('nav-placeholder');
    if (ph) ph.innerHTML = navHtml;
})();