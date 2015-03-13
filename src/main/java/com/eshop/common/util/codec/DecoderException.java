package com.eshop.common.util.codec;

import org.springframework.core.NestedRuntimeException;

/**
 * 解码异常。
 * 
 * @author Spencer
 * @since 18 August 2014
 * @see NestedRuntimeException
 */
public class DecoderException extends NestedRuntimeException {
	
	private static final long serialVersionUID = 8862452193888553128L;
	
    public DecoderException(String message) {
    	super(message);
    }
	
    public DecoderException(String message, Throwable cause) {
        super(message, cause);
    }
}
