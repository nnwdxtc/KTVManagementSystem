// /js/datatables-init.js
window.initDataTable = function(selector, options = {}) {
    const table = document.querySelector(selector);
    if (!table) {
        console.warn('表格元素不存在:', selector);
        return null;
    }

    // 如果已经初始化过，先销毁
    if (table.datatable) {
        table.datatable.destroy();
    }

    // 默认配置
    const defaultOptions = {
        perPage: 10,
        perPageSelect: [5, 10, 20, 50],
        searchable: true,
        sortable: true,
        fixedHeight: false,
        labels: {
            placeholder: "输入关键词搜索...",
            perPage: "每页显示 {select} 条",
            noRows: "没有找到相关数据",
            info: "显示第 {start} 到 {end} 条，共 {rows} 条数据"
        }
    };

    // 合并配置
    const mergedOptions = { ...defaultOptions, ...options };

    // 初始化
    table.datatable = new simpleDatatables.DataTable(table, mergedOptions);

    // 添加一些样式调整
    setTimeout(() => {
        const searchInput = table.parentElement.querySelector('.datatable-search input');
        if (searchInput) {
            searchInput.classList.add('form-control');
            searchInput.style.width = '200px';
        }
    }, 100);

    return table.datatable;
};