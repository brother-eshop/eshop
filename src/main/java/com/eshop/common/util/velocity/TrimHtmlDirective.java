package com.eshop.common.util.velocity;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

public class TrimHtmlDirective extends Directive {
	private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签

	@Override
	public String getName() {
		return "trim";
	} // 指定指令的名称

	@Override
	public int getType() {
		return LINE;
	} // 指定指令类型为块指令

	public static String filterHtml(String str) {
		str = str.replaceAll("&lt;", "<");
		Pattern pattern = Pattern.compile(regxpForHtml);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	/*
	 * (non-Javadoc)
	 * @see org.apache.velocity.runtime.directive.Directive#render()
	 */
	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException,
			MethodInvocationException {
		// 获得缓存信息
		SimpleNode sn_content = (SimpleNode) node.jjtGetChild(0);
		if (sn_content != null) {
			String content = (String) sn_content.value(context);
			if (StringUtils.isNotBlank(content)) {
				content = filterHtml(content);
				content = content.trim();
				content = content.replaceAll("\r|\n", "");
				if (node.jjtGetNumChildren() > 1) {
					SimpleNode sn_size = (SimpleNode) node.jjtGetChild(1);
					Integer size = (Integer) sn_size.value(context);
					int l = 0;
					if (size != null && size.intValue() > 0) {
						l = content.length();
						if (l > size) {
							content = content.substring(0, size) + "...";
						}
					}
				}
				writer.write(content);
			}
		}
		return true;
	}
}
