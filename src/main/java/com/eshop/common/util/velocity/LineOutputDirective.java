package com.eshop.common.util.velocity;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

/**
 * Velocity模板上用于去除换行的指令
 * ps：未完成
 */
public class LineOutputDirective extends Directive {

	// 指定指令的名称
	@Override
	public String getName() {
		return "line";
	}

	// 指定指令类型为块指令
	@Override
	public int getType() {
		return BLOCK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.velocity.runtime.directive.Directive#render()
	 */
	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException {
		System.out.println("1111111111111111111111111111");
		// 获得缓存信息
		SimpleNode sn_region = (SimpleNode) node.jjtGetChild(0);
		// 获得请求连接
		Boolean mark = (Boolean) sn_region.value(context);
		if(mark){
			
		}else{
			SimpleNode body = (SimpleNode) node.jjtGetChild(1);
			body.render(context, writer);
		}
		
		return true;
	}

}