/*===== placeholder 兼容 =====*/
var JPlaceHolder = {
	//检测
	_check : function(){
		return 'placeholder' in document.createElement('input');
	},
	//初始化
	init : function(){
		if(!this._check()){
			this.fix();
		}
	},
	//修复
	fix : function(){
		jQuery(':input[placeholder]').each(function(index, element) {
			var self = $(this), txt = self.attr('placeholder');
			self.wrap($('<div></div>').css({position:'relative', zoom:'1', border:'none', background:'none', padding:'none', margin:'none'}));
			var pos = self.position(), h = self.outerHeight(true), paddingleft = self.css('padding-left'), paddingL = parseInt(paddingleft), paddingT = parseInt(self.css('padding-top')), marginL = parseInt(self.css('margin-left')), lh = self.css('line-height');
			var holder = $('<span class="span"></span>').text(txt).css({position:'absolute', left:0, top:15, height:h, paddingLeft:(paddingL+marginL)+"px", color:'#aaa'}).appendTo(self.parent());
			self.focusin(function(e) {
				holder.hide();
			}).focusout(function(e) {
				if(!self.val()){
					holder.show();
				}
			});
			holder.click(function(e) {
				holder.hide();
				self.focus();
			});
		});
	}
};
// 初始化placeholder
	
	window.onload = function(){
		JPlaceHolder.init();
	};
/*===== placeholder 兼容结束 =====*/