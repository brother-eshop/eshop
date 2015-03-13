package com.eshop.common.util.velocity;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

public class PageContentDirective extends Directive {
	@SuppressWarnings("unused")
	private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签
	private final static String PAGE_MARK = "<-page->|&lt;-page-&gt;";

	@Override
	public String getName() {
		return "page";
	} // 指定指令的名称

	@Override
	public int getType() {
		return LINE;
	} // 指定指令类型为块指令

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException,
			MethodInvocationException {
		// 获得缓存信息
		SimpleNode sn_content = (SimpleNode) node.jjtGetChild(0);
		if (sn_content != null) {
			String content = (String) sn_content.value(context);
			if (StringUtils.isNotBlank(content)) {
				String[] pages = content.split(PAGE_MARK);
				if (pages.length > 1) {
					StringBuilder sb = new StringBuilder();
					String page = "";
					for (int i = 0; i < pages.length; i++) {
						sb.append("<div class='news_page' id='news_page").append(i).append("'");
						if (i != 0) {
							sb.append(" style='display:none' ");
						}
						sb.append(">").append(pages[i]).append("</div>");
						page += "<a href='javascript:;' onclick=\"$('.news_page').hide();$('#news_page" + i + "').show();\">" + (i + 1) + "</a>";
					}
					sb.append("<div class='page_list'><div class='page_con floatnone tc'>").append(page).append("</div></div>");
					writer.write(sb.toString());
				} else {
					writer.write(content);
				}
			}
		}
		return true;
	}
}
