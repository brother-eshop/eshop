package com.eshop.common.util.codec;

import org.springframework.core.NestedRuntimeException;

/**
 * 编码异常。
 * 
 * @author Spencer
 * @since 18 Aug 2014
 * @see NestedRuntimeException
 */
public class EncoderException extends NestedRuntimeException {
	
	private static final long serialVersionUID = 8862452193888553128L;
	
    public EncoderException(String message) {
    	super(message);
    }
	
    public EncoderException(String message, Throwable cause) {
        super(message, cause);
    }
}
