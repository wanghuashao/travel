function addPageNation(url,pageBean,cid,rname) {
	var pageNation = '';
	///////////////首页和上一页功能//////////////////////
	//首页
	pageNation += '<li><a href="'+url+'?cid='+cid+'&rname='+rname+'&pageNumber=1">首页</a></li>';
	//上一页
	if(pageBean.pageNumber > 1) {
		pageNation += '<li class="threeword"><a href="'+url+'?cid='+cid+'&rname='+rname+'&pageNumber=' + pageBean.prePage + '">上一页</a></li>';
	} else {
		pageNation += '<li class="threeword"><a href="javascript:void(0);">上一页</a></li>';
	}

	///////////////具体页码数显示功能///////////////////
	for(var i = pageBean.start; i <= pageBean.end; i++) {
		// 当前页高亮(且不可用！)
		if(pageBean.pageNumber == i) {
			pageNation += '<li class="curPage"><a href="javascript:void(0);">' + i + '</a></li>';
		} else {
			pageNation += '<li><a href="'+url+'?cid='+cid+'&rname='+rname+'&pageNumber=' + i + '">' + i + '</a></li>';
		}
	}

	///////////////下一页和末页功能/////////////////////
	//下一页
	if(pageBean.pageNumber < pageBean.totalPage) {
		pageNation += '<li class="threeword"><a href="'+url+'?cid='+cid+'&rname='+rname+'&pageNumber=' + pageBean.nextPage + '">下一页</a></li>';
	} else {
		pageNation += '<li class="threeword"><a href="javascript:void(0);">下一页</a></li>';
	}

	//末页
	pageNation += '<li><a href="'+url+'?cid='+cid+'&rname='+rname+'&pageNumber=' + pageBean.totalPage + '">末页</a></li>';

	return pageNation;
}