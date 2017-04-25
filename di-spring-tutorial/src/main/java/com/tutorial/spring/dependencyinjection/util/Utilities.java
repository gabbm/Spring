package com.tutorial.spring.dependencyinjection.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class Utilities {
	
	private static MessageSource messageSource;
	
	@Autowired
	public Utilities(MessageSource messageSource){
		Utilities.messageSource = messageSource;
	}
	
	public static void flash(RedirectAttributes redirectAttributes,
			String kind, String messageKey){
		redirectAttributes.addFlashAttribute("flashKind", kind);
		redirectAttributes.addFlashAttribute("flashMessage", Utilities.getMessage(messageKey));
	}
	
	private static String getMessage(String messageKey, Object... args){
		return messageSource.getMessage(messageKey, args, Locale.getDefault());
	}
}
