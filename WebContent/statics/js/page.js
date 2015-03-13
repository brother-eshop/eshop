	function goPage(pageNum){
		if(/^\d+$/.test(pageNum)==false) {
			return;
		}
		if(pageNum < 1) {
			pageNum = 1;
		}
		if(pageNum > totalPageSize) {
			if(totalPageSize>0){
				pageNum = totalPageSize;
			}else{
				pageNum=1;
			}
		}
		$("#pageCurrentPage").val(pageNum);
		$("#searchForm").submit();
	}
	function showPageNumber(c,t) {
		var currentPage = c-1<1?1:c;
		var totalPage = t;
		var pageHtml="";
		var maxNum_new = currentPage>4?6:7-currentPage;//最大显示页码数
		var discnt=1;
		for(var i=4; i>0; i--) {
			if(currentPage>i) {
				pageHtml = pageHtml + "<li class='paginItem'><a href='javascript:goPage("+(currentPage-i)+");'>"+ (currentPage-i) +"</a></li>";
				discnt++;
			}
		}
		pageHtml = pageHtml + '<li class="paginItem current"><a href="javascript:;">'+currentPage+'</a></li>';
		for(var	i=1; i < maxNum_new; i++) {
			if(currentPage+i <=totalPage && discnt < 6) {
				pageHtml = pageHtml + "<li class='paginItem'><a href='javascript:goPage("+(currentPage+i)+");'>"+ (currentPage+i) +"</a></li>";
				discnt++;
			} else {
				break;
			}
		}
		$(pageHtml).insertBefore("#nextpage");
    }
  	//跳转到页面
	function goPageByInput() {
		var pageNo = document.getElementById("pageNoIpt").value;
		if(/^\d+$/.test(pageNo)==false) {
			alert("只能输入整数，请重新输入！");
			document.getElementById("pageNoIpt").value='';
			return;
		}
		goPage(pageNo);
	}